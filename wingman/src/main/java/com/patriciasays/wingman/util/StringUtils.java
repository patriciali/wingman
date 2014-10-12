package com.patriciasays.wingman.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class StringUtils {

    private static Calendar cal = Calendar.getInstance();

    public static String getFormattedInspectionTime(long timeInMillis) {
        return String.format("%.1f", (timeInMillis + 0.0) / 1000);
    }

    public static String getFormattedStackmatTime(long timeInMillis) {
        cal.setTimeInMillis(timeInMillis);
        int minutes = cal.get(Calendar.MINUTE);
        if (minutes == 0) {
            int seconds = cal.get(Calendar.SECOND);
            if (seconds < 10) {
                return new SimpleDateFormat("s.SSS").format(cal.getTime());
            }
            return new SimpleDateFormat("ss.SSS").format(cal.getTime());
        }
        return new SimpleDateFormat("m:ss.SSS").format(cal.getTime());
    }

}
