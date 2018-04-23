<template>
  <div id="navigationbar">
    <el-menu
      :default-active="$route.path" class="el-menu-demo" mode="horizontal" router
      text-color="#fff" active-text-color="#ffd04b">


      <el-menu-item index="1">我的任务</el-menu-item>
      <el-menu-item index="2">发布新任务</el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-plus"></i>
      </el-menu-item>
      <el-submenu index="2">
        <template slot="title">我的工作台</template>
        <el-menu-item index="2-1">选项1</el-menu-item>
        <el-menu-item index="2-2">选项2</el-menu-item>
        <el-menu-item index="2-3">选项3</el-menu-item>
        <el-submenu index="2-4">
          <template slot="title">选项4</template>
          <el-menu-item index="2-4-1">选项1</el-menu-item>
          <el-menu-item index="2-4-2">选项2</el-menu-item>
          <el-menu-item index="2-4-3">选项3</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-button @click="logOut">登出</el-button>


    </el-menu>

  </div>
</template>

<script>
  import first from './firstcomponent'
  import second from './secondcomponent'
  import {mapActions} from 'vuex'
    export default {
        name: "navigation",
        components: {
          first,
          second
       },
       methods:{
         ...mapActions(['logOut'])
       }

    }
</script>

<style scoped>

</style>
