package com.example.upload.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName readHistory
 */
@TableName(value ="readHistory")
@Data
public class Readhistory implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 已读取的文件名
     */
    private String filenames;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}