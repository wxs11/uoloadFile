package com.example.upload.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upload.entity.AtipRoutMachineLogData;

import java.util.List;

/**
* @author xiaoshuai.wei
* @description 针对表【ATIP_ROUT_MACHINE_LOG_DATA】的数据库操作Service
* @createDate 2023-01-06 14:45:47
*/
public interface AtipRoutMachineLogDataService extends IService<AtipRoutMachineLogData> {

    /**
     * description: 插入数据
     * @date: 2023/2/7
     * @param: []
     * @return: void
     **/
    void insertLogMsg();
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
     * @param: [atipRoutMachineLogData] 实体类
     * @return: boolean
     **/
    boolean insertEndMsg(AtipRoutMachineLogData atipRoutMachineLogData);


}
