<template>
  <div id="navigation-bar">

    <el-menu
      :default-active="$route.path" class="el-menu-demo" mode="horizontal"
      router text-color="#fff" background-color="#222C62" active-text-color="#ffd04b"
    >

      <el-submenu index="1">
        <template slot="title">发布者</template>
        <el-menu-item index="1-1">我发布的任务</el-menu-item>
        <el-menu-item index="1-2">发布新任务</el-menu-item>
      </el-submenu>

      <el-submenu index="2" disabled>
        <template slot="title">工人</template>
        <el-menu-item index="2-1">我发布的任务</el-menu-item>
        <el-menu-item index="2-2">发布新任务</el-menu-item>
      </el-submenu>

      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-plus"></i>
          <span>{{ amount }}</span>
        </template>
        <el-menu-item index="3-1">充值</el-menu-item>
        <el-menu-item index="3-2">提现</el-menu-item>

      </el-submenu>
      <el-menu-item index="4">个人信息</el-menu-item>
      &nbsp;
      <el-button class="navigation-button" @click="logOut">登出</el-button>
    </el-menu>

  </div>
</template>

<script>
  import {mapActions} from 'vuex'

  export default {
    name: "navigation",

    data () {
      return {

      }
    },
    methods: {
      ...mapActions(['logOut'])
    },
    computed: {
      amount: function () {
        return this.$store.state.user.userInfo.points;
      }
    }

  }
</script>

<style scoped>
  #navigation-bar {
    alignment: right;
  }

  .navigation-button {
    background-color: #22326c;
    color: aliceblue;
    vertical-align: center;
    padding-top: 10px;
    padding-bottom: 10px;
    margin-top: 12px;
  }

  .el-menu-demo {
    padding-right: 20px;
    /*background-color: #222C62;*/
    /*text-color: #fff;*/
    /*active-text-color: #ffd04b;*/
    border-bottom-width: 0px;
  }
</style>
