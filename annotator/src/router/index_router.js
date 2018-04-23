import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter);
import first from '../components/firstcomponent'
import second from '../components/secondcomponent'

export default new VueRouter({
  routes: [
    {
      path: "/1",
      component: first
    },
    {
      path: "/4",
      component: second
    }
  ]
});
