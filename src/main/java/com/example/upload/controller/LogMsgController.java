package com.example.upload.controller;

import com.example.upload.entity.ResultVO;
import com.example.upload.service.LogmsgService;
import com.example.upload.util.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:wxs
 * @create: 2022-11-21 09:10
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class LogMsgController {
    @Resource
    LogmsgService logmsgService;


    /**
     * description: 上传txt文档
     * @date: 2022/11/18
     * @param: []
     * @return: com.example.upload.entity.ResultVO
     **/
    @PostMapping("/upload")
    public ResultVO insert(){
        boolean res = logmsgService.insertMsg();
        if (res) {
            return ResultUtil.success("保存成功！");
        }
        return ResultUtil.error();

    }


}
