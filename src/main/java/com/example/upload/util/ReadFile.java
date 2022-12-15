package com.example.upload.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.example.upload.entity.Logmsg;
import com.example.upload.service.LogmsgService;
import com.example.upload.service.ReadhistoryService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    ReadhistoryService readhistoryService;
    @Resource
    LogmsgService logmsgService;

    /**
     * description: hutool流读取
     * @date: 2022/11/21
     * @param: [url]
     * @return: java.util.List<com.example.upload.entity.Logmsg>
     **/
    public static List<Logmsg> getFiles(String url) {
//        保存需要插入的数据
        List<Logmsg> resList = new ArrayList<>();
//        使用Hutool工具类进行获取目录下所有文件名
        List<File> fileList = FileUtil.loopFiles(url);
//      获取数据库已存在的数据列表
        List<Logmsg> checklist =readFile.logmsgService.list();
//        通过循环读取每一个文件
        assert fileList != null;
//        读取数据库中已经读过的文件记录
//        List<String> readHistory = readFile.readhistoryService.selectAll();
        for (File i : fileList) {
//            获取文件名
//            String fileName = i.getName();
//            判断文件是否已经读取过
//            if (readHistory.contains(fileName)) {
//                System.out.println("文件：" + fileName + "已读取");
//            } else {
//            获取文件尾缀
            String fileSuffix = FileNameUtil.getSuffix(i);
//            if (fileName.contains("OLD") || fileName.contains("PRO"))  {
            if (fileSuffix.equals("PRO")) {
                try {
                    // 保存文件读取记录
//                        readFile.readhistoryService.saveFileName(fileName);
                    ArrayList<String> read = FileUtil.readUtf8Lines(i, new ArrayList<>());
//                    对数组反转
                    Collections.reverse(read);
//                    后序遍历
                    for (String readUtf8 : read) {
                        String[] sp = readUtf8.split("\\s+:");
                        //获取msg
                        String msg = sp[1];
                        String[] res = sp[0].replaceFirst("\\s", "-").split("\\s", 2);
                        //获取日志时间
                        String timeDate = res[0];
                        //获取日志num
                        String num = res[1];
//                      查询当前行 数据数据库是否存在
                        boolean check =  checklist.stream().anyMatch(m ->m.getTimeDate().equals(timeDate) && m.getMsg().equals(msg));
//                      不存在则加入新增列表中
                        if (!check) {
                            Logmsg logmsg = new Logmsg();
                            logmsg.setTimeDate(timeDate);
                            logmsg.setNum(num);
                            logmsg.setMsg(msg);
                            resList.add(logmsg);
                        }
                    }

                } catch (Exception e) {
                    System.out.println("读取文件内容出错");
                    e.printStackTrace();
                }
            } else if (fileSuffix.equals("trs")) {
                try {
//                  保存读取记录
//                  readFile.readhistoryService.saveFileName(fileName);

                    ArrayList<String> read = FileUtil.readUtf8Lines(i, new ArrayList<>());
//                    对数组反转
                    Collections.reverse(read);
//                    后序遍历
                    for (String readUtf8 : read) {
                        String[] sp = readUtf8.replaceFirst("\\$", "").split("\\$");
                        //获取日志编码
                        String code = sp[0];
                        String[] res = sp[1].replaceFirst("\\s", "-").split("\\s");
                        //获取日志时间
                        String timeDate = res[0];
                        //获取日志num
                        String num = res[1];
                        //获取日志信息
                        String msg = sp[2];
//                      查询当前行 数据数据库是否存在
                        boolean check =  checklist.stream().anyMatch(m ->m.getTimeDate().equals(timeDate) && m.getMsg().equals(msg));
//                      不存在则加入新增列表中
                        if (!check) {
                            Logmsg logmsg = new Logmsg();
                            logmsg.setCode(code);
                            logmsg.setTimeDate(timeDate);
                            logmsg.setNum(num);
                            logmsg.setMsg(msg);
                            resList.add(logmsg);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            }
        }
        return resList;
    }

    /**
     * description: 初始化
     **/
    @PostConstruct
    public void init() {
        readFile = this;
        readFile.readhistoryService = this.readhistoryService;
    }

}
