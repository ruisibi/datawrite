<!--
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 -->
<template>
  <div class="wrapper-content">
    <div class="ibox" id="mainDiv">
      <div class="ibox-title">角色管理 >> 授权仪表盘</div>
      <div class="ibox-content">
        <div class="row">
          <div class="col-sm-6">
            <!--
           <a href="javascript:;" @click="openall">全部展开</a> &nbsp;&nbsp; <a href="javascript:;" @click="closeall">全部关闭</a>
           -->
           <div id="dashboardTree"></div>
          </div>
          <div class="col-sm-6" align="right">
            <el-button type="primary" @click="save()">确 定</el-button>
			    	<el-button @click="backpage()">取 消</el-button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { baseUrl, ajax } from "@/common/biConfig";
import $ from "jquery";

export default {
  data() {
    return {
      roleId : null
  	}
  },
  components: {
	},
  mounted() {
    this.roleId = this.$route.query.roleId;
    this.initTree();
  },
  computed: {},
  methods: {
    //初始化 jstree
    initTree: function () {
		let ts = this;
      $("#dashboardTree")
        .jstree({
          core: {
            check_callback: true,
            data: function (obj, callback) {
                ajax({
                  type: "GET",
                  data: {roleId:ts.roleId},
                  postJSON: false,
                  url: 'auth/role/Dashboard.action',
                  success: function (resp) {
                    callback.call(this, resp.rows);
                  },
                }, ts);
            },
          },
          plugins: ["checkbox","wholerow"],
        })
        .bind("open_node.jstree", function (a, b) {
          if (b.node.id == "0") {
            return;
          }
          const ref = $("#dashboardTree").jstree(true);
          ref.set_icon(b.node, "fa fa-folder-open-o");
        })
        .bind("close_node.jstree", function (a, b) {
          if (b.node.id == "0") {
            return;
          }
          const ref = $("#dashboardTree").jstree(true);
          ref.set_icon(b.node, "fa fa-folder-o");
        });
        this.treeRef = $("#dashboardTree").jstree(true);
    },
    save:function(){
     var ref = this.treeRef;
      var ids = [];
      var nodes = ref.get_selected(true);
      for(let i=0; nodes&&i<nodes.length; i++){
        if(nodes[i].li_attr.type == 'cata'  ){
          continue;
        }
        ids.push(Number(nodes[i].id));
      }

      ajax({
        type:"POST",
        url:"auth/role/dashboard/save.action",
        dataType:"JSON",
        postJSON:true,
        data:JSON.stringify({dashboard:ids, roleId:this.roleId}),
        success:(resp) => {
          this.$notify.success("操作成功！");
        }
      }, this);
    },
    backpage:function(){
      this.$router.push('Role')
    },
    openall:function(){
      this.treeRef.open_all();
    },
    closeall:function(){
        this.treeRef.close_all();
    }
  },
  watch: {},
  beforeRouteLeave: function(to, from, next) {
    this.$destroy();
    next();
  }
};
</script>
