package com.validator.web.validator.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidatorInterceptor implements HandlerInterceptor{

    @Autowired
    private ValidatorServices validatorServices;


    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 
    {
        log.info("interceptor preHandle called");
		//GET 메소드와 POST 메소드중 ModelAttribute로 받는 콘트롤러 로그 저장
		// Map<String, String> params = buildParametersMap(request);
		if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && (request.getMethod().equals(HttpMethod.GET.name()) || (request.getMethod().equals(HttpMethod.POST.name())))) {

			// shareService.insertAdminLog(request, params);

            boolean isValid = validatorServices.mainValidatorService(request);
            if(!isValid) {
                log.warn("Validation failed for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request parameters");
                return false; // Return false to stop further processing
            }   

            // Cookie referer_token = new Cookie("referer_token", null);
            // referer_token.setMaxAge(0); // Remove the cookie
            // response.addCookie(referer_token);

            // Cookie token_message = new Cookie("token_message", null);
            // token_message.setMaxAge(0); // Remove the cookie
            // response.addCookie(token_message);


		} 
		return true;
	}


}
