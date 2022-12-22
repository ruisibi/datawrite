/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.model;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName TableController
 * @Description TableController
 * @Author huangqin
 * @Date 2022/12/5 2:21 下午
 */
@Controller("modelTableController")
@RequestMapping(value = "/model")
public class TableController extends BaseController {

    @Autowired
    private FormMetaService formMetaService;

    @Autowired
    private FormMetaColService formMetaColService;

    @RequestMapping(value="/listTables.action")
    public @ResponseBody
    Object list(){
        List<FormMeta> ls = formMetaService.listWirteForms(null);
        return super.buildSucces(ls);
    }

    @RequestMapping(value="/listColumns.action")
    public @ResponseBody
    Object listColumns(String tableId){
        List<FormMetaCol> ls = formMetaColService.selectByTableId(tableId);
        return super.buildSucces(ls);
    }
}
