/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.form;

import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import com.ruisitech.bi.service.form.TableService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 填报表管理
 * @Author huangqin
 * @Date 2022/10/23 1:54 下午
 */
@Controller("dwTableController")
@RequestMapping(value = "/form")
public class TableController extends BaseController {

    private static Logger log = Logger.getLogger(TableController.class);

    @Autowired
    private TableService tableService;

    @Autowired
    private FormMetaService formMetaService;

    @Autowired
    private FormMetaColService colService;

    /**
     * 根据json配置创建table
     * @param id
     * @return
     */
    @RequestMapping(value="/crtTable.action")
    public @ResponseBody
    Object crtTable(String id) {
        try {
            tableService.createTable(id);
            return super.buildSucces();
        }catch (Exception ex){
            log.error("创建表出错了.", ex);
            return super.buildError(ex.getMessage());
        }
    }

    @RequestMapping(value="/listTables.action")
    public @ResponseBody
    Object listTables() {
        List<FormMeta> ret = formMetaService.listWirteForms(null);
        return super.buildSucces(ret);
    }

    @RequestMapping(value="/listTablesCols.action")
    public @ResponseBody
    Object listTablesCols(String tableId) {
        List<FormMetaCol> ret = colService.selectByTableId(tableId);
        return super.buildSucces(ret);
    }
}
