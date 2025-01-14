package com.nfl.national_football_league.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt.clientauth")
@Data
public class ClientAuthConfig {
    private String secretKey;
    private List<String> isses;
    private long expiredIntervalSeconds;
    private String typ;
}
