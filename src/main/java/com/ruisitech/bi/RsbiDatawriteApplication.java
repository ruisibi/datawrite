/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi;

import com.ruisitech.bi.util.ContextInitializeListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 睿思bi企业版springboot启动程序
 */
@ServletComponentScan
@SpringBootApplication
public class RsbiDatawriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsbiDatawriteApplication.class, args);
    }

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(contextInitializeListener());
        return servletListenerRegistrationBean;
    }

    @Bean
    public ContextInitializeListener contextInitializeListener(){
        return new ContextInitializeListener();
    }
}

