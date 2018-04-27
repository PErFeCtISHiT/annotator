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
      let result = isNaN(inputID)?inputID:parseInt(inputID);
      state.currentTaskID = result;
    },
    updateTaskInfo: function (state,inputInfo) {
      state.currentTaskInfo = inputInfo;
    },
    updateCurrentImageURL:function (state,inputURL) {
      state.currentImageURL = inputURL;
    },
    updateCurrentSponsor:function (state,inputSponsor) {
      state.updateCurrentSponsor = inputSponsor;
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
