/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.write;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.common.RequestStatus;
import com.ruisitech.bi.entity.common.Result;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
/**
 * @ClassName ImportService
 * @Description ImportService
 * @Author huangqin
 * @Date 2022/11/24 4:41 下午
 */
@Service
public class ImportService {

    @Autowired
    private FormMetaService formMetaService;

    @Autowired
    private FormMetaColService formMetaColService;

    @Autowired
    private WriteSaveService writeSaveService;

    @Resource(name = "daoHelper")
    private DaoHelper daoHelper;

    private final static Logger log = Logger.getLogger(ImportService.class);

    public FormMeta exportTemplate(String tableId, HttpServletResponse resp) {
        FormMeta meta = formMetaService.selectByPrimaryKey(tableId);
        List<FormMetaCol> cols = formMetaColService.selectByTableId(tableId);
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("页1");

        if(cols.size() > 1) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, cols.size() - 1);
            sheet.addMergedRegion(region);
        }

        //写标题
        Row row0 = sheet.createRow(0);
        Cell title = row0.createCell(0);
        title.setCellType(CellType.STRING);
        title.setCellValue(meta.getTableName()+" - " + meta.getTableNote());
        CellStyle titleStyle = createStyle(workbook, false);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        title.setCellStyle(titleStyle);

        //第一行写中文，第二行写英文
        Row row1 = sheet.createRow(1);
        for(int i=0; i<cols.size(); i++) {
            FormMetaCol col = cols.get(i);
            Cell cell = row1.createCell(i);
            cell.setCellValue(col.getColNote());
            cell.setCellType(CellType.STRING);
            CellStyle style = createStyle(workbook, true);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置前景填充样式
            style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 256 * 20);
        }
        Row row2 = sheet.createRow(2);
        for(int i=0; i<cols.size(); i++) {
            FormMetaCol col = cols.get(i);
            Cell cell = row2.createCell(i);
            cell.setCellValue(col.getColName());
            cell.setCellType(CellType.STRING);
            CellStyle style = createStyle(workbook, false);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置前景填充样式
            style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
            cell.setCellStyle(style);
        }
        //输出100行空格
        for(int i=3; i<=100; i++) {
            Row r = sheet.createRow(i);
            for(int j =0; j<cols.size(); j++) {
                FormMetaCol col = cols.get(j);
                Cell cell = r.createCell(j);
                if(col.getColType().equalsIgnoreCase("String")) {
                    cell.setCellType(CellType.STRING);
                }else if(col.getColType().equalsIgnoreCase("Int")
                        || col.getColType().equalsIgnoreCase("Double")
                        || col.getColType().equalsIgnoreCase("Long")) {
                    cell.setCellType(CellType.NUMERIC);
                }else {
                    cell.setCellType(CellType.NUMERIC);
                }

                cell.setCellStyle(createStyle(workbook, false));
            }
        }
        try {
            OutputStream os = resp.getOutputStream();
            workbook.write(os);
            os.flush();
        }catch(Exception ex) {
            log.error("下载模板出错。", ex);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return meta;
    }

    private CellStyle createStyle(XSSFWorkbook workbook, boolean isTitle) {
        //设置单元格样式
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        //font.setBold(true);
        font.setFontHeightInPoints((short)12);
        //font.setColor(XSSFColor.toXSSFColor(java.awt.Color.CYAN));
        if(isTitle) {
            font.setColor((short)44);
        }
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        //cell.setCellStyle(style);
        return style;
    }

    public Result upLoadFile(MultipartHttpServletRequest multiRequest, String tableId){
        Result ret = new Result();
        //获取multiRequest 中所有的文件名
        Iterator<String> iter=multiRequest.getFileNames();
        while(iter.hasNext()){
            //一次遍历所有文件
            MultipartFile file=multiRequest.getFile(iter.next().toString());
            if(file!=null) {

                //上传
                try {
                    int r = this.readExcel(file, tableId);
                    ret.setRows(r);
                } catch (Exception e) {
                    ret.setResult(RequestStatus.FAIL_FIELD.getStatus());
                    ret.setMsg(e.getMessage());
                    return ret;
                    //e.printStackTrace();
                }
            }
        }
        ret.setResult(RequestStatus.SUCCESS.getStatus());
        ret.setMsg("文件上传成功。");
        return ret;
    }

    public int readExcel(MultipartFile f, String tableId) throws Exception {
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = f.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet st = workbook.getSheetAt(0);
            int rownums = st.getLastRowNum();
            //第二行是表头
            Row title = st.getRow(2);
            List<String> titles = new ArrayList<String>();
            for(int i=0; i<title.getLastCellNum(); i++) {
                Cell c = title.getCell(i);
                titles.add(c.getStringCellValue());
            }
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            for(int i=3; i<rownums; i++) {  //数据从第三行开始
                Map<String, Object> data = new HashMap<String, Object>();
                Row row = st.getRow(i);
                for(int j=0; j<row.getLastCellNum(); j++) {
                    Cell c = row.getCell(j);
                    Object v = null;
                    CellType type = c.getCellTypeEnum();
                    if(type == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(c)) {
                            v = c.getDateCellValue();
                        }else {
                            //处理pom 读取把数字 读取为 .0 样式的小数。
                            double doubleVal = c.getNumericCellValue();
                            long longVal = Math.round(doubleVal);
                            Object inputValue = null;
                            if(Double.parseDouble(longVal + ".0") == doubleVal)
                                inputValue = longVal;
                            else
                                inputValue = doubleVal;
                            v = inputValue;
                        }
                    }else if(type == CellType.STRING) {
                        v = c.getStringCellValue();
                    }else if(type == CellType.BLANK){
                        v = null;
                    }
                    data.put(titles.get(j), v);
                }
                if(!allNull(data)) {
                    datas.add(data);
                }
            }
            //如果是必填，判断必填
            List<FormMetaCol> cols = formMetaColService.selectByTableId(tableId);
            for(int j=0; j< datas.size(); j++) {
                Map<String, Object> ent = datas.get(j);
                int size = cols.size();
                for(int i=0; i<size; i++) {
                    FormMetaCol col = cols.get(i);
                    Object val = ent.get(col.getColName());
                    if(col.getRequired() != null && col.getRequired() == 1 &&
                            (val == null || val.toString().length() == 0 )){

                        throw new RuntimeException("第"+(j +3 )+"行数据,字段 ["+col.getColNote()+"] 是必填项。");
                    }
                }

            }
            FormMeta meta = formMetaService.selectByPrimaryKey(tableId);
            process(datas, cols, meta);
            return datas.size();
        }finally {
            if(is != null) {
                is.close();
            }
            if(workbook != null) {
                workbook.close();
            }
        }
    }

    private void process(List<Map<String, Object>> datas, List<FormMetaCol> target, FormMeta meta) throws Exception{
        String userId = RSBIUtils.getLoginUserInfo().getUserId();
        String insertSql = writeSaveService.createSql(meta, target);
        daoHelper.execute(insertSql, ps->{
            for(Map<String, Object> ent : datas) {
                int size = target.size();
                for(int i=0; i<size; i++){
                    FormMetaCol col = target.get(i);
                    Object val = ent.get(col.getColName());
                    setPreparedStatementValue(ps, i + 1, val);
                }
                setPreparedStatementValue(ps, size + 1, UUID.randomUUID().toString().replaceAll("-", ""));
                setPreparedStatementValue(ps, size + 2, userId);
                setPreparedStatementValue(ps, size + 3, 0);
                setPreparedStatementValue(ps, size + 4, new Timestamp(new Date().getTime()));
                ps.addBatch();
            }
            ps.executeBatch();
            return null;
        });
    }

    private boolean allNull(Map<String, Object> data) {
        boolean ret = true;
        for(Map.Entry<String, Object> ent : data.entrySet()) {
            if(ent.getValue() != null && ent.getValue().toString().length() > 0) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static void setPreparedStatementValue(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setObject(index, null);
            return;
        }
        Object obj = value;
        if (obj instanceof Blob) {
            ps.setObject(index, null);
        } else if (obj instanceof Clob) {
            ps.setObject(index, null);
        } else if (obj instanceof Date) {
            ps.setTimestamp(index, new Timestamp(((Date) obj).getTime()));
        } else if (value instanceof oracle.sql.TIMESTAMP) {
            ps.setTimestamp(index, ((oracle.sql.TIMESTAMP) obj).timestampValue());
        } else if (value instanceof oracle.sql.DATE) {
            ps.setDate(index, ((oracle.sql.DATE) value).dateValue());
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, ((Timestamp) value));
        } else if (value instanceof java.sql.Date) {
            ps.setDate(index, (java.sql.Date) obj);
        } else {
            ps.setObject(index, value);
        }
    }
}
