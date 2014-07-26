package com.adv.yifangadv.tools;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * ClassName: LoadImageTools.java Function: date: 2014年4月21日
 * 
 * @author jj.q
 * @version 1.0
 */
public class LoadImageTools {
	private static ExecutorService executorService = null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public LoadImageTools() {
		executorService = Executors.newFixedThreadPool(6);// 最大线程数

		options = new DisplayImageOptions.Builder().cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				// .imageScaleType(ImageScaleType.EXACTLY)//图像将完全按比例缩小的目标大小
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.displayer(new SimpleBitmapDisplayer())// 正常显示一张图片
				.build();
	}

	public void setLoadImage(String imagePath, ImageView imageView){
		if(imagePath !=null  && imagePath.trim().startsWith("http")){
			imageLoader.displayImage(imagePath, imageView, options, animateFirstListener);
		}else
			imageLoader.displayImage("file:///"+imagePath, imageView, options, animateFirstListener);

	}
	
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	public void DisplayImage(String imagePath, ImageView imageView) {
		imageViews.put(imageView, imagePath);

		queuePhoto(imagePath, imageView);
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public static void pauseImageThread() {
		try {
			executorService.shutdownNow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Task for the queue
	private class PhotoToLoad {
		public String ImagePath;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			ImagePath = u;
			imageView = i;
		}
	}

	private Handler handler = new Handler();

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		String path;
		Bitmap mBitmap = null;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@SuppressWarnings("unused")
		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				path=photoToLoad.ImagePath;
				handler.post(new Runnable() {
					public void run() {
						if (imageViewReused(photoToLoad))
							return;
						
						if (path != null && !path.equals("")) {
							String path1="";
							if(path.indexOf("file://")!=-1){
								path1=path;
							}else
							 path1 = "file://" + photoToLoad.ImagePath;
							if (!imageViewReused(photoToLoad)) {
								imageLoader.displayImage(path1,
										photoToLoad.imageView, options);
							}
						}

					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
			}

		}
	}

	public void setDefultImage(String path1, ImageView imageView) {
		imageLoader.displayImage("drawable://" + path1, imageView, options);
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.ImagePath))
			return true;
		return false;
	}
}
