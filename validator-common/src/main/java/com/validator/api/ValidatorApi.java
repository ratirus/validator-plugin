package com.validator.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.validator.common.utils.ShareUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ValidatorApi {
    
    @Value("${validator.host.api}")
    private String validatorApiHost;
    
    private final RestTemplate restTemplate;

    public void attackLogWrite(HttpServletRequest request, String logType, String originToken, String verifyToken, long interval) throws Exception {
        String validatorApiUrl = validatorApiHost + "/api/attacklog";
        log.info("attackLogWrite start : {}", validatorApiUrl);
        Map<String, String> params = ShareUtils.buildParametersMap(request);
        if(request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")){
        // if (request instanceof MultipartHttpServletRequest) {
            log.info("Multipart request detected");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            params = ShareUtils.makeMultipartParametersMap(multipartRequest);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Map<String, Object> apiParams = new HashMap<>();
        MultiValueMap<String, Object> apiParams = new LinkedMultiValueMap<>();
        // String userId = Security.userID();
        // apiParams.put("host", request.getHeader("Host"));
        // apiParams.put("url", request.getRequestURI().toString());
        // apiParams.put("method", request.getMethod());
        // apiParams.put("contentType", request.getContentType());
        // apiParams.put("userAgent", request.getHeader("User-Agent"));
        // apiParams.put("refererIp", ShareUtils.getClientIP(request));
        // apiParams.put("logType", logType);
        // apiParams.put("parameter", params.toString());
        // apiParams.put("userId", userId);

        apiParams.add("host", request.getHeader("Host"));
        apiParams.add("url", request.getRequestURI().toString());
        apiParams.add("method", request.getMethod());
        apiParams.add("contentType", request.getContentType());
        apiParams.add("userAgent", request.getHeader("User-Agent"));
        apiParams.add("refererIp", ShareUtils.getClientIP(request));
        apiParams.add("logType", logType);
        apiParams.add("parameter", params);
        apiParams.add("Cookie", request.getHeader("Cookie"));
        apiParams.add("originToken", originToken);
        apiParams.add("verifyToken", verifyToken);
        apiParams.add("language",request.getHeader("Accept-Language"));
        if(interval > 0) {
            apiParams.add("intervalTime", String.valueOf(interval));
        }

        // HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(apiParams, headers);
        log.info("apiParams = {}", apiParams);
        ResponseEntity<String> response = restTemplate.postForEntity(validatorApiUrl, apiParams,  String.class);
        // ResponseEntity<String> response = restTemplate.exchange(validatorApiUrl, HttpMethod.POST, httpEntity,  String.class);
        // ResponseEntity<String> response = restTemplate.getForEntity(validatorApiUrl, httpEntity,  String.class); // (null)
        // UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validatorApiUrl)
        //     .queryParam("host", request.getHeader("Host"))
        //     .queryParam("logType", logType)
        //     .queryParam("userId", userId);
        
        // log.info("build: {}", builder.toUriString());
        
        // ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);


        // if(response.getStatusCode().is2xxSuccessful()) {
            log.info("Validator API response: {}", response.getBody());
        // }


    }

}
