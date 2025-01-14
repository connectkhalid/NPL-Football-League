package com.nfl.national_football_league.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpServletUtil {
    private HttpServletUtil() {}

    public static void doResponseJson(HttpServletResponse response, Object result) throws JsonProcessingException, IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                SingletonJacksonObjectMapper.getInstance().writeValueAsString(result)
        );
    }
}
