<template>
  <div id="app">
    <home-page v-if = "isLogined"></home-page>
    <incoming-main v-if = "!isLogined"></incoming-main>
  </div>
</template>

<script>
  import registerHolder from './components/registerHolder.vue';
  import incomingMain from './components/incomingMain.vue';
  import homePage from './components/homePage.vue';

  export default {
    name: 'app',
    data() {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },

    components: {
      registerHolder,
      incomingMain,
      homePage
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
