package com.example.chart.renderer;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Multiple XY series renderer. 多个XY系列渲染器。
 */
public class XYMultipleSeriesRenderer extends DefaultRenderer {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * 柱状图的宽度
	 */
	private float				customWidth;

	/**
	 * 是否设置柱状图宽度
	 */
	private boolean				isCustomWidth		= false;

	public boolean isCustomWidth() {
		return isCustomWidth;
	}

	public float getCustomWidth() {
		return customWidth;
	}

	public void setCustomWith(float customWidth) {
		this.customWidth = customWidth;
		this.isCustomWidth = true;
	}

	private int	lineWidth	= 2;

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getLineWidth() {
		return this.lineWidth;
	}

	/** The X axis title. X轴标题 */
	private String								mXTitle					= "";
	/** The Y axis title. Y轴标题 */
	private String[]							mYTitle;
	/** The axis title text size. 轴标题文本的大小。 */
	private float								mAxisTitleTextSize		= 12;
	/** Y坐标标签的上限 **/
	private double								mMaxLabelY;
	/** Y坐标标签的下限 */
	private double								mMinLabelY;

	/**
	 * The approximative number of labels on the x axis.
	 * 近似的数量的标签在x轴上。(应该就是间隔距离大小)
	 */
	private int									mXLabels				= 5;
	/**
	 * The approximative number of labels on the y axis.
	 * 近似的数量的标签在y轴上。(应该就是间隔距离大小)
	 */
	private int									mYLabels				= 5;
	/** The X axis text labels. X轴的文本标签。 */
	private Map<Double, String>					mXTextLabels			= new HashMap<Double, String>();
	/** The Y axis text labels. Y轴的文本标签。 */
	private Map<Integer, Map<Double, String>>	mYTextLabels			= new LinkedHashMap<Integer, Map<Double, String>>();
	/** A flag for enabling or not the pan on the X axis. 一个标记,使X轴上的平移。(X轴是否被平移) */
	private boolean								mPanXEnabled			= true;
	/** A flag for enabling or not the pan on the Y axis. 一个标记,使Y轴上的平移。(Y轴是否被平移) */
	private boolean								mPanYEnabled			= true;
	/**
	 * A flag for enabling or not the zoom on the X axis. 一个标记,使X轴上的缩放。(X轴是否被缩放)
	 */
	private boolean								mZoomXEnabled			= true;
	/**
	 * A flag for enabling or not the zoom on the Y axis .
	 * 一个标记,使Y轴上的缩放。(Y轴是否被缩放)
	 */
	private boolean								mZoomYEnabled			= true;
	/** The spacing between bars, in bar charts. 在条形图中，俩条形图之间的间距。 */
	private double								mBarSpacing				= 0;
	/** The margins colors. 边缘的颜色。 */
	private int									mMarginsColor			= NO_COLOR;
	/** The pan limits. 平移的限制。 */
	private double[]							mPanLimits;
	/** The zoom limits. 缩放的限制。 */
	private double[]							mZoomLimits;
	/** The X axis labels rotation angle. X轴标签旋转角度。 */
	private float								mXLabelsAngle;
	/** The Y axis labels rotation angle. Y轴标签旋转角度。 */
	private float								mYLabelsAngle;
	/** The initial axis range. 初始轴范围。 */
	private Map<Integer, double[]>				initialRange			= new LinkedHashMap<Integer, double[]>();
	/** The point size for charts displaying points. 图表中显示点的大小。 */
	private float								mPointSize				= 3;
	/** The grid color. 网格的颜色。 */
	private int									mGridColor				= Color.argb(75, 150, 150, 150);
	/** The number of scales. 尺度的数量。 */
	private int									scalesCount;
	/** The X axis labels alignment. X轴标签对齐。 */
	private Align								xLabelsAlign			= Align.CENTER;
	/** The Y axis labels alignment. Y轴标签对齐。 */
	private Align[]								yLabelsAlign;
	/** The X text label padding. X坐标值与坐标的 Padding 值。 */
	private float								mXLabelsPadding			= 0;
	/** The Y text label padding. Y坐标值与坐标的 Padding 值。 */
	private float								mYLabelsPadding			= 0;
	/** The Y axis labels vertical padding. Y轴标签的垂直间距。 */
	private float								mYLabelsVerticalPadding	= 0;
	/** The Y axis alignment. Y轴对齐。 */
	private Align[]								yAxisAlign;
	/** The X axis labels color. X轴标签的颜色。 */
	private int									mXLabelsColor			= TEXT_COLOR;
	/** The Y axis labels color. Y轴标签的颜色。 */
	private int[]								mYLabelsColor			= new int[] { TEXT_COLOR };
	/**
	 * If X axis value selection algorithm to be used. Only used by the time
	 * charts. 如果要使用X轴值选择算法。仅使用在时间图表。
	 */
	private boolean								mXRoundedLabels			= true;
	/** The label format. 标签格式。 */
	private NumberFormat						mLabelFormat;

