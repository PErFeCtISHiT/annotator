import Vue from 'vue'
import App from './App.vue'

//引入element-ui
import ElementUI from 'element-ui'

//引入element-ui所需的CSS
import 'element-ui/lib/theme-chalk/index.css'

// 引入vuex配置文件
import store from './store/index_store'

// 引入ajax框架axios配置
import axios from './axios/index_axios'

// 引入router配置文件
import router from './router/index_router'

//使用element-ui组件
Vue.use(ElementUI);
//挂载axios到vue的原型上，方便使用
Vue.prototype.$http = axios;

// 设置 Vue.config.productionTip = false 来关闭生产模式下给出的提示
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  el: '#app',
  render: h => h(App)
})
