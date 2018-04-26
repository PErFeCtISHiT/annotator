export default {
  state:{
    doingList:[],
    currentTaskID:0,
    currentImageURL:"",
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
    },
    updateCurrentImageURL:function (state,inputURL) {
      state.currentImageURL = inputURL
    }
  },

  actions:{
    updateDoingListAjax({commit}){
      commit('updateDoingList');
    },
    updateCurrentTaskIDAjax({commit}){
      commit('updateCurrentTaskID');
    },
    updateTaskInfoAjax({commit}){
      commit('updateTaskInfo');
    },
    updateCurrentImageURL({commit}){
      commit('updateCurrentImageURL');
    }
  }
}
