package com.example.chart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.example.chart.model.XYSeries;
import com.example.chart.tool.DataChangeListener;
import com.example.chart.tool.ZoomSelector;
import com.example.chart.tool.DataChangeListener.DataType;
import com.example.chart.tool.ZoomSelector.Operate;
import com.example.chart.util.PointUtils;
import com.example.chart.util.Util;
import com.example.demo.R;

public class HearRateChart extends View implements OnGestureListener {

	public static final String		TAG					= "LinearChart";
	private Scroller				mScroller;
	private GestureDetectorCompat	gesture;
	private Paint					paint				= new Paint();
	private Path					path				= new Path();

	private float					leftPadding			= 80, rightPadding, topPadding = 60, bottomPadding;
	private float					xStep, yStep, height, width;

	private float					left, top, bottom, right;
	private final int				xLabels				= 7;
	private final int				yLabes				= 9;
	/**
	 * 不缩放的情况下显示的时长
	 */
	private final long				initTotalInterval	= 12 * 60 * 60;

	private int						color				= Color.parseColor("#3edada");
	private int						abnormalColor		= Color.parseColor("#fa8b28");
	private int						greyColor			= Color.parseColor("#d8d5d5");

	private XYSeries				series				= new XYSeries();
	private XYSeries				series1				= new XYSeries();

	private static final double		MIN_VALUE			= 0;
	private static final double		MAX_VALUE			= 240;

	private float					low					= 120, high = 60;

	private Rect					rect				= new Rect();

	private int						mTouchSlop;
	// TODO 缩放相关可以抽离处理
	private short					zoomRate			= 1;
	private long					initTimemillis		= Calendar.getInstance().getTimeInMillis();

	PathEffect						effect				= new DashPathEffect(new float[] { 6, 6, 6, 6 }, 20);
	Bitmap							bitmap				= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	private DataChangeListener		listener;

	public void addDataChangeLisnter(DataChangeListener listener) {
		this.listener = listener;
		requestData(DataType.TYPE_MINUTE);
		requestData(DataType.TYPE_SECOND);
		devAddPoint();
	}

	private void devAddPoint() {
		for (int i = 0; i < 1000; i++) {
			series.addPoint(i, (float) (Math.random() + 100));
			series1.addPoint(i, (float) (Math.random() + 100));
		}
	}

