/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.write;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.frame.Role;
import com.ruisitech.bi.entity.write.DataLoadDto;
import com.ruisitech.bi.service.write.WriteService;
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
 * 数据填报表
 * @Author huangqin
 * @Date 2022/10/23 11:29 上午
 */
@Controller
@RequestMapping(value = "/write")
public class WriteController extends BaseController {

    private static Logger log = Logger.getLogger(WriteController.class);

    @Autowired
    private WriteService writeService;

    @RequestMapping(value="/list.action")
    public @ResponseBody
    Object list(PageParam page, String typeId) {
        if(page != null && page.getPage() != null && page.getRows() != null){
            PageHelper.startPage(page.getPage(), page.getRows());
        }
        List<FormMeta> ls = writeService.list(typeId);
        PageInfo<FormMeta> pageInfo=new PageInfo<>(ls);
        return super.buildSucces(pageInfo);
    }

    @RequestMapping(value="/getTable.action")
    public @ResponseBody
    Object getTable(String tableId) {
        return super.buildSucces(writeService.getTableInfo(tableId));
    }

    @RequestMapping(value="/listDatas.action")
    public @ResponseBody
    Object listDatas(@RequestBody DataLoadDto dto) {
        try {
            List<Map<String, Object>> dts = writeService.queryTableDatas(dto);
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

    @RequestMapping(value="/getData.action")
    public @ResponseBody
    Object getTable(String tableId, String id) {
        return super.buildSucces(writeService.getDataById(tableId, id));
    }

    @RequestMapping(value="/loadOpts.action")
    public @ResponseBody
    Object loadOpts(String tableId) {
        return super.buildSucces(writeService.loadOpts(tableId));
    }
}
