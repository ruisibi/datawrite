<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  <div class="wrapper-content">
    <div class="ibox">
      <div class="ibox-title">数据填报</div>
      <div class="ibox-content">
          <form class="form-horizontal" novalidate="novalidate">
            <div align="right">
              <el-select v-model="typeId" clearable size="mini" placeholder="选择分类" @change="loadDatas()">
								<el-option v-for="(item) in opts.types"
									:key="item.id"
									:label="item.name"
									:value="item.id">
								</el-option>
							</el-select>
            </div>
			  <div class="row">
				  <template v-for="item in tableData">
				  <div :key="item.tableId" class="col-sm-2">
						<div class="file" style="margin-right:0px;">
							<a href="javascript:;" @click="writeData(item.id)">
								<div class="image">
									<div align="center" style="font-size:60px; color:#ccc;">
									<i class='fa fa-file-text-o'></i>
									</div>
								</div>
								<div class="file-name">
											{{item.tableNote}}
								</div>
							</a>

						</div>
					</div>
				  </template>
			  </div>
          </form>
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
  </div>
</template>

<script>
import { baseUrl, ajax } from "@/common/biConfig";
import $ from "jquery";

export default {
  data() {
    return {
	  tableData:[],
	  total:0,
      page:1,
      rows:10,
      typeId:null,
      opts:{
        types:[]
      }
    }
  },
  components: {
	  
	},
  mounted() {
    this.loadDatas();
    this.loadTypes();
  },
  computed: {},
  methods: {
	   loadDatas:function(){
			let ts = this;
			ajax({
				type:"GET",
				data:{page:this.page, rows:this.rows, typeId: this.typeId},
				url:"write/list.action", 
				success:function(resp){
          ts.tableData = resp.rows;
          ts.total=resp.total;
				}
			}, ts);
    },	
    loadTypes(){
      ajax({
        url:"form/typeTree.action",
        type:"GET",
        data:{},
        success:(r)=>{
          this.opts.types = r.rows;
        }
      }, this);
    },
		writeData(tableId){
			this.$router.push({path:"/write/DataList", query:{tableId:tableId}});
    },
    handleSizeChange(v){
			this.rows = v;
			this.loadDatas();
		},
		handleCurrentChange(v){
			this.page = v;
			this.loadDatas();
		},
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