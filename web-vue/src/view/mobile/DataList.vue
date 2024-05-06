<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
    <div class="ibox">
      <div class="ibox-title" style="padding:10px;height:38px;">
        {{table.tableNote}}
      <button class="btn btn-outline btn-info btn-xs" @click="goback()"><i class="fa fa-chevron-left"></i>返回</button>
      </div>
      <div class="ibox-content">
         <div style="margin-bottom:10px;" class="btn-group" role="group">
            <button type="button" class="btn btn-outline btn-default" @click="insertData()">
              <i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
            </button>
            <button type="button" class="btn btn-outline btn-default" @click="delData()">
              <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>
            </button>
         </div>
           <searchParam ref="searchParamForm" :table="table" />
          <el-table :data="tableData" border style="width: 100%" header-row-class-name="tableHeadbg" @selection-change="handleSelectionChange">>
						<el-table-column label="" width="45" fixed="left" type="selection" :selectable="canSelect">

						</el-table-column>
            <template v-for="item in table.cols? table.cols.filter(m=>m.compType!=='upload') : []">
					  	<el-table-column :key="item.id" :formatter="valfmt" align="center" :prop="item.id" :label="item.colDesc" :width="150"></el-table-column>
            </template>
            <el-table-column align="center" prop="tmp_data_id" label="操作" fixed="right" width="100" >
							<template slot-scope="scope">
                  <a class="btn btn-primary btn-xs" v-if="scope.row.tmp_audit_state != 2" @click.stop="updateData(scope.row.tmp_data_id)"> 编辑 </a>
                  <a class="btn btn-primary btn-xs" @click.stop="viewData(scope.row.tmp_data_id)"> 查看 </a>
							</template>
						</el-table-column>
					</el-table>
					<el-pagination
						background
						@size-change="handleSizeChange"
						@current-change="handleCurrentChange"
						:page-sizes="[10, 20, 50, 100]"
						:current-page="page"
						:page-size="rows"
						layout="total, prev, pager, next, jumper"
						:total="total">
					</el-pagination>
      </div>
      <dataWrite ref="dataWriteForm" />
    </div>
</template>

<script>
import { baseUrl, ajax, formatDate } from "@/common/biConfig";
import $ from "jquery";
import dataWrite from '@/view/write/DataWrite'
import searchParam from '@/view/write/SearchParam'

export default {
  data() {
    return {
      table:{},
      json:null, //填报表配置信息
      total:0,
      page:1,
      rows:10,
      tableData:[],
      selects:[],
    }
  },
  components: {
	  dataWrite,  searchParam
	},
  mounted() {
    this.getTable();
  },
  computed: {},
  methods: {
	   getTable(){
       ajax({
         url:"write/getTable.action",
         data:{tableId: this.getTableId()},
         type:"GET",
         success:(resp)=>{
           this.table = resp.rows;
           this.json = JSON.parse(this.table.tableCfg);
           this.loadDatas();
         }
       });
     },
     getTableId(){
       let tid = this.$route.query.tableId;
       return tid;
     },
     goback(){
       this.$router.back();
     },
     loadDatas(){
       let table = this.table;
       let json = {tableId:table.id, tableName: table.tableName,  page:this.page, rows:this.rows};
       json.pms = this.$refs['searchParamForm'].getParams();
       ajax({
         url:"write/listDatas.action",
         data:JSON.stringify(json),
         type:"POST",
         postJSON:true,
         success:(r)=>{
           this.tableData = r.rows;
           this.total = r.total;
         }
       }, this);
     },
    handleSizeChange(v){
			this.rows = v;
			this.loadDatas();
		},
		handleCurrentChange(v){
			this.page = v;
			this.loadDatas();
    },
    insertData(){
      this.$refs['dataWriteForm'].openDialog(this.json);
    },
    updateData(id){
      this.$refs['dataWriteForm'].modifyDialog(id, this.json);
    },
    delData(){
      if(this.selects.length == 0){
        this.$notify.error("未勾选数据.");
        return;
      }
      if(confirm("是否确认？")){
        ajax({
          url:"write/delete.action",
          data:JSON.stringify({tableId: this.json.id, ids:this.selects.map(m=>m.tmp_data_id)}),
          type:"POST",
          postJSON:true,
          success:()=>{
            this.loadDatas();
          }
        });
      }
    },
    viewData(id){
       this.$refs['dataWriteForm'].viewDialog(id, this.json);
    },
    handleSelectionChange(val){
      this.selects = val;
    },
    valfmt(row, column, cellValue, index){
      let c = column.property;
      if(c == 'tmp_audit_state'){ //系统内建审核字段
        let s = "";
        if(cellValue == 0){
          s = "未审核";
        }else if(cellValue == 2){
          s = "审核通过";
        }else if(cellValue == 3){
          s = "审核不通过";
        }
        return s;
      }
      let comp = this.json.comps[c];
      if(cellValue){
        if(comp.type=='switch'){
          return cellValue == 1 ? "是":"否";
        }else if(comp.type ==='datepicker'){
          let dt = new Date(cellValue);
          let fmt = "yyyy-MM-dd";
          if(comp.properties && comp.properties.fmt){
            fmt = comp.properties.fmt;
          }
          return formatDate(dt, fmt);
        }else{
          return cellValue;
        }
      }else{
        return "";
      }
    },
    canSelect(row, index){
      if(row.tmp_audit_state == 2){
        return false;
      }else{
        return true;
      }
    },
  },
  watch: {},
  beforeRouteLeave(to, from, next) {
    this.$destroy();
    next();
  }
};
</script>
