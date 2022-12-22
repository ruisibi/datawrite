<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<!-- 仪表盘组件编辑页面 -->
<template>
	<div class="dataWriter" v-if="show">
		<el-menu @select="handleSelect" class="el-menu-demo" background-color="#f6f8f8" text-color="#777" mode="horizontal">
			<el-menu-item index="1" v-if="onlyView==false">保存并关闭</el-menu-item>
			<el-menu-item index="2" v-if="onlyView==false">保存并新建</el-menu-item>
			<el-menu-item index="3" v-if="onlyView==false">清空</el-menu-item>
			<el-menu-item index="4">关闭</el-menu-item>
		</el-menu>
		<writePanel ref="writePanelForm" :onlyView="onlyView" :pageInfo="pageInfo"></writePanel>
	</div>
</template>

<script>
	import {baseUrl, ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import writePanel from './WritePanel'

	export default {
	    data(){
			return {
				show: false,
				pageInfo: {},
				id: null, //数据ID
				isupdate: false,  //是否修改状态
				onlyView: false, //是否查看状态
			}
		},
		components: {
			writePanel
    	},
		mounted(){
		},
		computed: {
			
		},
		methods: {	
			openDialog(pageInfo){
				this.id = null;
				this.show = true;
				this.isupdate = false;
				this.onlyView = false;
				this.pageInfo = pageInfo;
			},
			modifyDialog(id, pageInfo){
				this.id = id;
				this.show = true;
				this.isupdate = true;
				this.onlyView = false;
				this.pageInfo = pageInfo;
				ajax({
					url:"write/getData.action",
					data:{tableId:pageInfo.id, id: id},
					type:"POST",
					success:(resp)=>{
						this.$nextTick(()=>this.backData(resp.rows, false));
					}
				}, this);
			},
			viewDialog(id, pageInfo){
				this.id = id;
				this.show = true;
				this.isupdate = false;
				this.onlyView = true;
				this.pageInfo = pageInfo;
				ajax({
					url:"write/getData.action",
					data:{tableId:pageInfo.id, id: id},
					type:"POST",
					success:(resp)=>{
						this.$nextTick(()=>this.backData(resp.rows, true));
					}
				}, this);
			},
            handleSelect(index){
                if(index == 4){
					this.closeDialog();
					this.$parent.loadDatas();
                }else if(index == 1){
					this.saveData(()=>{
						this.$parent.loadDatas();
						this.closeDialog();
					});
				}else if(index == 3){
					this.clearData();
				}else if(index == 2){  //保存并新建
					this.saveData(()=>{
						this.closeDialog();
						this.$nextTick(()=>this.openDialog(this.pageInfo));
					});
				}
            },
            closeDialog(){
                this.show = false;
			},
			saveData(cb){
				this.$refs['writePanelForm'].saveData(cb, this.isupdate, this.id);
			},
			clearData(){
				this.$refs['writePanelForm'].clearData();
			},
			backData(vls, isview){
				this.$refs['writePanelForm'].backData(vls, isview);
			}
		},
		watch: {
		},
	}
</script>

<style lang="less" scoped>
.dataWriter { 
	z-index: 1400; 
	position: absolute; 
	width: 100%; 
	height: 100%; 
	top: 0px; 
	left: 0px; 
	background-color: #f0f3f4;
}
</style>
