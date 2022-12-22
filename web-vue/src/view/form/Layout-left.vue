<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div class="layout-left">
		<div class="ibox" style="height:100%;">
			<div class="ibox-title" style="height:30px;">
				表单组件
			</div>
			<div class="ibox-content" style="height: calc(100% - 32px); padding:0px;">
				<div id="formComps"></div>
			</div>
		</div>
  	</div>
</template>

<script>
	import {baseUrl, ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import * as utils from './Utils'
	
	export default {
		components: {
			 
		},
	    data(){
			return {
				
			}
		},
		mounted(){
			this.initCompsTree();
		},
		computed: {
		},
		methods: {	
			initCompsTree(){
				let ref = $("#formComps").jstree(true);
				if(ref){
					ref.destroy();
				}
				const dragfunc = () => {
					$("#formComps .jstree-node").draggable({
						cursor: "point",
						appendTo: "body",
						revert: 'invalid',
						revertDuration: 250,
						cursorAt: { top: 0, left: -35 },
						scroll :false,
						helper:function(e){
							var id = $(this).find("a.jstree-anchor:first").text();
							return "<div class=\"vakata-dnd\"><span class=\"miconcancel glyphicon glyphicon-remove\"></span>"+id+"</div>";
						},
						start:function(e){
							var ref = $('#formComps').jstree(true),node = ref.get_node(this);
							
						}
					});
				};
				var ts = this;
				$('#formComps').jstree({
					core: {
						data:utils.compTypes,
						check_callback: false
					},
					"plugins": ["wholerow"]
				}).bind("ready.jstree", function (a, b) {
					dragfunc();
				}).bind("after_open.jstree", function () {
					dragfunc();
				});
				
			},
		},
		watch: {
		},
	}
</script>
<style lang="less" scoped>
.layout-left {
  position: fixed;
  width: 220px;
  height: calc(100% - 125px);
}
</style>
<style lang="less">
    .comp_color {
        color:#23b7e5;
    }
</style>