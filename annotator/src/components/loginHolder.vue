<template>
  <div id="loginHolder">

    <el-form :model="loginForm" status-icon :rules="rules2" ref="loginForm" class="demo-ruleForm">

      <el-form-item prop="username">
        <el-input prefix-icon="el-icon-goods" placeholder="请输入账号" v-model="loginForm.username">
        </el-input>
      </el-form-item>

      <el-form-item prop="passwr">
        <el-input prefix-icon="el-icon-view" type="password" placeholder="请输入密码"
                  v-model="loginForm.passwr">
        </el-input>
      </el-form-item>

      <el-form-item prop="verification">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-input prefix-icon="el-icon-edit-outline" placeholder="请输入验证码（不区分大小写）"
                      v-model="loginForm.verification">
            </el-input>
          </el-col>
          <el-col :span="8">
            <el-button type="info" plain @click="changeVer">{{ver}}</el-button>
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item>
        <el-col :span="8">
          <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
        </el-col>
        <el-col :span="8">
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="text" @click="changePage">没有账号?注册</el-button>
        </el-col>
      </el-form-item>

    </el-form>

  </div>
</template>

<script>

  import {mapActions} from 'vuex'

  //登录用组件？？
  export default {

    mounted() {
      this.ver = this.getVerCode();    //根据生命周期模型，挂载的时候被渲染
    },

    data() {

      let ver = "";
      let verNum = 6;
      let verReg = new RegExp('\\w{' + verNum + '}');//验证码的正则表达式

      let validateUsername = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('用户名不能为空'));
        }
        else if (value === 'admin') {              //这里以后要注释掉
          callback();
        }
        else {

          this.$http.get('user/findUser', {
            params: {
              username: value
            }
          })
            .then(function (response) {
              if (!response.data.existed) {
                // console.log(response.data.existed);
                callback(new Error('该用户不存在'));
              } else {
                callback();
              }
            })
            .catch(function (error) {
              console.log(error);
              setTimeout(() => callback(new Error('网路连接不畅')), 1000);  //setTimeOut传递的是函数
            });
        }
      };


      let validatePassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('密码不能为空'));
        } else {
          callback();
        }
      };


      let validateVerification = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('验证码不能为空'));
        } else if (!this.verReg.test(value)) {
          return callback(new Error('验证码格式不对'));
        } else if (this.ver.toUpperCase() !== value.toUpperCase()) {
          return callback(new Error('输入了错误的验证码'));
        } else {
          callback();
        }
      };

      return {
        ver: ver,       //验证码
        verNum: verNum,    //验证码长度
        verReg: verReg, //验证码的正则表达式
        // verReg: /\w{6}/, //验证码的正则表达式
        loginForm: {
          username: '',
          passwr: '',
          verification: ''
        },
        rules2: {
          username: [
            {validator: validateUsername, trigger: 'blur'}
          ],
          passwr: [
            {validator: validatePassword, trigger: 'blur'}
          ],
          verification: [
            {validator: validateVerification, trigger: 'blur'}
          ]
        }
      };
    },


    methods: {

      ...mapActions(['logIn']),

      submitForm(formName) {
        let that = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.loginForm.username === "admin") {
              this.logIn({level: 1, name: "", username: "admin", points: 1000, roleList: [3, 4]});
              that.$message({
                message: '登录成功',
                type: 'success',
                duration: 1000
              });
            }
            else {
              // console.log({
              //   username: that.loginForm.username,
              //   password: that.loginForm.password
              // });

              that.$http.post('/user/login', {
                username: that.loginForm.username,
                passwr: that.loginForm.passwr
              })
                .then(function (response) {
                  // console.log('有回传信息');
                  // console.log(response.data.mes);
                  if (response.data.mes === "UnknownAccount") {
                    that.sendAlert('此用户不存在', '登录错误提示');
                  } else if (response.data.mes === "IncorrectCredentials") {
                    that.sendAlert('密码错误', '登录错误提示');
                  } else if (response.data.mes === "success") {        //为何success就是开头小写
                    // console.log('准备拿取登入用户的信息');
                    that.actualLogin();
                  }

                })
                .catch(function (error) {
                  that.sendAlert('请检查您的网络连接', '网络错误');
                  console.log(error);
                });
            }
          } else {
            this.sendAlert('您填写的内容不符合要求', '登录错误提示');
            return false;
          }
        });
      },
      actualLogin() {
        // console.log("拿取当前登录的用户信息");
        let that = this;
        this.$http.get('/user/getCurrentUser')
          .then(function (response) {
            let result = response.data;
            // result['password'] = that.loginForm.password;
            console.log(result.roleList)
            that.logIn(result);
            that.$message({
              message: '登录成功',
              type: 'success',
              duration: 500
            });
            that.$router.push('/0');
          })
          .catch(function (error) {
            that.sendAlert('请检查您的网络连接', '网络错误');
            console.log(error);
          });
      },
      sendAlert(msg, title) {
        this.$alert(msg, title, {
          confirmButtonText: '确定',
          callback: () => {
            this.$message({
              type: 'info',
              message: '已回到登录页面',
              duration: 1000
            });
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      changePage() {
        this.$emit('changePart');
      },
      changeVer() {
        this.ver = this.getVerCode();
      },
      getVerCode() {
        let result = "";
        for (let i = 0; i < this.verNum; i++) {
          result += this.getRandomChar();
        }
        return result;
      },
      getRandomChar() {
        let va = this.getRandomIntInclusive(65, 90);
        let vb = this.getRandomIntInclusive(97, 122);
        let vc = this.getRandomIntInclusive(48, 57);

        let temp = [va, vb, vc];
        return String.fromCharCode(temp[this.getRandomIntInclusive(0, 2)]);
      },
      getRandomIntInclusive(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min + 1)) + min; //The maximum is inclusive and the minimum is inclusive
      }
    }
  }
</script>

<style scoped>
  #loginHolder {
    width: 450px;
    margin: 0 auto;
    /*下面这行用来把表单位置看得更清楚*/
    /*border: 5px solid black;*/
  }
</style>
