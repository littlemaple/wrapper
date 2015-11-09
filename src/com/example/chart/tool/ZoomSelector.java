package com.example.chart.tool;

import android.util.Log;

public class ZoomSelector {

	public enum Operate {
		ZoomIn, ZoomOut;
	}

	private double	unit;

	public ZoomSelector(double overallLength, double compareDistance) {
		this.compareDistance = compareDistance;
		if (overallLength == 0) throw new NullPointerException("Do you set the overall length?");
		unit = overallLength / 6;
		Log.d("matrix", "unit:" + unit + ",initDis:" + compareDistance);
	}

	private double	compareDistance;

	public Operate getState(double curDistance) {
		Log.v("matrix", "curdis:" + curDistance + ",deviation:" + (curDistance - compareDistance));
		double dose = compareDistance - curDistance;
		if (Math.abs(dose) > unit) {
			Log.v("matrix", "the dis is enough");
			this.compareDistance = curDistance;
			if (dose > 0) return Operate.ZoomOut;
			return Operate.ZoomIn;
		}
		return null;

	}

}
