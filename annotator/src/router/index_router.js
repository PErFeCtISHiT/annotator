import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter);
import requesterTasks from '../components/requesterTasks'
import distributeNewTask from '../components/distributeNewTask'
import testDraw from '../components/testDraw'
import admin from '../components/admin'
import recharge from '../components/recharge'
import workerGetNewTask from '../components/workerGetNewTask'
import workerOnDoing from '../components/workerOnDoing'
import workerHistRank from '../components/workerHistRank'
import workerNoteMark from '../components/workerNoteMark'
import taskDetail from '../components/taskDetail'
import firstPart from '../components/firstPart'


export default new VueRouter({
  routes: [
    {
      path: "/1-1",
      component: requesterTasks
    },
    {
      path: "/1-2",
      component: distributeNewTask
    },
    {
      path: "/2-1",
      component: workerGetNewTask
    },
    {
      path: "/2-2",
      component: workerOnDoing
    },
    {
      path: "/2-3",
      component: workerHistRank
    },
    {
      path: "/taskDetail/:taskID",
      name: 'taskDetail',
      component: taskDetail,
      props: true
    },
    {
      path: "/noteAndMark/:taskID",
      name:'noteAndMark',
      component: workerNoteMark,
      props: true
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
    },
    {
      path: "/taskDetailC",
      component: taskDetail
    },
    {
      path: '/index',
      component: firstPart
    }
  ]
});
