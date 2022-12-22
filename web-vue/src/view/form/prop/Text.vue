<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
<div>
  <el-form :model="prop" ref="propForm" label-position="left" size="mini">
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="基本信息" name="1" >
          <formBaseProp ref="baseProp"></formBaseProp>
       </el-collapse-item>
       <el-collapse-item title="文本信息" name="2" >
       <el-form-item label="文本内容：" label-width="100px">
          <el-input v-model="prop.desc" @change="changevalue('desc')"></el-input>
        </el-form-item>
        <el-form-item label="文本颜色：" label-width="210px">
          <el-color-picker v-model="prop.color" @change="changevalue('color')"></el-color-picker>
        </el-form-item>
        <el-form-item label="字体大小：" label-width="110px">
          <el-slider v-model="prop.size" :max="46" :min="9" @change="changevalue('size')"></el-slider>
        </el-form-item>
        <el-form-item label="行高：" label-width="110px">
              <el-input-number size="small" v-model="prop.lineheight" :min="20" :precision="0" controls-position="right" @change="changevalue('lineheight')"></el-input-number>
        </el-form-item>
        <el-form-item label="位置：" label-width="110px">
          <el-select v-model="prop.align" @change="changevalue('align')" :clearable="true" placeholder="请选择">
            <el-option
              v-for="item in opts.aligns"
              :key="item.value"
              :label="item.name"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否粗体：" label-width="200px">
          <el-switch v-model="prop.bold" @change="changevalue('bold')"></el-switch>
        </el-form-item>
        <el-form-item label="是否斜体：" label-width="200px">
          <el-switch v-model="prop.italic" @change="changevalue('italic')"></el-switch>
        </el-form-item>
        <el-form-item label="是否下划线：" label-width="200px">
          <el-switch v-model="prop.underline" @change="changevalue('underline')"></el-switch>
        </el-form-item>
       </el-collapse-item>
    </el-collapse>
  </el-form>
</div>
</template>

<script>
import {baseUrl, ajax} from '@/common/biConfig'
import $ from 'jquery'
import * as utils from './../Utils'
import formBaseProp from '@/components/FormBaseProp'

export default {
  components:{
    formBaseProp
  },
  props:{
      
  },
  data(){
    return {
      activeName: "1",
      comp: null,
      prop:{
        desc:null,
        color:null,
        size:null,
        lineheight: null,
        align: null,
        bold: false,
        italic: false,
        underline: false,
      },
      opts:{
        aligns:[{value:"left", name:"居左"},{value:"center", name:"居中"},{value:"right", name:"居右"}]
      }
    }
  },
  mounted(){
    
  },
  computed: {

  },
  methods: {
    init(comp){
      this.comp = comp;
      //初始化formBaseProp
      this.$refs['baseProp'].init(comp);
       //初始化属性值
      if(this.comp.properties){
        for(let m in this.comp.properties){
          this.prop[m] = this.comp.properties[m];
        }
      }
      this.prop.desc = comp.desc;
    },

    changevalue(prop){
      let v = this.prop[prop];
       if(!this.comp.properties){
        this.comp.properties = {};
      }
      if(prop === 'desc'){
        this.comp.desc = v;
      }else{
        this.comp.properties[prop] = v;
      }
      this.setUpdate();
      this.render();
    },
    setUpdate(){
      this.$parent.setUpdate();
    },
    render(){
      let o = this.$parent.$parent.$refs['centerForm'].$refs['c_' + this.comp.id][0];
      o.$refs['v_'+this.comp.id].$forceUpdate();
    },
  },
  watch: {
    
  }
}
</script>
