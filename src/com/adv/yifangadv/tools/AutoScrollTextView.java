package com.adv.yifangadv.tools;

import com.adv.yifangadv.R;

import android.R.bool;
import android.app.Fragment.SavedState;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

public class AutoScrollTextView extends TextView {

	public final static String TAG = AutoScrollTextView.class.getSimpleName();

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f;// 文字的横坐标
	private float y = 0f;// 文字的纵坐标
	private float x = 0f;//不滚动时的横坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = null;// 绘图样式
	private String text = "";// 文本内容
	private boolean first = true;

	public AutoScrollTextView(Context context) {
		super(context);
		this.setFocusable(true);
		init();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(true);
		init();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setFocusable(true);
		init();
	}

	public static final float SPEED_SLOW=1.0f;
	public static final float SPEED_NORMAL=2.5f;
	public static final float SPEED_FAST=5.0f;
	
	private float speed=SPEED_NORMAL;
	/**
	 * 设置滚动速度
	 * @param model =速度
	 */
	public void setSpeed(float model,boolean res){
		if(res){
		if(model!=SPEED_FAST&&model!=SPEED_NORMAL&&model!=SPEED_SLOW)
			model=SPEED_NORMAL;
			speed=model;
		}else{
			if(model<0)
				model=SPEED_NORMAL;
			else
				speed=model;
		}
		
	}
	
	/**
	 * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下
	 */
	private void init() {
		paint = getPaint();
//		paint.setARGB(255, 0, 255, 0);
//		paint.setColor(Color.parseColor("#ff0000"));
	}
	public void setColor(String c){
		paint = getPaint();
		paint.setColor(Color.parseColor(c));
	}

	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;

	}

	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;

	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	/**
	 * 开始滚动
	 */
	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	/**
	 * 停止滚动
	 */
	public void stopScroll() {
		isStarting = false;
		invalidate();
	}
	@Override
	public void onDraw(Canvas canvas) {
		if (first) {
			viewWidth = getWidth();
			textLength = paint.measureText(text);
			if (textLength > viewWidth) {
				isStarting = true;
			} else {
				isStarting = false;
			}
			step = textLength;
			temp_view_plus_text_length = viewWidth + textLength;
			temp_view_plus_two_text_length = viewWidth + textLength * 2;
			y = getTextSize() + getPaddingTop();
			x = getPaddingLeft();
			first = false;
		}
		if (!isStarting) {
			canvas.drawText(text, x, y, paint);
			return;
		}
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
		step += speed;
		if (step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
