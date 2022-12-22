<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  	<div class="wrapper-content">
		  <div class="ibox">
			  <div class="ibox-content" style="border:none">

				  	<h2>
					睿思云数据填报系统功能模块：
					</h2>
					<p>
						<ul>
							<li>制作表单</li>
							<li>构建表</li>
							<li>数据填报</li>
							<li>数据审核</li>
							<li>数据建模</li>
							<li>统计分析</li>
						</ul>
						<div style="padding:10px 0px 30px 0px;">
						<img src="../assets/image/introduce.png">
						</div>
						<button type="button" class="btn btn-primary btn-sm" @click="goto()">官方网站</button>
						<button type="button" class="btn btn-primary btn-sm" @click="goto2()">睿思BI</button>
					</p>
			  </div>
		  </div>

  	</div>
</template>

<script>
	import { mapState } from "vuex";
	import {ajax} from '@/common/biConfig'
	import $ from 'jquery'
	import { Loading } from 'element-ui'

	export default {
	    data(){
			return {
				sysInfo:{},

			}
		},
		mounted(){
			this.getSysInfo();
		},
		computed: {
			...mapState(["isMini"])
		},
		methods: {
			getSysInfo(){
				ajax({
					url:"frame/Welcome.action",
					data:{},
					success:(resp)=>{
						this.sysInfo = resp.rows;
					}
				}, this);
			},
			goto(){
				window.open('https://www.ruisitech.com/');
			},
			goto2(){
				window.open('https://www.ruisitech.com/product.html');
			}
		},
		watch: {
		}
	}
</script>
