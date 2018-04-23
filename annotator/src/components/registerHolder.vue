<template>
  <div id="registerHolder">
    <el-form :model="registerForm" status-icon :rules="rules2" ref="registerForm" class="demo-ruleForm">
      <el-form-item prop="age">
        <el-input prefix-icon="el-icon-goods" v-model.number="registerForm.userName" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item prop="pass">
        <el-input prefix-icon="el-icon-goods" type="password" placeholder="请输入密码" v-model="registerForm.pass"></el-input>
      </el-form-item>

      <el-form-item prop="checkPass">
        <el-input prefix-icon="el-icon-view" type="password" v-model="registerForm.checkPass" placeholder="请再次输入密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-col :span="8">
          <el-button type="primary" @click="submitForm('registerForm')">注册</el-button>
        </el-col>
        <el-col :span="8">
          <el-button @click="resetForm('registerForm')">重置</el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="text" @click="changePage">已有账号?登录</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    data() {


      var checkUserName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('年龄不能为空'));
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


      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
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

      //真正导出的data
      return {
        registerForm: {
          pass: '',
          checkPass: '',
          userName: ''
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
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.log('error submit!!');
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
  #registerHolder {
    width: 450px;
    margin: 0 auto;
  }
</style>
