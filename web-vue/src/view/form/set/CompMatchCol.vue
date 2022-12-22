<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
    <el-dialog :visible.sync="show" :close-on-click-modal="false" custom-class="nopadding">
      <template slot="title">
            <span class="el-dialog__title">{{title}}</span>
            <button type="button" class="el-dialog__headerbtn minibtn" @click="$store.commit('setMini')" style="right:40px; font-size:14px; color:#909399;">
            <i class="fa" :class="isMini?'fa-expand':'fa-compress'"></i>
            </button>
          </template>
      <el-form :model="form" ref="myform" label-width="120px" :rules="rule" size="mini">
          <el-form-item label="映射字段名：" prop="valuestype">
           <el-input v-model="form.col" size="mini" placeholder="如果不设置系统自动生成"/>
          </el-form-item>
          <el-form-item label="字段长度：" prop="length">
           <el-input-number v-model="form.length" :min="1" size="mini" :precision="0" placeholder="如果不设置系统自动生成"/>
          </el-form-item>
          <el-form-item label="小数位：" prop="scale" v-if="comp.type==='inputNumber'">
           <el-input-number v-model="form.scale" :min="1" :max="4" :precision="0" size="mini" placeholder="如果不设置系统自动生成"/>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save()">确 定</el-button>
        <el-button @click="show = false">取 消</el-button>
      </div>
    </el-dialog>
</template>

<script>
import { mapState } from "vuex";
import {baseUrl, newGuid} from '@/common/biConfig'
import $ from 'jquery'
import * as utils from './../Utils'

export default {
  components:{

  },
  props:{
      
  },
  data(){
    return {
        show:false,
        comp:{},
        title:"",
        form:{
          col: null,
          length: null,
          scale: null,
        },
        rule:{
          
        },
        opt:{


        }
    }
  },
  mounted(){
    
  },
  computed: {
     ...mapState(["isMini"])
  },
  methods: {
     open(comp){
       this.show = true;
       this.comp = comp;
       this.form.col = comp.matchCol;
       this.form.length = comp.length;
       this.form.scale = comp.scale;
       this.title = utils.writeCompType(comp) + " - 组件映射字段设置";
     },
     save(){
        this.comp.matchCol = this.form.col;
        this.comp.length = this.form.length;
        this.show = false;
        this.comp.scale = this.form.scale;
        this.$parent.setUpdate();
     },
  }
}
</script>
