<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<el-dialog :visible.sync="show" :close-on-click-modal="false" custom-class="nopadding">
		  <template slot="title">
				<span class="el-dialog__title">表单定制说明</span>
				<button type="button" class="el-dialog__headerbtn minibtn" @click="$store.commit('setMini')" style="right:40px; font-size:14px; color:#909399;">
				<i class="fa" :class="isMini?'fa-expand':'fa-compress'"></i>
				</button>
			</template>
		  <ul>
			  <li>
				  从表单组件直接拖拽到右边操作区域来创建组件。
			  </li>
			  <li>
				  在组件上点击右键对组件进行操作，支持字段/属性/删除等操作。
			  </li>
			   <li>
				  字段表示当前组件对应数据表的字段名称，如果不设置系统自动生成。
			  </li>
			   <li>
				  属性菜单设置组件属性信息。
			  </li>
			  <li>
				  删除菜单直接从操作区域删除组件。
			  </li>
			   <li>
				  在操作区域可以任意拖拽、放大、缩小组件。
			  </li>
			  <li>
				  表单设计完成并保存后，请一定要点击构建表来创建表单的实际数据表。
			  </li>
		  </ul>
		<div slot="footer" class="dialog-footer">
			<el-button @click="show = false">关 闭</el-button>
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
				
			}
		},
		mounted(){
			
		},
		computed: {
			...mapState(["isMini"])
		},
		methods: {
			showDailog(){
				this.show = true;
			},
		},
		watch: {
		}
	}
</script>