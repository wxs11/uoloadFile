package com.example.upload.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upload.entity.Readhistory;

import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【readHistory】的数据库操作Mapper
* @createDate 2022-12-02 10:53:29
* @Entity com.example.upload.entity.Readhistory
*/
public interface ReadhistoryMapper extends BaseMapper<Readhistory> {

    List<String> selectAllFileNames();

    void saveFileName(String fileName);


}




