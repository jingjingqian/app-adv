package com.adv.yifangadv.application;


import java.io.File;
import java.util.List;

import com.adv.yifangadv.config.Config;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class YiFangappApplication extends Application {

	public int position = 1;


	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@SuppressWarnings("unused")
	@Override
    public void onCreate() {
	    super.onCreate();
		
		if (ConfigImage.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		initImageLoader(getApplicationContext());
	}
	 public static class ConfigImage {
			public static final boolean DEVELOPER_MODE = false;
		}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration configImg = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()//当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//设置缓存文件的名字
				.discCache(new UnlimitedDiscCache(new File(Config.CacheImagePath+"imageFile"))) // 缓存目录
				.discCacheFileCount(120)//缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				.writeDebugLogs() // Remove for release app//是否打印日志用于检查错误
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configImg);
	}
}
