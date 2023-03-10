package com.example.upload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upload.entity.AtipRoutMachineLogData;

import javax.mail.MessagingException;
import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【ATIP_ROUT_MACHINE_LOG_DATA】的数据库操作Service
* @createDate 2023-01-06 14:45:47
*/
public interface AtipRoutMachineLogDataService extends IService<AtipRoutMachineLogData> {

    /**
     * description: 插入压合机器日志
     * @date: 2023/2/7
     * @param: []
     * @return: void
     **/
    void insertLogMsg() throws MessagingException;
/**
 * description: 查询总数据表
 * @date: 2023/2/7
 * @param: [date, time, msg]
 * @return: java.util.List<java.lang.String>
 **/
    List<String> selectMsg(String date,String time,String msg);

/**
 * description: 查询记录对比表
 * @date: 2023/2/7
 * @param: [date, time, msg]
 * @return: java.util.List<java.lang.String>
 **/
    List<String> selectCheckMsg(String date,String time,String msg,String fileName);

    /**
     * description: 插入尾行数据到对比表中
     * @date: 2023/2/7
     * @param: [atipRoutMachineLogData]
     * @return: boolean
     **/
    boolean insertEndMsg(AtipRoutMachineLogData atipRoutMachineLogData);
    /**
     * description: 插入Error信息
     * @date: 2023/2/17
     * @param:  atipRoutMachineLogData
     * @return: boolean
     **/
    boolean insertErrorMsg(AtipRoutMachineLogData atipRoutMachineLogData);

    /**
     * description: 查询邮件发送内容
     * @date: 2023/2/23
     * @param: [date, time, msg, fileName]
     * @return: java.util.List<java.lang.String>
     **/
    List<AtipRoutMachineLogData> selectEmailMsg();

    /**
     * description: 查询邮件发送地址
     * @date: 2023/2/23
     * @param: [date, time, msg, fileName]
     * @return: java.util.List<java.lang.String>
     **/

    List<String> selectEmailAdress();

    /**
     * description: 插入钻孔机器日志
     * @date: 2023/3/9
     * @param: []
     * @return: void
     **/
    void insertZkLog() throws MessagingException;

    /**
     * description: 查询钻孔记录对比表
     * @date: 2023/2/7
     * @param: [date, time, msg]
     * @return: java.util.List<java.lang.String>
     **/
    List<String> selectZkCheckMsg(String date,String time,String msg,String fileName);
    /**
     * description:插入钻孔Error 日志
     * @date: 2023/3/10
     * @param: [logData]
     * @return: boolean
     **/
    boolean insertZkErrorMsg(AtipRoutMachineLogData logData);
    /**
     * description: 插入钻孔尾行数据到对比表
     * @date: 2023/3/10
     * @param: [logData]
     * @return: boolean
     **/
    boolean insertZkEndMgs(AtipRoutMachineLogData logData);


}
