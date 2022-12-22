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
import com.ruisitech.bi.entity.write.DataSaveDto;
import com.ruisitech.bi.service.write.WriteSaveService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据填报保存
 * @Author huangqin
 * @Date 2022/10/31 10:32 上午
 */
@Controller
@RequestMapping(value = "/write")
public class WriteSaveController extends BaseController {

    @Autowired
    private WriteSaveService writeSaveService;

    @PostMapping(value="/save.action")
    public @ResponseBody
    Object save(@RequestBody DataSaveDto dto) {
        writeSaveService.saveData(dto);
        return super.buildSucces();
    }

    @PostMapping(value="/update.action")
    public @ResponseBody
    Object update(@RequestBody DataSaveDto dto) {
        writeSaveService.updateData(dto);
        return super.buildSucces();
    }

    @PostMapping(value="/delete.action")
    public @ResponseBody
    Object delete(@RequestBody DataSaveDto dto) {
        writeSaveService.deleteData(dto);
        return super.buildSucces();
    }
}
