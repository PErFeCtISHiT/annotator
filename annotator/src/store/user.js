import Vue from 'vue'
// sessionStorage会话存储
export default {
  // 必须在mutations中改变state的值，一般调用方法为$store.commit('methodName')

  // JSON.parse方法将一个字符串解析成一个JSON对象
  state: {
    //userInfo的属性：level":1,"name":"somnus","username":"somnus","points":0

    userInfo: JSON.parse(localStorage.getItem('user')) || {},
    loginState: Boolean(JSON.parse(localStorage.getItem('user')))        // 有就是真的，没有就是假的
  },

  mutations: {
    logIn: function (state, user) {
      localStorage.setItem('user', JSON.stringify(user));
      state.loginState = true;
      Object.assign(state.userInfo, user)
    },
    logOut: function (state) {
      localStorage.removeItem('user');
      state.loginState = false;
      Object.keys(state.userInfo).forEach(k => Vue.delete(state.userInfo, k))
    },
  },

  actions: {
    logIn ({commit}, user) {
      commit('logIn', user)
    },
    logOut ({commit}) {
      commit('logOut')
    },
    updateWithoutPointer({commit}){
      window.myHttp.get('/user/getCurrentUser')
        .then(function (response) {
          let result = response.data;
          commit('logIn', result);
          window.myMessage({
            message: '刷新用户数据成功',
            type: 'success'
          });
        })
        .catch(function (error) {
          window.myMessage({
            message: '刷新用户数据失败',
            type: 'error'
          });
          console.log(error);
        });
    }
  }
}
