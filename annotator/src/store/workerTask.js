export default {
  state:{
    doingList:[],
    currentTaskID:0,
    currentTaskInfo:{},
  },

  mutations:{
    updateDoingList: function (state,inputList) {
      state.doingList = inputList;
    },
    updateCurrentTaskID: function (state,inputID) {
      state.currentTaskID = inputID;
    },
    updateTaskInfo: function (state,inputInfo) {
      state.currentTaskInfo = inputInfo;
    }
  },

  actions:{
    updateDoingList({commit}){
      commit('updateDoingList');
    },
    updateCurrentTaskID({commit}){
      commit('updateCurrentTaskID');
    },
    updateTaskInfo({commit}){
      commit('updateTaskInfo');
    }
  }
}
