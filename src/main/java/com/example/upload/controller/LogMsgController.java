package com.example.upload.controller;

import com.example.upload.entity.AtipRoutMachineLogData;
import com.example.upload.service.AtipRoutMachineLogDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:wxs
 * @create: 2022-11-21 09:10
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class LogMsgController {


    @Resource
    AtipRoutMachineLogDataService logDataService;


    /**
     * description: 上传txt文档
     * @date: 2022/11/18
     * @param: []
     * @return: com.example.upload.entity.ResultVO
     **/
    @PostMapping("/upload")
    public void insert(){
        logDataService.insertLogMsg();
    }

    @PostMapping("/select")
    public List<AtipRoutMachineLogData> selectEmailMsg(){
        return  logDataService.selectEmailMsg();
    }


}
