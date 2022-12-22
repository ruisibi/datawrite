<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div class="layout-center">
		  <grid-layout
            :layout.sync="layout"
            :col-num="colNum"
            :row-height="rowHeight"
            :is-draggable="editor"
            :is-resizable="editor"
            :is-mirrored="false"
            :vertical-compact="true"
            :margin="[0, 0]"
            :use-css-transforms="true"
			:responsive="false"
			>
				<grid-item v-for="item in layout" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i" :key="item.i"
					@resized="chgCompEvent"
					@moved="chgCompEvent">
				<div class="form-ibox" >
					<compView :ref="'c_' + item.i" model="edit" :onlyView="onlyView" :pageInfo="pageInfo" :comp="pageInfo.comps[item.i]"></compView>
				</div>
				</grid-item>
		</grid-layout>
  	</div>
</template>

<script>
	import {baseUrl, ajax, newGuid} from '@/common/biConfig'
	import $ from 'jquery'
	import { GridLayout, GridItem } from "vue-grid-layout"
	import compView from '@/view/form/Comp-view'

	export default {
		components: {
			GridLayout,GridItem,compView
		},
		props:{
			pageInfo: {
				type: Object,
				required: true,
			},
			onlyView: {
				type:Boolean,
				required: false,
				default: false,
			}
		},
	    data(){
			return {
				layout:[],
				colNum:10,
				rowHeight:30,
				editor: false,
			}
		},
		mounted(){
			this.init();
		},
		computed: {
		},
		methods: {
			init(){
				this.layout = this.pageInfo.layout;
			},
			chgCompEvent(){

			},
			saveData(cb, isupdate, dataId){
				let vls = {};
				let comps = this.pageInfo.comps;
				let isok = true;
				for(let c in comps){
					let comp = comps[c];
					if(comp.type === 'text' || comp.type ==='table'){
						continue;
					}
					let inner = this.$refs['c_' + c][0];
					inner.$refs['ruleForm'].validate((v)=>{
						if(v){
							let v = inner.col.val;
							if(!(comp.type == 'input' || comp.type == 'inputNumber' || comp.type == 'switch')){
								v = inner.$refs['v_' + c].name;
								if(comp.type === 'checkbox' && v){  //处理多选
									v = v.join(",");
								}
								if(comp.type == 'upload'){
									v = inner.$refs['v_' + c].getName();
								}
							}
							vls[comp.id] = v;
							//console.log(comp.name+"="+v);
						}else{
							isok = false;
						}
					});
				}
				if(isok == false){
					this.$notify.error("数据未填/异常。");
				}else{
					let p = {tableId: this.pageInfo.id, values:vls};
					if(isupdate){
						p.id = dataId;
					}
					ajax({
						url:isupdate?"write/update.action":"write/save.action",
						data:JSON.stringify(p),
						type:"POST",
						postJSON: true,
						success:(resp)=>{
							this.$notify.success("保存成功");
							cb();
						}
					});
				}
			},
			clearData(){
				let comps = this.pageInfo.comps;
				for(let c in comps){
					let comp = comps[c];
					if(comp.type === 'text' || comp.type ==='table'){
						continue;
					}
					let inner = this.$refs['c_' + c][0];
					inner.col.val = null;
					if(!(comp.type == 'input' || comp.type == 'inputNumber' || comp.type == 'switch')){
						if(comp.type === 'checkbox'){ //区分多选
							inner.$refs['v_' + c].name = [];
						}else{
							inner.$refs['v_' + c].name = null;
						}
						
					}
				}
			},
			//回写值
			backData(vls, isview){
				let comps = this.pageInfo.comps;
				for(let c in comps){
					let comp = comps[c];
					if(comp.type === 'text' || comp.type ==='table'){
						continue;
					}
					let v = vls[c];
					if(!v){
						continue;
					}
					let inner = this.$refs['c_' + c][0];
					inner.col.val = v;
					if(!isview){  //不是查看模式，才设置子组件的值
						if(!(comp.type == 'input' || comp.type == 'inputNumber' || comp.type == 'switch')){
							if(comp.type === 'checkbox' && v){ //区分多选
								inner.$refs['v_' + c].name = v.split(",");
							}else if(comp.type == 'upload'){  //处理图片上传
								inner.$refs['v_' + c].backFiles(v);
							}else{
								inner.$refs['v_' + c].name = v;
							}
							
						}
					}
				}
			}
		
		},
		watch: {
		},
	}
</script>
<style lang="less" scoped>
.layout-center{
	margin:5px;
	border: 1px solid #dee5e7;
	background-color:#fff;
}
.form-ibox {
	width: 100%;
	height: 100%;
	padding: 0;
}
</style>