	public void setMaxLabelY(double mMaxLabelY) {
		this.mMaxLabelY = mMaxLabelY;
	}

	public double getMaxLabelY() {
		return mMaxLabelY;
	}

	public void setMinLabelY(double mMinLabelY) {
		this.mMinLabelY = mMinLabelY;
	}

	public double getMinLabelY() {
		return mMinLabelY;
	}

	/**
	 * Returns the title for the X axis. 返回X轴的标题。
	 * 
	 * @return the X axis title
	 */
	public String getXTitle() {
		return mXTitle;
	}

	/**
	 * Sets the title for the X axis. 设置X轴的标题。
	 * 
	 * @param title
	 *            the X axis title
	 */
	public void setXTitle(String title) {
		mXTitle = title;
	}

	/**
	 * Returns the title for the Y axis. 返回Y轴的标题。
	 * 
	 * @return the Y axis title
	 */
	public String getYTitle() {
		return getYTitle(0);
	}

	/**
	 * Returns the title for the Y axis.
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the Y axis title
	 */
	public String getYTitle(int scale) {
		return mYTitle[scale];
	}

	/**
	 * Sets the title for the Y axis. 设置Y轴的标题。
	 * 
	 * @param title
	 *            the Y axis title
	 */
	public void setYTitle(String title) {
		setYTitle(title, 0);
	}

	/**
	 * Sets the title for the Y axis.
	 * 
	 * @param title
	 *            the Y axis title
	 * @param scale
	 *            the renderer scale
	 */
	public void setYTitle(String title, int scale) {
		mYTitle[scale] = title;
	}

	/**
	 * Returns the axis title text size. 返回轴标题文本的大小。
	 * 
	 * @return the axis title text size
	 */
	public float getAxisTitleTextSize() {
		return mAxisTitleTextSize;
	}

	/**
	 * Sets the axis title text size. 设置轴标题文本的大小。
	 * 
	 * @param textSize
	 *            the chart axis text size
	 */
	public void setAxisTitleTextSize(float textSize) {
		mAxisTitleTextSize = textSize;
	}

	/**
	 * Returns the approximate number of labels for the X axis.
	 * 
	 * @return the approximate number of labels for the X axis
	 */
	public int getXLabels() {
		return mXLabels;
	}

	/**
	 * Sets the approximate number of labels for the X axis. 集的近似数量为X轴标签。
	 * 
	 * @param xLabels
	 *            the approximate number of labels for the X axis
	 */
	public void setXLabels(int xLabels) {
		mXLabels = xLabels;
	}

	/**
	 * Adds a new text label for the specified X axis value.
	 * 
	 * @param x
	 *            the X axis value
	 * @param text
	 *            the text label
	 * @deprecated use addXTextLabel instead
	 */
	public void addTextLabel(double x, String text) {
		addXTextLabel(x, text);
	}

	/**
	 * Adds a new text label for the specified X axis value.
	 * 
	 * @param x
	 *            the X axis value
	 * @param text
	 *            the text label
	 */
	public synchronized void addXTextLabel(double x, String text) {
		mXTextLabels.put(x, text);
	}

	/**
	 * Removes text label for the specified X axis value.
	 * 
	 * @param x
	 *            the X axis value
	 */
	public synchronized void removeXTextLabel(double x) {
		mXTextLabels.remove(x);
	}

	/**
	 * Returns the X axis text label at the specified X axis value.
	 * 
	 * @param x
	 *            the X axis value
	 * @return the X axis text label
	 */
	public synchronized String getXTextLabel(Double x) {
		return mXTextLabels.get(x);
	}

