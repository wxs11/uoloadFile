package com.example.upload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.example.upload.config.ReadFileConfig;
import com.example.upload.entity.Logmsg;
import com.example.upload.service.LogmsgService;
import com.example.upload.service.ReadhistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UploadApplicationTests {
    @Resource
    ReadhistoryService readhistoryService;
    @Resource
    LogmsgService logmsgService;
    @Resource
    ReadFileConfig readFileConfig;

    @Test
    public void test() {
//        使用Hutool工具类进行获取目录下所有文件名
        String url = "D:/test/pro";
        List<File> fileList = FileUtil.loopFiles(url);
        List<Logmsg> resList = new ArrayList<>();
//        保存所有的文件名
        List<String> fileNames = new ArrayList<>();
//      获取数据库已存在的数据列表
        List<Logmsg> checklist =logmsgService.list();
//        循环获取文件夹下的所有文件名
        if (fileList != null) {
            for (File f : fileList) {
                fileNames.add(f.getName());
            }
        }
//        读取数据库中已经读过的文件记录
//        List<String> readHistory = readhistoryService.selectAll();
//        通过循环读取每一个文件
        assert fileList != null;
        for (File i : fileList) {
//            获取文件名
//            String fileName = i.getName();
//            if (readHistory.contains(fileName)) {
//                System.out.println("文件："+fileName+"已读取");
//            }else {
//                保存读取记录
//                readhistoryService.saveFileName(fileName);
//            获取文件尾缀
            String fileSuffix = FileNameUtil.getSuffix(i);
            if (fileSuffix.equals("PRO")) {
                try {
                     TimeInterval timer = DateUtil.timer();
                    ArrayList<String> read = FileUtil.readUtf8Lines(i, new ArrayList<>());


                    for (String readUtf8 : read) {
                            String[] sp = readUtf8.split("\\s+:");
                            //获取msg
                            String msg = sp[1];
                            String[] res = sp[0].replaceFirst("\\s", "-").split("\\s", 2);
                            //获取日志时间
                            String timeDate = res[0];
                            //获取日志num
                            String num = res[1];
                        boolean check =  checklist.stream().anyMatch(m ->m.getTimeDate().equals(timeDate) && m.getMsg().equals(msg));
                        if (!check) {
                            Logmsg logmsg = new Logmsg();
                            logmsg.setTimeDate(timeDate);
                            logmsg.setNum(num);
                            logmsg.setMsg(msg);
                            resList.add(logmsg);
//                            logmsgMapper.insertMsg(logmsg);
                        }
                    }
                    System.out.println("插入" + read.size() + "条数据花费时间" + timer.interval());//花费毫秒数);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (fileSuffix.equals("trs")) {
                try {
                    ArrayList<String> read = FileUtil.readUtf8Lines(i, new ArrayList<>());
                    TimeInterval timer = DateUtil.timer();
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
                        Logmsg logmsg = new Logmsg();
                        logmsg.setCode(code);
                        logmsg.setTimeDate(timeDate);
                        logmsg.setNum(num);
                        logmsg.setMsg(msg);
                        resList.add(logmsg);
//                        logmsgMapper.insertMsg(logmsg);
                    }
                    System.out.println("插入" + read.size() + "条数据花费时间" + timer.interval());//花费毫秒数);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }
            }
        }




    }
}





