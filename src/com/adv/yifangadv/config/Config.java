package com.adv.yifangadv.config;


import java.io.File;



/**
 * ClassName: Config.java
 * Function: 
 * date: 2014年4月16日
 * @author jj.q
 * @version 1.0
 */
public class Config {
   
	
	public static String ImagePath=getImagePath("yifangImage/");
	public static String CacheImagePath=getImagePath("CacheImage/");
	
	
	public static String getImagePath(String path){
		File f=new File(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/yifang/"+path);
		if (!f.exists())
		  f.mkdirs();
		return android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/yifang/"+path;
	}
	
	 public static boolean IsExistFile(String filePath){
	    	File file = new File(filePath);
	        if (file.isFile() && file.exists()) {
	          return true;
	        }
	        return false;
	    }
	
}
