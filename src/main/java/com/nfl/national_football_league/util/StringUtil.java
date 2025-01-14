package com.nfl.national_football_league.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {
    private StringUtil() {}
    public static String createRandomAlphanumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
    public static String createRandomAlphabetic(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }
    public static String createRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
    public static String encodeBCrypt(String str) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(str);
    }
    public static boolean matchesBCrypt(String str, String encodePassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(str, encodePassword);
    }
    public static boolean matchesAscii(String str) {
        return StringUtils.hasText(str)
                && str.matches("^[\\x20-\\x7E]+$");
    }
    public static String encodeUrl(String str) {
        try {
            /**
             * RFC3986準拠
             * ・「~」は[%7E]に変換されるので元に戻す
             * ・半角スペースは「+」に変換されるので再変換
             * ・「*」は変換されないので変換
             */
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name())
                    .replace("%7E", "~")
                    .replace("+", "%20")
                    .replace("*", "%2A");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String substrLastIndexAfterString(String targetStr, String serachStr) {
        return targetStr.lastIndexOf(serachStr) == -1
                ? targetStr
                : targetStr.substring(targetStr.lastIndexOf(serachStr));

    }
    public static List<String> unescapeHtml4List(List<String> htmlEscapedList) {
        return htmlEscapedList.stream()
                .map(StringEscapeUtils::unescapeHtml4)
                .collect(Collectors.toList());
    }

}
