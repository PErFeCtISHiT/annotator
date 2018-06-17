<template>
  <div id="registerHolder">
    <el-form :model="registerForm" status-icon :rules="rules2" ref="registerForm" class="demo-ruleForm">

      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item prop="userName">
            <el-input prefix-icon="el-icon-goods" v-model="registerForm.userName"
                      placeholder="请输入用户名"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item prop="name">
            <el-input prefix-icon="el-icon-goods" v-model="registerForm.name" placeholder="请输入昵称"></el-input>
          </el-form-item>
        </el-col>

      </el-row>

      <el-form-item prop="pass">
        <el-input prefix-icon="el-icon-view" type="password" :placeholder="'请输入密码('+minPass+'-'+maxPass+'位字母或数字)'"
                  v-model="registerForm.pass"></el-input>
      </el-form-item>

      <el-form-item prop="checkPass">
        <el-input prefix-icon="el-icon-view" type="password" v-model="registerForm.checkPass"
                  placeholder="请再次输入密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-col :span="9">
          <register-check-box ref="checkBox"></register-check-box>
        </el-col>
        <el-col :span="5">
          <el-button type="primary" @click="submitForm('registerForm')">注册</el-button>
        </el-col>
        <el-col :span="5">
          <el-button @click="resetForm('registerForm')">重置</el-button>
        </el-col>
        <el-col :span="5">
          <el-button type="text" @click="changePage">已有账号?登录</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import registerCheckBox from './registerCheckBox';
  //注册用组件？？
  export default {
    data() {

      var minPass = 8;
      var maxPass = 16;
      var passReg = new RegExp('\\w{' + minPass + ',' + maxPass + '}');//验证码的正则表达式

      var checkUserName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('用户名不能为空'));
        } else {
          //这里和ogIn几乎一样，就是then不一样
          this.$http.get('user/findUser', {
            params: {
              username: value
            }
          })
            .then(function (response) {
              if (response.data.existed) {
                // console.log(response.data.existed);
                callback(new Error('该用户已存在'));
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


      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else if (!this.passReg.test(value)) {
          return callback(new Error('密码格式不对'));
        } else {
          if (this.registerForm.checkPass !== '') {
            this.$refs.registerForm.validateField('checkPass');
          }
          callback();
        }
      };


      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };

      var checkName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入您的昵称'));
        } else {
          callback();
        }
      };

      //真正导出的data
      return {
        minPass,
        maxPass,
        passReg,
        registerForm: {
          pass: '',
          checkPass: '',
          userName: '',
          nickname: ''
        },
        rules2: {
          pass: [
            {validator: validatePass, trigger: 'blur'}
          ],
          checkPass: [
            {validator: validatePass2, trigger: 'blur'}
          ],
          userName: [
            {validator: checkUserName, trigger: 'blur'}
          ],
          nickname: [
            {validator: checkName, trigger: 'blur'}
          ],
        }
      };
    },
    methods: {
      submitForm(formName) {
        var that = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$http.post('/user/signUp', {
              username: this.registerForm.userName,
              nickname: this.registerForm.nickname,
              passwr: this.registerForm.pass,
              roleList: this.$refs.checkBox.getResult(),
            })
              .then(function (response) {
                // console.log(that.$refs.checkBox.getResult());
                // console.log({
                //   username: that.registerForm.userName,
                //   name: that.registerForm.name,
                //   password: that.registerForm.pass,
                //   roleList: that.$refs.checkBox.getResult(),
                // });
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
                // console.log(that.$refs.checkBox.getResult());
                // console.log({
                //   username: that.registerForm.userName,
                //   name: that.registerForm.name,
                //   password: that.registerForm.pass,
                //   roleList: that.$refs.checkBox.getResult(),
                // });
                that.sendAlert('请检查您的网络连接', '网络错误');
                console.log(error);
              });
          } else {
            this.sendAlert('您填写的内容不符合要求', '注册错误提示');
            return false;
          }
        });
      },
      sendAlert(msg, title) {
        this.$alert(msg, title, {
          confirmButtonText: '确定',
          callback: () => {
            this.$message({
              type: 'info',
              message: '已回到注册页面'
            });
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      changePage() {
        this.$emit('changePart');
      }
    },
    components: {
      registerCheckBox
    }
  }
</script>

<style scoped>
  #registerHolder {
    width: 450px;
    margin: 0 auto;
  }
</style>
