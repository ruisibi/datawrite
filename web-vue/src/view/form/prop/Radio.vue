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
          <el-form-item label="强制换行：" label-width="210px">
             <el-switch v-model="prop.focesBr" @change="changevalue('focesBr')"></el-switch>
          </el-form-item>
          <el-form-item label="显示方式：" label-width="110px">
             <el-select v-model="prop.showType" @change="changevalue('showType')" placeholder="请选择">
                <el-option
                  v-for="item in opts.types"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </el-form-item>
       </el-collapse-item>
    </el-collapse>
  </el-form>
</div>
</template>

<script>
import {baseUrl, ajax} from '@/common/biConfig'
import $ from 'jquery'
import { writeCompType } from './../Utils'
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
      prop:{
        focesBr: false,
        showType: null,
      },
      opts:{
       types:[
         {value:"def", label:"默认样式"},{value:"btn", label:"按钮样式"}, {value:"border", label:"带边框"},  
        ],
      },
      comp: null,
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
    },

    changevalue(prop){
      let c = this.comp;
      let v = this.prop[prop];
     
      if(!this.comp.properties){
        this.comp.properties = {};
      }
      this.comp.properties[prop] = v;
      
      this.setUpdate();
    },
    setUpdate(){
      this.$parent.setUpdate();
    },
  },
  watch: {
    
  }
}
</script>
