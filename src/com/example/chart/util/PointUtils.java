package com.example.chart.util;

import java.util.List;

import android.graphics.PointF;

public class PointUtils {

	public static PointF getSelectPoint(PointF targtePoint, List<PointF> list) {

		if (list == null || targtePoint == null) return null;
		for (int i = 0; i < list.size(); i++) {
			if (getDistance(targtePoint, list.get(i)) < 0.6) {
				return list.get(i);
			}
		}
		return null;
	}

	public static double getDistance(PointF p1, PointF p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
}
