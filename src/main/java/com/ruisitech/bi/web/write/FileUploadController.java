/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.write;

import com.ruisitech.bi.entity.write.FileUploadDto;
import com.ruisitech.bi.service.write.UpLoadFileService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName FileUploadController
 * @Description FileUploadController
 * @Author huangqin
 * @Date 2022/11/21 3:22 下午
 */
@Controller
@RequestMapping(value = "/write")
public class FileUploadController extends BaseController {

    @Autowired
    private UpLoadFileService service;

    private static final Logger log = Logger.getLogger(FileUploadController.class);

    @RequestMapping(value="/FileUpload.action", method = RequestMethod.POST)
    public @ResponseBody
    Object upLoad(String tempId, Long fileId, HttpServletRequest request){
        try {
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                List<FileUploadDto> ret = service.upLoadFile(multiRequest,  tempId, fileId);
                return super.buildSucces(ret);
            }else{
                return super.buildError("无文件");
            }
        }catch (Exception ex){
            log.error("上传文件出错", ex);
            return super.buildError(ex.getMessage());
        }
    }

    @RequestMapping(value="/fileReomve.action", method = RequestMethod.GET)
    public @ResponseBody
    Object fileRemove(String tempId, Long fileId, HttpServletRequest request){
        service.fileRemove(request, tempId, fileId);
        return super.buildSucces();
    }

    @RequestMapping(value="/img/{fileName}/{extName}")
    public void view(@PathVariable("fileName") String fileName, @PathVariable("extName") String extName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("image/png");
        service.readFile(request, response.getOutputStream(), fileName, extName);
    }

    @RequestMapping(value="/fileCache.action", method = RequestMethod.POST)
    public @ResponseBody
    Object fileCache(String tempId, String json){
        service.cacheFiles(tempId, json);
        return super.buildSucces();
    }

}
