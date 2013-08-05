package me.kstep.ucalc.formatters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import me.kstep.ucalc.numbers.UComplex;
import me.kstep.ucalc.numbers.UNumber;

public class FloatingFormat extends DecimalFormat {
    final static long serialVersionUID = 0L;

    private String bigFormat;
    private String expFormat;

    public FloatingFormat(String big, String exp, DecimalFormatSymbols symbols) {
        super(big, symbols);
        bigFormat = big;
        expFormat = exp;
    }

    public FloatingFormat(String big, String exp) {
        super(big);
        bigFormat = big;
        expFormat = exp;
    }

    public FloatingFormat(int decimal_digits, int group_size, char decimal_separator, char group_separator) {
        this("#," + repeat('#', group_size - 1) + "0." + repeat('#', decimal_digits),
             repeat('#', group_size - 1) + "0." + repeat('#', decimal_digits) + "E+0");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(decimal_separator);
        symbols.setGroupingSeparator(group_separator);
        symbols.setExponentSeparator(" e"); // here is "MEDIUM MATHEMATICAL SPACE" U+205F, not simple space U+0020!
        symbols.setMinusSign('−');
        setDecimalFormatSymbols(symbols);
    }

    private static String repeat(char ch, int num) {
        StringBuilder result = new StringBuilder(num);
        for (int i = 0; i < num; i++) {
            result.append(ch);
        }
        return result.toString();
    }

    public StringBuffer format(double value, StringBuffer buffer, FieldPosition field) {
        double absval = Math.abs(value);
        if (1e-6 > absval || absval > 1e12) {
            applyPattern(expFormat);
        } else {
            applyPattern(bigFormat);
        }
        return super.format(value, buffer, field);
    }

    public StringBuffer format(long value, StringBuffer buffer, FieldPosition field) {
        if (Math.abs(value) > 1e12) {
            applyPattern(expFormat);
        } else {
            applyPattern(bigFormat);
        }
        return super.format(value, buffer, field);
    }

    public Number parse(String p1, ParsePosition p2) {
        applyPattern(expFormat);
        if (p1.endsWith("j")) {
            return new UComplex(UNumber.ZERO, UNumber.valueOf(super.parse(p1.length() == 1? "1": p1, p2)));
        }
        return super.parse(p1, p2);
    }
}
