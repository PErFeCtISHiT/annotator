<template>
  <div id="app">
    <home-page v-if = "isLogined"></home-page>
    <incoming-main v-if = "!isLogined"></incoming-main>
  </div>
</template>

<script>
  import incomingMain from './components/incomingMain.vue';
  import homePage from './components/homePage.vue';

  export default {
    created(){
      window.myVueStore = this.$store;
      window.myHttp = this.$http;
      window.myMessage = this.$message;
      window.myMessageBox = this.$msgbox;
      window.myAlert = this.$alert;
      window.myConfirm = this.$confirm;
      window.myPrompt = this.$prompt;
    },

    name: 'app',
    data() {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },

    components: {
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
