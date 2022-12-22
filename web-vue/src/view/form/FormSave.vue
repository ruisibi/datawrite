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
		  <el-form :model="form" :rules="rule" size="mini" ref="form" label-position="left">
		
			<el-form-item label="表单名称" label-width="110px" prop="tableNote">
				<el-input v-model="form.tableNote" ></el-input>
			</el-form-item>
			<el-form-item label="映射表名" label-width="110px" prop="tableName">
				<el-input v-model="form.tableName" placeholder="不填系统自动生成"></el-input>
			</el-form-item>
			<el-form-item label="说明" label-width="110px" prop="tableDesc">
				<el-input v-model="form.tableDesc" ></el-input>
			</el-form-item>
			<el-form-item label="所属分类" label-width="110px" prop="cataId">
				<el-select v-model="form.cataId" placeholder="请选择" style="width:100%;">
					<el-option
					v-for="item in opts.types"
					:key="item.id"
					:label="item.name"
					:value="item.id">
					</el-option>
				</el-select>
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
	import {ajax, newGuid} from '@/common/biConfig'
	import $ from 'jquery'

	export default {
	    data(){
			return {
				show:false,
				title:"",
				isupdate:false,
				form: {
					tableName: null,
					tableNote: null,
					tableDesc: null,
					cataId: null,
					id:null,
				},
				pageInfo:null,
				rule:{
					tableNote:[	{ required: true, message: '必填', trigger: 'blur' }],
					cataId: [{ required: true, message: '必填', trigger: 'blur' }]
				},
				opts:{
					types:[],
				}
			}
		},
		mounted(){
			
		},
		computed: {
			...mapState(["isMini"])
		},
		methods: {
			showDailog(pageInfo){
				if(!pageInfo.comps){
					this.$notify.error("表单为空！");
					return;
				}
				this.form.tableNote = null;
				this.form.tableName = null;
				this.form.tableDesc = null;
				this.form.cataId = null;
				this.form.id = null;
				this.show = true;
				this.isupdate = false;
				this.title = "表单保存";
				this.pageInfo = pageInfo;
				this.loadTypes();
			},
			modify(pageInfo){
				if(!pageInfo.id){
					this.$notify.error("先保存表单.");
					return;
				}
				this.form.id = pageInfo.id;
				this.show = true;
				this.isupdate = true;
				this.title = "表单设置";
				this.pageInfo = pageInfo;
				this.loadTypes();
				this.getInfo();
			},
			save(){
				this.$refs['form'].validate((valid) => {
					if (valid) {
						if(this.isupdate){
							ajax({
								url:"form/update.action",
								type:"POST",
								data: this.form,
								success:(resp)=>{
									this.show = false;
									this.$notify.success("设置成功!");
								}
							}, this);
						}else{
							let d = JSON.parse(JSON.stringify(this.form));
							d.tableCfg = JSON.stringify(this.pageInfo);
							ajax({
								url:"form/save.action",
								type:"POST",
								data: d,
								success:(resp)=>{
									this.show = false;
									this.pageInfo.id = resp.rows;
									this.$notify.success("保存成功!");
								}
							}, this);
						}
					}
				})
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