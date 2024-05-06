/*
 * Copyright 2021 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */

/**
 * 系统使用的router
 */
import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/view/Login'
import Main from '@/view/Main'
import Welcome from '@/view/Welcome'
import Menu from '@/view/frame/Menu'
import Role from '@/view/frame/Role'
import RoleMenu from '@/view/frame/RoleMenu'
import User from '@/view/frame/User'
import UserMenu from '@/view/frame/UserMenu'
import OnlineUser from '@/view/frame/OnlineUser'
import NotFind from '@/view/NoFind'
import FormIndex from '@/view/form/Index'
import FormDesign from '@/view/form/Design'
import WriteTableList from '@/view/write/TableList'
import WriteDataList from '@/view/write/DataList'
import VerifyTableList from '@/view/verify/TableList'
import VerifyDataList from '@/view/verify/DataList'
import CubeList from '@/view/model/CubeList'
import ReportDesign from '@/view/bireport/ReportDesign'
import BireportPrint from '@/view/bireport/Print'
import MobileIndex from '@/view/mobile/Index'
import MobileDataList from '@/view/mobile/DataList'

Vue.use(Router)

let router = new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path:'*',
      component:NotFind
    },
    {
      path: '/main',
      name: 'main',
      component: Main,
      children: [
        {
          path: '/Welcome',
          name: 'welcome',
          component: Welcome
        },
        {
          path:'/frame/Menu',
          name:'menu',
          component:Menu
        },
        {
          path:'/frame/User',
          name:'user',
          component:User
        },
        {
          path:'/frame/Role',
          name:'role',
          component:Role
        },
        {
          path:'/frame/RoleMenu',
          name:'roleMenu',
          component:RoleMenu
        },
        {
          path:'/frame/UserMenu',
          name:'userMenu',
          component:UserMenu
        },
        {
          path:'/frame/OnlineUser',
          name:'onlineUser',
          component:OnlineUser
        },
        {
          path:"/form/Index",
          name: "formIndex",
          component: FormIndex
        },
        {
          path:"/form/Design",
          name: "formDesign",
          component: FormDesign
        },
        {
          path:"/write/TableList",
          name: "writeTableList",
          component: WriteTableList
        },
        {
          path:"/write/DataList",
          name: "writeDataList",
          component: WriteDataList
        },
        {
          path: "/verify/TableList",
          name: "verifyTableList",
          component: VerifyTableList
        },
        {
          path: "/verify/DataList",
          name: "verifyDataList",
          component: VerifyDataList
        },
        {
          path: "/model/CubeList",
          name: "cubeList",
          component: CubeList
        },
        {
          path: "/bireport/ReportDesign",
          name: "reportDesign",
          component: ReportDesign,
        }
      ]
    },
    {
      path: '/bireport/Print',
      name: 'bireportPrint',
      component: BireportPrint
    },
    {
      path:"/mobile/Index",
      name:"mobileIndex",
      component: MobileIndex,
    },
    {
      path: "/mobile/DataList",
      name:  'mobileDataList',
      component: MobileDataList
    }
  ],

})

router.beforeEach((to, from, next) => {
  if(to.path != "/"){
    // if(!checkIsLogin()){
    //   next("/")
    //   return;
    // }
  }
  next();
})

export default router;
