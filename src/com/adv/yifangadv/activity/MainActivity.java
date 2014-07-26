package com.adv.yifangadv.activity;



import io.vov.vitamio.LibsChecker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.adv.yifangadv.R;
import com.adv.yifangadv.tools.AutoScrollTextView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
public class MainActivity extends Activity implements OnClickListener {
    /**显示文本*/
    String str="";
    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
        setContentView(R.layout.activity_main);
        SimpleDateFormat formatter = new SimpleDateFormat(
              "yyyy年MM月dd日    HH:mm:ss");
      Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
      str = formatter.format(curDate)+"黑夜给了我黑色的眼睛，而我却用它来寻找光明...sfskhhkshfkjhshfkjsdkhksdfkhdfhdhkfshopqwpjjjkj拉撒都降到了急啊 啊受打击啦的煎熬了坚实的暗的暗的啊四大接收到 安神";
        setScrollText(str);
    }
    private void setScrollText(String showMsg){
    	 AutoScrollTextView auto=(AutoScrollTextView)findViewById(R.id.autoTxt);  
    		      auto.setText(showMsg);  //设置 要滚动的文字
    		      auto.setColor("#ff0000"); //设置字体颜色
    		      auto.setSpeed(AutoScrollTextView.SPEED_NORMAL,false);//设置滚动速度
    		      auto.startScroll(); //开始滚动
    	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}