package com.pstkm.util;

import java.text.DecimalFormat;

public class Stopwatch {

    private static long timeStart;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void start() {
        timeStart = System.currentTimeMillis();
    }

    public static void reset() {
        timeStart = 0;
    }

    public static String getTimeText() {
        return df.format((System.currentTimeMillis() - timeStart) * 0.001) + "s";
    }

    public static Double getTime() {
        return (System.currentTimeMillis() - timeStart) * 0.001;
    }
}