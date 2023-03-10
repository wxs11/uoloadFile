package com.example.upload.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upload.entity.AtipRoutMachineLogData;
import com.example.upload.mapper.AtipRoutMachineLogDataMapper;
import com.example.upload.service.AtipRoutMachineLogDataService;
import com.example.upload.util.EmailUtil;
import com.example.upload.util.ReadFile;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaoshuai.wei
 * @description 针对表【ATIP_ROUT_MACHINE_LOG_DATA】的数据库操作Service实现
 * @createDate 2023-01-06 14:45:47
 */
@Component
@Service
public class AtipRoutMachineLogDataServiceImpl extends ServiceImpl<AtipRoutMachineLogDataMapper, AtipRoutMachineLogData>
        implements AtipRoutMachineLogDataService {

    /**
     * description: 记录成型方法运行次数
     **/
    public static int count = 1;
    /**
     * description: 记录钻孔方法运行次数
     **/
    public static int ZkCount = 0;
    @Resource
    EmailUtil emailUtil;
    @Resource
    SpringTemplateEngine springTemplateEngine;
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;
    @Resource
    private AtipRoutMachineLogDataMapper logDataMapper;

    @Scheduled(cron = "0 0 2,15 1/1 * ? ")
    @Override
    public void insertLogMsg() throws MessagingException {
        //成型机器日志路径
        count++;
        String url = "\\\\172.16.9.121\\file3$\\share\\19 routdata";
        // type 1 成型  2 钻孔
        int type = 1;
        saveFileUtil(url, type, count);
        //获取成型邮件地址
        List<String> adress = logDataMapper.selectEmailAdress();
        //获取要发送的Error信息
        List<AtipRoutMachineLogData> errorMsgList = logDataMapper.selectEmailMsg();
        sendEmilUtil(adress, errorMsgList);

    }

    @Override
    public List<String> selectMsg(String date, String time, String msg) {
        return logDataMapper.selectMsg(date, time, msg);
    }

    @Override
    public List<String> selectCheckMsg(String date, String time, String msg, String fileName) {
        return logDataMapper.selectCheckMsg(date, time, msg, fileName);
    }

    @Override
    public boolean insertEndMsg(AtipRoutMachineLogData atipRoutMachineLogData) {
        return logDataMapper.insertEndMsg(atipRoutMachineLogData);
    }

    @Override
    public boolean insertErrorMsg(AtipRoutMachineLogData atipRoutMachineLogData) {
        return logDataMapper.insertErrorMsg(atipRoutMachineLogData);
    }

    @Override
    public List<AtipRoutMachineLogData> selectEmailMsg() {
        return logDataMapper.selectEmailMsg();
    }

    @Override
    public List<String> selectEmailAdress() {
        return logDataMapper.selectEmailAdress();
    }

    //    @Scheduled(cron = "0 20 2,15 1/1 * ? ")
    @Override
    public void insertZkLog() throws MessagingException {
        //钻孔机器日志路径
//        String url = "\\\\172.16.9.121\\share\\20 drilldata";
        String url = "D:/test";
        // type 1 成型  2 钻孔
        int type = 2;
        ZkCount++;
        saveFileUtil(url, type, ZkCount);
        //获取成型邮件地址
        List<String> adress = logDataMapper.selectZkEmailAdress();
        //获取要发送的Error信息
        List<AtipRoutMachineLogData> errorMsgList = logDataMapper.selectZkEmailMsg();
        sendEmilUtil(adress, errorMsgList);
    }

    public void saveFileUtil(String url, int type, int count) {
        /*1.手动提交 所以是false*/
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(
                ExecutorType.BATCH, false);
        /*2.获取mybatis对象*/
        AtipRoutMachineLogDataMapper tm = session.getMapper(AtipRoutMachineLogDataMapper.class);
        //        保存需要插入的数据
        List<AtipRoutMachineLogData> resList = new ArrayList<>();
        // 文件名
        String fileName = null;
        try {
            /*3.获取文件目录地址*/
            /*4.将文件存入集合 工具类 */
            File file = new File(url);
//          计时器
            TimeInterval timer = DateUtil.timer();
            //获取主目录下的所有文件夹
            File[] dirFile = file.listFiles();
            //计算插入数据总数量
            int sumCount = 0;
            //计算插入数据时间
            timer.start("1");
            //循环获取当前文件夹的文件并存储
            for (File file1 : dirFile) {
                //获取当前文件夹下的所有文件
                String localFileUrl = file1.getAbsolutePath();
                TimeInterval timer1 = DateUtil.timer();
                //        使用Hutool工具类进行获取目录下所有文件名
                List<File> fileList = FileUtil.loopFiles(new File(localFileUrl));
                //        通过循环读取每一个文件
                assert fileList != null;
                for (File fe : fileList) {
                    fileName = "文件夹" + file1.getName() + "-" + fe.getName();
                    List<AtipRoutMachineLogData> al = ReadFile.getFiles(fe, resList, count, fileName, type);
                    if (al.size() == 0) {
                        System.out.println("文件" + fileName + "没有新增数据" + "\n读取文件消耗时间" + timer1.interval());
                    } else {
                        System.out.println("文件" + fileName + "有新增数据，读取" + al.size() + "条数据耗时:" + timer1.interval());
                        /*5.每次1000条 共 size次*/
                        int size = al.size() / 1000;
                        if (type == 1) {
                            for (int i = 1; i <= size + 1; i++) {
                                /*6.遍历每条插入到数据库 例如 i 3 插入3000 到4000条 */
                                for (int k = (i - 1) * 1000; k < i * 1000 && k < al.size(); k++) {
                                    tm.insertLogMsg(al.get(k));
                                }
                                //手动每1000个一提交，提交后无法回滚
                                session.commit();
                                //清理缓存，防止溢出
                                session.clearCache();
                            }
                        } else {
                            for (int i = 1; i <= size + 1; i++) {
                                /*6.遍历每条插入到数据库 例如 i 3 插入3000 到4000条 */
                                for (int k = (i - 1) * 1000; k < i * 1000 && k < al.size(); k++) {
                                    tm.insertZkLogMsg(al.get(k));
                                }
                                //手动每1000个一提交，提交后无法回滚
                                session.commit();
                                //清理缓存，防止溢出
                                session.clearCache();
                            }
                        }
                        sumCount += al.size();
                        fileName = null;
                        resList.clear();
                    }
                }
            }
            System.out.println("插入" + sumCount + "条数据总耗时" + timer.intervalMs("1") + "ms");

        } catch (Exception e) {
            //没有提交的数据可以回滚
            e.printStackTrace();
            session.rollback();
            System.out.println("读取文件" + fileName + "内容出错");
        } finally {
            session.close();
        }
    }


    /**
     * description:  Error信息发送邮件类
     *
     * @date: 2023/3/9
     * @param: [adress 要发送的邮件地址, errorMsgList 要发送的邮件内容]
     * @return: void
     **/
    public void sendEmilUtil(List<String> adress, List<AtipRoutMachineLogData> errorMsgList) throws MessagingException {
        //发送邮件
        String to = "";
        String title = "机器异常错误日志信息";
        if (errorMsgList.size() > 0) {
            Context context = new Context();
            for (String i : adress) {
                to = to + i + ",";
            }
            // 获取当前时间字符串，yyyy-MM-dd HH:mm:ss
            String now = DateUtil.now();
            Date date = DateUtil.parse(now);
            //一天的开始 00:00:00
            Date beginOfDay = DateUtil.beginOfDay(date);
            //一天的结束 23:59:59
            Date endOfDay = DateUtil.endOfDay(date);
            context.setVariable("infoList", errorMsgList);
            context.setVariable("startTime", beginOfDay);
            context.setVariable("endTime", endOfDay);
            String tempContext = springTemplateEngine.process("mail.html", context);
            emailUtil.sendHtmlMail(to, title, tempContext);
        }
    }

    @Override
    public List<String> selectZkCheckMsg(String date, String time, String msg, String fileName) {
        return logDataMapper.selectZkCheckMsg(date, time, msg, fileName);
    }

    @Override
    public boolean insertZkErrorMsg(AtipRoutMachineLogData logData) {
        return logDataMapper.insertZkErrorMsg(logData);
    }

    @Override
    public boolean insertZkEndMgs(AtipRoutMachineLogData logData) {
        return logDataMapper.insertZkEndMsg(logData);
    }
}




