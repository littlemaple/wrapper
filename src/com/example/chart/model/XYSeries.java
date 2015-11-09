package com.example.chart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.PointF;

import com.example.chart.renderer.XYSeriesRenderer;

public class XYSeries {

	private ArrayList<PointF>		list		= new ArrayList<PointF>();
	private boolean					isSort		= false;
	private String					title;
	private XYSeriesRenderer		renderer	= new XYSeriesRenderer();
	public static RendererBuilder	builder;

	public PointF getMax() {
		sortList(list, false);
		return list.get(0);
	}

	private PointF	selectedPoint;

	public PointF getSelectPointF() {
		return selectedPoint;
	}

	public void setSelectedPoint(PointF pointF) {
		this.selectedPoint = pointF;
	}

	public String getTitle() {
		return this.title;
	}

	public void addPoint(float x, float y) {
		PointF point = new PointF(x, y);
		list.add(point);
		this.isSort = false;
	}

	public void addPoints(List<PointF> list) {
		if (list != null && list.size() > 0) {
			this.list.addAll(list);
		}
		this.isSort = false;
	}

	public int Size() {
		return this.list.size();
	}

	public void clear() {
		this.list.clear();
	}

	public ArrayList<PointF> getList() {
		sortList(list, false);
		return this.list;
	}

	public PointF getPointByIndex(List<PointF> list, float x) {
		if (list == null) return null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).x == x) return list.get(i);
		}
		return null;
	}

	public PointF getClosestPoint(float x, float precision) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).x == x) return list.get(i);
		}
		return null;
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public List<PointF> getList(double min, double max) {

		List<PointF> ret = new ArrayList<PointF>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).x >= min && list.get(i).x <= max) {
				ret.add(list.get(i));
			}
		}
		return ret;

	}

	private void sortList(List<PointF> list, boolean isForce) {
		if (!isSort) {
			Collections.sort(list, new Comparator<PointF>() {

				@Override
				public int compare(PointF lhs, PointF rhs) {
					if (lhs.x > rhs.x) {
						return 1;
					}
					else if (lhs.x < rhs.x) {
						return -1;
					}
					else {
						return 0;
					}
				}
			});
			this.isSort = true;
		}
	}

	public static class RendererBuilder {
		private XYSeries	series;

		public RendererBuilder() {
			series = new XYSeries();
		}

		public RendererBuilder setLineWidth(int lineWidth) {
			series.renderer.setLineWidth(lineWidth);
			return this;
		}

		public RendererBuilder setTitle(String title) {
			series.title = title;
			return this;
		}

		public RendererBuilder setColor(int color) {
			series.renderer.setColor(color);
			return this;
		}

		public RendererBuilder setFillPoints(boolean isFillPoints) {
			series.renderer.setFillPoints(isFillPoints);
			return this;
		}

		public RendererBuilder setTextSize(float textSize) {
			series.renderer.setChartValuesTextSize(textSize);
			return this;
		}

		public XYSeries build() {
			return this.series;
		}
	}
}
