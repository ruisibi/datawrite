<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%; background-color:white; ">
				<div style="background-color:#034d8f;color: white">
					<div>
						<!--折叠按钮-->
						<button type="button" @click="loginout()" class="navbar-toggle" style="padding:6px 10px;border-color: #ddd; display:block;">
							<span class="glyphicon-user-large glyphicon glyphicon-user"></span>
						</button>
						<img src="../../assets/image/log3.png">
					</div>
				</div>

		<div class="tabs-info">
      <form class="form-horizontal" novalidate="novalidate">
        <div align="right">
          <el-select v-model="typeId" clearable size="mini" placeholder="选择分类" @change="loadDatawriteTables()">
            <el-option v-for="(item) in opts.types"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="row">
          <template v-for="item in tableData">
            <div :key="item.id" class="row-file">
              <a href="javascript:;" @click="writeData(item.id)">
                <div class="row-image">
                  <div align="center" style="font-size:60px; color:#ccc;">
                    <i class='fa fa-file-text-o'></i>
                  </div>
                </div>
                <div class="file-name">
                  {{item.tableNote}}({{item.tableName}})
                </div>
              </a>
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
        layout="total, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  	</div>
</template>

<script>
	import {ajax} from '@/common/biConfig'
	import $ from 'jquery'

	export default {
	    data(){
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
		mounted(){
			this.loadDatawriteTables();
      this.loadTypes();
		},
		computed: {
		},
		methods: {
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
			loginout(){
				 ajax({
					url:"frame/Logout2.action",
					data:{},
					success:()=>{
						this.$notify.success({
							title: '退出成功',
							offset: 50
						});
						this.$router.push('/');
					}
				});
			},
			loadDatawriteTables:function(){
				let ts = this;
				ajax({
					type:"GET",
					data:{page:this.page, rows:this.rows,  typeId: this.typeId},
					url:"write/list.action",
					success:function(resp){
						ts.tableData = resp.rows;
						ts.total=resp.total;
					}
				}, ts);
			},
      handleSizeChange(v){
        this.rows = v;
        this.loadDatawriteTables();
      },
      handleCurrentChange(v){
        this.page = v;
        this.loadDatawriteTables();
      },
      writeData(id){
        this.$router.push({path:"/mobile/DataList", query:{tableId:id}});
      },
		},
		watch: {
		},
	}
</script>

<style lang="less" scoped>
.row-file {
  width: 46%;
  display: inline-block;
  border: 1px solid #e7eaec;
  background-color: #ffffff;
  margin: 5px 0px 0px 10px;
}
.file-name {
  text-align: center;
  padding: 5px;
  background-color: #f8f8f8;
  border-top: 1px solid #e7eaec;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.tabs-info {
	height: calc(100% - 100px);
	overflow: auto;
}
</style>
