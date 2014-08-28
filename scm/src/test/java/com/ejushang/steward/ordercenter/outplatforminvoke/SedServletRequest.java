package com.ejushang.steward.ordercenter.outplatforminvoke;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * User:  Sed.Lee(李朝)
 * Date: 14-8-7
 * Time: 下午2:41
 */
public class SedServletRequest implements HttpServletRequest
{
    HttpSession session = new HttpSession() {

        Map<String,Object> attribute = new HashMap<String, Object>();

        @Override
        public long getCreationTime() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getId() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getLastAccessedTime() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ServletContext getServletContext() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setMaxInactiveInterval(int interval) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getMaxInactiveInterval() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public HttpSessionContext getSessionContext() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getAttribute(String name) {
            return attribute.get(name);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getValue(String name) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Enumeration getAttributeNames() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String[] getValueNames() {
            return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void setAttribute(String name, Object value) {
            //To change body of implemented methods use File | Settings | File Templates.
            attribute.put(name,value) ;
        }

        @Override
        public void putValue(String name, Object value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void removeAttribute(String name) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void removeValue(String name) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void invalidate() {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean isNew() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    @Override
    public String getAuthType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getDateHeader(String name) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHeader(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration getHeaders(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration getHeaderNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getIntHeader(String name) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMethod() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPathInfo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPathTranslated() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContextPath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getQueryString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRemoteUser() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Principal getUserPrincipal() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRequestedSessionId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRequestURI() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getServletPath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HttpSession getSession(boolean create) {
        return session;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HttpSession getSession() {
        return session;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCharacterEncoding() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getContentLength() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContentType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getParameter(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration getParameterNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map getParameterMap() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProtocol() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getScheme() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getServerName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getServerPort() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRemoteAddr() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRemoteHost() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAttribute(String name, Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeAttribute(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Locale getLocale() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration getLocales() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSecure() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRealPath(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getRemotePort() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getLocalName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getLocalAddr() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getLocalPort() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
