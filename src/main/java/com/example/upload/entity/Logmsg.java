package com.example.upload.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * @TableName logMsg
 */
@TableName(value ="logMsg")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logmsg implements Serializable {
    /**
     * ID序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 时间
     */
    @TableField("timeDate")
    private String timeDate;

    /**
     * 型号
     */
    private String num;

    /**
     * 信息
     */
    private String msg;
    /**
     * 编码
     **/
    private String code;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}