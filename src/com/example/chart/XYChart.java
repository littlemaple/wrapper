package com.example.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.example.chart.model.XYMultipleSeriesDataset;
import com.example.chart.model.XYSeries;
import com.example.chart.renderer.BasicStroke;
import com.example.chart.renderer.XYMultipleSeriesRenderer;
import com.example.chart.util.Util;

public abstract class XYChart extends AbstractChart {

	private static final long			serialVersionUID	= 1L;

	public static final String			TAG					= "XYChart";
	protected XYMultipleSeriesRenderer	mRenderer;
	private XYMultipleSeriesDataset		mDataset;

	protected PointF					centerPoint;
	/**
	 * 显示的区域
	 */
	protected Rect						screenRect			= new Rect();
	/**
	 * 主要的绘制区域
	 */
	protected Rect						drawRect			= new Rect();
	private Path						path				= new Path();

	/**
	 * Builds a new XY chart instance.
	 * 
	 * @param dataset
	 *            the multiple series dataset
	 * @param renderer
	 *            the multiple series renderer
	 */
	public XYChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		mDataset = dataset;
		mRenderer = renderer;
	}

	protected void setDatasetRenderer(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
		mDataset = dataset;
		mRenderer = renderer;
	}

	@Override
	public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {

		paint.setAntiAlias(mRenderer.isAntialiasing());
		screenRect.bottom = y + height;
		screenRect.top = y;
		screenRect.left = x;
		screenRect.right = x + width;
		int[] margins = mRenderer.getMargins();
		drawRect.left = x + margins[1];
		drawRect.top = y + margins[0];
		drawRect.right = x + width - margins[3];
		drawRect.bottom = y + height - margins[2];

		drawBackground(mRenderer, canvas, x, y, width, height, paint, false, Color.TRANSPARENT);

		if (paint.getTypeface() == null || (mRenderer.getTextTypeface() != null && paint.getTypeface().equals(mRenderer.getTextTypeface()))
				|| !paint.getTypeface().toString().equals(mRenderer.getTextTypefaceName()) || paint.getTypeface().getStyle() != mRenderer.getTextTypefaceStyle()) {
			if (mRenderer.getTextTypeface() != null) {
				paint.setTypeface(mRenderer.getTextTypeface());
			}
			else {
				paint.setTypeface(Typeface.create(mRenderer.getTextTypefaceName(), mRenderer.getTextTypefaceStyle()));
			}
		}

		drawAxis(canvas, paint);
		drawlables: {
			if (!mRenderer.isShowLabels()) break drawlables;
			drawXLabel(canvas, paint);
			drawYLabel(canvas, paint);

		}

		centerPoint = new PointF((x + width) / 2, (y + height) / 2);
		int sLength = mDataset.getSeriesCount();
		for (int i = 0; i < sLength; i++) {
			XYSeries series = mDataset.getSeriesAt(i);
			synchronized (series) {
				drawSeries(canvas, paint, series);
			}
		}
	}

	protected abstract void drawSeries(Canvas canvas, Paint paint, XYSeries series);

	protected void drawXLabel(Canvas canvas, Paint paint) {
		if (!mRenderer.isShowXAxes()) return;
		paint.setColor(mRenderer.getLabelsColor());
		paint.setStyle(Style.STROKE);
		paint.setTextSize(mRenderer.getLabelsTextSize());
		paint.setStrokeJoin(BasicStroke.SOLID.getJoin());
		paint.setStrokeWidth(mRenderer.getLineWidth());
		float[] xLabels = getXLabels(mRenderer.getXLabels());
		for (int i = 0; i < xLabels.length; i++) {
			if (mRenderer.isShowGridY()) {
				path.reset();
				path.moveTo(xLabels[i], drawRect.top);
				path.lineTo(xLabels[i], drawRect.bottom);
				canvas.drawPath(path, paint);
			}
			canvas.drawText("" + xLabels[i], xLabels[i], drawRect.bottom, paint);
		}
	}

	protected void drawYLabel(Canvas canvas, Paint paint) {
		if (!mRenderer.isShowYAxes()) return;
		paint.setColor(mRenderer.getLabelsColor());
		paint.setTextSize(mRenderer.getLabelsTextSize());
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(mRenderer.getLineWidth());
		float[] yLabels = getYLabels(mRenderer.getYLabels());
		for (int i = 0; i < yLabels.length; i++) {
			if (mRenderer.isShowGridX()) {
				path.reset();
				path.moveTo(drawRect.left, yLabels[i]);
				path.lineTo(drawRect.right, yLabels[i]);
				canvas.drawPath(path, paint);
			}
			canvas.drawText("" + yLabels[i], drawRect.left, yLabels[i], paint);
		}
	}

	protected void drawAxis(Canvas canvas, Paint paint) {
		paint.setColor(mRenderer.getLabelsColor());
		paint.setStrokeJoin(BasicStroke.SOLID.getJoin());
		paint.setStrokeWidth(mRenderer.getLineWidth());

		canvas.drawLine(screenRect.left, drawRect.bottom, screenRect.right, drawRect.bottom, paint);
		canvas.drawLine(drawRect.left, drawRect.top, drawRect.left, drawRect.bottom, paint);
		canvas.drawLine(screenRect.left, drawRect.top, screenRect.right, drawRect.top, paint);
	}

	protected float[] getXLabels(int count) {
		float step = (drawRect.right - drawRect.left) / (count - 1);
		float start = (float) (Math.ceil(drawRect.left / step) * step);
		float[] ret = new float[count];
		for (int i = 0; i < count; i++) {
			ret[i] = start + i * step;
		}
		return ret;
	}

	protected float[] getYLabels(int count) {
		float step = (drawRect.bottom - drawRect.top) / (count - 1);
		float start = (float) (Math.ceil(drawRect.bottom / step) * step);
		float[] ret = new float[count];
		for (int i = 0; i < count; i++) {
			ret[i] = start - i * step;
		}
		return ret;
	}

}
