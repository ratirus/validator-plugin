package com.validator.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.validator.web.vo.ContentsVo;
import com.validator.web.vo.FileContentsVo;
import com.validator.web.vo.SearchVo;

@Mapper
public interface BoardMapper {
   void insertBoard(ContentsVo contentVo); 
   List<ContentsVo> selectBoard(SearchVo searchParams);
   void insertFileBoard(FileContentsVo contentVo); 
   List<FileContentsVo> selectFileBoard(SearchVo searchParams);
   ContentsVo selectBoardDetail(int idx);

}
