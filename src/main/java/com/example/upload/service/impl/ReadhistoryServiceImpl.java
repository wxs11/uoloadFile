package com.example.upload.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upload.entity.Readhistory;
import com.example.upload.mapper.ReadhistoryMapper;
import com.example.upload.service.ReadhistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【readHistory】的数据库操作Service实现
* @createDate 2022-12-02 10:53:29
*/
@Service
public class ReadhistoryServiceImpl extends ServiceImpl<ReadhistoryMapper, Readhistory>
    implements ReadhistoryService{

    @Resource
    ReadhistoryMapper readhistoryMapper;

    @Override
    public List<String> selectAllFileNames() {
        return readhistoryMapper.selectAllFileNames();
    }

    @Override
    public void saveFileName(String fileName) {
        readhistoryMapper.saveFileName(fileName);
    }

}




