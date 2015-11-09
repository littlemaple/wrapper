package com.example.chart;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.chart.model.XYMultipleSeriesDataset;
import com.example.chart.model.XYSeries;
import com.example.chart.renderer.XYMultipleSeriesRenderer;

public class LineChart extends XYChart {

	private static final long	serialVersionUID	= 1L;

	public LineChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		super(dataset, renderer);
	}

	@Override
	protected void drawSeries(Canvas canvas, Paint paint, XYSeries series) {

	}

}
