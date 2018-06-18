<template>
  <div>
    <div v-if="inTest">
      <tester @not-pass="handleNotPass" @update-pass="handleUpdatePass" mode="register">
      </tester>
    </div>
    <div v-show="!inTest">
      <div class="left-side" v-if="showLeft" :style="leftStyle"></div>
      <div class="login" id="login" :style="dialogStyle">
        <div class="log-bg">
          <div class="log-cloud cloud1"></div>
          <div class="log-cloud cloud2"></div>
          <div class="log-cloud cloud3"></div>
          <div class="log-cloud cloud4"></div>

          <div class="log-logo">众包标注平台</div>
          <div class="log-text">ImageAnnotator</div>
        </div>

        <div class="log-container">

          <login-holder v-if="inLoginNotRegister" v-on:changePart="changeLoginAndRegister">
          </login-holder>
          <register-holder v-if="!inLoginNotRegister" v-on:changePart="changeLoginAndRegister" ref="register"
                           @turn-to-test="handleTurnToTest">
          </register-holder>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import loginHolder from './loginHolder'
  import registerHolder from './registerHolder'
  import Tester from "./test/tester";

  const dialogMinWidth = 500;
  const dialogMinHeight = 580;
  const halfRate = 0.5;

  export default {
    data() {
      return {
        inLoginNotRegister: true,
        dialogStyle: '',
        leftStyle: '',
        inTest: false,
        showLeft: false,
      }
    },

    mounted() {
      if (window.innerWidth > 2 * dialogMinWidth) {
        this.showLeft = true;
        this.dialogStyle = `width:${dialogMinWidth}px;min-width:${dialogMinWidth}px;height:${dialogMinHeight}px;min-height:${dialogMinHeight}px;top:${(window.innerHeight - dialogMinHeight) * halfRate}px;left:${window.innerWidth * halfRate + (window.innerWidth * halfRate - dialogMinWidth) * halfRate}px;`;
        this.leftStyle = `width:${window.innerWidth * halfRate}px;height:${window.innerHeight}px;`
      } else {
        this.showLeft = false;
        this.dialogStyle = `width:${dialogMinWidth}px;min-width:${dialogMinWidth}px;height:${dialogMinHeight}px;min-height:${dialogMinHeight}px;top:${(window.innerHeight - dialogMinHeight) * halfRate}px;left:${(window.innerWidth - dialogMinWidth) * halfRate}px;`;
      }
      window.onresize = () => {
        if (window.innerWidth > 2 * dialogMinWidth) {
          this.showLeft = true;
          this.dialogStyle = `width:${dialogMinWidth}px;min-width:${dialogMinWidth}px;height:${dialogMinHeight}px;min-height:${dialogMinHeight}px;top:${(window.innerHeight - dialogMinHeight) * halfRate}px;left:${window.innerWidth * halfRate + (window.innerWidth * halfRate - dialogMinWidth) * halfRate}px;`;
          this.leftStyle = `width:${window.innerWidth * halfRate}px;height:${window.innerHeight}px;`
        } else {
          this.showLeft = false;
          this.dialogStyle = `width:${dialogMinWidth}px;min-width:${dialogMinWidth}px;height:${dialogMinHeight}px;min-height:${dialogMinHeight}px;top:${(window.innerHeight - dialogMinHeight) * halfRate}px;left:${(window.innerWidth - dialogMinWidth) * halfRate}px;`;
        }
      };
    },

    components: {
      Tester,
      loginHolder,
      registerHolder
    },

    methods: {
      changeLoginAndRegister() {
        this.inLoginNotRegister = !this.inLoginNotRegister;
      },
      handleTurnToTest() {
        this.inTest = true;
      },
      handleNotPass() {
        this.inTest = false;
      },

      handleUpdatePass(rate, num) {
        this.inTest = false;
        let username = this.$refs.register.registerForm.userName;
        let nickname = this.$refs.register.registerForm.nickname;
        let passwr = this.$refs.register.registerForm.pass;
        let roleList = this.$refs.register.getResult();
        let that = this;

        this.$http.post('/user/signUp', {
          username,
          nickname,
          passwr,
          roleList,
          rate,
          num
        })
          .then(function (response) {
            if (response.data.mes === false) {
              that.sendAlert('此用户已存在', '注册错误提示');
            } else {
              that.$message({
                message: '注册成功',
                type: 'success'
              });
            }

          })
          .catch(function (error) {
            that.sendAlert('请检查您的网络连接', '网络错误');
            console.log(error);
          });
      },

      handleRegisterSuccess(){

      },
    }
  }
