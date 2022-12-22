<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div class="wrapper-content-nomargin">
		 <el-menu @select="handleSelect" class="el-menu-demo" background-color="#f6f8f8" text-color="#777" mode="horizontal">
			<el-menu-item index="1"><i class="fa fa-arrow-left"></i> 返回</el-menu-item>
			<el-menu-item index="2"><i class="glyphicon glyphicon-save"></i> 保存</el-menu-item>
			<el-menu-item index="3"><i class="fa fa-gear"></i> 设置</el-menu-item>
			<el-menu-item index="4"><i class="fa fa-question-circle"></i> 帮助</el-menu-item>
		</el-menu>
		<div class="layouts" style="height:calc(100% - 44px)">
			<layoutLeft ref="leftForm"></layoutLeft>
			<layoutRight ref="rightForm" :pageInfo="pageInfo" v-if="showProp"></layoutRight>
			<layoutCenter ref="centerForm" :pageInfo="pageInfo"></layoutCenter>
		</div>
		<formSave ref="formSaveForm"></formSave>
		<helper ref="helperForm"/>
  	</div>
</template>

<script>
	import {baseUrl, ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import layoutLeft from './Layout-left'
	import layoutRight from './Layout-right'
	import layoutCenter from './Layout-center'
	import formSave from './FormSave'
	import "jquery-contextmenu";
	import "jquery-contextmenu/dist/jquery.contextMenu.min.css";
	import 'jquery-ui-dist/jquery-ui'
	import 'jquery-ui-dist/jquery-ui.css'
	import "jstree";
	import "jstree/dist/themes/default/style.min.css";
	import helper from './Helper'

	export default {
		components: {
			 layoutLeft, layoutRight, layoutCenter, formSave, helper
		},
	    data(){
			return {
				showProp:false,
				pageInfo: {},
				isupdate:false,
			}
		},
		mounted(){
			let id = this.$route.query.id;
			if(id){
				this.initForm(id);
			}
		},
		computed: {
		},
		methods: {	
			setUpdate(){
				this.isupdate = true;
			},
			handleSelect(key){
				if(key == 1){
					if(this.isupdate == true){
						if(confirm("您还未保存，是否继续推出？")){
							this.$router.push("/form/Index");
						}
					}else{
						this.$router.push("/form/Index");
					}
				}else if(key == 2){
					//必须更新layout
					this.pageInfo.layout = this.$refs['centerForm'].layout;
					if(this.pageInfo.id){
						this.updateForm();
					}else{
						this.$refs['formSaveForm'].showDailog(this.pageInfo);
					}
				}else if(key == 3){  //页面设置
					this.$refs['formSaveForm'].modify(this.pageInfo);
				}else if(key ==4){
					this.$refs['helperForm'].showDailog();
				}
			},
			openProp(comp){
				this.showProp = true;
				$(".layout-center").css({"margin-right": "266px"});
				this.$nextTick(()=>this.$refs['rightForm'].initPage(comp));
			},
			closeProp(){
				this.showProp = false;
				$(".layout-center").css({"margin-right": "5px"});
			},
			updateForm(){
				ajax({
					url:"form/save.action",
					type:"POST",
					data: {id: this.pageInfo.id, tableCfg: JSON.stringify(this.pageInfo)},
					success:(resp)=>{
						this.isupdate = false;
						this.$notify.success("保存成功！");
					}
				}, this);
			},
			initForm(id){
				ajax({
					url:"form/getForm.action",
					data:{id:id},
					type:"GET",
					success:(resp)=>{
						this.isupdate = false;
						let cfg = JSON.parse(resp.rows.tableCfg);
						this.pageInfo = cfg;
						this.$refs['centerForm'].layout = cfg.layout;
					}
				});
			}
		},
		watch: {
		},
		beforeRouteLeave(to, from, next) {
			this.$destroy();
			next();
		}
	}
</script>
