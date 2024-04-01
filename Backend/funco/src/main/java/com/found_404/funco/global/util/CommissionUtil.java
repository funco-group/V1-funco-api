package com.found_404.funco.global.util;

import static com.found_404.funco.global.util.DecimalCalculator.*;
import static com.found_404.funco.global.util.ScaleType.*;

public class CommissionUtil {
    private static final Double COMMISSION = 0.05;

    public static double getVolumeWithoutCommission(double volume) {
        return minus(volume, multiple(volume, COMMISSION, VOLUME_SCALE), VOLUME_SCALE);
    }

    public static long getCashWithoutCommission(long cash) {
        return cash - (long) multiple(cash, COMMISSION, CASH_SCALE);
    }
}
