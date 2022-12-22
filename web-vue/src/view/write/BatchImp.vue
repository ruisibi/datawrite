<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<!-- 批量导入 -->
<template>
  	<el-dialog title="批量导入数据" :visible.sync="show" width="30%" :close-on-click-modal="false" custom-class="nopadding">
		   <template slot="title">
			<span class="el-dialog__title">批量导入数据</span>
			<button type="button" class="el-dialog__headerbtn minibtn" @click="$store.commit('setMini')" style="right:40px; font-size:14px; color:#909399;">
			<i class="fa" :class="isMini?'fa-expand':'fa-compress'"></i>
			</button>
			</template>
		  <el-upload
                  class="upload-demo"
                  :action="getUrl()"
                  :data="dt"
                  :multiple="false"
                  :auto-upload="true"
                  :limit="1"
                  :before-upload="beforeUpload"
                  :on-success="successUpload"
				  :on-error="errorUpload"
                  :file-list="fileList">
                    <el-button type="primary">点击上传文件</el-button>
                  <div slot="tip" class="el-upload__tip text-warning">请上传刚才下载的模版文件。</div>
                </el-upload>
		<div slot="footer" class="dialog-footer">
			<el-button @click="show = false">关 闭</el-button>
		</div>
  </el-dialog>
</template>

<script>
	import { mapState } from "vuex";

	import {ajax, baseUrl} from '@/common/biConfig'
	import $ from 'jquery'
	import { Message } from 'element-ui'

	export default {
	    data(){
			return {
				show :false,
				fileList: [],
				dt:{
					tableId: null
				},
			}
		},
		mounted(){
		},
		computed: {
			...mapState(["isMini"])
		},
		methods: {	
			showDailog(tableId){
				console.log(tableId);
				this.dt.tableId = tableId;
				this.show = true;
				this.fileList = [];
			},
			beforeUpload(file){
				 var name = file.name;
				name = name.substring(name.lastIndexOf('.') + 1, name.length);
				if(name.toLowerCase() !== 'xlsx'){
					this.$message.error("只支持xlsx文件");
					return false;
				}
			},
			successUpload(response, file, fileList){
				this.$notify.success("成功导入"+response.rows+"条数据。");
				this.show = false;
				this.$parent.loadDatas();
			},
			errorUpload(err, file, fileList){
				const h = this.$createElement;
				Message.error({message:h('div',[h('h5','系统错误'), h('div', err)]), type:"error",showClose: true});
			},
			getUrl(){
				return baseUrl + "write/ImportUpload.action";
			}
		},
		watch: {
		}
	}
</script>

<style lang="less" scoped>

</style>
