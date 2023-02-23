package com.example.upload;

import cn.hutool.core.date.DateUtil;
import com.example.upload.entity.AtipRoutMachineLogData;
import com.example.upload.service.AtipRoutMachineLogDataService;
import com.example.upload.util.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class UploadApplicationTests {
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private AtipRoutMachineLogDataService logDataService;
    @Autowired
    SpringTemplateEngine springTemplateEngine;//Spring 模板引擎

    @Test
    public  void test1() throws MessagingException {
        String to = null;
        String title = "机器错误日志信息";
        Context context = new Context();
        List<String> adress = logDataService.selectEmailAdress();
        for (String i: adress) {
            to =to + i+",";
        }
// 获取当前时间字符串，yyyy-MM-dd HH:mm:ss
        String now = DateUtil.now();
        Date date = DateUtil.parse(now);
//一天的开始 00:00:00
        Date beginOfDay = DateUtil.beginOfDay(date);
//一天的结束 23:59:59
        Date endOfDay = DateUtil.endOfDay(date);
        List<AtipRoutMachineLogData> errorEmail = logDataService.selectEmailMsg();
        context.setVariable("infoList",errorEmail);
        context.setVariable("startTime",beginOfDay);
        context.setVariable("endTime",endOfDay);
        String tempContext = springTemplateEngine.process("mail.html", context);
        emailUtil.sendHtmlMail(to,title,tempContext);




    }

}





