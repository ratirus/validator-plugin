package com.validator.web.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException{
        // for SKIP SSL CERTIFICATE VERIFICATION IN SPRING REST TEMPLATE

    TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
         .loadTrustMaterial(null, acceptingTrustStrategy)
         .build();

    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

    CloseableHttpClient httpClient = HttpClients.custom()
         .setSSLSocketFactory(csf)
         // for 403 redirect
        //  .setRedirectStrategy(new LaxRedirectStrategy())
         .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    requestFactory.setConnectTimeout(3 * 1000);
    requestFactory.setReadTimeout(3 * 1000);

    RestTemplate restTemplate = new RestTemplate(requestFactory); 

    // // for 403 redirect
    // HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

    // factory.setHttpClient(httpClient);
    // restTemplate.setRequestFactory(factory);
  

    //   // api가 응답 메시지가 xml 포멧(application/xml)인데, 
    //   // 실제 HTTP Response 해더의 Media Type은 "text/html"로 오기 때문에 
    //   // Media Type 설정이 필요하다. 
    // StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();

    // stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8"))));

    // restTemplate.getMessageConverters().add(stringConverter);

  


    return restTemplate;
 
    //     return builder
    //             .setConnectTimeout(Duration.ofMillis(3000))
    //             .setReadTimeout(Duration.ofMillis(15000))
    //             .build();
    }


}