	/**
	 * Returns the X text label locations.
	 * 
	 * @return the X text label locations
	 */
	public synchronized Double[] getXTextLabelLocations() {
		return mXTextLabels.keySet().toArray(new Double[0]);
	}

	/**
	 * Clears the existing text labels.
	 * 
	 * @deprecated use clearXTextLabels instead
	 */
	public void clearTextLabels() {
		clearXTextLabels();
	}

	/**
	 * Clears the existing text labels on the X axis.
	 */
	public synchronized void clearXTextLabels() {
		mXTextLabels.clear();
	}

	/**
	 * If X axis labels should be rounded.
	 * 
	 * @return if rounded time values to be used
	 */
	public boolean isXRoundedLabels() {
		return mXRoundedLabels;
	}

	/**
	 * Sets if X axis rounded time values to be used.
	 * 
	 * @param rounded
	 *            rounded values to be used
	 */
	public void setXRoundedLabels(boolean rounded) {
		mXRoundedLabels = rounded;
	}

	/**
	 * Adds a new text label for the specified Y axis value.
	 * 
	 * @param y
	 *            the Y axis value
	 * @param text
	 *            the text label
	 */
	public void addYTextLabel(double y, String text) {
		addYTextLabel(y, text, 0);
	}

	/**
	 * Removes text label for the specified Y axis value.
	 * 
	 * @param y
	 *            the Y axis value
	 */
	public void removeYTextLabel(double y) {
		removeYTextLabel(y, 0);
	}

	/**
	 * Adds a new text label for the specified Y axis value.
	 * 
	 * @param y
	 *            the Y axis value
	 * @param text
	 *            the text label
	 * @param scale
	 *            the renderer scale
	 */
	public synchronized void addYTextLabel(double y, String text, int scale) {
		mYTextLabels.get(scale).put(y, text);
	}

	/**
	 * Removes text label for the specified Y axis value.
	 * 
	 * @param y
	 *            the Y axis value
	 * @param scale
	 *            the renderer scale
	 */
	public synchronized void removeYTextLabel(double y, int scale) {
		mYTextLabels.get(scale).remove(y);
	}

	/**
	 * Returns the Y axis text label at the specified Y axis value.
	 * 
	 * @param y
	 *            the Y axis value
	 * @return the Y axis text label
	 */
	public String getYTextLabel(Double y) {
		return getYTextLabel(y, 0);
	}

	/**
	 * Returns the Y axis text label at the specified Y axis value.
	 * 
	 * @param y
	 *            the Y axis value
	 * @param scale
	 *            the renderer scale
	 * @return the Y axis text label
	 */
	public synchronized String getYTextLabel(Double y, int scale) {
		return mYTextLabels.get(scale).get(y);
	}

	/**
	 * Returns the Y text label locations.
	 * 
	 * @return the Y text label locations
	 */
	public Double[] getYTextLabelLocations() {
		return getYTextLabelLocations(0);
	}

	/**
	 * Returns the Y text label locations.
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the Y text label locations
	 */
	public synchronized Double[] getYTextLabelLocations(int scale) {
		return mYTextLabels.get(scale).keySet().toArray(new Double[0]);
	}

	/**
	 * Clears the existing text labels on the Y axis.
	 */
	public void clearYTextLabels() {
		clearYTextLabels(0);
	}

	/**
	 * Clears the existing text labels on the Y axis.
	 * 
	 * @param scale
	 *            the renderer scale
	 */
	public synchronized void clearYTextLabels(int scale) {
		mYTextLabels.get(scale).clear();
	}

	/**
	 * Returns the approximate number of labels for the Y axis.
	 * 
	 * @return the approximate number of labels for the Y axis
	 */
	public int getYLabels() {
		return mYLabels;
	}

	/**
	 * Sets the approximate number of labels for the Y axis.
	 * 
	 * @param yLabels
	 *            the approximate number of labels for the Y axis
	 */
	public void setYLabels(int yLabels) {
		mYLabels = yLabels;
	}

