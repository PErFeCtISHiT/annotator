import Vue from 'vue'
// sessionStorage会话存储
export default {
  // 必须在mutations中改变state的值，一般调用方法为$store.commit('methodName')

  // JSON.parse方法将一个字符串解析成一个JSON对象
  state: {
    //userInfo的属性：level":[1, 2, 3],"name":"somnus","username":"somnus","points":0
    isAdmin: localStorage.getItem('isAdmin') ? JSON.parse(localStorage.getItem('isAdmin')).isAdmin : false,
    isRequester: localStorage.getItem('isRequester')? JSON.parse(localStorage.getItem('isRequester')).isRequester : false,
    isWorker: localStorage.getItem('isWorker')? JSON.parse(localStorage.getItem('isWorker')).isWorker : false,
    // isRequester: false,
    // isWorker: false,
    userInfo: JSON.parse(localStorage.getItem('user')) || {},
    loginState: Boolean(JSON.parse(localStorage.getItem('user')))        // 有就是真的，没有就是假的
  },

  mutations: {
    logIn: function (state, user) {
      localStorage.setItem('user', JSON.stringify(user));
      state.loginState = true;
      Object.assign(state.userInfo, user);

      function checkContains(arr, obj) {
        if (!arr.length) {
          return false;
        }
        for (let i = 0; i < arr.length; i++) {
          if (arr[i] === obj) {
            return true;
          }
        }
        return false;
      }

      // console.log(state.userInfo.roleList);

      if (state.userInfo.roleList) {
        state.isRequester = checkContains(state.userInfo.roleList, 3);
        localStorage.setItem('isRequester', JSON.stringify({isRequester: state.isRequester}));
        state.isWorker = checkContains(state.userInfo.roleList, 4);
        localStorage.setItem('isWorker', JSON.stringify({isWorker:state.isWorker}));
        state.isAdmin = checkContains(state.userInfo.roleList, 2);
        localStorage.setItem('isAdmin', JSON.stringify({isAdmin: state.isAdmin}));
        // console.log(localStorage.getItem('isRequester'));
        // console.log(localStorage.getItem('isWorker'));
      }
    },
    logOut: function (state) {
      localStorage.removeItem('user');
      localStorage.setItem('isRequester', JSON.stringify({isRequester: false}));
      localStorage.setItem('isWorker', JSON.stringify({isWorker:false}));
      localStorage.setItem('isAdmin', JSON.stringify({isAdmin:false}));
      state.loginState = false;
      state.isWorker = false;
      state.isRequester = false;
      state.isAdmin = false;
      Object.keys(state.userInfo).forEach(k => Vue.delete(state.userInfo, k));
    },
  },

  actions: {
    logIn({commit}, user) {
      commit('logIn', user)
    },
    logOut({commit}) {
      commit('logOut')
    },
    updateWithoutPointer({commit}) {
      window.myHttp.get('/user/getCurrentUser')
        .then(function (response) {
          let result = response.data;
          commit('logIn', result);
          console.log(result.roleList)
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
