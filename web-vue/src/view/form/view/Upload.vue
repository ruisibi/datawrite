<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%;">
		<el-upload
			class="upload-demo"
			:action="model=='edit'?'write/FileUpload.action':'#'"
			:on-preview="handlePreview"
			:on-remove="handleRemove"
			:before-remove="beforeRemove"
			:before-upload="beforeUpload"
			multiple
			:limit="maxNum"
			:on-exceed="handleExceed"
			:auto-upload="model=='edit'?true:false"
			list-type="picture"
			:data="uploadData"
			:file-list="fileList">
			<el-button size="small" type="primary">选择文件</el-button>
			<div slot="tip" class="el-upload__tip" style="line-height: normal; margin-top:0;">只能上传gif/jpg/png文件，且不超过500kb</div>
		</el-upload>
  	</div>
</template>

<script>
	import {baseUrl, ajax, newGuid} from '@/common/biConfig'
	import $ from 'jquery'

	export default {
		props:{
			pageInfo: {
				type: Object,
				required: true,
				default:{}
			},
			comp: {
				type: Object,
				required: true,
			},
			model:{  //组件所处模式，（design设计模式，edit编辑模式）
				type:String,
				required: false,
				default: "design"
			}
		},
		components: {
			 
		},
	    data(){
			return {
				fileList: [],
				maxNum: 3,
				tempId: null,
				uploadData:{},
			};
		},
		mounted(){
			let comp = this.comp;
			if(comp.properties && comp.properties.max){
				this.maxNum = comp.properties.max;
			}
			this.tempId = newGuid();
		},
		computed: {
		},
		methods: {	
			getName(){
				return this.tempId;
			},
			//在修改状态，回写 图片上传
			backFiles(v){
				this.json = v;
				let json = JSON.parse(v);
				this.fileList = json.map(m=>{
					return {uid:m.name, name:m.fileName, url:"write/img/"+m.name+"/"+m.extName};
				});
				this.cacheFiles(v);
			},
			//在后台缓存文件
			cacheFiles(v){
				ajax({
					url:"write/fileCache.action",
					data:{tempId: this.tempId, json: v},
					type:"POST",
					success:()=>{

					}
				}, this);
			},
			handleRemove(file, fileList) {
				ajax({
					url:"write/fileReomve.action",
					data:{tempId: this.tempId, fileId: file.uid},
					type:"GET",
					success:(resp)=>{

					}
				}, this);
			},
			handlePreview(file) {

			},
			handleExceed(files, fileList) {
				this.$message.error(`当前限制选择 ${this.maxNum} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
			},
			beforeRemove(file, fileList) {
				return this.$confirm(`确定移除 ${ file.name }？`);
			},
			beforeUpload(file){
				this.uploadData.tempId = this.tempId;
				this.uploadData.fileId = file.uid;
			}
		},
		watch: {
		},
	}
</script>