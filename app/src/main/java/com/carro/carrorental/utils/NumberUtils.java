package com.carro.carrorental.utils;

import java.text.DecimalFormat;

public class NumberUtils {
    public static String formatWithCommas(double number) {
        DecimalFormat formatter = new DecimalFormat("#,##,###");
        return formatter.format(number);
    }

}
