package com.example.chart.factory;

import android.content.Context;

import com.example.chart.GraphicView;
import com.example.chart.LineChart;
import com.example.chart.XYChart;
import com.example.chart.model.XYMultipleSeriesDataset;
import com.example.chart.renderer.XYMultipleSeriesRenderer;

public class ChartFactory {

	public GraphicView createLineChartView(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		checkParameters(dataset, renderer);
		XYChart mChart = new LineChart(dataset, renderer);
		return new GraphicView(context, mChart);
	}

	private static void checkParameters(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		if (dataset == null || renderer == null || dataset.getSeriesCount() != renderer.getSeriesRendererCount()) {
			throw new IllegalArgumentException("Dataset and renderer should be not null and should have the same number of series");
		}
	}

}
