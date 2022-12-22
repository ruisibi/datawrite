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
		  <el-form :model="formType" :rules="rule" size="mini" ref="form" label-position="left">
		
			<el-form-item label="名称" label-width="110px" prop="name">
				<el-input v-model="formType.name" ></el-input>
			</el-form-item>
			<el-form-item label="说明" label-width="110px" prop="note">
				<el-input v-model="formType.note" ></el-input>
			</el-form-item>
			<el-form-item label="排序" label-width="110px" prop="ord">
				<el-input-number v-model="formType.ord" :precision="0"></el-input-number>
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
				formType: {
					name: null,
					note: null,
					ord: null,
					id: null,
				},
				rule:{
					name:[	{ required: true, message: '必填', trigger: 'blur' }],
					ord: [{ required: true, message: '必填', trigger: 'blur' }]
				}
			}
		},
		mounted(){
			
		},
		computed: {
			...mapState(["isMini"])
		},
		methods: {
			showDailog(node, isupdate){
				if(isupdate == true){
					this.title = "编辑分类";
				}else{
					this.title = "创建分类";
					this.formType.id = null;	
				}
				this.show = true;
				this.$nextTick(()=>{
					this.$refs['form'].resetFields();
					if(isupdate == true){
						ajax({
							url:"form/getType.action",
							type:"get",
							data: {id: node.id},
							success:(resp)=>{
								let d = resp.rows;
								let f = this.formType;
								f.id = d.id;
								f.name = d.name;
								f.note = d.note;
								f.ord = d.ord;
							}
						});
					}
				});
			},
			save(){
				this.$refs['form'].validate((valid) => {
					if (valid) {
						ajax({
							url:this.formType.id?"form/updateType.action":"form/addType.action",
							type:"POST",
							data: this.formType,
							success:()=>{
								this.show = false;
								this.$parent.initTypeTree();
							}
						}, this);
					}
				})
			}
		},
		watch: {
		}
	}
</script>