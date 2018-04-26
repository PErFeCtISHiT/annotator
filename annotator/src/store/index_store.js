//这是数据仓库的入口

import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

import user from './user'
import workerTask from './workerTask'
import requestTask from './requester'

export default new Vuex.Store({
  modules: {
    user,
    workerTask,
    requestTask
  }
});
