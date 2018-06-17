<template>
  <el-row>
    <el-col :span="22">
      <el-menu
        :default-active="$route.path" class="el-menu-demo" mode="horizontal"
        router text-color="#fff" background-color="#222C62" active-text-color="#ffd04b"
        ref="naviBar">

        <el-menu-item index="/0">首页</el-menu-item>

        <el-menu-item v-if="$store.state.user.isAdmin" index="/admin">系统信息</el-menu-item>

        <el-submenu index="1" :disabled="!$store.state.user.isRequester" v-if="!$store.state.user.isAdmin">
          <template slot="title">发布者</template>
          <el-menu-item index="/1-1">我发布的任务</el-menu-item>
          <el-menu-item index="/1-2">发布新任务</el-menu-item>
        </el-submenu>

        <el-submenu index="2" :disabled="!$store.state.user.isWorker" v-if="!$store.state.user.isAdmin">
          <template slot="title">工人</template>
          <el-menu-item index="/2-1">开始标注</el-menu-item>
          <el-menu-item index="/2-2">积分历史与排名</el-menu-item>
        </el-submenu>

        <el-submenu index="3" v-if="!$store.state.user.isAdmin">
          <template slot="title">
            <i class="el-icon-plus"></i>
            <span>{{ $store.state.user.userInfo.points }}</span>
          </template>
          <el-menu-item index="/3-1">充值</el-menu-item>
          <el-menu-item index="/3-2">提现</el-menu-item>

        </el-submenu>
        <el-menu-item index="/4">个人信息</el-menu-item>
      </el-menu>

    </el-col>

    <el-col :span="2">
      <el-row type="flex" justify="center" align="middle" style="height: 100%; width: 100%">
        <el-button class="navigation-button" @click="handleLogOut">登出</el-button>
      </el-row>
    </el-col>
  </el-row>
</template>

<script>
  import {mapActions} from 'vuex'

  export default {
    name: "navigation",

    // mounted(){
    //   console.log(this.$store.state.user.isRequester);
    //   console.log(this.$store.state.user.isWorker);
    // },

    data() {
      return {}
    },

    computed: {
      amount() {
        return this.$store.state.user.userInfo.points;
      }
    },

    methods: {
      ...mapActions(['logOut']),

      handleLogOut() {
        if (this.$store.state.user.userInfo.username === 'admin') {
          this.$router.push('/');
          this.$message({       //回调函数要用that
            message: '登出成功',
            type: 'success'
          });
          this.logOut();
        } else {
          let that = this;
          this.$http.get('/user/logout')
            .then(function (response) {
              //console.log(response);
              that.$router.push('/');
              that.$message({       //回调函数要用that
                message: '登出成功',
                type: 'success'
              });
              that.logOut();
            })
            .catch(function (error) {
              console.log(error);
              that.$message({
                message: '登出失败，请检查网络连接',
                type: 'error'
              });
            });
        }
      },

      checkContains(arr, obj) {
        if (!arr.length) {
          return false;
        }
        for (let i = 0; i < arr.length; i++) {
          if (arr[i] === obj) {
            return true;
          }
        }
        return false;
      }
    },

  }
</script>

<style scoped>

  .navigation-button {
    background-color: #22326c;
    color: aliceblue;
    vertical-align: center;
    margin-top: 10px;
  }

  .el-menu-demo {
    border-bottom-width: 0;
  }
</style>
