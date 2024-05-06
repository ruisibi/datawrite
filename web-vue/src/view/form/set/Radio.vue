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
          <el-form-item label="值类型：" prop="valuestype">
            <el-radio v-model="form.valuestype" label="static" border>固定值</el-radio>
            <el-radio v-model="form.valuestype" label="dynamic" border>动态值</el-radio>
          </el-form-item>
          <div style="padding:0px 10px 10px 0px;" v-if="form.valuestype === 'static'">
						<el-row>
							<el-col :span="22">
						<el-table :data="form.options" border style="width: 100%" header-row-class-name="tableHeadbg">
              <el-table-column align="center" label="序号">
								<template slot-scope="scope">
                 {{scope.$index + 1}}
                </template>
							</el-table-column>
							<el-table-column align="center" label="值">
								<template slot-scope="scope">
                  <el-input v-model="scope.row.name" size="mini"></el-input>
                </template>
							</el-table-column>
							<el-table-column align="center" label="操作">
								<template slot-scope="scope">
									<a class="btn btn-default btn-outline btn-xs" @click="deleteOptionsVal(scope.$index)"><i class="glyphicon glyphicon-trash"></i></a>
								</template>
							</el-table-column>
						</el-table>
							</el-col>
							<el-col :span="2" align="right">
									<a class="btn btn-default btn-outline btn-xs" @click="addOptionsVal()"><i class="glyphicon glyphicon-plus"></i></a>
							</el-col>
						</el-row>
					</div>
          <template v-if="form.valuestype === 'dynamic'">
						<el-form-item label="映射表：" label-width="120px" prop="matchTable">
							<el-select style="width:100%" v-model="form.matchTable"	@change="chgTableCols">
								<el-option
									v-for="item in opt.tables"
									:key="item.tableName"
									:label="item.tableName"
									:value="item.tableName"
								>
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="表ID字段：" label-width="120px" prop="matchCol">
							<el-select style="width:100%"
								v-model="form.matchCol"
								>
								<el-option
									v-for="item in opt.tableCols"
									:key="item.id"
									:label="item.name"
									:value="item.id"
								>
							</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="过滤条件：" label-width="120px" prop="condition">
							<el-input v-model="form.condition"></el-input>
						</el-form-item>
					</template>

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
        comp:null,
        title:"",
        form:{
          valuestype: null,
          options:[], //静态值值列表, 字符串数组
          matchTable: null,
          matchCol: null,
          condition:null,
        },
        rule:{
          valuestype:[{ required: true, message: '必填', trigger: 'blur' }],
          matchTable:[{ required: true, message: '必填', trigger: 'blur' }],
          matchCol:[{ required: true, message: '必填', trigger: 'blur' }]
        },
        opt:{
          tables:[],
          tableCols:[],

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
       this.form.options = JSON.parse(JSON.stringify(comp.options || []));
       this.comp = comp;
       this.form.valuestype = comp.valuestype;
       this.form.matchTable = comp.matchTable;
       this.form.matchCol = comp.matchCol;
       this.form.condition = comp.condition;
       this.title = utils.writeCompType(comp) + " - 数据设置";
     },
     save(){
       this.$refs['myform'].validate((valid) => {
         if(valid){
           if(this.form.valuestype =='static'){
             if(this.form.options.length == 0){
               this.$notify.error("未定义值！");
               return false;
             }
             let iserr = false;
             $(this.form.options).each((a, b)=>{
               if(b.name === ''){
                 this.$notify.error("第"+(a+1)+"行值为空！");
                 iserr = true;
                 return false;
               }
             });
             if(iserr == true){
               return;
             }
             this.comp.options = this.form.options;
             this.comp.valuestype = this.form.valuestype;
             this.show = false;
           }else{
             this.comp.valuestype = this.form.valuestype;
             this.comp.matchTable = this.form.matchTable;
             this.comp.matchCol = this.form.matchCol;
             this.comp.condition = this.form.condition;
             delete this.comp.options;
             this.show = false;
           }
         }
       });
     },
     chgTableCols(){

     },
     addOptionsVal(){
       this.form.options.push({id:newGuid(), name: ""});
     },
     deleteOptionsVal(idx){
       this.form.options.splice(idx, 1);
     }
  }
}
</script>
