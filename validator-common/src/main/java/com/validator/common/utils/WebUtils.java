package com.validator.common.utils;

import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

// @Component
@Slf4j
public class WebUtils {

    public static final String OPT_RESPONSE_JSON = "OPT_0001";

    private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
    private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";

    private static final String DEFAULT_COUNTRY = "KR";

    /**
     * AJAX 요청인지 아닌지 체크
     */
    public static boolean isAjax(HttpServletRequest request){
        if(StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest")){
            return true;
        }
        return false;
    }

    /**
     * Application/Json 요청인지 체크
     * @param request
     * @return
     */
    public static boolean isJsonContents(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        if( contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)){
            return true;
        }
        return false;
    }

    /**
     * multipart/form-data 요청인지 체크
     * @param request
     * @return
     */
    public static boolean isMultipartForm(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        if( contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)){
            return true;
        }
        return false;
    }

    /**
     * 컨텍스트 영역에있는 리퀘스트를 조회
     */
    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 컨텍스트 영역에있는 리스판스 조회
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getResponse();
    }

    /**
     * 속성 값을 가져옴
     */
    public static <T> T getAttribute(String name) {
        return getAttribute(getRequest(), name);
    }

    /**
     * 속성 값을 가져옴
     */
    public static <T> T getAttribute(HttpServletRequest request, String name) {
        if( request == null || request.getAttribute(name) == null ) return null;
        return (T)request.getAttribute(name);
    }

    /**
     * 속성 값 세팅
     */
    public static void setAttribute(String name, Object value) {
        setAttribute(getRequest() ,name, value);
    }

    /**
     * 속성 값 세팅
     */
    public static void setAttribute(HttpServletRequest request, String name, Object value) {
        request.setAttribute(name, value);
    }


    /**
     * 세션데이터 조회
     */
    public static <T> T getSessionAttribute(String key){
        HttpServletRequest request = getRequest();
        return getSessionAttribute(request, key);
    }

    /**
     * 세션데이터 조회
     */
    public static <T> T getSessionAttribute(HttpServletRequest request, String key){
        if(request == null) return null;
        if(StringUtils.isEmpty(key)) return null;
        Object obj = request.getSession().getAttribute(key);
        return obj == null ? null : (T)obj;
    }


    /**
     * 세션데이터 저장
     */
    public static void setSessionAttribute(String key, Object value){
        HttpServletRequest request = getRequest();
        setSessionAttribute(request, key, value);
    }

    /**
     * 세션데이터 저장
     */
    public static void setSessionAttribute(HttpServletRequest request, String key, Object value){
        if(request == null) throw new RuntimeException();
        if(StringUtils.isEmpty(key)) throw new IllegalArgumentException("key must not be null");
        request.getSession().setAttribute(key, value);
    }

    /**
     * 세션데이터 삭제
     */
    public static void removeSessionAttribute(String key){
        HttpServletRequest request = getRequest();
        removeSessionAttribute(request, key);
    }

    /**
     * 세션데이터 삭제
     */
    public static void removeSessionAttribute(HttpServletRequest request, String key){
        if(request == null) throw new RuntimeException();
        if(StringUtils.isEmpty(key)) throw new IllegalArgumentException("key must not be null");
        request.getSession().removeAttribute(key);
    }

    /**
     * 속성값이 같은지 비교
     * @param source
     * @param target
     */
    public static boolean equalAttribute(String source, Object target){
        return equalAttribute(getRequest(), source, target);
    }

    /**
     * 속성값이 같은지 비교
     * @param source
     * @param target
     */
    public static boolean equalAttribute(HttpServletRequest request, String source, Object target){
        Object o = getAttribute(source);
        if( o == null){
            if( target == null){
                return true;
            }else{
                return false;
            }
        }else{
            return o.equals(target);
        }
    }

    /**
     * IPv6인지 체크
     * @param request
     * @return
     */
    public static boolean isIPv6(HttpServletRequest request){
        return isIPv6(getRemoteAddr(request));
    }

    /**
     * IPv6인지 체크
     * @param remoteAddr 아이피 주소
     * @return
     */
    public static boolean isIPv6(String remoteAddr){
        if(remoteAddr == null) return false;
        Pattern pattern = Pattern.compile(regexIPv6);
        if (pattern.matcher(remoteAddr).matches()) {
            return true;
        }
        return false;
    }

    /**
     * IPv4 인지 체크
     * @param request
     * @return
     */
    public static boolean isIPv4(HttpServletRequest request){
        return isIPv4(getRemoteAddr(request));
    }

    /**
     * IPv4 인지 체크
     * @param remoteAddr 아이피 주소
     * @return
     */
    public static boolean isIPv4(String remoteAddr){
        if(remoteAddr == null) return false;
        Pattern pattern = Pattern.compile(regexIPv4);
        if (pattern.matcher(remoteAddr).matches()) {
            return true;
        }
        return false;
    }

    /**
     * 클라이언트 아이피 조회
     * @return IP
     */
    public static String getRemoteAddr() {
        return getRemoteAddr(getRequest());
    }

    /**
     * 클라이언트 아이피 조회
     * @param request 리퀘스트
     * @return IP
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = null;

        if (ip == null) {
            ip = WebUtils.getSessionAttribute("X-DEBUG-IP");
        }
        if (ip == null) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        if( StringUtils.contains(ip, ",")){
            ip = StringUtils.splitAt(ip, ",", 0);
        }
        if(StringUtils.equals(ip, "0:0:0:0:0:0:0:1")){
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 클라이언트 브라우저 정보 조회
     * @param request 리퀘스트
     * @return  유저 브라우저
     */
    public static String getClientBrowser(HttpServletRequest request) {
        if(request == null) return null;
        String userAgent = request.getHeader("user-agent");
        String browser = "other";// =  UserBrowser.Other;

        if (userAgent.indexOf("Trident/7.0") > -1) {
            browser = "IE11";
        }
        else if (userAgent.indexOf("MSIE 10") > -1) {
            browser = "IE10";
        }
        else if (StringUtils.matches(userAgent, ".*(Chrome|CriOS).*")) {
            browser = "Chrome";
        }
        else if (StringUtils.matches(userAgent, ".*(Edge|Edg).*")) {
            browser = "Edge";
        }
        else if (StringUtils.matches(userAgent, ".*(Safari).*")) {
            browser = "Safari";
        }
        else if (StringUtils.matches(userAgent, ".*(Firefox|FxiOS).*")) {
            browser = "Firefox";
        }
        else if (StringUtils.matches(userAgent, ".*(Opera|OPR|OPiOS).*")) {
            browser = "Opera";
        }
        return browser;
    }

    public static String getClientBrowser(String userAgent) {
        if(userAgent == null) return null;
        String browser = "other";// =  UserBrowser.Other;

        if (userAgent.indexOf("Trident/7.0") > -1) {
            browser = "IE11";
        }
        else if (userAgent.indexOf("MSIE 10") > -1) {
            browser = "IE10";
        }
        else if (StringUtils.matches(userAgent, ".*(Edge|Edg).*")) {
            browser = "Edge";
        }
        else if (StringUtils.matches(userAgent, ".*(Chrome|CriOS).*")) {
            browser = "Chrome";
        }
        else if (StringUtils.matches(userAgent, ".*(Safari).*")) {
            browser = "Safari";
        }
        else if (StringUtils.matches(userAgent, ".*(Firefox|FxiOS).*")) {
            browser = "Firefox";
        }
        else if (StringUtils.matches(userAgent, ".*(Opera|OPR|OPiOS).*")) {
            browser = "Opera";
        }
        return browser;
    }

    /**
     * 사용자 에이전트 조회
     * @param request 리퀘스트
     * @return 유저에이전트 문자열
     */
    public static String getUserAgent(HttpServletRequest request){
        return request.getHeader("user-agent");
    }


    /**
     * 쿠키 생성
     */
    public static void setCookie(Cookie cookie){
       setCookie(getResponse(), cookie);
    }

    /**
     * 쿠키 생성
     * @param response
     */
    public static void setCookie(HttpServletResponse response, Cookie cookie){
        response.addCookie(cookie);
    }

    /**
     * 쿠키 값 가져오기
     * @return
     */
    public static String getCookieValue(String name) {
        return getCookieValue(getRequest(), name);
    }

    /**
     * 쿠키 값 가져오기
     * @param request
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {

        Cookie[] getCookie = request.getCookies();
        if (getCookie != null) {
            for (int i = 0; i < getCookie.length; i++) {
                Cookie c = getCookie[i];
                if( name.equals(c.getName())) return c.getValue();
            }
        }
        return "";
    }

    /**
     * 쿠키 가져오기
     * @param request
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {

        Cookie[] getCookie = request.getCookies();
        if (getCookie != null) {
            for (int i = 0; i < getCookie.length; i++) {
                Cookie c = getCookie[i];
                if( name.equals(c.getName())) return c;
            }
        }

        return null;
    }

    /**
     * 클라이언트 OS 정보 조회
     * @param request 리퀘스트
     * @return 유저OS
     */
    public static String getUserOS(HttpServletRequest request) {
        if(request == null) return null;
        String userAgent = request.getHeader("user-agent");
        String os = "Other";
        userAgent = userAgent.toLowerCase();
        if (userAgent.indexOf("windows") > -1) {
            os = "Windows";
        }else if (userAgent.indexOf("iphone") > -1) {
            os = "iPhone";
        }else if (userAgent.indexOf("ipad") > -1) {
            os = "iPad";
        }else if (userAgent.indexOf("android") > -1) {
            os = "Android";
        }else if (userAgent.indexOf("mac") > -1) {
            os = "Mac";
        }else if (userAgent.indexOf("linux") > -1) {
            os = "Linux";
        }
        return os;
    }


    public static String getUserOS(String userAgent) {
        if(userAgent == null) return null;
        String os = "Other";
        userAgent = userAgent.toLowerCase();
        if (userAgent.indexOf("windows") > -1) {
            os = "Windows";
        }else if (userAgent.indexOf("iphone") > -1) {
            os = "iPhone";
        }else if (userAgent.indexOf("ipad") > -1) {
            os = "iPad";
        }else if (userAgent.indexOf("android") > -1) {
            os = "Android";
        }else if (userAgent.indexOf("mac") > -1) {
            os = "Mac";
        }else if (userAgent.indexOf("linux") > -1) {
            os = "Linux";
        }
        return os;
    }
    public static HttpServletRequest toHttpServletRequest(ServletRequest servletRequest) {
        return (HttpServletRequest)servletRequest;
    }


    public static HttpServletResponse toHttpServletResponse(ServletResponse servletResponse) {
        return (HttpServletResponse) servletResponse;
    }
}