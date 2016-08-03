package com.tiagoderlan.showcase.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Pichau on 28/07/2016.
 */
public class FormatUtils {

    public static String formatCurrency(Double value)
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("R$ #,###.00", symbols);
        return decimalFormat.format(value);
    }
}
