package com.validator.web.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.validator.web.mapper.BoardMapper;
import com.validator.web.vo.ContentsVo;
import com.validator.web.vo.FileContentsVo;
import com.validator.web.vo.SearchVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardMapper boardMapper;

    @Transactional(readOnly = false)
    public void insertBoard(ContentsVo cvo){
        boardMapper.insertBoard(cvo);
    }

    @Transactional(readOnly = false)
    public void insertFileBoard(HttpServletRequest request, FileContentsVo cvo){
        // Iterator<String> fileNames = request.getFileNames();

        boardMapper.insertFileBoard(cvo);
    }

    @Transactional(readOnly = true)
    public List<ContentsVo> boardList(SearchVo searchParams){
        return boardMapper.selectBoard(searchParams);
    }
    
    @Transactional(readOnly = true)
    public List<FileContentsVo> fileBoardList(SearchVo searchParams){
        return boardMapper.selectFileBoard(searchParams);
    }


    @Transactional(readOnly = true)
    public ContentsVo boardDetail(int boardIdx){
        return boardMapper.selectBoardDetail(boardIdx);
    }
}
