<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
<div>
  <el-form :model="form" ref="propForm" label-position="left" size="mini">
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="基本信息" name="1" >
        <el-form-item label="表单名称：" label-width="110px" prop="tableNote">
          <el-input v-model="form.tableNote" ></el-input>
        </el-form-item>
        <el-form-item label="映射表名：" label-width="110px" prop="tableName">
          <el-input v-model="form.tableName" placeholder="不填系统自动生成"></el-input>
        </el-form-item>
        <el-form-item label="说明：" label-width="110px" prop="tableDesc">
          <el-input v-model="form.tableDesc" ></el-input>
        </el-form-item>
        <el-form-item label="所属分类：" label-width="110px" prop="cataId">
          <el-select v-model="form.cataId" placeholder="请选择" style="width:100%;">
            <el-option
            v-for="item in opts.types"
            :key="item.id"
            :label="item.name"
            :value="item.id">
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
import * as utils from './../Utils'

export default {
  components:{
    
  },
  props:{
      pageInfo: {
        type: Object,
        required: true,
        default:{}
      },
  },
  data(){
    return {
      activeName: "1",
      form:{
        tableNote:null,
        tableName:null,
        tableDesc:null,
        cataId:null,
      },
      opts:{
        types:[]
      }
    }
  },
  mounted(){
    this.getInfo();
    this.loadTypes();
  },
  computed: {

  },
  methods: {
    init(){
     
    },

    changevalue(prop){
      let c = this.comp;
      let v = this.prop[prop];
      
      this.setUpdate();
    },
    setUpdate(){
      this.$parent.setUpdate();
    },
    loadTypes(){
      ajax({
        url:"form/typeTree.action",
        data:{},
        type:"GET",
        success:(resp)=>{
          this.opts.types = resp.rows;
        }
      }, this);
    },
    getInfo(){
      ajax({
        url:"form/getForm.action",
        data:{id:this.pageInfo.id},
        type:"GET",
        success:(resp)=>{
          let r = resp.rows;
          this.form.tableNote = r.tableNote;
          this.form.tableName = r.tableName;
          this.form.tableDesc = r.tableDesc;
          this.form.cataId = r.cataId;
        }
      }, this);
    }
  },
  watch: {
    
  }
}
</script>
