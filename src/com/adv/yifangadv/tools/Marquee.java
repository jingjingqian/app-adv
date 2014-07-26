package com.adv.yifangadv.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
/**
 * 重写TextView，实现文字的滚动效果
 * */
public class Marquee extends TextView implements Runnable {
    /** 当前位置坐标 */
    private int currentScrollX;
    /** 停止标志 */
    private boolean isStop = false;
    /** 文本长度 */
    private int textWidth;
    /** 可测量标志 */
    private boolean isMeasure = false;
    /** 三个构造函数 */
    public Marquee(Context context) {
        super(context);
    }
    public Marquee(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public Marquee(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        // 查看可测量标志
        // 若不可测量的话，我们就获得文本信息得宽度，改变状态标志为可测量
        if (!isMeasure) {
            getTextWidth();
            isMeasure = true;
        }
    }
    /** 测量文本的宽度 */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }
    @Override
    public void run() {
        currentScrollX -= 1;
        scrollTo(currentScrollX, 0);// 偏移至(currentScrollX, 0)
        if (isStop) {
            return;
        }
        Log.e("", "run------------this.getwidth:" + this.getWidth());// ---244
        Log.e("", "run------------textWidth:" + textWidth);// ---0
        Log.e("", "currentScrollX:" + currentScrollX);
        Log.e("", "getScrollX:" + getScrollX());
        if (getScrollX() <= -(this.getWidth())) {// -255<-244
            scrollTo(textWidth, 0);// 移动到(0,0)位置
            currentScrollX = textWidth;// 改变当前位置=0
        }
        postDelayed(this, 10);// 第二个参数控制滚动速度，数值越大滚动越慢
    }
    /** 开始滚动 */
    public void startScroll() {
        currentScrollX = 0;// 起始坐标X=0
        isStop = false;// 停止标志=false,标示开始滚动
        this.removeCallbacks(this);
        post(this);// 开始滚动
    }
    /** 停止滚动 */
    public void stopScroll() {
        isStop = true;// 停止
    }
}