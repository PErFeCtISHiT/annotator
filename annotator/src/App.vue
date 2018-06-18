<template>
  <div id="app">
    <home-page v-if="isLogined"></home-page>
    <incoming-main v-if="!isLogined"></incoming-main>
  </div>
</template>

<script>
  import incomingMain from './components/incomingMain.vue';
  import homePage from './components/homePage.vue';

  export default {
    created() {
      // window.myVueStore = this.$store;
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

    computed: {
      isLogined() {
        return this.$store.state.user.loginState;
      }
    },


  }
</script>

<style>
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  textarea.el-textarea__inner {
    font-family: Microsoft YaHei, serif;
  }

  .el-card.is-hover-shadow:hover {
    -webkit-box-shadow: 0 4px 12px 0 rgba(0, 0, 0, .4);
    box-shadow: 0 4px 12px 0 rgba(0, 0, 0, .4)
  }

  .el-scrollbar {
    overflow: hidden;
    position: relative
  }

  ::-webkit-scrollbar-track-piece {
    -webkit-border-radius: 0;
  }

  ::-webkit-scrollbar {
    position: absolute;
    right: 2px;
    bottom: 2px;
    z-index: 1;
    border-radius: 4px;
    opacity: 0;
    -webkit-transition: opacity 120ms ease-out;
    transition: opacity 120ms ease-out;
  }

  ::-webkit-scrollbar-thumb {
    position: relative;
    display: block;
    width: 0;
    height: 0;
    cursor: pointer;
    border-radius: 4px;
    background-color: rgba(144, 147, 153, .3);
    -webkit-transition: .3s background-color;
    transition: .3s background-color;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: rgba(144, 147, 153, .5);
  }
</style>
