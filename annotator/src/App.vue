<template>
  <div id="app">
    <incoming-main></incoming-main>
  </div>
</template>

<script>
  import registerHolder from './components/registerHolder.vue';
  import incomingMain from './components/incomingMain.vue';


  export default {
    name: 'app',
    data() {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },

    components: {
      registerHolder,
      incomingMain
    },

    computed:{
      isLogined(){
        return this.$store.state.user.loginState;
      }
    },


  }
</script>

<style>
  #app {

  }
</style>
