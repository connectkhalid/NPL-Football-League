package com.nfl.national_football_league.security;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(99)
public class JWTWebSecurityConfig {

    @Value("${jwt.get.token.uri}")
    private String authenticationPath;

    @Value("${insecurity.path.publicPath}")
    private String publicPath;

    @Value("${csrf.enable}")
    private boolean enableCsrf;

    private final JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

    private final JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

    public JWTWebSecurityConfig(JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint, JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter) {
        this.jwtUnAuthorizedResponseAuthenticationEntryPoint = jwtUnAuthorizedResponseAuthenticationEntryPoint;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoderBean () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (
            AuthenticationConfiguration authConfig ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    private CsrfTokenRepository getCsrfTokenRepository ( String path ) {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setCookiePath(path);
        return tokenRepository;
    }

    @Bean
    public SecurityFilterChain filterChain ( HttpSecurity httpSecurity ) throws Exception {
        if (enableCsrf)
            httpSecurity.cors(Customizer.withDefaults())
                    .csrf(csrf -> csrf.ignoringRequestMatchers(authenticationPath, publicPath)
                            .csrfTokenRepository(getCsrfTokenRepository("/")));
        else
            httpSecurity.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable());

        httpSecurity.exceptionHandling(( exception ) -> exception.authenticationEntryPoint(
                        jwtUnAuthorizedResponseAuthenticationEntryPoint))

                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests()
                .requestMatchers(authenticationPath, publicPath)
                .permitAll()
                .anyRequest()
                .authenticated();

        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);


        httpSecurity
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin().cacheControl(cacheControl -> cacheControl.disable())
                        )
                        .addHeaderWriter(new StaticHeadersWriter(HttpHeaders.STRICT_TRANSPORT_SECURITY,
                                "max-age=31536000; includeSubDomains;preload"))
                        .contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy.policyDirectives(
                                "default-src 'self'; script-src 'self' ; img-src *; style-src 'self' 'unsafe-inline'"))
                );

        return httpSecurity.build();
    }
}
