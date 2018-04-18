<template>


  <div class="login" id="login">
    <a class="log-close"><i class="icons close"></i></a>
    <div class="log-bg">
      <div class="log-cloud cloud1"></div>
      <div class="log-cloud cloud2"></div>
      <div class="log-cloud cloud3"></div>
      <div class="log-cloud cloud4"></div>

      <div class="log-logo">Welcome!</div>
      <div class="log-text">ImageAnnotator</div>
    </div>

    <div class="log-container">

      <!--<input type="text" placeholder="Email" :class="'log-input' + (account==''?' log-input-empty':'')"-->
      <!--v-model="account">-->
      <!--<input type="password" placeholder="Password" :class="'log-input' + (password==''?' log-input-empty':'')"-->
      <!--v-model="password">-->
      <!--<a class="log-btn" @click="login">Login</a>-->

      <login-holder v-if = "inLoginNotRegister" v-on:changePart="changeLoginAndRegister"></login-holder>
      <register-holder v-if = "!inLoginNotRegister" v-on:changePart="changeLoginAndRegister"></register-holder>

    </div>


  </div>
</template>

<script>
  import loginHolder from './loginHolder'
  import registerHolder from './registerHolder'

  export default {
    data(){
      return {
        inLoginNotRegister: true
      }
    },

    components: {
      loginHolder,
      registerHolder
    },

    methods:{
      changeLoginAndRegister(){
        this.inLoginNotRegister = !this.inLoginNotRegister;
      }
    }
  }
</script>

<style scoped>

  .login {
    position: fixed;
    overflow: hidden;
    left: 50%;
    margin-left: -250px;
    top: 55%;
    margin-top: -350px;
    width: 500px;
    min-height: 555px;
    z-index: 10;
    right: 140px;
    background: #fff;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    border-radius: 5px;
    -webkit-box-shadow: 0px 3px 16px -5px #070707;
    box-shadow: 0px 3px 16px -5px #070707
  }

  .log-close {
    display: block;
    position: absolute;
    top: 12px;
    right: 12px;
    opacity: 1;
  }

  .log-close:hover .icons {
    transform: rotate(180deg);
  }

  .log-close .icons {
    opacity: 1;
    transition: all .3s
  }

  .log-cloud {
    background-image: url(../images/login-cloud.png);
    width: 63px;
    height: 40px;
    position: absolute;
    z-index: 1
  }

  .login .cloud1 {
    top: 21px;
    left: -30px;
    transform: scale(.6);
    animation: cloud1 20s linear infinite;
  }

  .login .cloud2 {
    top: 87px;
    right: 20px;
    animation: cloud2 19s linear infinite;
  }

  .login .cloud3 {
    top: 160px;
    left: 5px;
    transform: scale(.8);
    animation: cloud3 21s linear infinite;
  }

  .login .cloud4 {
    top: 150px;
    left: -40px;
    transform: scale(.4);
    animation: cloud4 19s linear infinite;
  }

  .log-bg {
    background: url(../images/login-bg.jpg);
    width: 100%;
    height: 312px;
    overflow: hidden;
  }

  .log-logo {
    height: 80px;
    margin: 120px auto 25px;
    text-align: center;
    color: #1fcab3;
    font-weight: bold;
    font-size: 40px;
  }

  .log-text {
    color: #57d4c3;
    font-size: 13px;
    text-align: center;
    margin: 0 auto;
  }

  .log-logo, .log-text {
    z-index: 2
  }

  .icons {
    background: url(../images/icons.png) no-repeat;
    display: inline-block;
  }

  .close {
    height: 16px;
    width: 16px;
    background-position: -13px 0;
  }

  .log-container {
    text-align: center;
    margin-top: 20px;
  }

  @-webkit-keyframes cloud1 {
    0% {
      left: 200px
    }
    100% {
      left: -130px;
    }
  }

  @keyframes cloud1 {
    0% {
      left: 200px
    }
    100% {
      left: -130px;
    }
  }

  @-webkit-keyframes cloud2 {
    0% {
      left: 500px;
    }
    100% {
      left: -90px;
    }
  }

  @keyframes cloud2 {
    0% {
      left: 500px;
    }
    100% {
      left: -90px;
    }
  }

  @-webkit-keyframes cloud3 {
    0% {
      left: 620px;
    }
    100% {
      left: -70px;
    }
  }

  @keyframes cloud3 {
    0% {
      left: 620px;
    }
    100% {
      left: -70px;
    }
  }

  @-webkit-keyframes cloud4 {
    0% {
      left: 100px;
    }
    100% {
      left: -70px;
    }
  }

  @keyframes cloud4 {
    0% {
      left: 100px;
    }
    100% {
      left: -70px;
    }
  }


</style>
