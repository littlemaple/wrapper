package com.example.ui;

import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.demo.R;

/**
 * 
 * @author blue
 * 
 */
public class ZoomButton extends LinearLayout {

	private int					count		= 1;
	private int					limiteMin	= 1;
	private int					limiteMax	= Integer.MAX_VALUE;

	private static final int	increaseId	= 0x1001;
	private static final int	decreaseId	= 0x1002;

	public ZoomButton(Context context) {
		super(context);
		addComponent();
	}

	public ZoomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		addComponent();
	}

	private boolean isLocaleLangue() {
		if (Locale.getDefault().toString().equals(Locale.CHINA.toString()) || Locale.getDefault().toString().equals(Locale.CHINESE.toString())) return true;
		return false;
	}

	private String getIncreaseText() {
		if (isLocaleLangue()) return "增加";
		else
			return "Increase";
	}

	private String getDecreaseText() {
		if (isLocaleLangue()) return "减少";
		else
			return "Decrease";
	}

	public void setMinLimite(int value) {
		this.limiteMin = value;
	}

	public void setMaxLimite(int value) {
		this.limiteMax = value;
	}

	public void setLimite(int min, int max) {
		if (min >= max) throw new IllegalArgumentException("The limite max value must be bigger than min value");
		setMaxLimite(max);
		setMinLimite(min);
	}

	/**
	 * @attention it will set the extreme value if the value greater than
	 *            maximum or
	 *            less than minimum
	 * @param value
	 */
	public void setCurrent(int value) {
		initBackGround();
		if (value >= limiteMax) {
			this.count = limiteMax;
			findViewById(increaseId).setClickable(false);
			findViewById(decreaseId).setClickable(true);
		}
		else if (value <= limiteMin) {
			this.count = limiteMin;
			findViewById(decreaseId).setClickable(false);
			findViewById(increaseId).setClickable(true);
		}
		else {
			this.count = value;
		}
		if (mOnCheckedValueChangedInstantly != null) mOnCheckedValueChangedInstantly.onValue(count);
	}

	private void zoomOut() {
		findViewById(increaseId).setClickable(true);
		setBackgroundResource(R.drawable.btn_suoxiao);
		Button btnIncrease = (Button) findViewById(increaseId);
		btnIncrease.setTextColor(Color.parseColor("#40000000"));
		Button btnDecrease = (Button) findViewById(decreaseId);
		btnDecrease.setTextColor(Color.WHITE);
		btnIncrease.setText(getIncreaseText());
		btnDecrease.setText(getDecreaseText());
		if (count - 1 > 0) {
			int num = (count - 1) * 10;
			btnIncrease.append("  X" + num);
		}
	}

	private void zoomIn() {
		findViewById(decreaseId).setClickable(true);
		setBackgroundResource(R.drawable.btn_fangda);
		Button btnIncrease = (Button) findViewById(increaseId);
		btnIncrease.setTextColor(Color.WHITE);
		Button btnDecrease = (Button) findViewById(decreaseId);
		btnDecrease.setTextColor(Color.parseColor("#40000000"));
		btnIncrease.setText(getIncreaseText());
		btnDecrease.setText(getDecreaseText());
		if (count - 1 > 0) {
			int num = (count - 1) * 10;
			btnIncrease.append("  X" + num);
		}

	}

	private void initBackGround() {
		if (count >= limiteMax) zoomOut();
		else
			zoomIn();
	}

	private void addComponent() {
		removeAllViews();
		this.setOrientation(LinearLayout.HORIZONTAL);
		Button btnIncrease = new Button(getContext());
		Button btnDecrease = new Button(getContext());
		this.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams params1 = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams params2 = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		params1.weight = 1;
		params2.weight = 1;

		btnIncrease.setId(increaseId);
		btnDecrease.setId(decreaseId);
		btnIncrease.setLayoutParams(params1);
		btnDecrease.setLayoutParams(params2);

		btnIncrease.setBackgroundColor(Color.TRANSPARENT);
		btnDecrease.setBackgroundColor(Color.TRANSPARENT);
		this.addView(btnDecrease);
		this.addView(btnIncrease);
		initBackGround();
		btnIncrease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (count < limiteMax) {
					count++;
				}
				else {
					findViewById(increaseId).setClickable(false);
				}
				zoomIn();
				if (mOnCheckedValueChangedInstantly != null) mOnCheckedValueChangedInstantly.onValue(count);
			}
		});
		btnDecrease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (count > limiteMin) {
					count--;
				}
				else {
					findViewById(decreaseId).setClickable(false);
				}
				zoomOut();
				if (mOnCheckedValueChangedInstantly != null) mOnCheckedValueChangedInstantly.onValue(count);
			}
		});
	}

	public interface onCheckedValueChangedInstantly {
		public void onValue(int value);
	}

	private onCheckedValueChangedInstantly	mOnCheckedValueChangedInstantly;

	public void setOnCheckedValueChangedInstantly(onCheckedValueChangedInstantly mOnCheckedValueChangedInstantly) {
		this.mOnCheckedValueChangedInstantly = mOnCheckedValueChangedInstantly;
	}

	public int getValue() {
		return this.count;
	}

}
