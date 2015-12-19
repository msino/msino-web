package com.msino.web.filter;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class XssFilter implements Filter {

    Log log = LogFactory.getLog(XssFilter.class);

    List<Pattern> urlPatternList;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String urlPattern = config.getInitParameter("urlPattern");
        if(StringUtils.isNotBlank(urlPattern)){
            String[] patterns = urlPattern.trim().split(",");
            urlPatternList=new ArrayList<Pattern>();
            for(String p:patterns){
                urlPatternList.add(Pattern.compile(p.trim()));
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest newHttpRequest = (HttpServletRequest) request;

        String uri = newHttpRequest.getRequestURI();
        if(isXssCheckMatcher(uri)){
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(newHttpRequest);
            chain.doFilter(xssRequest, response);
        }else {
            chain.doFilter(request,response);
        }
    }

    private boolean isXssCheckMatcher(String uri){
        try {
            if(CollectionUtils.isNotEmpty(urlPatternList)){
                if(StringUtils.isBlank(uri))
                    return false;
                for(Pattern p : urlPatternList){
                    if(p.matcher(uri).matches()){
                        return true;
                    }
                }
                return false;
            }else{
                //未配置则全部xss检查
                return true;
            }
        } catch (Exception e) {
            //igore
            return false;
        }
    }

    @Override
    public void destroy() {
    }


    class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
        HttpServletRequest orgRequest = null;

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            orgRequest = request;
        }

        /**
         * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
         * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
         * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
         */
        @Override
        public String getParameter(String name) {
            String value = super.getParameter(filter(name));
            if (value != null) {
                value = filter(value);
            }
            return value;
        }

        @Override
        public String[] getParameterValues(String name) {
            try{
                if (name != null && filter(name) != null && super.getParameterValues(filter(name))!=null){
                    List<String> values = Arrays.asList(super.getParameterValues(filter(name)));
                    List<String> returnList = new ArrayList<String>();
                    for(String value :values){
                        if (value != null) {
                            value = filter(value);
                            returnList.add(value);
                        }

                    }
                    return (String[]) returnList.toArray(new String[returnList.size()]);
                }

                return null;
            }catch(Exception e){
                log.error(e,e);
                return null;
            }

        }




        /**
         * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
         * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
         * getHeaderNames 也可能需要覆盖
         @Override
         public String getHeader(String name) {

         String value = super.getHeader(filter(name));
         if (value != null) {
         value = filter(value);
         }
         return value;
         }
         */


        @Override
        public String getQueryString() {
            String value = super.getQueryString();
            if (value != null){
                value = filter(value);
            }
            return value;
        }

        private String filter(String value){
          /*
            "	&#34;	&quot;
            &	&#38;	&amp;
            <	&#60;	&lt;
            >	&#62;	&gt;
            不断开空格(non-breaking space)	&#160;	&nbsp;

            */

            /*
            value = value.replaceAll("\\+", "%2B");

            value = value.replaceAll("/", "%2F");
            value = value.replaceAll("\\?", "%3F");
            value = value.replaceAll("%", "%25");
            value = value.replaceAll("#", "%23");
            */


//            value = value.replaceAll("&", "&amp;");
            value = value.replaceAll("<", "&lt;");
            value = value.replaceAll(">", "&gt;");
            value = value.replaceAll("\"", "&quot;");
            value = value.replaceAll(" ", "&nbsp;");


            return value;
        }

        /**
         * 获取最原始的request
         *
         * @return
         */
        public HttpServletRequest getOrgRequest() {
            return orgRequest;
        }
    }



}
