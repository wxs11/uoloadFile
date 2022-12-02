package com.example.upload.util;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * @author:wxs
 * @create: 2022-11-21 10:29
 * @Description: Api生成
 */
public class Api {
    public static void main(String[] args) {
                DocsConfig config = new DocsConfig();
        // 项目根目录
        config.setProjectPath("D:\\projetc\\upload");
        // 项目名称
        config.setProjectName("upload");
        // 声明该API的版本
        config.setApiVersion("V1.0");
        // 生成API 文档所在目录
        config.setDocsPath("D:\\projetc\\upload\\src\\main\\resources\\apiDoc");
        // 配置自动生成
        config.setAutoGenerate(Boolean.TRUE);
        // 执行生成文档
        Docs.buildHtmlDocs(config);
    }
}
