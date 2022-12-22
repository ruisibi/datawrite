/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.form;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormType;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.service.form.FormMetaService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName FormMetaController
 * @Description FormMetaController
 * @Author huangqin
 * @Date 2022/9/20 2:11 下午
 */
@Controller
@RequestMapping(value = "/form")
public class FormMetaController extends BaseController {

    @Autowired
    private FormMetaService formMetaService;

    @RequestMapping(value="/list.action")
    public @ResponseBody
    Object list(String cataId, PageParam page){
        if(page != null && page.getPage() != null && page.getRows() != null){
            PageHelper.startPage(page.getPage(), page.getRows());
        }
        List<FormMeta> ls = formMetaService.list(cataId);
        PageInfo<FormMeta> pageInfo=new PageInfo<>(ls);
        return super.buildSucces(pageInfo);
    }

    @RequestMapping(value="/save.action", method = RequestMethod.POST)
    public @ResponseBody
    Object save(FormMeta form) {
        if(form.getId() == null || form.getId().length() == 0) {
            form.setCreateUser(RSBIUtils.getLoginUserInfo().getUserId());
            form.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            JSONObject obj = JSONObject.parseObject(form.getTableCfg());
            obj.put("id", form.getId());
            form.setTableCfg(obj.toJSONString());
            form.setCreateDate(new Date());
            form.setUpdateDate(form.getCreateDate());
            form.setVersion(0);
            //设置表名
            if(form.getTableName() == null || form.getTableName().length() == 0){
                form.setTableName("t_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            }
            formMetaService.insertSelective(form);
        }else{
            form.setUpdateDate(new Date());
            formMetaService.updateByPrimaryKeySelective(form);
        }
        return super.buildSucces(form.getId());
    }

    @RequestMapping(value="/update.action", method = RequestMethod.POST)
    public @ResponseBody
    Object update(FormMeta form) {
        form.setUpdateDate(new Date());
        formMetaService.updateByPrimaryKeySelective(form);
        return super.buildSucces(form.getId());
    }

    @RequestMapping(value="/delete.action")
    public @ResponseBody
    Object delete(String id) {
        formMetaService.deleteByPrimaryKey(id);
        return super.buildSucces();
    }

    @RequestMapping(value="/getForm.action")
    public @ResponseBody
    Object getForm(String id) {
        return super.buildSucces(formMetaService.selectByPrimaryKey(id));
    }
}
