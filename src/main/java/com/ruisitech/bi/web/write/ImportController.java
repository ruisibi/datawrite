/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.write;

import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.service.write.ImportService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导入数据
 * @Author huangqin
 * @Date 2022/11/24 4:39 下午
 */
@Controller
@RequestMapping(value = "/write")
public class ImportController extends BaseController {

    @Autowired
    private ImportService importService;

    private static Logger log = Logger.getLogger(ImportController.class);

    @RequestMapping(value="/ExportTemplate.action", method = RequestMethod.GET)
    public void exportTemplate(String tableId, HttpServletRequest req, HttpServletResponse res) {
        try {
            res.setContentType("application/x-msdownload");
            String contentDisposition = "attachment; filename=\"file.xlsx\"";
            res.setHeader("Content-Disposition", contentDisposition);
            FormMeta meta = importService.exportTemplate(tableId, res);
        }catch (Exception ex){
            log.error("导出出错了", ex);
        }
    }

    @RequestMapping(value="/ImportUpload.action", method = RequestMethod.POST)
    public @ResponseBody
    Object upLoad(String tableId, HttpServletRequest request){

        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest)request;
            return importService.upLoadFile(multiRequest, tableId);
        }

        return super.buildSucces();
    }

}
