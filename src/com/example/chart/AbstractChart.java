/**
 * Copyright (C) 2009 - 2012 SC 4ViewSoft SRL
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.chart;

import java.io.Serializable;
import java.text.NumberFormat;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.example.chart.renderer.DefaultRenderer;

/**
 * An abstract class to be implemented by the chart rendering classes.
 */
public abstract class AbstractChart implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * The graphical representation of the chart. 图形抽象类
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param x
	 *            the top left x value of the view to draw to
	 * @param y
	 *            the top left y value of the view to draw to
	 * @param width
	 *            the width of the view to draw to
	 * @param height
	 *            the height of the view to draw to
	 * @param paint
	 *            the paint
	 */
	public abstract void draw(Canvas canvas, int x, int y, int width, int height, Paint paint);

	/**
	 * Draws the chart background.
	 * 
	 * @param renderer
	 *            the chart renderer
	 * @param canvas
	 *            the canvas to paint to
	 * @param x
	 *            the top left x value of the view to draw to
	 * @param y
	 *            the top left y value of the view to draw to
	 * @param width
	 *            the width of the view to draw to
	 * @param height
	 *            the height of the view to draw to
	 * @param paint
	 *            the paint used for drawing
	 * @param newColor
	 *            if a new color is to be used
	 * @param color
	 *            the color to be used
	 */
	protected void drawBackground(DefaultRenderer renderer, Canvas canvas, int x, int y, int width, int height, Paint paint, boolean newColor, int color) {
		if (renderer.isApplyBackgroundColor() || newColor) {
			if (newColor) {
				paint.setColor(color);
			}
			else {
				paint.setColor(renderer.getBackgroundColor());
			}
			paint.setStyle(Style.FILL);
			canvas.drawRect(x, y, x + width, y + height, paint);
		}
	}

	/**
	 * Draw a multiple lines string.
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param text
	 *            the text to be painted
	 * @param x
	 *            the x value of the area to draw to
	 * @param y
	 *            the y value of the area to draw to
	 * @param paint
	 *            the paint to be used for drawing
	 */
	protected void drawString(Canvas canvas, String text, float x, float y, Paint paint) {
		if (text != null) {
			String[] lines = text.split("\n");
			Rect rect = new Rect();
			int yOff = 0;
			for (int i = 0; i < lines.length; ++i) {
				canvas.drawText(lines[i], x, y + yOff, paint);
				paint.getTextBounds(lines[i], 0, lines[i].length(), rect);
				yOff = yOff + rect.height() + 5; // space between lines is 5
			}
		}
	}

	/**
	 * Makes sure the fraction digit is not displayed, if not needed.
	 * 
	 * 
	 * @param format
	 *            the number format for the label
	 * @param label
	 *            the input label value
	 * @return the label without the useless fraction digit
	 */
	protected String getLabel(NumberFormat format, double label) {
		String text = "";
		if (format != null) {
			text = format.format(label);
		}
		else if (label == Math.round(label)) {
			text = Math.round(label) + "";
		}
		else {
			text = label + "";
		}
		return text;
	}

}
