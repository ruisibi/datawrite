<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%;">
		<el-date-picker v-model="name" size="mini" :format="getFmt()" style="width:100%" :type="getType()" :value-format="getFmt()"></el-date-picker>
  	</div>
</template>

<script>
	import {baseUrl, ajax, formatDate} from '@/common/biConfig'
	import $ from 'jquery'
	import * as utils from '@/view/form/Utils'

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
			}
		},
		components: {

		},
	    data(){
			return {
				name: null,
			}
		},
		mounted(){
			this.initDefVal();
		},
		computed: {
		},
		methods: {
      flush(){
        this.$forceUpdate();
      },
			initDefVal(){
				if(this.comp.defval){
					this.name = utils.parserDefDate(this.comp.defval, this.getFmt());
				}
			},
			getType(){
				let c = this.comp;
				if(c.properties && c.properties.showType){
					return c.properties.showType;
				}else{
					return "date";
				}
			},
			getFmt(){
				let c = this.comp;
				if(c.properties && c.properties.fmt){
					return c.properties.fmt;
				}else{
					return "yyyy-MM-dd";
				}
			},
		},
		watch: {
		},
	}
</script>
