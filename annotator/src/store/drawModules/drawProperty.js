export default {
  state: {
    canvasID:'',
    strokeWidth: 0,
    strokeNormalColor:'',
    strokeFocusColor:'',
  },

  getters:{
    canvasTarget: state =>{
      return $('#'+state.canvasID);
    }
  },

  mutations: {

  },

  actions: {

  }

}
