package com.example.chart.tool;

public interface DataChangeListener {

	public enum DataType {
		TYPE_SECOND, TYPE_MINUTE
	}

	public static final int	PER_LENGTH	= 180;

	/**
	 * 
	 * @param start
	 *            起点从0开始
	 * @param end
	 * @param type
	 *            数据源的类别，目前是取点间隔为分钟和秒两种
	 * @return
	 */
	public float[] applyData(int start, int end, DataType type);

}
