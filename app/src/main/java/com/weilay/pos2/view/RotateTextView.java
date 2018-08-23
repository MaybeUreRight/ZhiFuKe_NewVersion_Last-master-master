package com.weilay.pos2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class RotateTextView extends TextView {

	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
//	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
		canvas.rotate(180, this.getWidth() / 2f, this.getHeight() /2f);
		super.onDraw(canvas);
		canvas.restore();
	}

}
