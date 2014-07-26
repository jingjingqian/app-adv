package com.adv.yifangadv.tools;

import java.io.File;
import java.io.IOException;

import android.text.TextUtils;

public class IsExistfile {
	/**
	 * 判断文件路径是否存在，不存在就添加
	 * @param path=路径
	 */
	public static String IsexistDir(String path) {
		File cacheDir = new File(path);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return path;
	}
	/**
	 * 判断文件是否存在，不存在就添加
	 * @param path=文件
	 */
	public static void Isexistfile(String path){
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath=true（文件夹也删除）false（不删文件夹）
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
	/**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
    File file = new File(filePath);
        if (file.isFile() && file.exists()) {
        return file.delete();
        }
        return false;
    }

    public static boolean IsExistFile(String filePath){
    	File file = new File(filePath);
        if (file.isFile() && file.exists()) {
          return true;
        }
        return false;
    }

}
