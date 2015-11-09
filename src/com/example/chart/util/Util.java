package com.example.chart.util;

import java.text.DecimalFormat;

public class Util {

	public static final int		INVALID	= -1;
	public static DecimalFormat	format	= new DecimalFormat();

	public static String getDisplayTextFromSecond(int interval) {
		int hour = 0, minute = 0, second = 0;

		if (interval >= 3600) {
			hour = interval / 3600;
			int mod = interval % 3600;
			if (mod >= 60) {
				minute = mod / 60;
			}
			second = mod % 60;
		}
		else {
			if (interval >= 60) {
				minute = interval / 60;
			}
			second = interval % 60;
		}
		StringBuffer buffer = new StringBuffer();
		if (hour != 0) buffer.append(hour + "h");
		if (minute != 0) buffer.append(minute + "m");
		if (second != 0) buffer.append(second + "s");
		return buffer.toString();
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static float formatNum(float num) {
		format.setMaximumFractionDigits(1);
		try {
			return (Float) format.parse(format.format(num));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return INVALID;
	}
}
