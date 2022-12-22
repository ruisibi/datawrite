<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%;">
		<template v-if="comp.options" >
			<template v-if="!comp.properties || !comp.properties.showType || comp.properties.showType == 'def'">
				<el-checkbox-group v-model="name" size="mini">
					<template v-for="item in datas">
						<el-checkbox :key="item.id" :label="item.name">{{item.name}}</el-checkbox> 
						<br v-if="comp.properties && comp.properties.focesBr == true"/>
					</template>
				</el-checkbox-group>
			</template>
			<template v-if="comp.properties.showType == 'btn'">
				<el-checkbox-group v-model="name" size="mini">
					<template v-for="item in datas">
						<el-checkbox-button :key="item.id" :label="item.name">{{item.name}}</el-checkbox-button> 
						<br v-if="comp.properties && comp.properties.focesBr == true"/>
					</template>
				</el-checkbox-group>
			</template>
			<template v-if="comp.properties.showType == 'border'">
				<el-checkbox-group v-model="name" size="mini">
					<template v-for="item in datas">
						<el-checkbox border="" :key="item.id" :label="item.name">{{item.name}}</el-checkbox> 
						<br v-if="comp.properties && comp.properties.focesBr == true"/>
					</template>
				</el-checkbox-group>
			</template>
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
				name: [],
				datas:[],
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
				if(this.comp.defval){
					this.name = [this.comp.defval];
				}
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