</script>

<style scoped>

  .login {
    position: fixed;
    overflow: hidden;
    z-index: 10;
    right: 140px;
    background: #fff;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    border-radius: 5px;
    -webkit-box-shadow: 0 3px 16px -5px #070707;
    box-shadow: 0 3px 16px -5px #070707
  }

  /*.login {*/
  /*overflow: hidden;*/
  /*width: 500px;*/
  /*margin: 2% auto;*/
  /*z-index: 10;*/
  /*background: #fff;*/
  /*-webkit-border-radius: 5px;*/
  /*-moz-border-radius: 5px;*/
  /*border-radius: 5px;*/
  /*-webkit-box-shadow: 0 3px 16px -5px #070707;*/
  /*box-shadow: 0 3px 16px -5px #070707*/
  /*}*/

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
    background-size: 100% 100%;
    width: 100%;
    height: 312px;
    overflow: hidden;
  }

  .log-logo {
    height: 80px;
    margin: 120px auto 25px;
    text-align: center;
    color: #222C62;
    font-weight: bold;
    font-size: 40px;
  }

  .log-text {
    color: #22326c;
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

  .left-side {
    position: fixed;
    left: 0;
    top: 0;
    background-color: #344187;
    background-image: repeating-linear-gradient(transparent, transparent 50px, rgba(0, 0, 0, .4) 50px, rgba(0, 0, 0, .4) 53px,
    transparent 53px, transparent 63px, rgba(0, 0, 0, .4) 63px, rgba(0, 0, 0, .4) 66px, transparent 66px, transparent 116px,
    rgba(0, 0, 0, .5) 116px, rgba(0, 0, 0, .5) 166px, rgba(255, 255, 255, .2) 166px, rgba(255, 255, 255, .2) 169px,
    rgba(0, 0, 0, .5) 169px, rgba(0, 0, 0, .5) 179px, rgba(255, 255, 255, .2) 179px, rgba(255, 255, 255, .2) 182px,
    rgba(0, 0, 0, .5) 182px, rgba(0, 0, 0, .5) 232px, transparent 232px),
    repeating-linear-gradient(270deg, transparent, transparent 50px, rgba(0, 0, 0, .4) 50px, rgba(0, 0, 0, .4) 53px,
      transparent 53px, transparent 63px, rgba(0, 0, 0, .4) 63px, rgba(0, 0, 0, .4) 66px, transparent 66px, transparent 116px,
      rgba(0, 0, 0, .5) 116px, rgba(0, 0, 0, .5) 166px, rgba(255, 255, 255, .2) 166px, rgba(255, 255, 255, .2) 169px,
      rgba(0, 0, 0, .5) 169px, rgba(0, 0, 0, .5) 179px, rgba(255, 255, 255, .2) 179px, rgba(255, 255, 255, .2) 182px,
      rgba(0, 0, 0, .5) 182px, rgba(0, 0, 0, .5) 232px, transparent 232px),
    repeating-linear-gradient(125deg, transparent, transparent 2px, rgba(0, 0, 0, .2) 2px, rgba(0, 0, 0, .2) 3px,
      transparent 3px, transparent 5px, rgba(0, 0, 0, .2) 5px);
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
