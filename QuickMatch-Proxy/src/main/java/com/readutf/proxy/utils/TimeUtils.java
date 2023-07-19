package com.readutf.proxy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    public static long stringToDuration(final String source) {
        if (source.equalsIgnoreCase("perm") || source.equalsIgnoreCase("permanent")) {
            return 2147483647L;
        }
        long totalTime = 0L;
        boolean found = false;
        final Matcher matcher = Pattern.compile("\\d+\\D+").matcher(source);
        while (matcher.find()) {
            final String s = matcher.group();
            final Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            final String s2 = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
            switch (s2) {
                case "s": {
                    totalTime += value;
                    found = true;
                    continue;
                }
                case "m": {
                    totalTime += value * 60L;
                    found = true;
                    continue;
                }
                case "h": {
                    totalTime += value * 60L * 60L;
                    found = true;
                    continue;
                }
                case "d": {
                    totalTime += value * 60L * 60L * 24L;
                    found = true;
                    continue;
                }
                case "w": {
                    totalTime += value * 60L * 60L * 24L * 7L;
                    found = true;
                    continue;
                }
                case "M": {
                    totalTime += value * 60L * 60L * 24L * 30L;
                    found = true;
                    continue;
                }
                case "y": {
                    totalTime += value * 60L * 60L * 24L * 365L;
                    found = true;
                    continue;
                }
            }
        }
        return found ? totalTime : -1L;
    }

}
