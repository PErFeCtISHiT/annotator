import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter);
import first from '../components/firstcomponent'
import distributeNewTask from '../components/distributeNewTask'
import testDraw from '../components/testDraw'
import admin from '../components/admin'
import recharge from '../components/recharge'

export default new VueRouter({
  routes: [
    {
      path: "/1-1",
      component: first
    },
    {
      path: "/1-2",
      component: distributeNewTask
    },
    {
      path: "/3-1",
      component: recharge
    },
    {
      path: "/3-2",
      component: admin
    },
    {
      path: '/testDraw',
      component: testDraw
    }
  ]
});
