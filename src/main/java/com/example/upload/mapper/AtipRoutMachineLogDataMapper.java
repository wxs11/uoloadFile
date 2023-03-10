package com.example.upload.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upload.entity.AtipRoutMachineLogData;

import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【ATIP_ROUT_MACHINE_LOG_DATA】的数据库操作Mapper
* @createDate 2023-01-06 14:45:47
* @Entity com.example.upload.entity.AtipRoutMachineLogData
*/
public interface AtipRoutMachineLogDataMapper extends BaseMapper<AtipRoutMachineLogData> {

    /**
     * description:插入压合日志数据
     * @date: 2023/1/30
     * @param: [atipRoutMachineLogData]实体类
     * @return: boolean
     **/
    boolean insertLogMsg(AtipRoutMachineLogData atipRoutMachineLogData);

    /**
     * description: 如果数据存在则更新
     * @date: 2023/1/30
     * @param: [atipRoutMachineLogData]实体类
     * @return: boolean
     **/
    boolean insertOrUpdate(AtipRoutMachineLogData atipRoutMachineLogData);

    /**
     * description: 查询是否重复
     * @date: 2023/1/30
     * @param: [date, time, msg]
     * @return: java.util.List<java.lang.String>
     **/
    List<String> selectMsg(String date,String time,String msg);
    /**
     * description: 插入尾行数据到对比表中
     * @date: 2023/2/7
     * @param: [atipRoutMachineLogData] 实体类
     * @return: boolean
     **/
    boolean insertEndMsg(AtipRoutMachineLogData atipRoutMachineLogData);

    /**
     * description:插入Error Log信息
     * @date: 2023/2/17
     * @param: [logData]
     * @return: boolean
     **/
    boolean insertErrorMsg(AtipRoutMachineLogData logData);

    /**
     * description:查询对比表数据
     * @date: 2023/2/7
     * @param: [date, time, msg]
     * @return: java.util.List<java.lang.String>
     **/
    List<String> selectCheckMsg(String date,String time,String msg,String fileName);

    /**
     * description: 查询邮件发送内容
     * @date: 2023/2/23
     * @param: [date, time, msg, fileName]
     * @return: java.util.List<java.lang.String>
     **/
    List<AtipRoutMachineLogData> selectEmailMsg();

    /**
     * description: 查询邮件地址
     * @date: 2023/2/23
     * @param: []
     * @return: java.util.List<java.lang.String>
     **/
    List<String> selectEmailAdress();
    /**
     * description: 插入钻孔日志数据
     * @date: 2023/3/10
     * @param: [atipRoutMachineLogData]
     * @return: boolean
     **/
    boolean insertZkLogMsg(AtipRoutMachineLogData logData);

    /**
     * description:查询钻孔对比表数据
     * @date: 2023/3/10
     * @param: [date, time, msg]
     * @return: java.util.List<java.lang.String>
     **/
    List<String> selectZkCheckMsg(String date,String time,String msg,String fileName);
    /**
     * description: 插入钻孔数据尾行到对比表
     * @date: 2023/3/10
     * @param: [logData]
     * @return: boolean
     **/
    boolean insertZkEndMsg(AtipRoutMachineLogData logData);
    /**
     * description: 插入钻孔Error 日志
     * @date: 2023/3/10
     * @param: [logData]
     * @return: boolean
     **/
    boolean insertZkErrorMsg(AtipRoutMachineLogData logData);
    /**
     * description: 查询钻孔Error邮件发送内容
     * @date: 2023/3/10
     * @param: []
     * @return: java.util.List<com.example.upload.entity.AtipRoutMachineLogData>
     **/
    List<AtipRoutMachineLogData> selectZkEmailMsg();
    /**
     * description: 查询钻孔邮件地址
     * @date: 2023/3/10
     * @param: []
     * @return: java.util.List<java.lang.String>
     **/
    List<String> selectZkEmailAdress();
}




