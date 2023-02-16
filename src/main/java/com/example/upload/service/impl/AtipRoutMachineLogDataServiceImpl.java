package com.example.upload.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upload.entity.AtipRoutMachineLogData;
import com.example.upload.mapper.AtipRoutMachineLogDataMapper;
import com.example.upload.service.AtipRoutMachineLogDataService;
import com.example.upload.util.ReadFile;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
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

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    private AtipRoutMachineLogDataMapper logDataMapper;
    /**
     * description: 记录方法运行次数
     **/
    public static int count =0;


    @Scheduled(cron = "0 46 14 1/1 * ? ")
    @Override
    public void insertLogMsg() {
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
            count++;
            /*3.获取文件目录地址*/
//            String url = "D:/test/old";
//            String url = "D:/fileTest";
            String url = "\\\\172.16.9.121\\file3$\\share\\19 routdata";
            /*4.将文件存入集合 工具类 ReadTxt.txt2String(file)*/
            File file = new File(url);
//            计时器
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
                    fileName = "文件夹"+file1.getName()+"-"+fe.getName();
                    List<AtipRoutMachineLogData> al = ReadFile.getFiles(fe, resList,count,fileName);
                    if (al.size() == 0) {
                        System.out.println("文件" + fileName + "没有新增数据" + "\n读取文件消耗时间" + timer1.interval());
                    } else {
                        System.out.println("文件"+fileName+"有新增数据，读取" + al.size() + "条数据耗时:" + timer1.interval());
                        /*5.每次1000条 共 size次*/
                        int size = al.size() / 1000;
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

    @Override
    public List<String> selectMsg(String date, String time, String msg) {
        return logDataMapper.selectMsg(date,time,msg);
    }

    @Override
    public List<String> selectCheckMsg(String date, String time, String msg,String fileName) {
        return logDataMapper.selectCheckMsg(date, time, msg,fileName);
    }

    @Override
    public boolean insertEndMsg(AtipRoutMachineLogData atipRoutMachineLogData) {
        return logDataMapper.insertEndMsg(atipRoutMachineLogData);
    }


}



