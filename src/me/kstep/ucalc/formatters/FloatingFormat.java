package me.kstep.ucalc.formatters;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.NumberFormat;
import java.text.DecimalFormatSymbols;

public class FloatingFormat extends DecimalFormat {
    private String bigFormat;
	private String expFormat;

	public FloatingFormat(String big, String exp, DecimalFormatSymbols symbols) {
		super(big, symbols);
		bigFormat = big;
		expFormat = exp;
	}
	
	public StringBuffer format(double value, StringBuffer buffer, FieldPosition field) {
        if (1e-6 > value || value > 1e12) {
			applyPattern(expFormat);
		} else {
			applyPattern(bigFormat);
		}
		return super.format(value, buffer, field);
	}

	public StringBuffer format(long value, StringBuffer buffer, FieldPosition field) {
		if (value > 1e12) {
			applyPattern(expFormat);
		} else {
			applyPattern(bigFormat);
		}
		return super.format(value, buffer, field);
	}
/*
	public Number parse(String p1, ParsePosition p2) {
		// TODO: Implement this method
		return null;
	}
	*/
}
