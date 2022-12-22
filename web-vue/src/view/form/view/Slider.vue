<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%;">
		<el-slider v-model="name" :max="calcMaxMin('max')" :min="calcMaxMin('min')"></el-slider>
  	</div>
</template>

<script>
	import {baseUrl, ajax} from '@/common/biConfig'
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
			initDefVal(){
				if(this.comp.defval){
					this.name = Number(this.comp.defval);
				}
			},
			calcMaxMin(tp){
				if(tp === 'max'){
					if(this.comp.properties && this.comp.properties.max){
						return this.comp.properties.max;
					}else{
						return 100;
					}
				}else if(tp === 'min'){
					if(this.comp.properties && this.comp.properties.min){
						return this.comp.properties.min;
					}else{
						return 0;
					}
				}
			}
		},
		watch: {
		},
	}
</script>