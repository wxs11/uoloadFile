package com.example.upload.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upload.entity.Logmsg;

import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【logMsg】的数据库操作Mapper
* @createDate 2022-11-21 08:50:30
* @Entity com.example.upload.entity.Logmsg
*/
public interface LogmsgMapper extends BaseMapper<Logmsg> {

    boolean insertMsg(Logmsg logmsg);

    List<String> selectTimeDate(String timeDate, String msg);

}




