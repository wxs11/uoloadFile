package com.example.upload.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upload.entity.Logmsg;
import com.example.upload.mapper.LogmsgMapper;
import com.example.upload.service.LogmsgService;
import com.example.upload.util.ReadFile;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiaoshuai.wei
 * @description 针对表【logMsg】的数据库操作Service实现
 * @createDate 2022-11-21 08:50:30
 */
@Component
@Service
public class LogmsgServiceImpl extends ServiceImpl<LogmsgMapper, Logmsg>
        implements LogmsgService {


    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Scheduled(cron = "0 10 8 * * ?")
    @Override
    public boolean insertMsg() {
        /*1.手动提交 所以是false*/
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(
                ExecutorType.BATCH, false);
        /*2.获取mybatis对象*/
        LogmsgMapper tm = session.getMapper(LogmsgMapper.class);
        try {
            /*3.获取文件目录地址*/
            String url = "D:/test/";
            /*4.将文件存入集合 工具类 ReadTxt.txt2String(file)*/
            TimeInterval timer1 = DateUtil.timer();
            List<Logmsg> al = ReadFile.getFiles(url);
            System.out.println("读取" + al.size() + "条数据耗时" + timer1.interval());
            /*5.每次1000条 共 size次*/
            int size = al.size() / 1000;
            //计算插入数据时间
            TimeInterval timer = DateUtil.timer();
            timer.start("1");
            for (int i = 1; i <= size + 1; i++) {
                timer.start("2");
                /*6.遍历每条插入到数据库 例如 i 3 插入3000 到4000条 */
                for (int k = (i - 1) * 1000; k < i * 1000 && k < al.size(); k++) {
                    tm.insertMsg(al.get(k));
                }
                //手动每1000个一提交，提交后无法回滚
                session.commit();
                //清理缓存，防止溢出
                session.clearCache();
                System.out.println("第" + i + "次耗时：" + (timer.intervalMs("2")) + "ms");
            }
            System.out.println("插入" + al.size() + "条数据总耗时" + timer.intervalMs("1") + "ms");
        } catch (Exception e) {
            //没有提交的数据可以回滚
            session.rollback();
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
        return true;
    }




}




