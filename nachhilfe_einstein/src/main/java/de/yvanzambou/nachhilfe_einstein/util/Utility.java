package de.yvanzambou.nachhilfe_einstein.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Utility {

    private Utility() {
        throw new UnsupportedOperationException("Utility-Klasse kann nicht instanziert werden");
    }

    public static String getPriceFormat(double price) {
        return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(price);
    }
}