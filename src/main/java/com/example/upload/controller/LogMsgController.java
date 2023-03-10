package com.example.upload.controller;

import com.example.upload.entity.AtipRoutMachineLogData;
import com.example.upload.service.AtipRoutMachineLogDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;
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
     * description: 上传压合机器日志
     * @date: 2022/11/18
     * @param: []
     * @return: com.example.upload.entity.ResultVO
     **/
    @PostMapping("/upload")
    public void insert() throws MessagingException {
        logDataService.insertLogMsg();
    }

    /**
     * description: 上传钻孔机器日志
     * @date: 2023/3/9
     * @param: []
     * @return: void
     **/
    @PostMapping("/saveZk")
    public void insertZkLog() throws MessagingException { logDataService.insertZkLog();}


    /**
     * description: 查询Error内容发送邮件
     * @date: 2023/3/9
     * @param: []
     * @return: java.util.List<com.example.upload.entity.AtipRoutMachineLogData>
     **/
    @PostMapping("/select")
    public List<AtipRoutMachineLogData> selectEmailMsg(){
        return  logDataService.selectEmailMsg();
    }


}
