/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.util;

import com.rsbi.ext.engine.control.ContextListener;
import com.rsbi.ext.engine.init.TemplateManager;
import com.rsbi.ext.engine.init.VelocityManager;
import com.rsbi.ext.engine.init.XmlParser;
import com.rsbi.ext.engine.view.context.ExtContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 在项目启动或结束是做一些初始化或清理工作
 * @author hq
 * @date 2015-4-5
 */
public class ContextInitializeListener implements ServletContextListener {

	@Value("${spring.datasource.dbType}")
	private String dbName;

	@Value("${rsbi.upFilePath}")
	private String upFilePath;

	private ContextListener listener;

	private static Logger log = Logger.getLogger(ContextInitializeListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if(listener != null){
			listener.contextDest(sce);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//把变量放入 ExtContext
		ExtContext.getInstance().putConstants("dbName", dbName);
		ExtContext.getInstance().putConstants("upFilePath", upFilePath);
		ServletContext sct = sce.getServletContext();
		this.runSysJobs(sct);
		this.initExt(sce);
	}

	private void initExt(ServletContextEvent sce){
		ServletContext sct = sce.getServletContext();
		//初始化配置信息
		try{
			TemplateManager.initTemplate(sct);
			VelocityManager.initVelocity(sct);

			XmlParser.initParser(sct);
			XmlParser.getInstance().parserConfig(2);
		}catch(Exception ex){
			log.error("初始化EXT配置信息出错.",ex);
		}

		 String cl = ExtContext.getInstance().getConstant("contextListener");
		 if(cl != null && cl.length() > 0){
		 	try{
		 		listener = (ContextListener)Class.forName(cl).newInstance();
		 		listener.contextInit(sce);
		 	}catch(Exception ex){
		 		log.error("初始化ContextListener出错.",ex);
		 	}
		 }
	}

	private void runSysJobs(ServletContext sct){

	}
}
