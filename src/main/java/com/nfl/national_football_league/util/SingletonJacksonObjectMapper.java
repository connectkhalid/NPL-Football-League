package com.nfl.national_football_league.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SingletonJacksonObjectMapper {
    private SingletonJacksonObjectMapper() {}
    public static ObjectMapper getInstance() {
        return ObjectMapperHolder.INSTANCE;
    }

    private static class ObjectMapperHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }
}
