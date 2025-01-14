package com.nfl.national_football_league.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateUtil {
    private DateUtil() {}
    private static Timestamp debugTimestamp = null;
    private static final String TIMEZONE_UTC = "UTC";

    public static void setCurrentTimeForDebug(Timestamp timestamp) {
        log.debug("setCurrentTimeForDebug {}", timestamp);
        debugTimestamp = timestamp;
    }

    public static Timestamp currentTime() {
        if (debugTimestamp != null) {
            return debugTimestamp;
        }
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_UTC));
        Date date = cal.getTime();
        return new Timestamp(date.getTime());
    }

    public static Timestamp stringToTimestampWithoutZone(String dateAsString, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);

        try {
            Date date = new Date(simpleDateFormat.parse(dateAsString).getTime());
            return new Timestamp(date.getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String timestampToString(Timestamp timestamp, String format) {
        if (timestamp == null) return "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(timestamp.getTime());
    }

}
