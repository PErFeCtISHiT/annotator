<template>
  <div>
    <el-container>
      <!-- navigation bar  -->
      <el-header>
        <el-row :gutter="0">
          <el-col :span="4">
            <div class="grid-content bg-purple">
              <span>ImgAnnotator</span>
            </div>
          </el-col>
          <el-col :span="20">
            <div class="grid-content bg-purple">
              <navigation></navigation>
            </div>
          </el-col>
        </el-row>
      </el-header>

      <!-- Main page -->
      <el-main>
        <router-view></router-view>
      </el-main>

      <el-footer>Footer</el-footer>
    </el-container>


    <router-link :to="'/testDraw'">我是测试画图页面</router-link>

  </div>
</template>

<script>
  import {mapActions} from 'vuex'
  import navigation from './navigation'
  import first from './firstcomponent'
  import second from './secondcomponent'

  export default {
      data() {
          return {

          }
      },
      components: {
          navigation,
          first,
          second
      }

  }
</script>

<style scoped>
  el-header, el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }
</style>
