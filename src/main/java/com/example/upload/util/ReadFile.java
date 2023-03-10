package com.example.upload.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.CharsetUtil;
import com.example.upload.entity.AtipRoutMachineLogData;
import com.example.upload.service.AtipRoutMachineLogDataService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author:wxs
 * @create: 2022-11-18 15:41
 * @Description: 读取文件
 */
@Component
public class ReadFile {
    public static ReadFile readFile;

    @Resource
    AtipRoutMachineLogDataService logDataService;

    /**
     * description: hutool流读取
     *
     * @date: 2022/11/21
     * @param: [url]
     * @return: java.util.List<com.example.upload.entity.Logmsg>
     **/
    public static List<AtipRoutMachineLogData> getFiles(File i, List<AtipRoutMachineLogData> resList, int count, String fileName, int type) throws ParseException {
//          获取文件尾缀
        String fileSuffix = FileNameUtil.getSuffix(i);
//            获取文件名
        String filePrefix = FileNameUtil.getPrefix(i);
        //OEM前缀
        String oem = "OEM";
        // OLD文件
        String old = "OLD";
        // PRO文件
        String pro = "PRO";
        // COM前缀
        String com = "COM";
        if (fileSuffix.equals(pro)) {
            //不读取OEM 和 COM前缀文件
            if (!filePrefix.contains(oem) && !filePrefix.contains(com)) {
                try {
                    // 保存文件读取记录
                    ArrayList<String> read = FileUtil.readLines(i, CharsetUtil.GBK, new ArrayList<>());
//                    对数组反转
                    Collections.reverse(read);
                    // 判断第一行数据是否是新加的
                    int fileCount = 0;
//                    后序遍历
                    for (String readUtf8 : read) {
                        fileCount++;
                        //查询当前行数据是否已存在
                        List<String> check = new ArrayList<>();
                        //count =1 说明程序第一次运行
                        if (readUtf8.indexOf("*") == 0) {
                            continue;
                        }
                        String[] sp = readUtf8.split("\\s+:");
                        //判断 msg是否为空 为空则跳过
                        if (sp.length < 2) {
                            continue;
                        }
                        //获取msg
                        String msg = sp[1];
                        String[] res = sp[0].replaceFirst("\\s", "-").split("\\s", 2);
                        //获取日志时间信息
                        String[] timeDate = res[0].split("-");
                        //判断字符是否异常
                        if (timeDate[0].length() > 50) {
                            String date = timeDate[0].substring(timeDate[0].length() - 10);
                            String time = timeDate[1];
                            String num = res[1];
                            check = checkLine(count, fileName, date, fileCount, time, msg, check,type);
                            //不存在则加入新增列表中
                            if (check.isEmpty()) {
                                AtipRoutMachineLogData logData = new AtipRoutMachineLogData();
                                logData.setDate(date);
                                logData.setTime(time);
                                logData.setNum(num);
                                logData.setMsg(msg);
                                logData.setFileName(fileName);
                                resList.add(logData);
                            } else {
                                break;
                            }
                        } else {
                            //获取日期
                            String date = timeDate[0];
                            //获取时间
                            String time = timeDate[1];
                            //获取日志num
                            String num = res[1];
                            //检查是否是错误信息
                            if (msg.contains("CHECK FOR BBD TOOL") || msg.contains("BBD CHANGE TOOL") || msg.contains("TOOL BROKEN Z(X)")
                                    || msg.contains("TOOL BROKEN WHEN PUT TOOL") || msg.indexOf("断刀") == 0) {
                                AtipRoutMachineLogData errorMsg = new AtipRoutMachineLogData();
                                errorMsg.setFileName(fileName);
                                errorMsg.setDate(date);
                                errorMsg.setTime(time);
                                errorMsg.setMsg(msg);
                                if (type == 1){
                                    readFile.logDataService.insertErrorMsg(errorMsg);
                                }else {
                                    readFile.logDataService.insertZkErrorMsg(errorMsg);
                                }
                            }
                            check = checkLine(count, fileName, date, fileCount, time, msg, check,type);
                            //不存在则加入新增列表中
                            if (check.isEmpty()) {
                                AtipRoutMachineLogData logData = new AtipRoutMachineLogData();
                                logData.setDate(date);
                                logData.setTime(time);
                                logData.setNum(num);
                                logData.setMsg(msg);
                                logData.setFileName(fileName);
                                resList.add(logData);
                            } else {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("读取文件内容出错");
                    e.printStackTrace();
                }
            }
        } else if ("txt".equals(fileSuffix)) {
            if (!filePrefix.contains("Cali")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date checkDate = dateFormat.parse("2019-12-31");
//                获取文件日期
                Date fileDate = dateFormat.parse(filePrefix);
//                判断txt文件是否是Cali scan 文件 并且文件时间创建大于2019-12-31
                if (fileDate.after(checkDate)) {
                    try {
                        ArrayList<String> reads = FileUtil.readLines(i, "UTF-16", new ArrayList<>());
                        Collections.reverse(reads);
                        //查询当前行数据是否已存在
                        List<String> check;
                        int fileCount = 0;
                        for (String line : reads) {
                            fileCount++;
                            String[] sp = line.split("\\s", 2);
                            if (sp[0].length() > 50) {
                                //获取时间
                                String[] res = sp[1].trim().split("\\s", 2);
                                String time = res[0];
                                //获取msg
                                String msg = res[1];
                                if (type == 1){
                                    check = readFile.logDataService.selectCheckMsg(filePrefix, time, msg, fileName);
                                }else {
                                    check = readFile.logDataService.selectZkCheckMsg(filePrefix,time,msg,fileName);
                                }
                                if (check.isEmpty()) {
                                    AtipRoutMachineLogData logData = new AtipRoutMachineLogData();
                                    logData.setDate(filePrefix);
                                    logData.setTime(time);
                                    logData.setMsg(msg);
                                    logData.setFileName(fileName);
                                    resList.add(logData);
                                }
                            } else {
                                String time = sp[0];
                                //获取msg
                                String msg = sp[1];
                                if (fileCount == 1) {
                                    // type = 1 压合  2 钻孔
                                    if (type == 1){
                                        check = readFile.logDataService.selectCheckMsg(filePrefix, time, msg, fileName);
                                    }else {
                                        check = readFile.logDataService.selectZkCheckMsg(filePrefix,time,msg,fileName);
                                    }
                                    if (check.isEmpty()) {
                                        AtipRoutMachineLogData endLine = new AtipRoutMachineLogData();
                                        endLine.setDate(filePrefix);
                                        endLine.setTime(time);
                                        endLine.setMsg(msg);
                                        endLine.setFileName(fileName);
                                        if (type == 1){
                                            readFile.logDataService.insertEndMsg(endLine);
                                        }else {
                                            readFile.logDataService.insertZkEndMgs(endLine);
                                        }
                                        AtipRoutMachineLogData logData = new AtipRoutMachineLogData();
                                        logData.setDate(filePrefix);
                                        logData.setTime(time);
                                        logData.setMsg(msg);
                                        logData.setFileName(fileName);
                                        resList.add(logData);
                                    } else {
                                        break;
                                    }
                                } else {
                                    AtipRoutMachineLogData logData = new AtipRoutMachineLogData();
                                    logData.setDate(filePrefix);
                                    logData.setTime(time);
                                    logData.setMsg(msg);
                                    logData.setFileName(fileName);
                                    resList.add(logData);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resList;
    }

    public static List<String> checkLine(int count, String fileName, String date, int fileCount, String time, String msg, List<String> check, int type) {
        //count =1 说明程序第一次运行 type =1 压合
        if (type == 1) {
            if (count == 1) {
                if (fileCount == 1) {
                    check = readFile.logDataService.selectCheckMsg(date, time, msg, fileName);
                    AtipRoutMachineLogData endLine = new AtipRoutMachineLogData();
                    endLine.setDate(date);
                    endLine.setTime(time);
                    endLine.setMsg(msg);
                    endLine.setFileName(fileName);
                    readFile.logDataService.insertEndMsg(endLine);
                }
            } else {
                check = readFile.logDataService.selectCheckMsg(date, time, msg, fileName);
                if (fileCount == 1 && check.isEmpty()) {
                    AtipRoutMachineLogData endLine = new AtipRoutMachineLogData();
                    endLine.setDate(date);
                    endLine.setTime(time);
                    endLine.setMsg(msg);
                    endLine.setFileName(fileName);
                    readFile.logDataService.insertEndMsg(endLine);
                }
            }
        }else {  // 钻孔
            if (count == 1) {
                if (fileCount == 1) {
                    check = readFile.logDataService.selectZkCheckMsg(date, time, msg, fileName);
                    AtipRoutMachineLogData endLine = new AtipRoutMachineLogData();
                    endLine.setDate(date);
                    endLine.setTime(time);
                    endLine.setMsg(msg);
                    endLine.setFileName(fileName);
                    readFile.logDataService.insertZkEndMgs(endLine);
                }
            } else {
                check = readFile.logDataService.selectZkCheckMsg(date, time, msg, fileName);
                if (fileCount == 1 && check.isEmpty()) {
                    AtipRoutMachineLogData endLine = new AtipRoutMachineLogData();
                    endLine.setDate(date);
                    endLine.setTime(time);
                    endLine.setMsg(msg);
                    endLine.setFileName(fileName);
                    readFile.logDataService.insertZkEndMgs(endLine);
                }
            }
        }
        return check;
    }

    /**
     * description: 初始化
     **/
    @PostConstruct
    public void init() {
        readFile = this;
    }

}
