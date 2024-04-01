package com.found_404.funco.global.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalCalculator {

    /*
    *   scale : 제한 할 소수점 자리 수 => 내림 처리 됨
    * */

    public static double multiple(double a, double b, int scale) {
        return BigDecimalMultiple(a, b, scale);
    }

    public static double multiple(long a, double b, int scale) {
        return BigDecimalMultiple((double) a, b, scale);
    }

    private static double BigDecimalMultiple(double a, double b, int scale) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.multiply(bdb)
                .setScale(scale, RoundingMode.DOWN)
                .doubleValue();
    }

    public static double plus(double a, double b, int scale) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.add(bdb)
                .setScale(scale, RoundingMode.DOWN)
                .doubleValue();
    }

    public static double divide(double a, double b, int scale) {
        return BigDecimalDivide(a, b, scale);
    }

    public static double divide(long a, double b, int scale) {
        return divide((double) a, b, scale);
    }

    public static double divide(long a, long b, int scale) {
        return BigDecimalDivide((double) a, (double) b, scale);
    }

    public static double minus(double a, double b, int scale) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.subtract(bdb)
                .setScale(scale, RoundingMode.DOWN)
                .doubleValue();
    }

    private static double BigDecimalDivide(double a, double b, int scale) {
        BigDecimal bda = BigDecimal.valueOf(a);
        BigDecimal bdb = BigDecimal.valueOf(b);

        return bda.divide(bdb,RoundingMode.DOWN)
                .setScale(scale, RoundingMode.DOWN)
                .doubleValue();
    }

}
