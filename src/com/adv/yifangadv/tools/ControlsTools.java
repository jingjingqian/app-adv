package com.adv.yifangadv.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.adv.yifangadv.config.Config;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ClassName: Controls_tools.java Function: date: 2014�?�?4�?
 * 
 * @author jj.q
 * @version 1.0
 */
public class ControlsTools {

	 /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static String readTxtFile(String filePath,String encoding){
    	String str="";
        try {
                if("".equals(encoding)){
                	encoding="GBK";
                }
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
//                        System.out.println(lineTxt);
                    	str+=lineTxt;
                    }
                    read.close();
        }else{
//            System.out.println("找不到指定的文件");
        	str="找不到指定的文件";
        }
        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
        	str="读取文件内容出错";
            e.printStackTrace();
        }
		return str;
     
    }
	
	public static void postTost(Context context, String s) {
		Toast t = Toast.makeText(context, s + "", Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
  
	public static void postTost(Context context, String s, int time) {
		Toast t = Toast.makeText(context, s + "", time);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static String ToHexStr(String strSource)  
	{
		strSource=strSource.replaceAll("/", "");
		strSource=strSource.replaceAll(",", "");
		strSource=strSource.replaceAll("\\.", "");
		strSource=strSource.replaceAll("\"", "");
		strSource=strSource.replaceAll(":", "");
		strSource=strSource.replaceAll("~", "");
		strSource=strSource.replaceAll(" ", "");
		strSource=strSource.replaceAll("|", "");
		strSource=strSource.replaceAll("_", "");
		strSource=strSource.replaceAll("-", "");
		/*
		byte bs[]=strSource.getBytes();
	    StringBuilder sb = new StringBuilder("");  
	    for (int i = 0; i < bs.length; i++)  
        {    
	    	String stmp = Integer.toHexString(bs[i] & 0xFF);  
	    	sb.append((stmp.length()==1)? "0"+stmp : stmp);  
	    	// sb.append(" ");    
        }
	    String str=sb.toString();
	    */
	    return  strSource;
	}
	public static String setdatatype(String str) {
		String s = "";
		if (str != null && !"null".equals(str)) {
			s = str;
		}
		return Html.fromHtml(s)+"";
	}
	
	public static String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		 string=Base64.encodeToString(bytes,Base64.DEFAULT);
		return string;
	}

	public static Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
//			byte[] bitmapArray = string.getBytes();
			 byte[]bitmapArray = null;
			 bitmapArray=Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static void dateDialog(final EditText tv, final Context context,final String title) {
		tv.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					tv.requestFocus();
					Calendar c = Calendar.getInstance();
					DatePickerDialog dlg = new DatePickerDialog(context,
							new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									String m = (monthOfYear + 1) < 10 ? "0"
											+ (monthOfYear + 1) : ""
											+ (monthOfYear + 1);
									String d = dayOfMonth < 10 ? "0"
											+ dayOfMonth : "" + dayOfMonth;
									tv.setText(year + "-" + m + "-" + d);
								}
							}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
									.get(Calendar.DAY_OF_MONTH));
					dlg.setTitle(title);
					dlg.show();
				}
				return true;
			}
		});
	}

	public static void dictDialog(final EditText tv, final String[] strs,
			final Context context, final String title) {

		tv.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					tv.requestFocus();
					new AlertDialog.Builder(context)
							.setTitle(title)
							.setItems(strs,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											tv.setTag(which);
											tv.setText(strs[which]);
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();

										}
									}).show();
				}
				return true;
			}
		});
	}
	
	public static void dictDialog(final TextView tv, final String[] strs,
			final Context context, final String title) {

		tv.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					tv.requestFocus();
					new AlertDialog.Builder(context)
							.setTitle(title)
							.setItems(strs,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											tv.setTag(which);
											tv.setText(strs[which]);
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();

										}
									}).show();
				}
				return true;
			}
		});
	}
	public static void dictDialog(final EditText tv,final EditText tv2, final String[] strs,
			final Context context, final String title) {

		tv.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					tv.requestFocus();
					new AlertDialog.Builder(context)
							.setTitle(title)
							.setItems(strs,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											tv.setTag(which);
											tv.setText(strs[which]);
											tv2.setText("");
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();

										}
									}).show();
				}
				return true;
			}
		});
	}

	public static void PicDialog(final Context context, final String path) {
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(context).setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据�?择的方式�?在items数组里面定义了两种方式，拍照的下标为1�?��就调用拍照方�?
						if (item == 1) {
							Intent camera = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
									.fromFile(new File(Config.ImagePath, path)));
							((Activity) context).startActivityForResult(camera,0);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							((Activity) context).startActivityForResult(
									getImage, 1);
						}
					}
				}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						dialog.dismiss();

					}
				}).create();
		dlg.show();
	}

	/**
	 * 获取系统缺省路径选择的图�?
	 * 
	 * @param data
	 * @return
	 */
	public static String getPhoneImage(Context context, String uriString) {

		Uri selectedImage = Uri.parse(uriString);
		String fileName = selectedImage+"";
		Cursor cursor=null;
		try {
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
				fileName = cursor.getString(columnIndex);
				cursor.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				if(cursor != null)
					cursor.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return fileName;
	}

	/**
	 * 获取相机拍照的图�?
	 * 
	 * @param data
	 * @return
	 */
	public static String getCameraImage(Bundle bundle) {
		String strState = Environment.getExternalStorageState();
		if (!strState.equals(Environment.MEDIA_MOUNTED)) {
			Log.i("Test File", "SD card is not available/writealble right now!");
		}
		String fileName = new DateFormat().format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		// Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");
		File file = new File("/sdcard/com.test.diary/");
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = "/sdcard/com.test.diary/" + fileName;
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ((ImageView) findViewById(R.id.iv_temp)).setImageBitmap(bitmap);
		return fileName;
	}
	
	public static String getDate(){
		String date="";
		try {
			SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");     
			date = sDateFormat.format(new   java.util.Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String getDateAndTime(){
		String date="";
		try {
			SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("MM-dd HH:mm");     
			date = sDateFormat.format(new   java.util.Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	* 将double类型的科学计数法转换成指定格式的正常字符�?
	* start 转换double类型的格式为:两位小数
	* @param d
	* @return String
	* @author ljp
	*/
	public static String formatToTwo(double d) {
	DecimalFormat a = new DecimalFormat("#,##0.00");
	String frmStr = a.format(d);
	return frmStr;
	}
}
