package com.nfl.national_football_league.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nfl.national_football_league.factory.JwtAlgorithmFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtWrapper {
    private final JwtAlgorithmFactory jwtAlgorithmFactory;
    public DecodedJWT decode(String jwt) {
        return JWT.decode(jwt);
    }

    public boolean verify(String jwt) {
        log.info("START verify jwt: {}", jwt);
        Algorithm algorithm = jwtAlgorithmFactory.create().orElse(null);
        if (algorithm == null) {
            log.warn("ERROR get algorithm failed");
            return false;
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(jwt);
        } catch (JWTVerificationException e) {
            log.warn("ERROR verify", e);
            return false;
        }

        log.info("END verify success");
        return true;
    }
}
