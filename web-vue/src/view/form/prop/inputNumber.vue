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
          <el-form-item label="最小值：" label-width="120px">
            <el-input-number v-model="prop.min" size="mini" @change="changevalue('min')"></el-input-number>
          </el-form-item>
          <el-form-item label="最大值：" label-width="120px">
            <el-input-number v-model="prop.max" size="mini" @change="changevalue('max')"></el-input-number>
          </el-form-item>
          <el-form-item label="小数位：" label-width="120px">
            <el-input-number v-model="prop.precision" :min="0" size="mini" :precision="0" :max="4" @change="changevalue('precision')"></el-input-number>
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
        min:null,
        max:null,
        precision: 0
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
