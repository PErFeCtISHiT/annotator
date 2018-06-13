import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter);
import requesterTasks from '../components/requesterTasks'
import distributeNewTask from '../components/distributeNewTask'
import testDraw from '../components/testDraw'
import admin from '../components/admin'
import recharge from '../components/recharge'
import workerAcceptTasks from '../components/worker/workerAcceptTasks'
import workerOnDoing from '../components/worker/workerOnDoing'
import workerHistRank from '../components/worker/workerHistRank'
import workerNoteMark from '../components/worker/workerNoteMark'
import taskDetail from '../components/taskDetail'
import firstPart from '../components/firstPart'
import requesterWannaSee from '../components/requestorWannaSee'
import requestorTotal from '../components/requestorTotal'

export default new VueRouter({
  routes: [
    /**
     * 前三个是一样的*/
    {
      path: "/",
      component: firstPart
    },
    {
      path: "/0",
      component: firstPart
    },
    {
      path: '/index',
      component: firstPart
    },

    /**
     * 菜单项*/
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
      component: workerAcceptTasks
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
      path: "/3-1",
      component: recharge
    },

    {
      path: "/requesterLike/:taskID/:workerName",
      name: 'forTest',
      component: requesterWannaSee,
      props: true
    },

    {
      path: "/requesterLike/:taskID",
      name: 'forReTotal',
      component: requestorTotal,
      props: true
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
      path: '/admin',
      name: 'admin',
      component: admin
    },

    {
      path: '/testDraw',
      component: testDraw
    },

  ]
});
