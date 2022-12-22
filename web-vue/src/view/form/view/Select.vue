<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%;">
		<template v-if="comp.options" >
			<el-select v-model="name" :clearable="true" placeholder="请选择" filterable size="mini" style="width:100%;">
				<el-option
					v-for="item in datas"
					:key="item.id"
					:label="item.name"
					:value="item.name">
				</el-option>
			</el-select>
		</template>
		<template v-else>
			未定义数据
		</template>
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
				datas:[]
			}
		},
		mounted(){
			this.initDefVal();
			this.initDatas();
		},
		computed: {
		},
		methods: {	
			initDefVal(){
				this.name = this.comp.defval;
			},
			initDatas(){
				if(this.comp.valuestype == 'static' && this.comp.options){
					this.datas = this.comp.options;
				}else if(this.comp.matchTable && this.comp.matchCol){
					ajax({
						url:"form/dynamicDatas.action",
						type:"POST",
						data:JSON.stringify(this.comp),
						postJSON:true,
						success:(resp)=>{
							this.datas = resp.rows.map(m=>{
								return {id:m.col, name: m.col}
							});
						}
					});
				}
			},
		},
		watch: {
		},
	}
</script>