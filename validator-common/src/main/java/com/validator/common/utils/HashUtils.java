package com.validator.common.utils;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashUtils {
    

    public static String Hmac(String key, String value, String algorithm) throws Exception{
        log.info("key = {}", key);
        log.info("value = {}", value);
        log.info("alg  = {}", algorithm);
        Mac hasher = Mac.getInstance(algorithm);
        hasher.init(new SecretKeySpec(key.getBytes("UTF-8"), algorithm));
        byte[] hash = hasher.doFinal(value.getBytes());
        // String haString = Hex.encodeHexString(hash);
        // String haString = hash.toString();
        String haString = Base64.getEncoder().encodeToString(hash);
        // log.info("hash = {}", haString);
        return haString;
    }
}