	/**
	 * Sets if the chart point values should be displayed as text.
	 * 
	 * @param display
	 *            if the chart point values should be displayed as text
	 * @deprecated use SimpleSeriesRenderer.setDisplayChartValues() instead
	 */
	public void setDisplayChartValues(boolean display) {
		SimpleSeriesRenderer[] renderers = getSeriesRenderers();
		for (SimpleSeriesRenderer renderer : renderers) {
			renderer.setDisplayChartValues(display);
		}
	}

	/**
	 * Sets the chart values text size.
	 * 
	 * @param textSize
	 *            the chart values text size
	 * @deprecated use SimpleSeriesRenderer.setChartValuesTextSize() instead
	 */
	public void setChartValuesTextSize(float textSize) {
		SimpleSeriesRenderer[] renderers = getSeriesRenderers();
		for (SimpleSeriesRenderer renderer : renderers) {
			renderer.setChartValuesTextSize(textSize);
		}
	}

	/**
	 * Returns the enabled state of the pan on at least one axis.
	 * 
	 * @return if pan is enabled
	 */
	public boolean isPanEnabled() {
		return isPanXEnabled() || isPanYEnabled();
	}

	/**
	 * Returns the enabled state of the pan on X axis.
	 * 
	 * @return if pan is enabled on X axis
	 */
	public boolean isPanXEnabled() {
		return mPanXEnabled;
	}

	/**
	 * Returns the enabled state of the pan on Y axis.
	 * 
	 * @return if pan is enabled on Y axis
	 */
	public boolean isPanYEnabled() {
		return mPanYEnabled;
	}

	/**
	 * Sets the enabled state of the pan.
	 * 
	 * @param enabledX
	 *            pan enabled on X axis
	 * @param enabledY
	 *            pan enabled on Y axis
	 */
	public void setPanEnabled(boolean enabledX, boolean enabledY) {
		mPanXEnabled = enabledX;
		mPanYEnabled = enabledY;
	}

	/**
	 * Override {@link DefaultRenderer#setPanEnabled(boolean)} so it can be
	 * delegated to {@link #setPanEnabled(boolean, boolean)}.
	 */
	@Override
	public void setPanEnabled(final boolean enabled) {
		setPanEnabled(enabled, enabled);
	}

	/**
	 * Returns the enabled state of the zoom on at least one axis. 返回
	 * 在X轴或是Y轴上是否可以被缩放，有一个可以则返回true; 父类中也有
	 * 
	 * @return if zoom is enabled
	 */
	public boolean isZoomEnabled() {
		return isZoomXEnabled() || isZoomYEnabled();
	}

	/**
	 * Returns the enabled state of the zoom on X axis. 返回 在X轴是否可以被缩放;
	 * 
	 * @return if zoom is enabled on X axis
	 */
	public boolean isZoomXEnabled() {
		return mZoomXEnabled;
	}

	/**
	 * Returns the enabled state of the zoom on Y axis. 返回 在Y轴上是否可以被缩放;
	 * 
	 * @return if zoom is enabled on Y axis
	 */
	public boolean isZoomYEnabled() {
		return mZoomYEnabled;
	}

	/**
	 * Sets the enabled state of the zoom.
	 * 设置X轴和Y轴是否可以被缩放，父类也有这个方法，但传入的为一个值，也就是X、Y轴都可以缩放或是都不能
	 * 
	 * @param enabledX
	 *            zoom enabled on X axis
	 * @param enabledY
	 *            zoom enabled on Y axis
	 */
	public void setZoomEnabled(boolean enabledX, boolean enabledY) {
		mZoomXEnabled = enabledX;
		mZoomYEnabled = enabledY;
	}

	/**
	 * Returns the spacing between bars, in bar charts.
	 * 
	 * @return the spacing between bars
	 * @deprecated use getBarSpacing instead
	 */
	public double getBarsSpacing() {
		return getBarSpacing();
	}

	/**
	 * Returns the spacing between bars, in bar charts.
	 * 
	 * @return the spacing between bars
	 */
	public double getBarSpacing() {
		return mBarSpacing;
	}

	/**
	 * Sets the spacing between bars, in bar charts. Only available for bar
	 * charts. This is a coefficient of the bar width. For instance, if you want
	 * the spacing to be a half of the bar width, set this value to 0.5.
	 * 
	 * @param spacing
	 *            the spacing between bars coefficient
	 */
	public void setBarSpacing(double spacing) {
		mBarSpacing = spacing;
	}

