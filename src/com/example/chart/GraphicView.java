package com.example.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
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

import com.example.chart.model.XYMultipleSeriesDataset;
import com.example.chart.model.XYSeries;
import com.example.chart.renderer.XYMultipleSeriesRenderer;
import com.example.chart.tool.ZoomSelector;
import com.example.chart.tool.ZoomSelector.Operate;
import com.example.chart.util.PointUtils;
import com.example.demo.R;

public class GraphicView extends View implements OnGestureListener {

	public static final String		TAG		= "LinearChart";
	private Scroller				mScroller;
	private GestureDetectorCompat	gesture;

	private int						mTouchSlop;

	private AbstractChart			mChart;
	private Paint					paint	= new Paint();
	private String					type;

	public GraphicView(Context context, AttributeSet attrs) {
		this(context, attrs, null);
	}

	public GraphicView(Context context, AttributeSet attrs, AbstractChart mChart) {
		super(context, attrs);
		mScroller = new Scroller(context);
		gesture = new GestureDetectorCompat(context, this);
		if (attrs != null) {
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CChart);
			type = array.getString(R.styleable.CChart_type);
			Log.d(getClass().getName(), "type:" + type);
		}
		init(context);
	}

	public GraphicView(Context context, AbstractChart mChart) {
		this(context, null, mChart);
	}

	private void init(Context context) {
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mChart == null) {
			mChart = devCreateEmptyView();
		}
		if (mChart instanceof XYChart) {
			mChart.draw(canvas, getScrollX(), getScrollY(), getMeasuredWidth(), getMeasuredHeight(), paint);
		}

	}

	private AbstractChart devCreateEmptyView() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		XYSeries.RendererBuilder builder = new XYSeries.RendererBuilder();
		XYSeries series = builder.setColor(Color.GRAY).setFillPoints(true).setTextSize(50).setTitle("chart").build();
		dataset.addSeries(series);
		AbstractChart chart = new LineChart(dataset, renderer);
		return chart;
	}

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
		msg.arg2 = (int) distanceY;
		msg.what = 1;
		handler.sendMessage(msg);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		mScroller.startScroll(getScrollX(), getScrollY(), -(int) (velocityX * 0.3), -(int) (velocityY * 0.3), 600);
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
										GraphicView.this.scrollBy(msg.arg1, msg.arg2);
										break;
									case 2:
										scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
										break;
									default:
										break;
									}
									postInvalidate();
								};
							};

}
