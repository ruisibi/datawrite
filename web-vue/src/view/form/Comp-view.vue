<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div style="width:100%; height:100%; overflow:auto;">
		<template v-if="comp.type === 'text'">
			<textView  :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"/>
		</template>
		<template v-else>
			<template v-if="onlyView == true">
				<el-form>
					<el-form-item :label="comp.name+'：'" :label-width="comp.labelWidth?comp.labelWidth+'px':'130px'">
						<template v-if="comp.type==='upload'">
							<div v-html="writeUpload(col.val)"></div>
						</template>
						<template v-else>
							{{getVal()}}
						</template>
					</el-form-item>
				</el-form>
			</template>
			<template v-else>
				<el-form :rules="rules" :model="col" ref="ruleForm" >
					<el-form-item prop="val" :label-width="comp.labelWidth?comp.labelWidth+'px':'130px'">
							<div slot="label" :align="comp.align?comp.align:'left'">
								<span v-if="comp.required == true" style="color:red;">*</span> {{comp.name}}：
							</div>
							<template v-if="comp.type == 'input' ">
								<el-input v-model="col.val" size="mini"></el-input>
							</template>
							<template v-if="comp.type == 'radio' ">
								<radioView :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"></radioView>
							</template>
							<template v-if="comp.type === 'inputNumber'">
								<el-input-number v-model="col.val"
								:max="comp.properties?comp.properties.max:null"
								:min="comp.properties?comp.properties.min:null"
								:precision="comp.properties?comp.properties.precision:null"
								:step="1" size="mini"></el-input-number>
							</template>
							<template v-if="comp.type === 'checkbox'">
								<checkboxView :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"></checkboxView>
							</template>
							<template v-if="comp.type === 'select'">
								<selectView :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"></selectView>
							</template>
							<template v-if="comp.type ==='switch'">
								<div align="left">
								<el-switch	v-model="col.val" :active-value="1" :inactive-value="0"></el-switch>
								</div>
							</template>
							<template v-if="comp.type === 'slider'">
								<sliderView :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"/>
							</template>
							<template v-if="comp.type === 'datepicker'">
								<datePicker :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"/>
							</template>
							<template v-if="comp.type === 'upload'">
								<uploadView :model="model"  :ref="'v_' + comp.id" :pageInfo=pageInfo :comp="comp"/>
							</template>
					</el-form-item>
				</el-form>
			</template>
		</template>
  	</div>
</template>

<script>
	import {baseUrl, ajax, formatDate} from '@/common/biConfig'
	import $ from 'jquery'
	import checkboxView from './view/Checkbox'
	import radioView from './view/Radio'
	import selectView from './view/Select'
	import sliderView from './view/Slider'
	import datePicker from './view/DatePicker'
	import uploadView from './view/Upload'
	import textView from './view/Text'

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
			},
			onlyView: {
				type:Boolean,
				required: false,
				default: false,
			},
			model:{  //组件所处模式，（design设计模式，edit编辑模式）
				type:String,
				required: false,
				default: "design"
			}
		},
		components: {
			 checkboxView, radioView, selectView, sliderView, datePicker, uploadView, textView
		},
	    data(){
			let checkData = (rule, value, callback) => {
				let comp = this.comp;
				if(!(comp.type == 'input' || comp.type == 'inputNumber' || comp.type == 'switch')){  //非这三种类型，采用子模块
					let v = this.$refs['v_' + comp.id].name;
					if(this.comp.required == true && (v == null || v == '')){
						callback(new Error('必填字段'));
					}else{
						callback();
					}
				}else{
					if(this.comp.required == true && (this.col.val == null || this.col.val == "")){
						callback(new Error('必填字段'));
					}else{
						callback();
					}
				}
			};
			return {
				col: {
					val:null,
				},
				rules:{
					val: [
						{ validator: checkData, trigger: 'blur' }
					]
				},
			}
		},
		mounted(){
			this.initDefVal();
		},
		computed: {
		},
		methods: {
			initDefVal(){
				this.col.val = this.comp.defval;
			},
			writeUpload(v){
				if(v){
					let json = JSON.parse(v);
					let s = [];
					$(json).each((a, b)=>{
						s.push("<img src='");
						s.push("write/img/"+b.name+"/"+b.extName);
						s.push("'");
						s.push(" ");
						s.push("class='img-responsive'");
						s.push(">");
					});
					return s.join("");
				}
				return "";
			},
      getVal(){
			  let v = this.col.val;
			  if(this.comp.type == 'datepicker'){
			    let prop = this.comp.properties;
			    let fmt = 'yyyy-MM-dd';
			    if(prop){
			      fmt = prop.fmt;
          }
          var date = new Date(v);
			    return formatDate(date, fmt);
        }
			  return v;
      },
		},
		watch: {
		},
	}
</script>
