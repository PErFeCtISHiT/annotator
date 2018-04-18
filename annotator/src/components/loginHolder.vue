<template>
  <div id="loginHolder">

    <el-form :model="loginForm" status-icon :rules="rules2" ref="loginForm" class="demo-ruleForm">

      <el-form-item prop="username">
        <el-input prefix-icon="el-icon-goods" placeholder="请输入账号" v-model="loginForm.username"></el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input prefix-icon="el-icon-view" type="password" placeholder="请输入密码" v-model="loginForm.password"></el-input>
      </el-form-item>

      <el-form-item prop="verification">
        <el-input prefix-icon="el-icon-edit-outline" placeholder="请输入验证码" v-model="loginForm.verification"></el-input>
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
  export default {
    data() {


      var validateUsername = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('用户名不能为空'));
        } else {
          callback();
        }
      };


      var validatePassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('密码不能为空'));
        } else {
          callback();
        }
      };


      var validateVerification = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('验证码不能为空'));
        }
        setTimeout(() => {
          if (!Number.isInteger(value)) {
            callback(new Error('请输入数字值'));
          } else {
            if (value < 18) {
              callback(new Error('必须年满18岁'));
            } else {
              callback();
            }
          }
        }, 1000);
      };

      return {
        loginForm: {
          username: '',
          password: '',
          verification: ''
        },
        rules2: {
          username: [
            {validator: validateUsername, trigger: 'blur'}
          ],
          password: [
            {validator: validatePassword, trigger: 'blur'}
          ],
          verification: [
            {validator: validateVerification, trigger: 'blur'}
          ]
        }
      };
    },


    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('登录成功!');
          } else {
            alert('您未按要求填写!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      changePage(){
        this.$emit('changePart');
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
