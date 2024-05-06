<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
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
				options:[]
			}
		},
		render(h){
			let comp = this.comp;
			let style = {width:"100%", height:"100%"};
			if(comp.properties && comp.properties.color){
				style['color'] = comp.properties.color;
			}
			if(comp.properties && comp.properties.size){
				style['font-size'] = comp.properties.size + "px";
			}
			if(comp.properties && comp.properties.lineheight){
				style['line-height'] = comp.properties.lineheight + "px";
			}
			if(comp.properties && comp.properties.align){
				style['text-align'] = comp.properties.align;
			}
			if(comp.properties && comp.properties.bold == true){
				style['font-weight'] = "bold";
			}
			if(comp.properties && comp.properties.italic == true){
				style['font-style'] = "italic";
			}
			if(comp.properties && comp.properties.underline == true){
				style['text-decoration'] = "underline";
			}
			return h('div', {style:style}, comp.desc?comp.desc:"未设置文本内容");
		},
		mounted(){

		},
		computed: {
		},
		methods: {
			flush(){
			  this.$forceUpdate();
      }
		},watch: {
		},
	}
</script>
