package com.example.upload.config;

import lombok.Data;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;


@Configuration
@ConfigurationProperties(prefix = "file")
@PropertySource(value= "classpath:FileReader.properties")//配置文件路径
@Data
@Component
/**
 * @author:wxs
 * @create: 2022-12-14 16:47
 * @Description: 文件读取配置
 */
public class ReadFileConfig {
    private int proReadLineNumber;
    private int trsReadLineNumber;
    private int oldReadLineNumber;


    /***
     * 动态获取配置文件内容不需要重启服务器
     * @param proFileName 配置文件名称
     * @param proKey 配置文件key值
     * @return 返回对应key的值
     */
    public static String getProperties(String proFileName,String proKey) {
        String retKeyVal = "";
        try {
            retKeyVal = PropertiesLoaderUtils.loadAllProperties(proFileName).get(proKey).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retKeyVal;
    }

    /**
     * 传递键值对的Map，更新properties文件
     *
     * @param fileName
     *            文件名(放在resource源包目录下)，需要后缀
     * @param keyValueMap
     *            键值对Map
     */
    public static void updateProperties(String fileName, Map<String, String> keyValueMap) {
        //getResource方法使用了utf-8对路径信息进行了编码，当路径中存在中文和空格时，他会对这些字符进行转换，这样，
        //得到的往往不是我们想要的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的中文及空格路径。

        BufferedWriter bw = null;

        try {
            String filePath = PropertiesUtil.class.getClassLoader().getResource(fileName).getFile();
            filePath = URLDecoder.decode(filePath,"utf-8");
            System.out.println("updateProperties propertiesPath:" + filePath);
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));
            System.out.println("updateProperties old:"+props);

            // 写入属性文件
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

//            props.clear();// 清空旧的文件

            for (String key : keyValueMap.keySet()) {
                props.setProperty(key, keyValueMap.get(key));
            }
            System.out.println("updateProperties new:"+props);
            props.store(bw, "");
        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
