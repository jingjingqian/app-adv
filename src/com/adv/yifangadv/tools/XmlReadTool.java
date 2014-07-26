package com.adv.yifangadv.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class XmlReadTool {

	private Context mContext;
	private String encoding = "utf-8";
	private String fileName;
	private XmlPullParserFactory pullFactory;
	private XmlPullParser parser;
	public XmlReadTool(Context context) throws XmlPullParserException{
		super();
		this.mContext = context;
		
		pullFactory = XmlPullParserFactory.newInstance();  
         parser = pullFactory.newPullParser(); 
	}
	
	//设置编码格式
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	
	//获取parser
	public XmlPullParser getXmlSerializer(){
		return parser;
	}
	
	//开始读
	public void startRead(String path, String fileName) throws XmlPullParserException, IOException{
		this.fileName = path + fileName;
		FileInputStream inputStream = mContext.openFileInput(this.fileName);
		parser.setInput(inputStream, encoding);   
  
		//产生第一个事件    
        int eventType = parser.getEventType();  
        while(eventType!=XmlPullParser.END_DOCUMENT){
        	switch (eventType) {
        	//触发开始文档事件    
            case XmlPullParser.START_DOCUMENT: 
            	break;
            	//触发开始元素事件    
            case XmlPullParser.START_TAG:  
            	break;
            	
            	//触发结束元素事件    
            case XmlPullParser.END_TAG:
            	break;
        	}
        	eventType = parser.next();    
        }
        inputStream.close();
	}
}
