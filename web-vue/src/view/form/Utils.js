import $ from 'jquery'
import {baseUrl, ajax, formatDate} from '@/common/biConfig'

export const compTypes = [
    {id:"input",text:"输入框", "icon":"comp_color fa fa-binoculars"},
    {id:"inputNumber",text:"数字框", "icon":"comp_color fa fa-binoculars"},
    {id:"radio",text:"单选框", "icon":"comp_color fa fa-binoculars"},
    {id:"checkbox",text:"多选框", "icon":"comp_color fa fa-binoculars"},
    {id:"select",text:"下拉框", "icon":"comp_color fa fa-binoculars"},
    /** {id:"cascader",text:"及联选择器", "icon":"comp_color fa fa-binoculars"}, **/
    {id:"switch",text:"Switch开关", "icon":"comp_color fa fa-binoculars"},
    {id:"slider",text:"Slider滑块", "icon":"comp_color fa fa-binoculars"},
    {id:"datepicker",text:"日期选择器", "icon":"comp_color fa fa-binoculars"},
    {id:"upload",text:"图片上传", "icon":"comp_color fa fa-binoculars"},
   /**{id:"rate",text:"评分", "icon":"comp_color fa fa-binoculars"},  */ 
    /**{id:"colorpicker",text:"颜色选择器", "icon":"comp_color fa fa-binoculars"}, */
    {id:"text",text:"文本", "icon":"comp_color fa fa-binoculars"},
    /**{id:"table",text:"表格", "icon":"comp_color fa fa-binoculars"},**/
]

export const writeCompType = (comp)=>{
    let ret = "";
    if(!comp){
        return ret;
    }
    $(compTypes).each((a, b)=>{
        if(b.id == comp.type){
            ret = b.text;
            return false;
        }
    });
    return ret;
}

/**
 * 根据 now, now + 1 等字符串提取日期
 */
export const parserDefDate = (input, type) => {
    var reg = /\s*now\s*([+|-])*\s*([0-9]*)/g;
    var r = reg.exec(input);
    if(r){
        if(r[1] && r[2]){
            //时间运算
            var dt = new Date();
            var v = Number(r[2]);
            if(r[1] === '-'){
                v = -v;
            }
            if("yyyy" === type){
                dt.setFullYear(dt.getFullYear() + v);
            }else if("yyyy-MM" === type || "yyyyMM" === type){
                dt.setMonth(dt.getMonth() + v);
            }else if("yyyy-MM-dd" === type || "yyyy-MM-dd") {
                dt.setDate(dt.getDate() + v);
            }else{
                dt.setSeconds(dt.getSeconds() + v);
            }
            return formatDate(dt, type);
        }else{
            //当前时间
            return formatDate(new Date(), type);
        }
    }else{
        return input;
    }
}