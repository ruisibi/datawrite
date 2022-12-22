<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div class="layout-right">
		<div class="ibox" style="height:100%;">
			<div class="ibox-title" style="height:30px;">
				<h5> {{title}} - 属性面板</h5>
				<div class="ibox-tools">
					<button @click="closeprop()" class="btn btn-link btn-xs" style="font-size: 16px;"><i class="el-icon el-icon-close"></i></button>
				</div>
			</div>
			<div class="ibox-content" style="height: calc(100% - 32px); padding:0px;">
				<radioForm ref="radioForm" v-if="showRadio"/>
				<inputForm ref="inputForm" v-if="showInput" />
				<inputNumber ref="inputNumberForm" v-if="inputNumber"></inputNumber>
				<checkboxForm ref="checkboxForm" v-if="showCheckbox"></checkboxForm>
				<selectForm ref="selectForm" v-if="showSelect"/>
				<switchForm ref="switchForm" v-if="showSwitch"></switchForm>
				<sliderForm ref="sliderForm" v-if="showSlider"/>
				<datepickerForm ref="datepickerForm" v-if="showDatePicker"/>
				<uploadForm ref="uploadForm" v-if="showUpload"/>
				<textForm ref="textForm" v-if="showText" />
			</div>
		</div>
  	</div>
</template>

<script>
	import {baseUrl, ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import radioForm from './prop/Radio'
	import inputForm from './prop/Input'
	import inputNumber from './prop/inputNumber'
	import checkboxForm from './prop/Checkbox'
	import selectForm from './prop/Select'
	import switchForm from './prop/Swith'
	import sliderForm from './prop/Slider'
	import datepickerForm from './prop/DatePicker'
	import uploadForm from './prop/Upload'
	import textForm from './prop/Text'

	export default {
		components: {
			 radioForm, inputForm, inputNumber, checkboxForm, selectForm, switchForm, sliderForm, datepickerForm, 
			 uploadForm, textForm
		},
		props:{
			pageInfo: {
				type: Object,
				required: true,
				default:{}
			},
		},
	    data(){
			return {
				title:"",
				comp: null,
				showRadio: false,
				showInput: false,
				inputNumber: false,
				showCheckbox: false,
				showSelect: false,
				showSwitch: false,
				showSlider : false,
				showDatePicker: false,
				showUpload: false,
				showText: false,
			}
		},
		mounted(){

		},
		computed: {
		},
		methods: {	
			closeprop(){
				this.closeAllProps;
				this.$parent.closeProp();
			},
			initPage(comp){
				if(!comp){  
					return;
				}else{
					this.title = comp.name;
				}
				this.closeAllProps();
				if(comp.type ==='radio'){
					this.showRadio = true;
				}else if(comp.type === 'input'){
					this.showInput = true;
				}else if(comp.type === 'inputNumber'){
					this.inputNumber = true;
				}else if(comp.type === 'checkbox'){
					this.showCheckbox = true;
				}else if(comp.type ==='select'){
					this.showSelect = true;
				}else if(comp.type === 'switch'){
					this.showSwitch = true;
				}else if(comp.type === 'slider'){
					this.showSlider = true;
				}else if(comp.type === 'datepicker'){
					this.showDatePicker = true;
				}else if(comp.type == 'upload'){
					this.showUpload = true;
				}else if(comp.type == 'text'){
					this.showText = true;
				}
				this.$nextTick(()=>{
					this.$refs[ comp.type + 'Form'].init(comp);
				});
			},
			closeAllProps(){
				this.showRadio = false;
				this.showInput = false;
				this.inputNumber = false;
				this.showCheckbox = false;
				this.showSelect = false;
				this.showSwitch = false;
				this.showSlider = false;
				this.showDatePicker = false;
				this.showUpload = false;
				this.showText = false;
			},
			setUpdate(){
				this.$parent.setUpdate();
			},
		},
		watch: {
		},
	}
</script>
<style lang="less" scoped>
.layout-right{
	position: fixed;
    width: 263px;
    height: calc(100% - 125px);
    right: 0;
}
.ibox-title h5 {
    display: inline-block;
    font-size: 14px;
    margin: 3px 0 2px;
    padding: 0;
    text-overflow: ellipsis;
    float: left;
  }
  .ibox-tools {
    display: inline-block;
    float: right;
    margin-top: -6px;
    position: relative;
    padding: 0;
}
</style>