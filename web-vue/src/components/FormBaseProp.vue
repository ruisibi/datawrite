<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
<div>
  <el-form :model="prop" ref="propForm" label-position="left" size="mini">

          <el-form-item label="组件名称：" label-width="100px">
            <el-input v-model="prop.name" @change="changevalue('name')"></el-input>
          </el-form-item>
          <el-form-item label="组件类型：" label-width="100px">
            <div align="right">
              {{ findName() }}
            </div>
          </el-form-item>
          <el-form-item label="映射字段：" label-width="100px">
            <div align="right">
              {{ comp && comp.matchCol ? comp.matchCol :"未设置" }}
            </div>
          </el-form-item>
          <el-form-item label="文本位置：" label-width="145px" v-if="comp && comp.type != 'text'">
             <el-radio-group v-model="prop.align" @change="changevalue('align')">
                <el-radio-button label="left"></el-radio-button>
                <el-radio-button label="right"></el-radio-button>
             </el-radio-group>
          </el-form-item>
          <el-form-item label="文本宽度：" label-width="120px" v-if="comp && comp.type != 'text'">
            <el-input-number v-model="prop.labelWidth" :min="50" :max="500" @change="changevalue('labelWidth')"></el-input-number>
          </el-form-item>
          <el-form-item label="默认值：" label-width="100px" v-if="showDefval()">
            <el-input v-model="prop.defval" @change="changevalue('defval')"></el-input>
          </el-form-item>
          <el-form-item label="是否必填项：" label-width="210px" v-if="showRequired()">
            <el-switch v-model="prop.required" @change="changevalue('required')"></el-switch>
          </el-form-item>
          <el-form-item label="设为查询条件：" label-width="210px" v-if="showSearch()">
            <el-switch v-model="prop.searchCol" @change="changevalue('searchCol')"></el-switch>
          </el-form-item>

  </el-form>
</div>
</template>

<script>
import {baseUrl, ajax} from '@/common/biConfig'
import $ from 'jquery'
import { writeCompType } from '@/view/form/Utils'

export default {
  components:{

  },
  props:{

  },
  data(){
    return {
      comp: null,
      prop:{
        name:null,
        defval: null,
        required: false,
        align: null,
        labelWidth: 140,
        searchCol:false,
      },
      opts:{

      }
    }
  },
  mounted(){

  },
  computed: {

  },
  methods: {
    findName(){
       let ret = writeCompType(this.comp);
       return ret;
    },
    init(comp){
      this.prop.name = comp.name;
      this.comp = comp;
      this.prop.defval = comp.defval;
      this.prop.required = comp.required;
      this.prop.align = comp.align || "left";
      this.prop.labelWidth = comp.labelWidth || 140;
      this.prop.searchCol = comp.searchCol;
    },

    changevalue(prop){
      let c = this.comp;
      let v = this.prop[prop];
      if(prop == 'name'){
        c.name = v;
        this.render();
      }else if(prop === 'defval'){
        c.defval = v;
        this.refreshDefVal();
      }else if(prop == 'required'){
        c.required = v;
        this.render();
      }else if(prop == 'align'){
        c.align = v;
        this.render();
      }else if(prop == 'labelWidth'){
         c.labelWidth = v;
        this.render();
      }else if(prop =='searchCol'){
        c.searchCol = v;
      }
      this.setUpdate();
    },
    setUpdate(){
      this.$parent.$parent.$parent.$parent.setUpdate();
    },
    //刷新组件的默认值显示
    refreshDefVal(){
      let o = this.$parent.$parent.$parent.$parent.$parent.$parent;
      let target = o.$refs['centerForm'].$refs['c_' + this.comp.id][0];
      if(target.$refs['v_' + this.comp.id]){  //可能下级还有
        target.$refs['v_' + this.comp.id].initDefVal();
      }else{
        target.initDefVal();
      }
    },
    render(){
      let o = this.$parent.$parent.$parent.$parent.$parent.$parent;
      let target = o.$refs['centerForm'].$refs['c_' + this.comp.id][0];
      target.$forceUpdate();
    },
    showDefval(){
      let comp = this.comp;
      if(comp == null){
        return false;
      }
      if(comp.type === 'upload' || comp.type === 'text'){
        return false;
      }
      return true;
    },
    showRequired(){
      let comp = this.comp;
      if(comp == null){
        return false;
      }
      if(comp.type === 'text'){
        return false;
      }
      return true;
    },
    showSearch(){
      let comp = this.comp;
      if(comp == null){
        return false;
      }
      if(comp.type === 'upload' || comp.type === 'text' || comp.type == 'switch' || comp.type == 'checkbox'){
        return false;
      }
      return true;
    }
  },
  watch: {

  }
}
</script>
