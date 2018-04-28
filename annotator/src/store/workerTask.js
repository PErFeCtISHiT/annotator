export default {
  state:{
    doingList:[],
    currentTaskID:0,
    currentImageURL:"",
    currentSponsor:"",
    currentTaskInfo:{},
  },

  mutations:{
    updateDoingList: function (state,inputList) {
      state.doingList = inputList;
    },
    updateCurrentTaskID: function (state,inputID) {
      state.currentTaskID = isNaN(inputID)?inputID:parseInt(inputID);
    },
    updateTaskInfo: function (state,inputInfo) {
      state.currentTaskInfo = inputInfo;
    },
    updateCurrentImageURL:function (state,inputURL) {
      state.currentImageURL = inputURL;
    },
    updateCurrentSponsor:function (state,inputSponsor) {
      state.currentSponsor = inputSponsor;
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
