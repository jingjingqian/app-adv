package com.adv.yifangadv.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.adv.yifangadv.R;
import com.adv.yifangadv.config.Config;
import com.adv.yifangadv.tools.LoadImageTools;
import com.adv.yifangadv.tools.ZoomImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * @author jignjing 长按放大图像
 */
public class paizhao_yulanactivity extends Activity implements OnTouchListener {
	private ZoomImageView mImageView;

	private final Matrix matrix = new Matrix();
	private final Matrix savedMatrix = new Matrix();
	private DisplayMetrics mDisplyMetrcs;
	private Bitmap mBitmap;

	private float minScaleR;// 最小缩放比例
	private static final float MAX_SCALE = 4f;// 最大缩放比例

	private static final int NONE = 0;// 初始状态
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 缩放
	private int mode = NONE;

	private final PointF prev = new PointF();
	private final PointF mid = new PointF();
	private float dist = 1f;
	Button guanbi;
	int s;
	String pathname = "";
	String path = "";
	private ProgressDialog progressDialog;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 1:
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							guanbi.setVisibility(View.GONE);
						}
					}, 3000);

					break;
				case 2:
					mBitmap = BitmapFactory.decodeFile(pathname, null);
					progressDialog.dismiss();
					if (mBitmap != null) {
						setupViews();
					}
					break;
				}
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.photo_bg);
		mImageView = (ZoomImageView) this.findViewById(R.id.mImageView);
		guanbi = (Button) this.findViewById(R.id.fanghui);
		guanbi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paizhao_yulanactivity.this.finish();
			}
		});
		try {
			// 引用本地资源文件创建位图对象
			Intent it = getIntent();
			//path = it.getStringExtra("imgpath");
			path = it.getStringExtra("imgpath");
			Log.e("--path--", path);
			LoadImageTools mLoadImageTools = new LoadImageTools();
			mLoadImageTools.setLoadImage(path, mImageView);//从imageloder获取数据
			mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//			progressDialog = ProgressDialog.show(paizhao_yulanactivity.this,
//					"", "正在下载图片...", true, false);
//			new Thread(connectNet).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				Toast.makeText(paizhao_yulanactivity.this, "图片下载失败", 800)
						.show();
				Thread.sleep(1000);

			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			paizhao_yulanactivity.this.finish();
		}

	}

	private void setupViews() {
		// 实例化图片控件
		mImageView = (ZoomImageView) this.findViewById(R.id.mImageView);
		guanbi = (Button) this.findViewById(R.id.fanghui);
		guanbi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				paizhao_yulanactivity.this.finish();
			}
		});

		// 将位图对象设置到图片控件中
		mImageView.setImageBitmap(mBitmap);
		// 为图片控件添加触控事件
		mImageView.setOnTouchListener(this);
		// 获取当前屏幕分辨率对象
		mDisplyMetrcs = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(mDisplyMetrcs);
		this.setMinZoom();
		this.setCenter();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 程序启动0.5秒以后设置图片控件的缩放属性
				// 如果在描述文件或一开始就设置，那么，图片就会出现在屏幕的左上角，而我们希望图片出现在屏幕的中间位置
				mImageView.setScaleType(ImageView.ScaleType.MATRIX);
			}
		}, 500);
		mImageView.setImageMatrix(matrix);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
		case MotionEvent.ACTION_DOWN:
			guanbi.setVisibility(View.VISIBLE);
			handler.sendEmptyMessage(1);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<1111");
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// 副点按下
		case MotionEvent.ACTION_POINTER_DOWN:
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<222");
			dist = spacing(event);
			// 如果连续两点距离大于10，则判定为多点模式
			if (spacing(event) > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<333");
			break;

		case MotionEvent.ACTION_POINTER_UP:
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<444");
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY()
						- prev.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;
					matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			}
			break;
		}
		mImageView.setImageMatrix(matrix);
		this.checkView();
		return true;
	}

	private void checkView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR)
				matrix.setScale(minScaleR, minScaleR);
			if (p[0] > MAX_SCALE)
				matrix.set(savedMatrix);
		}
		this.setCenter();
	}

	/**
	 * 设置最小缩放比列，最大值和图片大小相等
	 */
	private void setMinZoom() {
		try {

			minScaleR = Math.min(
					(float) mDisplyMetrcs.widthPixels
							/ (float) mBitmap.getWidth(),
					(float) mDisplyMetrcs.heightPixels
							/ (float) mBitmap.getHeight());
		} catch (Exception e) {
			// TODO: handle exception
			minScaleR = Math.min(1, 1);
		}
		if (minScaleR < 1.0)
			matrix.postScale(minScaleR, minScaleR);
	}

	private void setCenter() {
		this.setCenter(true, true);
	}

	private void setCenter(boolean horizontal, boolean vertical) {

		Matrix mMatrix = new Matrix();
		mMatrix.set(matrix);
		RectF mRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		mMatrix.mapRect(mRectF);

		float height = mRectF.height();
		float width = mRectF.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			int screenHeight = mDisplyMetrcs.heightPixels;
			if (height < screenHeight)
				deltaY = (screenHeight - height) / 2 - mRectF.top;
			else if (mRectF.top > 0)
				deltaY = -mRectF.top;
			else if (mRectF.bottom < screenHeight)
				deltaY = mImageView.getHeight() - mRectF.bottom;
		}

		if (horizontal) {
			int screenWidth = mDisplyMetrcs.widthPixels;
			if (width < screenWidth)
				deltaX = (screenWidth - width) / 2 - mRectF.left;
			else if (mRectF.left > 0)
				deltaX = -mRectF.left;
			else if (mRectF.right < screenWidth)
				deltaX = screenWidth - mRectF.right;
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 两点的距离
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 两点的中点
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	String temp = android.os.Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/fenxiang.jpg";
	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {

			try {

				if (path.contains("http://")) {

					try {
						File file = new File(pathname);
						if (file.exists()) {
							file.delete();
						} else {
							file.mkdirs();
						}
						pathname = Config.getImagePath("cech") + "/"
								+ System.currentTimeMillis() + ".jpg";
						FileOutputStream fos = new FileOutputStream(pathname);
						InputStream is = new URL(path).openStream();

						int data = is.read();
						while (data != -1) {
							fos.write(data);
							data = is.read();
						}
						is.close();
						fos.close();

					} catch (IOException e) {

						e.printStackTrace();
					}
				} else {
					pathname = path;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				handler.sendEmptyMessage(2);
			}
		}
	};

}