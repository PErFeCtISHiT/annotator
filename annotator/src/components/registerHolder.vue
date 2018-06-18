<template>
  <div id="registerHolder">
    <el-form :model="registerForm" status-icon :rules="rules2" ref="registerForm" class="demo-ruleForm">

      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item prop="userName">
            <el-input prefix-icon="el-icon-goods" v-model="registerForm.userName"
                      placeholder="请输入用户名">
            </el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item prop="name">
            <el-input prefix-icon="el-icon-goods" v-model="registerForm.nickname" placeholder="请输入昵称">
            </el-input>
          </el-form-item>
        </el-col>

      </el-row>

      <el-form-item prop="pass">
        <el-input prefix-icon="el-icon-view" type="password" :placeholder="'请输入密码('+minPass+'-'+maxPass+'位字母或数字)'"
                  v-model="registerForm.pass">
        </el-input>
      </el-form-item>

      <el-form-item prop="checkPass">
        <el-input prefix-icon="el-icon-view" type="password" v-model="registerForm.checkPass"
                  placeholder="请再次输入密码">
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-col :span="9">
          <el-radio-group v-model="radio">
            <el-radio label="发起者">
            </el-radio>
            <el-radio label="工人">
            </el-radio>
          </el-radio-group>
        </el-col>
        <el-col :span="5">
          <el-popover
            placement="top-start"
            :disabled="radio!=='工人'"
            title="提示"
            width="200"
            trigger="hover"
            content="您选择注册成为工人，需要进行正确率测试，达标后方可注册成功；请在确认后再点击此按钮。">
            <el-button type="primary" slot="reference" @click="submitForm('registerForm')">注册</el-button>
          </el-popover>
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
  export default {
    data() {

      let minPass = 8;
      let maxPass = 16;
      let passReg = new RegExp('\\w{' + minPass + ',' + maxPass + '}');//验证码的正则表达式

      let checkUserName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('用户名不能为空'));
        } else {
          //这里和logIn几乎一样，就是then不一样
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


      let validatePass = (rule, value, callback) => {
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


      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };

      let checkName = (rule, value, callback) => {
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
        radio: '发起者',
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
        let that = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {

            if (that.radio === '发起者') {
              this.$http.post('/user/signUp', {
                username: this.registerForm.userName,
                nickname: this.registerForm.nickname,
                passwr: this.registerForm.pass,
                roleList: this.getResult(),
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
            }

            if (that.radio === '工人') {
              this.$confirm('为了注册工人账号，需要进行预测试，是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                that.$emit('turn-to-test');
              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消测试'
                });
              });

              // this.$http.get('/user/findUser', {
              //   params: {
              //     username: this.registerForm.userName,
              //   }
              // })
              //   .then(function (response) {
              //     if (response.data.existed === true) {
              //       that.sendAlert('此用户已存在', '注册错误提示');
              //     } else {
              //       that.$emit('turn-to-test');
              //     }
              //   })
              //   .catch(function (error) {
              //     that.sendAlert('请检查您的网络连接', '网络错误');
              //     console.log(error);
              //   });
            }
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
      },
      getResult: function () {
        return [this.mapNum(this.radio)];
      },
      mapNum: function (str) {
        if (str === '发起者') {
          return 3;
        } else if (str === '工人') {
          return 4;
        }
      }
    },
  }
</script>

<style scoped>
  #registerHolder {
    width: 450px;
    margin: 0 auto;
  }
</style>
