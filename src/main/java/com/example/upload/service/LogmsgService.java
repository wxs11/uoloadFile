package com.example.upload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upload.entity.Logmsg;

/**
* @author xiaoshuai.wei
* @description 针对表【logMsg】的数据库操作Service
* @createDate 2022-11-21 08:50:30
*/
public interface LogmsgService extends IService<Logmsg> {

    boolean insertMsg();

}
