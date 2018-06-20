
export default {
  // 必须在mutations中改变state的值，一般调用方法为$store.commit('methodName')
  // JSON.parse方法将一个字符串解析成一个JSON对象

  state: {
    currentNum:localStorage.getItem('currentNum'),
  },

  mutations: {
    writeNum: function (state,num) {
      localStorage.setItem('currentNum', num);
      state.currentNum = num;
    }
  },

  actions: {
    writeNum({commit}, num) {
      commit('writeNum', num)
    },
  }

}
