package com.ejushang.steward.ordercenter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 字符处理工具，含各种数据类型间的转换
 * User: Shiro
 * Date: 14-8-1
 * Time:下午15:10
 *
 */
public class FmtUtil {
    private static String defaultDatePattern = "yyyy-MM-dd";
    private static String defaultDateTimePattern = "yyyy-MM-dd hh:mm:ss";

	public static Double getDouble(String str){
		try{
			return  Double.valueOf(str);
		}catch(Exception e ){
			return null;
		}
	}
	
	public static Long getLong(String str){
		try{
			return  Long.valueOf(str);
		}catch(Exception e ){
			return null;
		}
	}
	
	public static  String getString(Double db){
		if (db==null) return "";
		return db.toString();
	}
	
	public static  String getString(String str){
		if (str==null||str.equals("null")) return "";
		return str;
	}
	
	public static  Integer getInteger(String str){
		if (str==null||str.equals("null")||str.equals("")) return null;
		str=str.trim();
		if("".equals(str)) return null;
		return new Integer(str);
	}
	
	public static  int getInt(String str,int def){
		if (getInteger(str)==null){ 
			return def;
		}else {
			return getInteger(str).intValue();
		}
	}
	
	public static Date getDate(String dateText,String format){
		if (dateText == null||dateText.equals("")){
			return null;
		}
		DateFormat df ;
		try
		{
			if (format == null||format.equals("")){
				df = new SimpleDateFormat(defaultDatePattern);
			}
			else{
				df = new SimpleDateFormat(format);
			}
			df.setLenient(false);
			return df.parse(dateText);
		}catch (ParseException e){
			System.out.println("错误："+e);
			return null;
		}
	}
	public static Date getDate(String dateText){
		return getDate(dateText,null);
	}
	
	
	public static Date getDateTime(String dateText){
         return getDate(dateText,defaultDateTimePattern);
	}
	
	
    /**
     * 比较两个值，返回seleted
     * @param value1
     * @param value2
     * @return
     */
	public static String getSelected(String value1,String value2){
		if (value1==null||value2==null) return "";		
		if (value1.equals(value2)) return "selected";
		return "";
	}
	/** 
	 * 将list中对象toString 加上format格式 例如:张三;李四;
	 * @param list
	 * @param format
	 * @return
	 */
	public static String joinString(List list,String format){
		StringBuffer sb = new StringBuffer();
		for(Object o : list){
		   sb.append(o.toString())
		   .append(format)
		   ;
		}
		return sb.toString().substring(0,sb.lastIndexOf(format)==-1? 0 :sb.lastIndexOf(format));
	}
	//重载
	public static String joinString(String str[],String format){
		return joinString(Arrays.asList(str),format);
	}
	
	/**
	 * 与getListString相反，返回数组
	 * @param text
	 * @return
	 */
	public static String[] getStringArray(String text,String format){
		//text=text.replaceAll("\\pP",format);//将汉字符号过滤掉
		text=text.replace("，", ",");
		//text=text.replaceAll(" ",format);
		String sa[]=text.split(format);
		for(int i=0;i<sa.length;i++){
			sa[i]=sa[i].trim();
		}
		return sa;
	}
	
}
