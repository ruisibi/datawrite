<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div class="wrapper-content">
	  <div class="row">
		<div class="col-sm-3">
			<div class="ibox">
				<div class="ibox-title" style="height:30px;">
					表单分类
				</div>
				<div class="ibox-content" style="padding:0">
					<div class="text-warning" style="margin:5px;">通过右键菜单编辑分类</div>
					<div id="formTypes"></div>
				</div>
			</div>
		</div>
      	<div class="col-sm-9">
		  <div class="ibox">
			  <div class="ibox-content" style="border-top:0;">
				   <div class="mail-box-header">
					<h2>表单列表</h2>
					</div>
					<div class="btn-group optbtncls" role="group">
						<button type="button" class="btn btn-outline btn-default" title="新增" @click="addForm(false)">
							<i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
						</button>
						<button type="button" class="btn btn-outline btn-default" title="修改" @click="addForm(true)">
							<i class="glyphicon glyphicon-edit" aria-hidden="true"></i>
						</button>
						<button type="button" class="btn btn-outline btn-default" title="删除" @click="delForm()">
							<i class="glyphicon glyphicon-trash" aria-hidden="true"></i>
						</button>
					</div>
					<el-table :data="tableData" @row-click="selectme" border style="width: 100%" header-row-class-name="tableHeadbg">
						<el-table-column label="" width="45">
							<template slot-scope="scope">
								<el-radio v-model="checked" name="myselect" :label="scope.row.id">&nbsp;</el-radio>
							</template>
						</el-table-column>
						<el-table-column align="center" prop="tableNote" label="表单名称"></el-table-column>
						<el-table-column align="center" prop="tableName" label="对应表"></el-table-column>
						<el-table-column align="center" prop="createDate" label="创建时间"></el-table-column>
						<el-table-column align="center" prop="updateDate" label="更新时间"></el-table-column>

						<el-table-column align="center" prop="buildDate" label="最后构建时间"></el-table-column>
						<el-table-column align="center" prop="id" label="操作" width="80">
							<template slot-scope="scope">
									<a class="btn btn-primary btn-xs" @click.stop="createTable(scope.row.id)"> 构建表 </a>
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
						layout="total, sizes, prev, pager, next, jumper"
						:total="total">
					</el-pagination>
			  </div>

		  </div>
      	</div>
	  </div>
	  <formType ref="formTypeForm"></formType>
  	</div>
</template>

<script>
	import {baseUrl, ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import formType from './FormType'

	export default {
		components: {
			 formType
		},
	    data(){
			return {
				tableData:[],
				checked:null,
				total:0,
				page:1,
				rows:10,
				cataId:null,
			}
		},
		mounted(){
			this.loadDatas();
			this.initTypeTree();
		},
		computed: {
		},
		methods: {
			loadDatas(){
				let ts = this;
				ajax({
					type:"POST",
					data:{page:ts.page, rows:ts.rows, cataId: this.cataId},
					url:"form/list.action",
					success:function(resp){
						ts.tableData = resp.rows;
						ts.total = resp.total;
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
			addForm(isupdate){
				if(isupdate == true){
					if(this.checked == null){
						this.$notify.error("未勾选数据！");
						return;
					}
					this.$router.push({path:"/form/Design", query:{id:this.checked}});
				}else{
					this.$router.push("/form/Design");
				}
			},
			delForm(){
				let ts = this;
				if(!ts.checked){
					ts.$notify.error('未勾选数据!');
						return false;
					}
					if(confirm('是否确认？')){
						ajax({
							url:"form/delete.action",
							type:"GET",
							data:{id: ts.checked},
							success:()=>{
								ts.checked = null;
								ts.loadDatas();
							}
					}, this);
				}
			},
			addType(node, isupdate){
				this.$refs['formTypeForm'].showDailog(node, isupdate);
			},
			delType(node){
				if(confirm("是否确认？")){
					ajax({
						url:"form/deleteType.action",
						data:{id:node.id},
						type:"GET",
						success: ()=>{
							this.initTypeTree();
						}
					}, this);
				}
			},
			initTypeTree(){
				let ts = this;
				let func = (dt)=>{
					let ref = $("#formTypes").jstree(true);
					if(ref){
						ref.destroy();
					}
					$('#formTypes').jstree({
						core: {
							data:[
								{id:"0",text:"表单分类", "icon":"glyphicon glyphicon-globe", state:{opened:true}, "children":dt},
							],
							check_callback: false
						},
						contextmenu: {
							items: {
							add: {
								label: "新增",
								icon: "glyphicon glyphicon-plus",
								action: function (data) {
									const inst = $.jstree.reference(data.reference),
									node = inst.get_node(data.reference);
									ts.addType(node, false);
								},
							},
							modify: {
								label: "修改",
								icon: "glyphicon glyphicon-edit",
								action: function (data) {
									const inst = $.jstree.reference(data.reference),
									node = inst.get_node(data.reference);
									ts.addType(node, true);
								},
								_disabled: function (data) {
									const inst = $.jstree.reference(data.reference),
									node = inst.get_node(data.reference);
									if (node.id == "0") {
										return true;
									} else {
										return false;
									}
								},
							},
							remove: {
								label: "删除",
								icon: "glyphicon glyphicon-trash",
								_disabled: function (data) {
									const inst = $.jstree.reference(data.reference),
									node = inst.get_node(data.reference);
									if (node.id == "0") {
										return true;
									} else {
										return false;
									}
								},
								action: function (data) {
									const inst = $.jstree.reference(data.reference),
									node = inst.get_node(data.reference);
									ts.delType(node);
								},
							},
							},
						},
						"plugins": ["wholerow", "contextmenu"]
					}).on("changed.jstree", function(e){
						var ref = $("#formTypes").jstree(true);
						var node = ref.get_selected(true);
						ts.cataId = node[0].id == 0 ? null : node[0].id;
						ts.loadDatas();
					});
				}
				ajax({
					url:"form/typeTree.action",
					data:{},
					type:"GET",
					success:(resp)=>{
						func(resp.rows);
					}
				}, this);
			},
			createTable(id){
				if(confirm("是否确认？\n重建后表数据会丢失。")){
					ajax({
						url:"form/crtTable.action",
						type:"GET",
						data:{id:id},
						success:(resp)=>{
							this.$notify.success("表构建成功。");
              this.loadDatas();
						}
					}, this);
				}
			}
		},
		watch: {
		},
		beforeRouteEnter: function(to, from, next) {
			next((vm)=>vm.loadDatas());
		},
	}
</script>
