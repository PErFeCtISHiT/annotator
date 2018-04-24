<template>
  <div>
    <el-container>
      <!-- navigation bar  -->
      <el-header>
        <el-row class="navigation-background">
          <el-col :span="4" >
            <div id="navigation-div1" class="grid-content bg-purple">
              <span id="navigation-name">ImgAnnotator</span>
            </div>
          </el-col>

          <el-col :span="20">
            <div>

              <!-- 这里放一个靠右边的栅格 -->
              <el-row type="flex" id="navigation-div2" justify="end">
                <el-col :span="18"><div id="navigation-div2-1"><navigation></navigation></div></el-col>
              </el-row>

            </div>
          </el-col>
        </el-row>
      </el-header>

      <!-- Main page -->
      <el-main>

        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="18"><div id="main-div1">
            <router-view></router-view>
          </div></el-col>
        </el-row>

      </el-main>

      <el-footer>Footer</el-footer>
    </el-container>


    <router-link :to="'/testDraw'">我是测试画图页面</router-link>

  </div>
</template>

<script>
  import {mapActions} from 'vuex'
  import navigation from './navigation'


  export default {
      data() {
          return {

          }
      },
      components: {
          navigation,
      }

  }
</script>

<style scoped>
  el-header, el-footer {

    text-align: center;
    line-height: 60px;
  }

  el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }


  <!-- 用来修改ImgAnnotator几个字 -->
  #navigation-name {
    ;
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
  }
</style>