	/**
	 * Returns the margins color. 返回 边缘的颜色。
	 * 
	 * @return the margins color
	 */
	public int getMarginsColor() {
		return mMarginsColor;
	}

	/**
	 * Sets the color of the margins. 设置 边缘的颜色。
	 * 
	 * @param color
	 *            the margins color
	 */
	public void setMarginsColor(int color) {
		mMarginsColor = color;
	}

	/**
	 * Returns the grid color.
	 * 
	 * @return the grid color
	 */
	public int getGridColor() {
		return mGridColor;
	}

	/**
	 * Sets the color of the grid.
	 * 
	 * @param color
	 *            the grid color
	 */
	public void setGridColor(int color) {
		mGridColor = color;
	}

	/**
	 * Returns the pan limits.
	 * 
	 * @return the pan limits
	 */
	public double[] getPanLimits() {
		return mPanLimits;
	}

	/**
	 * Sets the pan limits as an array of 4 values. Setting it to null or a
	 * different size array will disable the panning limitation. Values:
	 * [panMinimumX, panMaximumX, panMinimumY, panMaximumY] 设置平移的限制。{-10, 20,
	 * -10, 40} 表示，最左移到-10，最右移到20，最下移到-10，最上移到40
	 */
	public void setPanLimits(double[] panLimits) {
		mPanLimits = panLimits;
	}

	/**
	 * Returns the zoom limits. 返回 缩放的限制。{-10, 20, -10, 40}
	 * 表示，最左缩小到-10，最右缩小到20，最下缩小到-10，最上缩小到40
	 */
	public double[] getZoomLimits() {
		return mZoomLimits;
	}

	/**
	 * Sets the zoom limits as an array of 4 values. Setting it to null or a
	 * different size array will disable the zooming limitation. Values:
	 * [zoomMinimumX, zoomMaximumX, zoomMinimumY, zoomMaximumY] 设置 缩放的限制。{-10,
	 * 20, -10, 40} 表示，最左缩小到-10，最右缩小到20，最下缩小到-10，最上缩小到40
	 * 
	 * @param zoomLimits
	 *            the zoom limits
	 */
	public void setZoomLimits(double[] zoomLimits) {
		mZoomLimits = zoomLimits;
	}

	/**
	 * Returns the rotation angle of labels for the X axis.
	 * 
	 * @return the rotation angle of labels for the X axis
	 */
	public float getXLabelsAngle() {
		return mXLabelsAngle;
	}

	/**
	 * Sets the rotation angle (in degrees) of labels for the X axis.
	 * 
	 * @param angle
	 *            the rotation angle of labels for the X axis
	 */
	public void setXLabelsAngle(float angle) {
		mXLabelsAngle = angle;
	}

	/**
	 * Returns the rotation angle of labels for the Y axis.
	 * 
	 * @return the approximate number of labels for the Y axis
	 */
	public float getYLabelsAngle() {
		return mYLabelsAngle;
	}

	/**
	 * Sets the rotation angle (in degrees) of labels for the Y axis.
	 * 
	 * @param angle
	 *            the rotation angle of labels for the Y axis
	 */
	public void setYLabelsAngle(float angle) {
		mYLabelsAngle = angle;
	}

	/**
	 * Returns the size of the points, for charts displaying points.
	 * 
	 * @return the point size
	 */
	public float getPointSize() {
		return mPointSize;
	}

	/**
	 * Sets the size of the points, for charts displaying points.
	 * 
	 * @param size
	 *            the point size
	 */
	public void setPointSize(float size) {
		mPointSize = size;
	}

	/**
	 * Sets the axes initial range values. This will be used in the zoom fit
	 * tool.
	 * 
	 * @param range
	 *            an array having the values in this order: minX, maxX, minY,
	 *            maxY
	 */
	public void setInitialRange(double[] range) {
		setInitialRange(range, 0);
	}

	/**
	 * Sets the axes initial range values. This will be used in the zoom fit
	 * tool.
	 * 
	 * @param range
	 *            an array having the values in this order: minX, maxX, minY,
	 *            maxY
	 * @param scale
	 *            the renderer scale
	 */
	public void setInitialRange(double[] range, int scale) {
		initialRange.put(scale, range);
	}

