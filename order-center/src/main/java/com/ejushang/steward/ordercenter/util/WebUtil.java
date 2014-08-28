package com.ejushang.steward.ordercenter.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * 页面相关功能综合处理工具
 * User: Shiro
 * Date: 14-8-1
 * Time:下午15:10
 */
public class WebUtil {
	
    //取得COOKIE
    public static String getCookieValue(Cookie[] cookies, String cookieName) {
        for (int i = 0; cookies!=null&&i <cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return (cookie.getValue());
            }
        }
        return null;
    }

    //取得COOKIE
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
    	Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies!=null&&i <cookies.length; i++) {
            Cookie cookie = cookies[i];
            cookie.setDomain(".caakee.com");
//            cookie.setPath(WebUtil.getWebAppPath());
//             System.out.println("domain:"+cookie.getDomain()+"  path:"+cookie.getPath()+"  cookieName:"+cookie.getName()+"   cookieValue:"+cookie.getValue());
//            cookie.getPath();
            if (cookieName.equals(cookie.getName())) {
                return (cookie.getValue());
            }
        }
        return null;
    }
	
	public static Double StringToDouble(String str){
		try{
			return  Double.valueOf(str);
		}catch(Exception e ){
			return null;
		}
	}
	
	public static Double getDouble(String str){
		return StringToDouble(str);
	}
	
	
	public static  String getString(Double db){
		if (db==null) return "";
		return db.toString();
	}
	
	private static  String getString(String str){
		if (str==null||str.equals("null")) return "";
		return str;
//		String s="";
//		try{
//			s=new String(str.getBytes("ISO8859_1"));
//		}catch(Exception e ){
//			
//		}		
//		return s; 
	}
	
	public static  Integer getInteger(String str){
		if (str==null||str.equals("null")) return null;
		return new Integer(str);
	}

	public static  Integer getInteger(HttpServletRequest request,String str){	 
		return FmtUtil.getInteger(request.getParameter(str));
	}

	public static  Long getLong(HttpServletRequest request,String str){	 
		return FmtUtil.getLong(request.getParameter(str));
	}

	public static  int getInt(String str){
		if (str==null||str.equals("null")) return 0;
		return new Integer(str).intValue();
	}

	public static   Date getDate(HttpServletRequest request,String str){
       if (request.getParameter(str)==null) {return null;}      
       return FmtUtil.getDate(request.getParameter(str));
       
	}

	public static Double getDouble(HttpServletRequest request,String str){
		return FmtUtil.getDouble(request.getParameter(str));
	}

	/**
	 * 获取参数。如果取不到值，返回默认值
	 * @param request
	 * @param paramName
	 * @param def
	 * @return
	 */
	public static int getInt(HttpServletRequest request,String paramName,int def){
		String str = getString(request.getParameter(paramName));
		if (str.equals("")){
			return def;
		}else{
			return new Integer(str).intValue();
		}
	}
	
    public static String getString(HttpServletRequest request,String paramName){
    	return  getString(request.getParameter(paramName));
    }
    
    public static String[] getStrings(HttpServletRequest request,String paramName){
    	return   request.getParameterValues(paramName);
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
	 * 取得当前的WebContext的路径.例如:d:/tomcat 5.0/webapps/caakee
	 * @return
	 */
	public static String getWebAppPath(){
		String rs="";
        Class clazz = WebUtil.class;        
//        /** 得到解析类SystemConfig的URL绝对路径 **/
//        URL configUrl = clazz.getClassLoader().getResource("");
//        /** 转换为URI，这是关键，URI处理了空格，中文等问题 **/
//        URI uri = configUrl.toURI();
//        /** 以下就简单了，用文件方法得到绝对String路径 **/
//        String absolutePath = new File(uri).getAbsolutePath();      
        try {
//            if (clazz.getResource("/")==null){
//            	return "/";
//            }
        	
			String absolutePath = URLDecoder.decode(clazz.getResource("/").getPath(),"UTF-8");
			if (absolutePath!=null){
				if (absolutePath.indexOf("WEB-INF")>-1){
					rs = absolutePath.substring(0,absolutePath.indexOf("WEB-INF")-1);
				}
			}
		
        } catch (UnsupportedEncodingException e) {
			System.out.println("getWebContext()错误.");
			e.printStackTrace();
		}
        return rs;		
	}
	
	public static void main(String[] args){
		System.out.println(WebUtil.class.getResource("/").getPath());
	}
	
	
	
}
