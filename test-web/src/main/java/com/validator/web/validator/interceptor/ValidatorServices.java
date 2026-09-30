package com.validator.web.validator.interceptor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.validator.common.utils.DateUtils;
import com.validator.common.utils.EncUtils;
import com.validator.common.utils.HashUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidatorServices {

    @Value("${validator.hash.algorithm}")
    private String hashAlgorithm;

    @Value("${validator.hash.saltKey}")
    private String hashSaltKey;

    @Value("${validator.encryption.algorithm}")
    private String encryptionAlgorithm;

    @Value("${validator.encryption.saltKey}")
    private String encryptionSaltKey;

    @Value("${validator.limit-timer.enabled}")
    private boolean limitTimeEnabled;

    @Value("${validator.limit-timer.interval}")
    private long limitTimeInterval;

    
    public Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String[] value = httpServletRequest.getParameterValues(key);
			if(value.length == 1) {
				resultMap.put(key,  value[0]);
			} else {
				resultMap.put(key,  Arrays.toString(value));
			}
            
        }
        
        return resultMap;
    }

    public String generateHash(HttpServletRequest request, String key, String algorithm) throws Exception {
        Map<String, String> params = buildParametersMap(request);
        log.info("Request parameters: {}", params);

        // Assuming HashUtil.Hmac is a utility method to generate HMAC
        String hashValue = HashUtils.Hmac(key, params.toString(), algorithm);
        log.info("Generated hash: {}", hashValue);
        
        return hashValue;
    }

    public String generateHash(String key, String value, String algorithm) throws Exception {
        log.info("Generating hash for value: {}", value);
        return HashUtils.Hmac(key, value, algorithm);
    }

    public boolean mainValidatorService(HttpServletRequest request) throws Exception {
        // Placeholder for main validator service logic
        log.info("Main validator service executed");
        Map<String, String> params = buildParametersMap(request);
        log.info("Service parameters: {}", params);
        String ticket = request.getHeader("Referer-Token");
        String tokenMessage = request.getHeader("Token-Message");

        if (ticket == null || ticket.isEmpty()) {
        log.warn("referer_token header is missing or empty");
            Cookie[] cookies = request.getCookies();
            log.info("Cookies found: {}", cookies != null ? cookies.length : 0);
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    log.info("Cookie name: {}, value: {}", cookie.getName(), cookie.getValue());
                    if ("Referer-Token".equals(cookie.getName())) {
                        ticket = cookie.getValue();
                        // break;
                    }
                    if ("Token-Message".equals(cookie.getName())) {
                        tokenMessage = cookie.getValue();
                        // break;
                    }
                }
                
            }else {
                log.warn("referer_token header is missing or empty");
                return false; //header or cookie not found return false
            }
        } else {

            log.info("referer_token       : {}", ticket);

            String hashValue = HashUtils.Hmac(hashSaltKey, params.toString(), hashAlgorithm);
            log.info("Generated hash value: {}", hashValue);

            if (!ticket.equals(hashValue)) {
                log.warn("referer_token does not match the generated hash value");
                return false; // Return false if the referer_token does not match the generated hash
            }
        }

        if(limitTimeEnabled) {
            log.info("Limit time check is enabled");
            if (!limitTimeCheck(tokenMessage)) {
                log.warn("Limit time check failed");
                return false; // Return false if limit time check fails
            }
        }


        return true; // Return true if the service is healthy
    }


    public boolean useCaseCheck(HttpServletRequest request) throws Exception {
        // Placeholder for validator ticket check logic
        Map<String, String> params = buildParametersMap(request);
        log.info("Request URL: {}, Method: {}, Parameters: {}", request.getRequestURI(), request.getMethod(), params );
        log.info("Service parameters: {}", params);
        String hashValue = HashUtils.Hmac(hashSaltKey, params.toString(), hashAlgorithm);
        log.info("Generated hash value: {}", hashValue);
        return true; // Return true if the service is healthy
    }

    public boolean limitTimeCheck(String tokenMessage) throws Exception {
        // Placeholder for limit time check logic
        log.info("Limit time check executed");
        String decTime = EncUtils.decAES(tokenMessage, encryptionSaltKey, encryptionAlgorithm);
        log.info("Decrypted time from token_message: {}", decTime);
        LocalDateTime limitTime= LocalDateTime.parse(decTime);
        log.info("now = {}", DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        long gapSecounds = ChronoUnit.SECONDS.between(limitTime, LocalDateTime.now());
        log.info("Time interval in seconds: {}", gapSecounds);

        if(gapSecounds > limitTimeInterval) {
            log.warn("Time interval exceeds {} seconds, returning false", limitTimeInterval);
            return false; // Return false if the time interval exceeds the limit
        }
        return true; // Return true if the service is healthy
    }
}
