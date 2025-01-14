package com.nfl.national_football_league.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.nfl.national_football_league.config.jwt.ClientAuthConfig;
import com.nfl.national_football_league.factory.ClientAuthAlgorithmFactory;
import com.nfl.national_football_league.factory.JwtWrapperFactory;
import com.nfl.national_football_league.provider.JwtWrapper;
import com.nfl.national_football_league.service.ClientAuthService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@Service("ClientAuthService")
public class ClientAuthServiceImpl implements ClientAuthService {
    private final JwtWrapper jwtWrapper;
    private final ClientAuthConfig clientAuthConfig;

    @Autowired
    public ClientAuthServiceImpl(
            ClientAuthAlgorithmFactory clientAuthAlgorithmFactory,
            ClientAuthConfig clientAuthConfig) {
        this.jwtWrapper = new JwtWrapperFactory().create(clientAuthAlgorithmFactory);
        this.clientAuthConfig = clientAuthConfig;
    }
    @Override
    public Result verify(String jwt, String apiPath) {
        // parameter check
        if (Stream.of(jwt, apiPath).anyMatch(v -> !StringUtils.hasText(v))) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.error("END verify. invalid args jwt: {}, apiPath: {}, result: {}", jwt, apiPath, result);
            return result;
        }

        // signature verification
        if (!jwtWrapper.verify(jwt)) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.error("END verify. Failed verify jwt: {}, result: {}", jwt, result);
            return result;
        }

        DecodedJWT decodedJwt = jwtWrapper.decode(jwt);

        String typ = decodedJwt.getType();
        if (!typ.equals(clientAuthConfig.getTyp())) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.error("END verify. invalid typ: {}, result: {}", typ, result);
            return result;
        }

        String iss = decodedJwt.getIssuer();
        if (!clientAuthConfig.getIsses().contains(iss)) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.info("END verify. invalid iss: {}, result: {}", iss, result);
            return result;
        }

        List<String> audList = decodedJwt.getAudience();
        if (CollectionUtils.isEmpty(audList) || !apiPath.equals(audList.get(0))) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.info("END verify. invalid audList: {}, result: {}", audList, result);
            return result;
        }

        Date exp = decodedJwt.getExpiresAt();
        if (exp == null) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.info("END verify. jwt has no exp. result: {}", result);
            return result;
        }

        if (exp.getTime() < DateUtil.currentTime().getTime()) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.info("END verify. jwt has already expired. exp: {}, result: {}", exp, result);
            return result;
        }

        if (exp.getTime() > DateUtil.currentTime().getTime() + TimeUnit.SECONDS.toMillis(clientAuthConfig.getExpiredIntervalSeconds())) {
            Result result = Result.builder().resultCode(ResultCode.WARN_AUTH).build();
            log.info("END verify. jwt exp is illegal. exp: {}, result: {}", exp, result);
            return result;
        }

        Result result = Result.builder().resultCode(ResultCode.OK).build();
        log.info("END verify success: {}", result);
        return result;

    }
}