	/**
	 * Returns the X axis labels color.
	 * 
	 * @return the X axis labels color
	 */
	public int getXLabelsColor() {
		return mXLabelsColor;
	}

	/**
	 * Returns the Y axis labels color.
	 * 
	 * @return the Y axis labels color
	 */
	public int getYLabelsColor(int scale) {
		return mYLabelsColor[scale];
	}

	/**
	 * Sets the X axis labels color.
	 * 
	 * @param color
	 *            the X axis labels color
	 */
	public void setXLabelsColor(int color) {
		mXLabelsColor = color;
	}

	/**
	 * Sets the Y axis labels color.
	 * 
	 * @param scale
	 *            the renderer scale
	 * @param color
	 *            the Y axis labels color
	 */
	public void setYLabelsColor(int scale, int color) {
		mYLabelsColor[scale] = color;
	}

	/**
	 * Returns the X axis labels alignment. 返回 X轴标签的对齐方式。也就是值是在文本的左边、右边还是中间
	 * 
	 * @return X labels alignment
	 */
	public Align getXLabelsAlign() {
		return xLabelsAlign;
	}

	/**
	 * Sets the X axis labels alignment. 设置 X轴标签对齐。也就是值是在文本的左边、右边还是中间
	 * 
	 * @param align
	 *            the X labels alignment
	 */
	public void setXLabelsAlign(Align align) {
		xLabelsAlign = align;
	}

	/**
	 * Returns the Y axis labels alignment. 返回 Y轴标签的对齐方式。也就是值是在文本的左边、右边还是中间
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return Y labels alignment
	 */
	public Align getYLabelsAlign(int scale) {
		return yLabelsAlign[scale];
	}

	public void setYLabelsAlign(Align align) {
		setYLabelsAlign(align, 0);
	}

	public Align getYAxisAlign(int scale) {
		return yAxisAlign[scale];
	}

	public void setYAxisAlign(Align align, int scale) {
		yAxisAlign[scale] = align;
	}

	/**
	 * Sets the Y axis labels alignment. 设置 Y轴标签对齐。也就是值是在文本的左边、右边还是中间
	 * 
	 * @param align
	 *            the Y labels alignment
	 */
	public void setYLabelsAlign(Align align, int scale) {
		yLabelsAlign[scale] = align;
	}

	/**
	 * Returns the X labels padding. 返回 X坐标值与坐标的 Padding 值
	 * 
	 * @return X labels padding
	 */
	public float getXLabelsPadding() {
		return mXLabelsPadding;
	}

	/**
	 * Sets the X labels padding 设置 X坐标值与坐标的 Padding 值
	 * 
	 * @param padding
	 *            the amount of padding between the axis and the label
	 */
	public void setXLabelsPadding(float padding) {
		mXLabelsPadding = padding;
	}

	/**
	 * Returns the Y labels padding. 返回 Y坐标值与坐标的 Padding 值
	 * 
	 * @return Y labels padding
	 */
	public float getYLabelsPadding() {
		return mYLabelsPadding;
	}

	/**
	 * Sets the Y labels vertical padding
	 * 
	 * @param padding
	 *            the amount of vertical padding
	 */
	public void setYLabelsVerticalPadding(float padding) {
		mYLabelsVerticalPadding = padding;
	}

	/**
	 * Returns the Y labels vertical padding.
	 * 
	 * @return Y labels vertical padding
	 */
	public float getYLabelsVerticalPadding() {
		return mYLabelsVerticalPadding;
	}

	/**
	 * Sets the Y labels padding 设置 Y坐标值与坐标的 Padding 值
	 * 
	 * @param padding
	 *            the amount of padding between the axis and the label
	 */
	public void setYLabelsPadding(float padding) {
		mYLabelsPadding = padding;
	}

	/**
	 * Returns the number format for displaying labels.
	 * 
	 * @return the number format for labels
	 */
	public NumberFormat getLabelFormat() {
		return mLabelFormat;
	}

	/**
	 * Sets the number format for displaying labels.
	 * 
	 * @param format
	 *            the number format for labels
	 */
	public void setLabelFormat(NumberFormat format) {
		mLabelFormat = format;
	}

	/**
	 * 返回 尺度的数量。
	 * 
	 * @return
	 */
	public int getScalesCount() {
		return scalesCount;
	}

}
