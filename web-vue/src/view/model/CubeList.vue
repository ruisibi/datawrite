<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  <div class="wrapper-content">
    <div class="ibox" id="mainDiv">
      <div class="ibox-title">数据模型</div>
      <div class="ibox-content">

        <div class="btn-group optbtncls" role="group">
						<button type="button" class="btn btn-outline btn-default" title="新增" @click="addModel(false)">
							<i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
						</button>
						<button type="button" class="btn btn-outline btn-default" title="修改" @click="addModel(true)">
							<i class="glyphicon glyphicon-edit" aria-hidden="true"></i>
						</button>
						<button type="button" class="btn btn-outline btn-default" title="删除" @click="delModel()">
							<i class="glyphicon glyphicon-trash" aria-hidden="true"></i>
						</button>
					</div>
          <el-table :data="tableData" @row-click="selectme" border style="width: 100%" header-row-class-name="tableHeadbg">
						<el-table-column label="" width="45">
							<template slot-scope="scope">
								<el-radio v-model="checked" name="myselect" :label="scope.row.id">&nbsp;</el-radio>
							</template>
						</el-table-column>
						<el-table-column align="center" prop="id" label="标识"></el-table-column>
						<el-table-column align="center" prop="cubeName" label="名称"></el-table-column>
            <el-table-column align="center" prop="tableName" label="对应表"></el-table-column>
            <el-table-column align="center" prop="createDate" label="创建时间"></el-table-column>
					</el-table>
          <el-pagination
              background
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :page-sizes="[10, 20]"
              :current-page="page"
              :page-size="rows"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
      </div>
    </div>
    <operationDailog mainDiv="mainDiv" :title="cubeOperTitle" ref="cubeOper" :callback="saveCube">
      <cubeAdd ref="cubeForm"></cubeAdd>
    </operationDailog>
  </div>
</template>

<script>
import { baseUrl, ajax } from "@/common/biConfig";
import operationDailog from "@/components/OperationDailog";
import cubeAdd from './CubeAdd'
import $ from "jquery";

export default {
  data() {
    return {
      tableData:[],
      checked:null,
      total:0,
      page:1,
      rows:10,
      cubeOperTitle: "",
      isupdate: false,
    }
  },
  components: {
	  operationDailog, cubeAdd
	},
  mounted() {
    this.loadDatas();
  },
  computed: {},
  methods: {
	   loadDatas:function(){
			let ts = this;
			ajax({
				type:"post",
				data:{page:this.page, rows:this.rows},
				url:"model/pageCube.action", 
				success:function(resp){
          ts.tableData = resp.rows;
          ts.total=resp.total;
				}
			}, ts);
    },
    selectme:function(a, b){
				this.checked = a.id;
			},
    handleSizeChange(v){
			this.rows = v;
			this.loadDatas();
		},
		handleCurrentChange(v){
			this.page = v;
			this.loadDatas();
    },
    addModel(isupdate){
      if(isupdate){
        this.cubeOperTitle = "编辑立方体";
      }else{
        this.cubeOperTitle = "创建立方体";
      }
      this.isupdate = isupdate;
      if(isupdate == true){
        if(!this.checked){
          this.$notify.error("未勾选数据");
        return;
      }
      }
      let oper =  this.$refs['cubeOper'];
      oper.showDailog();
      this.$refs["cubeForm"].addCube(isupdate, this.checked);
    },
    delModel(){
      if(!this.checked){
        this.$notify.error("未勾选数据");
        return;
      }
      if(confirm("是否确认？")){
        ajax({
          url:"model/delCube.action",
          data:{cubeId: this.checked},
          type:"GET",
          success:(resp)=>{
            this.loadDatas();
          }
        });
      }
    },
    saveCube(){
      return this.$refs['cubeForm'].saveCube(this.isupdate);
    }
  },
  watch: {},
  beforeRouteLeave(to, from, next) {
    this.$destroy();
    next();
  }
};
</script>
<style lang="less" scoped>
.file {
    border: 1px solid #e7eaec;
    padding: 0;
    background-color: #ffffff;
    position: relative;
    margin-bottom: 20px;
    margin-right: 20px;
}

.file-manager .hr-line-dashed {
    margin: 15px 0;
}

.file .icon,
.file .image {
    height: 100px;
    overflow: hidden;
}

.file .icon {
    padding: 15px 10px;
    text-align: center;
}

.file-control {
    color: inherit;
    font-size: 11px;
    margin-right: 10px;
}

.file-control.active {
    text-decoration: underline;
}

.file .icon i {
    font-size: 70px;
    color: #23b7e5;
}

.file .file-name {
    padding: 10px;
    background-color: #f8f8f8;
	border-top: 1px solid #e7eaec;
	overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    text-align: center;
}

.file-name small {
    color: #676a6c;
}
</style>