/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.verify;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.verify.VerifyDto;
import com.ruisitech.bi.entity.write.DataLoadDto;
import com.ruisitech.bi.service.verify.VerifyService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @ClassName VerifyController
 * @Description VerifyController
 * @Author huangqin
 * @Date 2022/11/22 5:29 下午
 */
@Controller
@RequestMapping(value = "/verify")
public class VerifyController extends BaseController {

    private static Logger log = Logger.getLogger(VerifyController.class);

    @Autowired
    private VerifyService verifyService;

    @RequestMapping(value="/list.action")
    public @ResponseBody
    Object list(PageParam page, String typeId) {
        if(page != null && page.getPage() != null && page.getRows() != null){
            PageHelper.startPage(page.getPage(), page.getRows());
        }
        List<FormMeta> ls = verifyService.list(typeId);
        PageInfo<FormMeta> pageInfo=new PageInfo<>(ls);
        return super.buildSucces(pageInfo);
    }

    @RequestMapping(value="/listDatas.action")
    public @ResponseBody
    Object listDatas(@RequestBody DataLoadDto dto) {
        try {
            List<Map<String, Object>> dts = verifyService.queryTableDatas(dto);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(dts);
            pageInfo.setTotal(dto.getTotal());
            pageInfo.setPageSize(dto.getPage());
            pageInfo.setPageNum(dto.getRows());
            return super.buildSucces(pageInfo);
        }catch (Exception ex){
            log.error("获取数据出错", ex);
            return super.buildError(ex.getMessage());
        }
    }

    @RequestMapping(value="/verify.action")
    public @ResponseBody
    Object verify(@RequestBody VerifyDto dto) {
        try {
            verifyService.verify(dto);
            return super.buildSucces();
        }catch (Exception ex){
            log.error("审核数据出错", ex);
            return super.buildError(ex.getMessage());
        }
    }

    @RequestMapping(value="/getTable.action")
    public @ResponseBody
    Object getTable(String tableId) {
        return super.buildSucces(verifyService.getTableInfo(tableId));
    }
}
