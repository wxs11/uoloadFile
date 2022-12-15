package com.example.upload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upload.entity.Readhistory;

import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【readHistory】的数据库操作Service
* @createDate 2022-12-02 10:53:29
*/
public interface ReadhistoryService extends IService<Readhistory> {

    /**
     * description: 查询所有记录
     * @date: 2022/12/2
     * @param: []
     * @return: java.util.List<com.example.upload.entity.Readhistory>
     **/
   List<String> selectAllFileNames();

   /**
    * description: 保存读取文件记录
    * @date: 2022/12/2
    * @param: [fileName]文件名称
    * @return: void
    **/
    void saveFileName(String fileName);

}
