<template>
  <div>
    <el-container>
      <!-- navigation bar  -->
      <el-header style="position: fixed; top: 0; left:0; right: 0; z-index:50; padding-left: 0; padding-right: 0"
                 height="62px">
        <el-row class="navigation-background">
          <el-col :span="1" style="margin-top: 2px; margin-left: 15px">
            <img src="../images/logo.png" style="width: 55px; height: 55px;">
          </el-col>

          <el-col :span="3">
            <div id="navigation-div1" class="grid-content bg-purple">
              <span id="navigation-name">ImgAnnotator</span>
            </div>
          </el-col>

          <el-col :span="19">
            <!-- 这里放一个靠右边的栅格 -->
            <el-row type="flex" id="navigation-div2" justify="end">
              <div id="navigation-div2-1">
                <navigation>
                </navigation>
              </div>
            </el-row>

          </el-col>
        </el-row>
      </el-header>
      <!-- Main page -->
      <el-main style="margin-top: 62px;">

        <router-view>
        </router-view>

      </el-main>

      <el-footer>
        <el-col :span="24" style="text-align: center">
          copyright © ️2018 www.joker66.cn Joker 版权所有
        </el-col>
      </el-footer>
    </el-container>


    <router-link :to="'/testDraw'">我是测试画图页面</router-link>
  </div>
</template>

<script>
  import {mapActions} from 'vuex'
  import navigation from './navigation'


  export default {
    data() {
      return {}
    },
    components: {
      navigation,
    }

  }
</script>

<style scoped>

  /*<!-- 用来修改ImgAnnotator几个字 -->*/
  #navigation-name {;
  }

  /*上面的span在这个div里面*/
  #navigation-div1 {
    color: azure;
    margin-top: 18px;
    margin-left: 15px;
  }

  #navigation-div2 {

  }

  #navigation-div2-1 {
    float: right;
  }

  #main-div1 {;
    padding-left: 20%;
  }

  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .navigation-background {
    background-color: rgba(34, 44, 98, 1);
    width: 100%;
  }
</style>
