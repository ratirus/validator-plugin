package com.validator.common.utils;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShareUtils {

    public static Map<String, String> makeParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }
        
        return resultMap;
    }

    public static Map<String, String> makeMultipartParametersMap(MultipartHttpServletRequest httpServletRequest) throws Exception {

        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
		ObjectMapper objectMapper = new ObjectMapper();
        
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
			// log.info("makeMultipartParametersMap key: {}, value: {}", key, value);
        }

		Map<String, List<MultipartFile>> multiFileMap = httpServletRequest.getMultiFileMap();
		Map<String, String> fileInfoMap = new HashMap<>();
		for (String key : multiFileMap.keySet()) {
			List<MultipartFile> fileList = multiFileMap.get(key);
			for(MultipartFile file : fileList) {
				int index = fileList.indexOf(file);
				if(file != null && !file.isEmpty()) {
					String originalFilename = file.getOriginalFilename();
					String contentType = file.getContentType();
					long size = file.getSize();
					log.info("makeMultipartParametersMap file originalFilename: {}, contentType: {}, size: {}", originalFilename, contentType, size);

					fileInfoMap.put("vFileName", originalFilename);
					fileInfoMap.put("vFileContentType", contentType);
					fileInfoMap.put("vFileSize", String.valueOf(size));
					// resultMap.put(key + "[" + index + "]", fileInfoMap.toString());
					resultMap.put(key + "[" + index + "]", objectMapper.writeValueAsString(fileInfoMap));
					// resultMap.putAll(fileInfoMap);
				}
			}

		}
		// Iterator<String> itFiles = httpServletRequest.getFileNames();
		// Map<String, String> fileInfoMap = new HashMap<>();
		// // log.info("makeMultipartParametersMap fileNames: {}", itFiles.toString());
		// while (itFiles.hasNext()) {
		// 	String fileKey = (String) itFiles.next();
		// 	// log.info("makeMultipartParametersMap processing file: {}", (String)itFiles.next());
		// 	MultipartFile file = httpServletRequest.getFile(fileKey);
		// 	log.info("makeMultipartParametersMap processing fileKey: {}", fileKey);
		// 	if(file != null ) {
		// 		String originalFilename = file.getOriginalFilename();
		// 		String contentType = file.getContentType();
		// 		long size = file.getSize();
		// 		log.info("makeMultipartParametersMap file originalFilename: {}, contentType: {}, size: {}", originalFilename, contentType, size);

		// 		fileInfoMap.put("vFileName", originalFilename);
		// 		fileInfoMap.put("vFileContentType", contentType);
		// 		fileInfoMap.put("vFileSize", String.valueOf(size));
		// 		resultMap.put(fileKey, objectMapper.writeValueAsString(fileInfoMap));
		// 	}
		// 	// multipartFile을 사용하여 파일 처리
		// }
		// MultipartFile[] files = httpServletRequest.getFiles("file").toArray(new MultipartFile[0]);

        return resultMap;
    }

    public static Map<String, String> makeMultipartParametersMapLog(MultipartHttpServletRequest httpServletRequest) throws Exception {
        if((httpServletRequest.getParameterNames().hasMoreElements() == false) && (httpServletRequest.getFileNames().hasNext() == false)) {
			return null;
        }
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
		ObjectMapper objectMapper = new ObjectMapper();
        
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
			// log.info("makeMultipartParametersMap key: {}, value: {}", key, value);
        }

		Map<String, List<MultipartFile>> multiFileMap = httpServletRequest.getMultiFileMap();
		Map<String, String> fileInfoMap = new HashMap<>();
		for (String key : multiFileMap.keySet()) {
			List<MultipartFile> fileList = multiFileMap.get(key);
			for(MultipartFile file : fileList) {
				int index = fileList.indexOf(file);
				if(file != null && !file.isEmpty()) {
					String originalFilename = file.getOriginalFilename();
					String contentType = file.getContentType();
					long size = file.getSize();
					log.info("makeMultipartParametersMap file originalFilename: {}, contentType: {}, size: {}", originalFilename, contentType, size);

					fileInfoMap.put("vFileName", originalFilename);
					fileInfoMap.put("vFileContentType", contentType);
					fileInfoMap.put("vFileSize", String.valueOf(size));
					// resultMap.put(key + "[" + index + "]", fileInfoMap.toString());
					resultMap.put(key + "[" + index + "]", objectMapper.writeValueAsString(fileInfoMap));
					// resultMap.putAll(fileInfoMap);
				}
			}

		}

		// Iterator<String> itFiles = httpServletRequest.getFileNames();
		// Map<String, String> fileInfoMap = new HashMap<>();
		// // log.info("makeMultipartParametersMap fileNames: {}", itFiles.toString());
		// while (itFiles.hasNext()) {
		// 	// log.info("makeMultipartParametersMap processing file: {}", (String)itFiles.next());
		// 	String fileKey = (String) itFiles.next();
		// 	MultipartFile file = httpServletRequest.getFile(fileKey);
		// 	if(file != null && !file.isEmpty()) {
		// 	log.info("makeMultipartParametersMapLog processing fileKey: {}", fileKey);
		// 		String originalFilename = file.getOriginalFilename();
		// 		String contentType = file.getContentType();
		// 		long size = file.getSize();
		// 		log.info("makeMultipartParametersMap file originalFilename: {}, contentType: {}, size: {}", originalFilename, contentType, size);

		// 		fileInfoMap.put("vFileName", originalFilename);
		// 		fileInfoMap.put("vFileContentType", contentType);
		// 		fileInfoMap.put("vFileSize", String.valueOf(size));
		// 		resultMap.put(fileKey, objectMapper.writeValueAsString(fileInfoMap));
		// 	}
		// 	// multipartFile을 사용하여 파일 처리
		// }
        
        return resultMap;
    }

    // public static List<AttachFile> uploadFiles(HttpServletRequest request) throws IOException {
	// 	List<AttachFile> files = new ArrayList<AttachFile>();
	// 	MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
	// 	Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

    //     while (iterator.hasNext()) {
	// 		final List<MultipartFile> items = multipartHttpServletRequest.getFiles(iterator.next());
	// 		for(MultipartFile item : items){
	// 			if(item.getContentType().equals("application/json")){
	// 				continue;
	// 			}
	// 			try (final InputStream inputStream = item.getInputStream()) {
	// 				if (item.getSize() > 0) {
	// 					files.add(attachFile(item.getOriginalFilename(), item.getSize(), inputStream));
	// 				}
	// 				else inputStream.skip(inputStream.available());
	// 			}
	// 		}            
    //     }
	// 	return files;
	// }

    // public static AttachFile uploadFile(HttpServletRequest request) throws IOException {
	// 	List<AttachFile> files = uploadFiles(request);
	// 	if(files != null && files.size() == 1) {
	// 		return files.get(0);
	// 	}
	// 	return null;
	// }

   
    // public static AttachFile attachFile(String fileName, long length, InputStream is) {
		
	// 	AttachFile attach = new AttachFile();
	// 	try(InputStream iis = TikaInputStream.get(is)){

	// 		Tika tika = new Tika();
    //     	String contentType = tika.detect(iis);
	// 		// String mimeExt = getExtension(contentType);
	// 		final int dotIndex = fileName.lastIndexOf('.');
	// 		//파일명에 특수 문자 제거
	// 		final String filenameBody = (dotIndex >= 0 ? fileName.substring(0, dotIndex) : fileName).replaceAll("[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\+<>@\\#$%&\\\\\\=\\(\\’\\'\\.\\\"]", "");;

	// 		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
	// 		// if(ext == null || !mimeExt.equalsIgnoreCase(ext)) {
	// 		// 	ext = mimeExt;
	// 		// }
	// 		String path = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/";
	// 		// String name = attach.getFileCode();
	// 		attach.setFileSize(length);
	// 		attach.setOriginFileName(filenameBody + ext);
	// 		attach.setExtension(ext.replaceAll("\\.", ""));
	// 		attach.setFilePath(path);
	// 		// attach.setFileName(attach.ge);
	// 		attach.setContentType(contentType);

			
	// 	} catch(Exception e) {
	// 	}
	// 	return attach;
	// }

    // public static Path fileToSave(final Path location, final String baseFilename) throws IOException {
	// 	//V.noNull(location, "location");
	// 	//V.noEmpty(baseFilename, "baseFilename");

	// 	if (!Files.exists(location))
	// 		Files.createDirectories(location);
	// 	else if (!Files.isDirectory(location))
	// 		throw new IOException("`location` must be a directory. path=" + location.toAbsolutePath().toString());

	// 	final int dotIndex = baseFilename.lastIndexOf('.');
	// 	final String filenameBody = dotIndex >= 0 ? baseFilename.substring(0, dotIndex) : baseFilename;
	// 	final String extension = dotIndex >= 0 ? baseFilename.substring(dotIndex) : "";

	// 	Path file = location.resolve(baseFilename);

	// 	long sequence = 0;
	// 	boolean created = false;

	// 	while (!created) {
	// 		if (!Files.exists(file)) {
	// 			try {
	// 				Files.createFile(file);
	// 				created = true;
	// 				log.info("A file is created. {}", file.toAbsolutePath().toString());
	// 			} catch (FileAlreadyExistsException e) {
	// 				file = location.resolve(filenameBody + "." + (sequence++) + extension);
	// 			}
	// 		} else {
	// 			file = location.resolve(filenameBody + "." + (sequence++) + extension);
	// 		}
	// 	}

	// 	return file;

	// }

	public static boolean isAjax(HttpServletRequest request){
        if(request.getHeader("X-Requested-With").equals("XMLHttpRequest")){
            return true;
        }
        return false;
    }
	public static String getClientIP(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;

	}

    public static Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) throws Exception {
        if(httpServletRequest.getParameterNames().hasMoreElements() == false) {
            return null;
        }
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String[] value = httpServletRequest.getParameterValues(key);
			if(value.length == 1) {
				resultMap.put(key,  value[0].trim());
			} else {
				resultMap.put(key,  Arrays.toString(value));
			}
            
        }
        
        return resultMap;
    }
}
