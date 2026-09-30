package com.validator.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 암호화 유틸 클래스
 * @author korea
 *
 */
public class EncUtils {
	
	private EncUtils() {
		 throw new AssertionError();
	}

	private static final String IV = "f2a80731447311cb8d72e6d0b72ead1d";
	private static final String DEFAULT_KEY = "cc0863ef8feff4190bb38c89881d58c99ca41b3e8ba84a164dffe84bb653b437";
	private static final String DEFAULT_AES = "AES/CBC/PKCS5Padding";

	/**
	 * AES 암호화
	 * assertEquals("UMh15bCLarqZdQx8oSX4iA==", EncUtils.encAES("test", SECRET_KEY));
	 * @param str
	 * @return
	 */
	public static String encAES(String str) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		return encAES(str, DEFAULT_KEY, DEFAULT_AES);
	}

	/**
	 * AES 암호화
	 * assertEquals("UMh15bCLarqZdQx8oSX4iA==", EncUtils.encAES("test", SECRET_KEY));
	 * @param str
	 * @return
	 */
	public static String encAES(String str, String secretKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance(algorithm);
		SecretKeySpec secretKeySpec = new SecretKeySpec(toBytes(secretKey), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(toBytes(IV));

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

		//return toHex(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
		return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * AES 복호화
	 * assertEquals("test", EncUtils.decAES("UMh15bCLarqZdQx8oSX4iA==", SECRET_KEY));
	 * @param str
	 * @return
	 */
	public static String decAES(String str)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return decAES(str, DEFAULT_KEY, DEFAULT_AES);
	}

	/**
	 * AES 복호화
	 * assertEquals("test", EncUtils.decAES("UMh15bCLarqZdQx8oSX4iA==", SECRET_KEY));
	 * @param str
	 * @return
	 */
	public static String decAES(String str, String secretKey, String algorithm)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(algorithm);
		SecretKeySpec secretKeySpec = new SecretKeySpec(toBytes(secretKey), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(toBytes(IV));

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

		//return new String(cipher.doFinal(toBytes(str)), "UTF-8");
		return new String(cipher.doFinal(Base64.getDecoder().decode(str)), "UTF-8");
	}

	/**
	 * SHA-256 암호화
	 * assertNotNull(EncUtils.encSHA256("TEST", "salt"));
	 */
	public static String encSHA256(String str, String salt) throws NoSuchAlgorithmException {
		return encSHA(str, salt, "SHA-256");
	}

	/**
	 * SHA-512 암호화
	 * assertNotNull(EncUtils.encSHA512("TEST", "salt"));
	 */
	public static String encSHA512(String str, String salt, String algorithm) throws NoSuchAlgorithmException {
		// return encSHA(str, salt, "SHA-512");
		return encSHA(str, salt, algorithm);
	}

	/**
	 * SHA 암호화
	 */
	private static String encSHA(String str, String salt, String algorithm) throws NoSuchAlgorithmException {
		String result = "";

		byte[] a = str.getBytes();
		byte[] b = salt.getBytes();
		byte[] bytes = new byte[a.length + b.length];

		System.arraycopy(a, 0, bytes, 0, a.length);
		System.arraycopy(b, 0, bytes, a.length, b.length);

		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(bytes);

			byte[] byteData = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
			}

			result = sb.toString(); // 최종 결과값을 result에 반환
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

		return result;
	}

	/**
	 * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
	 * @param hex    hex string
	 * @return
	 */
	private static byte[] toBytes(String hex) {

		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];

		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}

		return ba;
	}

	/**
	 * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
	 * @param batyes byte[]
	 * @return
	 */
	private static String toHex(byte[] batyes) {

		if (batyes == null || batyes.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(batyes.length * 2);
		String hexNumber;

		for (int x = 0; x < batyes.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & batyes[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}

		return sb.toString();
	}

    public static String Hmac(String key, String value, String algorithm) throws Exception{
        Mac hasher = Mac.getInstance(algorithm);
        hasher.init(new SecretKeySpec(key.getBytes("UTF-8"), algorithm));
        byte[] hash = hasher.doFinal(value.getBytes());
        // String haString = Hex.encodeHexString(hash);
        // String haString = hash.toString();
        String haString = Base64.getEncoder().encodeToString(hash);

        return haString;
    }


	public static String base64Decode(String base64Value) {
		try {
			byte[] decodedCookieBytes = Base64.getDecoder().decode(base64Value);
			return new String(decodedCookieBytes);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public static String readCookieValues(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		String cookieValue = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					cookieValue =  base64Decode(cookie.getValue());
					if (cookieValue == null) {
						continue;
					}
				}
			}
		}
		return cookieValue;
	}

	// public static void generateEncryptionKey() {
	// 	byte[] key = new byte[32];
	// 	new SecureRandom().nextBytes(key);
	// 	String encryptionKey = BaseEncoding.base64().encode(key);

	// 	System.out.println("Generated Base64-encoded AES-256 encryption key: " + encryptionKey);
	// }


}