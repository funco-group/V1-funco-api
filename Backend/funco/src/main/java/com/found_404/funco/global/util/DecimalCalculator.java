package com.found_404.funco.global.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalCalculator {

    public static double multiple(double a, double b, ScaleType scaleType) {
        return BigDecimalMultiple(a, b, scaleType.getScale());
    }

    public static double multiple(long a, double b, ScaleType scaleType) {
        return BigDecimalMultiple((double) a, b, scaleType.getScale());
    }

    public static double divide(double a, double b, ScaleType scaleType) {
        return bigDecimalDivide(a, b, scaleType.getScale());
    }

    public static double divide(long a, double b, ScaleType scaleType) {
        return bigDecimalDivide((double) a, b, scaleType.getScale());
    }

    public static double divide(long a, long b, ScaleType scaleType) {
        return bigDecimalDivide((double) a, (double) b, scaleType.getScale());
    }


    // 구현체
    public static double plus(double a, double b, ScaleType scaleType) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.add(bdb)
                .setScale(scaleType.getScale(), RoundingMode.DOWN)
                .doubleValue();
    }

    public static double minus(double a, double b, ScaleType scaleType) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.subtract(bdb)
                .setScale(scaleType.getScale(), RoundingMode.DOWN)
                .doubleValue();
    }

    private static double BigDecimalMultiple(double a, double b, int scale) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.multiply(bdb)
                .setScale(scale, RoundingMode.DOWN)
                .doubleValue();
    }

    private static double bigDecimalDivide(double a, double b, int scale) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.divide(bdb,RoundingMode.DOWN)
                .setScale(scale, RoundingMode.DOWN)
                .doubleValue();
    }

}
