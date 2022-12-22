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
            :margin="[5, 5]"
            :use-css-transforms="true"
			:responsive="false"
			>
				<grid-item v-for="item in layout" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i" :key="item.i"
					@resized="chgCompEvent"
					@moved="chgCompEvent">
				<div class="form-ibox" @contextmenu="compRightMenus(pageInfo.comps[item.i])" >
					<compView :ref="'c_' + item.i" :pageInfo="pageInfo" :comp="pageInfo.comps[item.i]"></compView>
				</div>
				</grid-item>
		</grid-layout>
		<compData ref="dataForm"></compData>
		<compMatchCol ref="compMatchColForm" />
  	</div>
</template>

<script>
	import {baseUrl, ajax, newGuid} from '@/common/biConfig'
	import $ from 'jquery'
	import { GridLayout, GridItem } from "vue-grid-layout"
	import compView from './Comp-view'
	import compData from './set/CompData'
	import compMatchCol from './set/CompMatchCol'

	export default {
		components: {
			GridLayout,GridItem,compView, compData, compMatchCol
		},
		props:{
			pageInfo: {
				type: Object,
				required: true,
				default:{}
			}
		},
	    data(){
			return {
				layout:[],
				colNum:10,
				rowHeight:26,
				editor: true,
			}
		},
		mounted(){
			this.initDrop();
		},
		computed: {
		},
		methods: {
			initDrop(){
				let ts = this;
				$(".layout-center").droppable({
					accept:"#formComps .jstree-node",
					over:function(e, ui){
						 $(ui.helper[0]).find("span").removeClass("glyphicon-remove").addClass("glyphicon-ok");
					},
					out:function(e, ui){
						 $(ui.helper[0]).find("span").removeClass("glyphicon-ok").addClass("glyphicon-remove");
					},
					drop:function(e, ui){
						ts.setUpdate();
						//添加组件
						let id = newGuid();
						if(ts.layout.length == 0){
							ts.layout.push({"x":0,"y":0,"w":5,"h":2,"i":id});
						}else{
							let maxY = 0;
							$(ts.layout).each((a, b)=>{
								if(b.y + b.h >=  maxY){
									maxY = b.y + b.h;
								}
							});
							ts.layout.push({"x":0,"y":maxY,"w":5,"h":2,"i":id});
						}
						//设置组件信息
						let ref = $("#formComps").jstree(true);
						let node = ref.get_node(ui.draggable[0]);
						if(ts.pageInfo.comps == null){
							ts.pageInfo.comps = {};
						}
						ts.pageInfo.comps[id] ={
							id: id,
							type : node.id,
							name: node.text
						};
					}
				});
			},
			compRightMenus(comp){
				 $.contextMenu( 'destroy');
				let ts = this;
				$.contextMenu({
					selector: 'div.form-ibox',
					className: "compMenu",
					trigger: 'right',
					delay: 500,
					zIndex:999999,
					autoHide:true,
					items:{
						data:{name:"数据", icon:"fa-database", visible:function(){
							if(comp.type == 'radio' || comp.type === 'checkbox' ||
								comp.type ==='select'){
								return true;
							}else{
								return false;
							}
						}},
						col:{name:"字段", icon:"fa-navicon", visible:function(){
							if(comp.type == 'text'){
								return false;
							}else{
								return true;
							}
						}},
						edit:{name:"属性", icon:"fa-gears"},
						del:{name:"删除", icon:"fa-remove"}
					},
					callback:function(key, opt){
						if(key === 'del'){
							ts.delComp(comp);
						}else if(key ==='edit'){
							ts.editComp(comp);
						}else if(key == 'data'){
							ts.setCompData(comp);
						}else if(key ==='col'){
							ts.setCompMapCol(comp);
						}
					}
				});
			},
			delComp(comp){
				$(this.layout).each((a, b)=>{
					if(b.i === comp.id){
						this.layout.splice(a, 1);
						delete this.pageInfo.comps[b.i];
						this.setUpdate();
						this.$parent.closeProp();
					}
				});
			},
			editComp(comp){
				this.$parent.openProp(comp);
			},
			setCompData(comp){
				if(comp.type === 'radio' || comp.type === 'checkbox' || comp.type ==='select'){
					this.$refs['dataForm'].open(comp);
				}
			},
			setUpdate(){
				this.$parent.setUpdate();
			},
			chgCompEvent(){
				this.setUpdate();
			},
			setCompMapCol(comp){
				this.$refs['compMatchColForm'].open(comp);
			}
		},
		watch: {
		},
	}
</script>
<style lang="less" scoped>
.layout-center{
	margin:5px 5px 5px 225px;
	border: 1px solid #dee5e7;
	background-color:#fff;
	height: 100%;
}
.form-ibox {
	border: 1px solid #dee5e7;
    background-color: #f9fafb;
	width: 100%;
	height: 100%;
}
</style>