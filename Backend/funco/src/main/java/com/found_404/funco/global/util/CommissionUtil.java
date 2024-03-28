package com.found_404.funco.global.util;

public class CommissionUtil {
    private static final Double COMMISSION = 0.05;

    public static double getVolumeWithoutCommission(double volume) {
        return volume - (volume * COMMISSION);
    }

    public static long getCashWithoutCommission(long cash) {
        return (long) (cash - (cash * COMMISSION));
    }
}
