package com.example.upload.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author xiaoshuai.wei
 * @TableName ATIP_ROUT_MACHINE_LOG_DATA
 */
@TableName(value ="ATIP_ROUT_MACHINE_LOG_DATA")
@Data
public class AtipRoutMachineLogData implements Serializable {
    /**
     * LOG序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * LOG日期
     */
    private String date;

    /**
     * LOG时间
     */
    private String time;

    /**
     * LOG明细
     */
    private String msg;

    /**
     * LOG代码
     */
    private String code;

    /**
     * LOG创建时间
     */
    @TableField("CREATETIME")
    private Date createTime;

    /**
     * LOG编号
     */
    private String num;

    /**
     * 文件名
     */
    @TableField("FILENAME")
    private String fileName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}