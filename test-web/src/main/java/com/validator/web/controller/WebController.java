package com.validator.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.validator.common.utils.HashUtils;
import com.validator.common.utils.ShareUtils;
import com.validator.web.service.BoardService;
import com.validator.web.vo.ContentsVo;
import com.validator.web.vo.FileContentsVo;
import com.validator.web.vo.SearchVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class WebController {

    @Autowired
    private final BoardService boardService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Environment environment;

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
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

    
    @RequestMapping({"/",""})
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception{

        log.info("index");
        return "index.html";
    }

    // @RequestMapping({"/",""})
    // public String index(HttpServletRequest request, HttpServletResponse response) throws Exception{

    //     log.info("test");
    //     return "index.html";
    // }

    @RequestMapping("/type1")
    public String type1(){
        log.info("type1");
        return "test/type1";
    } 


    @RequestMapping("/type1_res")
    public @ResponseBody String type1Response(HttpServletRequest request, HttpServletResponse response) throws Exception{


        log.info("type1_response");

        Map<String, String> params = buildParametersMap(request);
        log.info("resqest = {}", params);

        String key = "validator";
        String algorithm = "HmacSHA512";

        String hashValue = HashUtils.Hmac(key, params.toString(), algorithm);
        
        return hashValue;
    }


    @RequestMapping("/type2_res")
    public  String type2Response(HttpServletRequest request, HttpServletResponse response) throws Exception{


        log.info("type1_response");

        Map<String, String> params = buildParametersMap(request);
        String haderCode = request.getHeader("code");
        log.info("resqest = {}", params);

        String key = "validator";
        String algorithm = "HmacSHA512";

        String hashValue = HashUtils.Hmac(key, params.toString(), algorithm);
        
        if(haderCode.equals(hashValue)){
            log.info("hadercode = {}", haderCode);
        }
        
        return "test/type2";
    }

    // @RequestMapping({"/",""})
    // public String index(HttpServletRequest request, HttpServletResponse response) throws Exception{

    //     log.info("index");
    //     return "index.html";
    // }

    @GetMapping("/get")
    public String getUrl(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return "get/index.html";
    }

    @GetMapping("/get_result")
    public String getResult(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String param1 = request.getParameter("param1");
        String param2 = request.getParameter("param2");
        String param3 = request.getParameter("param3");
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        model.addAttribute("param3", param3);
        return "get/result.html";
    }

    @GetMapping("/board")
    public String boardList(Model model, SearchVo searchParams) throws Exception{
        List<ContentsVo> boardList = boardService.boardList(searchParams);

        model.addAttribute("searchParams", searchParams);
        model.addAttribute("resource", PageInfo.of(boardList, searchParams.getNavigatePages()));
        return "board/list.html";
    }

    @GetMapping("/fileBoard")
    public String fileBoardList(Model model, SearchVo searchParams) throws Exception{
        List<FileContentsVo> boardList = boardService.fileBoardList(searchParams);

        model.addAttribute("searchParams", searchParams);
        model.addAttribute("resource", PageInfo.of(boardList, searchParams.getNavigatePages()));
        return "fileBoard/list.html";
    }

    @GetMapping("/boardInsert")
    public String boardInsert(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return "board/insert.html";
    }

    @PostMapping("/board/register")
    public String createBoard(Model model, ContentsVo contentsVo) throws Exception{
        boardService.insertBoard(contentsVo);
        return "board/list.html";
    }

    @GetMapping("/fileBoardInsert")
    public String fileBoardInsert(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return "fileBoard/insert.html";
    }

    @PostMapping("/board/fileRegister")
    public void createFileBoard(HttpServletRequest request, Model model, FileContentsVo fileContentsVo, @RequestParam("filename") MultipartFile file) throws Exception{
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Map<String, String> params = ShareUtils.makeParametersMap(request);
        logger.info("files {}",file);

        fileContentsVo.setFileName(file.getOriginalFilename());
        fileContentsVo.setFileSize(file.getSize());
        fileContentsVo.setFileExt(FilenameUtils.getExtension(fileContentsVo.getFileName()));

        String uploadPath = environment.getProperty("spring.servlet.multipart.location");

        logger.info("path = {}", uploadPath);

        Path dir = Paths.get(uploadPath);

        logger.info("dir = {}", dir);
        if(!Files.exists(dir)){
            try{
                Files.createDirectories(dir);
            } catch(IOException e){
                e.printStackTrace();
            }				
        }
        Path fullPath = Paths.get(uploadPath+file.getOriginalFilename());

        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        // FileCopyUtils.copy(file.getInputStream(), new FileOutputStream("upload"));

        logger.info("request = {}", params);
        logger.info("fileContents = {}",fileContentsVo.toString());
        boardService.insertFileBoard(request, fileContentsVo);
        // return "redirect:/fileBoard";
    }

    @RequestMapping("/board/detail")
    public String boardDetail(Model model, int boardIdx){

        ContentsVo detail = boardService.boardDetail(boardIdx);

        model.addAttribute("detail", detail);

        return "board/detail";
    }
}
