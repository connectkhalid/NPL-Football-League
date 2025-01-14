package com.nfl.national_football_league.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nfl.national_football_league.constant.RestResponseMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUnAuthorizedResponseAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info(String.valueOf(response.isCommitted()));
        response.setStatus(500);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("statusCode", 500);
        map.put("message", RestResponseMessage.INTERNAL_SERVER_ERROR);
        byte[] body = new ObjectMapper().writeValueAsBytes(Collections.synchronizedMap(map));
        response.getOutputStream().write(body);
    }
}