	public HearRateChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		gesture = new GestureDetectorCompat(context, this);
		init(context);
	}

	public void setLimite(float low, float high) {
		this.low = low;
		this.high = high;
	}

	public HearRateChart(Context context) {
		super(context);
		mScroller = new Scroller(context);
		gesture = new GestureDetectorCompat(context, this);
		init(context);
	}

	private void init(Context context) {
		initPaint();
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		scrollTo((int) -leftPadding, 0);
	}

	private void requestData(DataChangeListener.DataType type) {
		if (type == null || listener == null) return;
		if (type == DataType.TYPE_MINUTE) {
			float[] data = listener.applyData(this.series.Size(), this.series.Size() + DataChangeListener.PER_LENGTH, DataChangeListener.DataType.TYPE_MINUTE);
			if (data != null) {
				for (int i = 0; i < data.length; i++) {
					this.series.addPoint(this.series.Size() + 1, data[i]);
				}
				postInvalidate();
			}
		}
		else {
			float[] data = listener.applyData(this.series1.Size(), this.series1.Size() + DataChangeListener.PER_LENGTH, DataChangeListener.DataType.TYPE_SECOND);
			if (data != null) {
				for (int i = 0; i < data.length; i++) {
					this.series1.addPoint(this.series1.Size() + 1, data[i]);
				}
				postInvalidate();
			}
		}
	}

	private void initCoordinate() {
		yStep = (getMeasuredHeight() - topPadding - bottomPadding) / (yLabes - 1);
		xStep = (getMeasuredWidth() - leftPadding - rightPadding) / (xLabels - 1);
		top = topPadding;
		bottom = getMeasuredHeight();
		height = bottom - top;
		width = getMeasuredWidth() - leftPadding - rightPadding;
	}

	/**
	 * 实时获取当前屏幕上的左边界
	 * 
	 * @return
	 */
	private float getChartLeft() {
		return getScrollX() + leftPadding;
	}

	/**
	 * 实时获取当前屏幕的右边界
	 * 
	 * @return
	 */
	private float getChartRight() {
		return getScrollX() + getWidth() - rightPadding;
	}

	private void initPaint() {
		paint.setStyle(Style.STROKE);
		paint.setTextSize(50);
	}

	/**
	 * 将y轴的实际值转成屏幕上对应的坐标
	 * 
	 * @param value
	 * @return
	 */
	private float getYCoordinate(float value) {
		return (float) (bottom - getRealYRatio(value) * height);
	}

	/**
	 * 获取数值在其范围中的比例，超过则取最大值
	 * 
	 * @return
	 */
	private double getRealYRatio(float value) {
		return (value - MIN_VALUE) / (MAX_VALUE - MIN_VALUE) >= 1 ? 1 : (value - MIN_VALUE) / (MAX_VALUE - MIN_VALUE);
	}

	private float[] calcXLabels() {
		float[] lables = new float[xLabels + 2];
		float xStart = (float) (Math.ceil(getChartLeft() / xStep) * xStep);
		for (int i = 0; i <= xLabels + 1; i++) {
			lables[i] = xStart + xStep * (i - 1);
		}
		return lables;
	}

	// TODO======================与具体的需求相关，待可以抽离到子类=============//
	private float getRatio() {
		switch (zoomRate) {
		case 1:
			return 1.0f / 120;
		case 10:
			return 1.0f / 12;
		case 100:
			return 1.0f / 60;
		default:
			break;
		}
		return Float.valueOf((xLabels - 1)) / (initTotalInterval / zoomRate);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
		initCoordinate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawYLabel(canvas, paint);

		drawAxis(canvas, paint);

		drawXLabel(canvas, paint);

		drawHorizontalLine(canvas, paint);

		drawSeries(canvas, paint, greyColor, zoomRate == 100 ? series1 : series);

		drawCenterLine(canvas, paint);

	}

	private void drawCenterLine(Canvas canvas, Paint paint) {
		paint.setColor(color);
		paint.setStrokeWidth(6f);
		paint.setStyle(Style.FILL_AND_STROKE);
		float centerX = (getChartLeft() + getChartRight()) / 2;
		canvas.drawLine(centerX, top, centerX, bottom, paint);
	}

	private void drawHorizontalLine(Canvas canvas, Paint paint) {
		paint.setColor(abnormalColor);
		paint.setStrokeWidth(2f);
		paint.setStyle(Style.FILL_AND_STROKE);

		canvas.drawLine(getChartLeft(), getYCoordinate(low), getChartRight(), getYCoordinate(low), paint);
		canvas.drawLine(getChartLeft(), getYCoordinate(high), getChartRight(), getYCoordinate(high), paint);
	}

	private void drawSeries(Canvas canvas, Paint paint, int color, XYSeries series) {
		paint.setColor(color);
		paint.setStrokeWidth(7f);
		paint.setStyle(Style.STROKE);
		paint.setTextSize(30);
		canvas.save();
		rect.left = (int) getChartLeft();
		rect.right = (int) getChartRight();
		rect.top = 0;
		rect.bottom = getMeasuredHeight();
		canvas.clipRect(rect);
		double first, last;
		first = calcXLabels()[0] / xStep / getRatio();
		last = calcXLabels()[calcXLabels().length - 1] / xStep / getRatio();
		if (series.Size() < last) requestData(zoomRate == 100 ? DataType.TYPE_SECOND : DataType.TYPE_MINUTE);
		List<PointF> list = series.getList(first, last);
		float tmp = xStep * getRatio();
		float centerX = (getChartLeft() + getChartRight()) / 2;
		PointF retPoint = new PointF(centerX, bottom);
		for (int i = 0; i < list.size(); i++) {
			PointF p = list.get(i);
			PointF p1 = list.get((i + 1) >= (list.size()) ? (list.size() - 1) : (i + 1));
			if (p.y == -1 || p1.y == -1) continue;
			canvas.drawLine(p.x * xStep * getRatio(), getYCoordinate(p.y), p1.x * xStep * getRatio(), getYCoordinate(p1.y), paint);
			if (Math.abs(p.x * xStep * getRatio() - centerX) < tmp) {
				retPoint = p;
				tmp = Math.abs(p.x * xStep * getRatio() - centerX);
			}
		}
		paint.setStyle(Style.FILL);
		paint.setColor(this.color);
		if (retPoint.y != bottom) {
			canvas.drawBitmap(bitmap, retPoint.x * xStep * getRatio() + 20, getYCoordinate(retPoint.y) - 80, paint);
			canvas.drawText(formatTime((int) (retPoint.x * (zoomRate == 100 ? 1 : 60))), centerX + 50, getYCoordinate(retPoint.y) - 30, paint);
			canvas.drawText(retPoint.y + "", centerX + 50, getYCoordinate(retPoint.y) + 20, paint);
			canvas.drawText("bmp", centerX + 125, getYCoordinate(retPoint.y) + 20, paint);
			canvas.drawCircle(centerX, getYCoordinate(retPoint.y), 20, paint);
		}
		else {
			canvas.drawCircle(centerX, bottom - 25, 20, paint);
		}
		paint.setStyle(Style.STROKE);
		canvas.restore();
	}

	private void drawXLabel(Canvas canvas, Paint paint) {
		paint.setColor(greyColor);
		paint.setStrokeWidth(2f);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(30);
		canvas.save();
		rect.left = (int) getChartLeft();
		rect.right = (int) getChartRight();
		rect.top = 0;
		rect.bottom = (int) this.bottom;
		canvas.clipRect(rect);
		float labels[] = calcXLabels();
		String label = "";
		for (int i = 0; i < labels.length; i++) {
			path.reset();
			path.moveTo(labels[i], bottom);
			path.lineTo(labels[i], top);
			paint.setPathEffect(effect);
			canvas.drawPath(path, paint);
			paint.setPathEffect(null);
			if (Math.round(labels[i] / xStep) > 0) {
				switch (zoomRate) {
				case 1:
					label = Math.round(labels[i] / xStep) * 2 + "h";
					break;
				case 10:
					label = Util.getDisplayTextFromSecond(Math.round(labels[i] / xStep) * 12 * 60);
					break;
				case 100:
					label = Util.getDisplayTextFromSecond(Math.round(labels[i] / xStep) * 60);
					break;
				default:
					break;
				}

				canvas.drawText(label, labels[i] - 15, top - 5, paint);
			}
		}
		canvas.restore();

	}

	private void drawAxis(Canvas canvas, Paint paint) {
		paint.setColor(Color.GRAY);
		paint.setStyle(Style.FILL);
		paint.setPathEffect(null);
		paint.setStrokeWidth(3);
		// canvas.drawLine(getChartLeft(), top, getChartLeft(), bottom, paint);
		canvas.drawLine(getScrollX(), top, getScrollX() + getWidth(), top, paint);
		canvas.drawLine(getScrollX(), bottom - 2, getScrollX() + getWidth(), bottom - 2, paint);
	}

	private void drawYLabel(Canvas canvas, Paint paint) {
		paint.setColor(greyColor);
		paint.setStrokeWidth(2f);
		paint.setTextSize(30);
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.save();
		rect.left = getScrollX();
		rect.right = getScrollX() + getMeasuredWidth();
		rect.top = (int) this.top + 5;
		rect.bottom = (int) this.bottom;
		canvas.clipRect(rect);
		for (int i = 1; i < yLabes; i++) {
			path.reset();
			paint.setPathEffect(effect);
			path.moveTo(getChartLeft(), bottom - yStep * (i - 0.5f));
			path.lineTo(getChartRight(), bottom - yStep * (i - 0.5f));
			canvas.drawPath(path, paint);
			paint.setPathEffect(null);
			canvas.drawText("" + i * 30, getChartLeft() - 50, bottom - yStep * i, paint);
			canvas.drawLine(getChartLeft(), bottom - yStep * i, getChartRight(), bottom - yStep * i, paint);
		}
		paint.setPathEffect(null);
		canvas.restore();
	}

	// TODO 粗糙的缩放方式
	private ZoomSelector	selector;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			selector = new ZoomSelector(getWidth(), PointUtils.getDistance(new PointF(event.getX(0), event.getX(0)), new PointF(event.getX(1), event.getX(1))));
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() > 1) {
				Operate state = selector.getState(PointUtils.getDistance(new PointF(event.getX(0), event.getY(0)), new PointF(event.getX(1), event.getY(1))));
				if (state == null) break;
				switch (state) {
				case ZoomOut:
					switch (zoomRate) {
					case 1:
						Log.d(TAG, "当前已经是最小倍数");
						break;
					case 10:
						zoomRate = 1;
						break;
					case 100:
						zoomRate = 10;
					default:
						break;
					}
					break;
				case ZoomIn:
					switch (zoomRate) {
					case 1:
						zoomRate = 10;
						break;
					case 10:
						zoomRate = 100;
						break;
					case 100:
						Log.d(TAG, "已经是最大倍数");
					}
					break;
				}
				handler.sendEmptyMessage(-1);
			}
			break;
		default:
			break;
		}
		if (event.getPointerCount() == 1) return gesture.onTouchEvent(event);
		else
			return false;
	}

	private void clear() {
		handler.removeMessages(1);
		handler.removeMessages(2);
		handler.removeMessages(-1);
		mScroller.forceFinished(true);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		clearAnimation();
		clear();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = (int) distanceX;
		msg.what = 1;
		handler.sendMessage(msg);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		mScroller.startScroll(getScrollX(), 0, -(int) (velocityX * 0.3), 0, 600);
		return false;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			handler.sendEmptyMessage(2);
			postInvalidate();
		}
	}

	private Handler	handler	= new Handler() {
								public void dispatchMessage(android.os.Message msg) {
									switch (msg.what) {
									case 1:
										HearRateChart.this.scrollBy(msg.arg1, 0);
										break;
									case 2:
										scrollTo(mScroller.getCurrX(), 0);
										break;
									default:
										break;
									}
									if (getScrollX() < -leftPadding - width / 2) scrollTo((int) (-leftPadding - width / 2), 0);
									float rightLimite = zoomRate == 100 ? series1.Size() * getRatio() * xStep : series.Size() * getRatio() * xStep;
									if (getScrollX() >= rightLimite) scrollTo((int) rightLimite, 0);
									postInvalidate();
								};
							};

	private String formatTime(int timeseconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(initTimemillis + timeseconds * 1000);
		return sdf.format(cal.getTime());
	}

}
