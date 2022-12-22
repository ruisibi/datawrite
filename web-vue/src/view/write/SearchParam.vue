<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<!-- 批量导入 -->
<template>
   <div class="params" v-if="isShowBtn()">
	   <div class="row">
		   <template v-for="item in table.cols">
		   <div class="col-sm-4" :key="item.colId" v-if="item.searchCol === 1" style="margin-bottom:5px;">
					   <template v-if="item.compType === 'radio' || item.compType == 'select'">
							<el-select style="width:100%" v-model="params[item.colName]" clearable size="mini" :placeholder="item.colNote">
								<el-option v-for="(item) in opts[item.id]?opts[item.id]:[]"
									:key="item.id"
									:label="item.name"
									:value="item.name">
								</el-option>
							</el-select>
					   </template>
						<template v-if="item.compType == 'input'">
							<el-input v-model="params[item.colName]" size="mini" :placeholder="item.colNote"></el-input>
						</template>
						<template v-if="item.compType === 'inputNumber' || item.compType === 'slider'">
							<el-input-number  v-model="params[item.colName+'_st']" :precision="0" size="mini" controls-position="right" placeholder="起始">
							</el-input-number>
							&lt; {{item.colNote}} &gt;
							<el-input-number  v-model="params[item.colName+'_end']" :precision="0" size="mini" controls-position="right" placeholder="截止">
							</el-input-number>
						</template>

						<template v-if="item.compType === 'datepicker'">
								<el-date-picker size="mini" style="width:100%;"
									v-model="params[item.colName]"
									type="daterange"
									range-separator="-"
									format="yyyy-MM-dd" value-format="yyyy-MM-dd"
									:start-placeholder="item.colNote + '起始'"
									:end-placeholder="item.colNote + '截止'">
								</el-date-picker>
							</template>
				   
		   </div>
		   </template>
		   <div class="col-sm-4" v-if="isShowBtn()">
			   <button type="submit" @click="searchParam()" class="btn btn-primary btn-sm">搜索</button> 
			   <button type="button" @click="clearParam()" class="btn btn-info btn-sm">清除</button>
		 </div>
	   </div>
   </div>
</template>

<script>
	import {ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import { Message } from 'element-ui'

	export default {
	    data(){
			return {
				params:{

				},
				opts:{
					
				}
			}
		},
		props: {
			table: {
				type: Object,
				required: true,
				default:{}
			},
		},
		mounted(){
			
		},
		computed: {
		},
		methods: {
			loadOpts(tableId){
				ajax({
					url:"write/loadOpts.action",
					data:{tableId: tableId},
					type:"GET",
					success:(resp)=>{
						this.opts = resp.rows;
					}
				}, this);
			},
			isShowBtn(){
				let ret = false;
				if(!this.table.cols){
					return ret;
				}
				let match = this.table.cols.filter(m=>m.searchCol===1);
				if(match.length > 0){
					ret = true;
				}
				return ret;
			},
			searchParam(){
				this.$parent.loadDatas();
			},
			clearParam(){
				for(let c in this.params){
					this.params[c] = null;
				}
			},
			getParams(){
				let ret = {};
				$(this.table.cols).each((index, item)=>{
					if(item.searchCol === 1){
						if(item.compType === 'inputNumber' || item.compType === 'slider'){
							let v1 = this.params[item.colName+'_st'];
							let v2 = this.params[item.colName+'_end'];
							if(v1 == 0 && v2 == 0 ){
								return;
							}
							if(!v1 && !v2){
								return;
							}
							ret[item.colName] = [v1, v2];
						}else{
							ret[item.colName] = this.params[item.colName];
						}
					}
				});
				return ret;
			}
		},
		watch: {
			table:function(newVal){
				if(newVal.id){
					this.loadOpts(newVal.id);
				}
			}
		}
	}
</script>

<style lang="less" scoped>
.params{
	padding-bottom: 10px;
}
.param_txt {
	margin-top: 4px;
	text-align: right;
}
</style>
