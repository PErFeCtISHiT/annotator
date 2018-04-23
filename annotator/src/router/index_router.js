import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter);
import first from '../components/firstcomponent'
import distributeNewTask from '../components/distributeNewTask'
import testDraw from '../components/testDraw'

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
      path: '/testDraw',
      component: testDraw
    }
  ]
});
