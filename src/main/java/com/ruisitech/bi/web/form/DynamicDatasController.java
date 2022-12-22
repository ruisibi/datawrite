/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.form;

import com.ruisitech.bi.service.form.DynamicDatasService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @ClassName DynamicDatasController
 * @Description DynamicDatasController
 * @Author huangqin
 * @Date 2022/12/20 10:22 上午
 */
@Controller
@RequestMapping(value = "/form")
public class DynamicDatasController extends BaseController {

    private static Logger log = Logger.getLogger(DynamicDatasController.class);

    @Autowired
    private DynamicDatasService dynamicDatasService;

    @RequestMapping(value="/dynamicDatas.action")
    public @ResponseBody
    Object dynamicDatas(@RequestBody HashMap<String, Object> params) {
        try {
            return super.buildSucces(dynamicDatasService.queryDatas(params));
        } catch (Exception ex) {
            log.error("获取数据出错.", ex);
            return super.buildError(ex.getMessage());
        }
    }
}
