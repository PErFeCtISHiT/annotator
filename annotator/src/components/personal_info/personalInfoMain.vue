<template>
  <div style="width: 100%;" v-bind:class="{height5: h[0], height6: h[1]}">
    <div style="background-image: url(../../../src/images/bgp.jpg); opacity: 0.5;
        position: absolute; z-index: 1; width: 96%;
        box-shadow: 10px 10px 5px #9d9d9d"
        v-bind:class="{height3: h[0], height4: h[1]}"></div>

    <div style="position: absolute; z-index: 2;
        width: 105%; margin-left: -5%; margin-top: 20px;">
      <el-row type="flex" justify="center" class="row-bg">
        <el-col :span="18">
          <div class="grid-content bg-purple">

            <el-tabs type="border-card" v-model="activePane" @tab-click="handleTabEvent"
                     style="margin-top: 30px" v-bind:class="{height1: h[0], height2: h[1]}">
              <el-tab-pane name="information">
                <span slot="label" class="my-tag"><i class="el-icon-date"></i>&nbsp;个人信息修改</span>
                <div class="grid-content bg-purple">
                  <info-detail style="margin-top: 50px" :baseInfoDetail="personalInfo"></info-detail>
                </div>
              </el-tab-pane>


              <el-tab-pane name="statistics">
                <span slot="label" class="my-tag"><i class="el-icon-document"></i>&nbsp;统计数据</span>
                <div class="grid-content bg-purple">
                  <info-statistic></info-statistic>
                </div>
              </el-tab-pane>
            </el-tabs>

          </div>
        </el-col>
      </el-row>

    </div>

  </div>
</template>

<script>
  /*
  * 这里的问题迷得很。
  * 1.背景图片路径必须返回到src级别文件夹才能再拿到
  * 2.div之间互相重叠做背景比较好；而不是设置背景——没法设置opacity啊
  */


  import infoDetail from './infoDetail'
  import infoStatistic from './infoStatistic'


  const statisticMockItem = {
    tagScore: [79.1, 68.4, 32.5, 85.8, 92.6],
    inclination: [15, 7, 40, 28, 10]
  };

  /* nickname, username, points, e-mail 必选
  * level对于工人生效
  */
  const infoMockItem = {
    "lev": 2,
    "nickname": "somnus",
    "username": "somnus",
    "points": 4578,
    "type": "worker",
    "email": "1093797485@qq.com"
  };

  export default {
    components: {
      infoStatistic,
      infoDetail
    },
    name: "personal-info-main",

    component: {
      infoDetail,
      infoStatistic
    },

    //挂载的时候获取数据
    mounted() {
      this.personalInfo = infoMockItem;
      this.personalStatistic = statisticMockItem;
    },

    data() {
      return {
        personalInfo: {},
        personalStatistic: {},

        //为了设置CSS
        activePane: 'information',
        h: [true, false],
      }
    },

    methods: {
      handleTabEvent(tab) {
        console.log(tab);
        //
        // this.h1 ? this.h1 = false : this.h1 = true;
        // this.h2 ? this.h2 = false : this.h2 = true;
        // this.h3 ? this.h3 = false : this.h3 = true;
        // this.h4 ? this.h4 = false : this.h4 = true;

        for (let i = 0; i < 2; i++){
          this.h[i] ? this.h[i] = false : this.h[i] = true;
        }

        console.log(this.h);
        //console.log(this.h1, this.h2, this.h3, this.h4);
      }


    }


  }
</script>

<style scoped>
  .my-tag {
    font-size: large;
    font-family: "等线 Light";
  }

  .height1 {
    height: 700px;
  }

  .height2 {
    height: 1400px;
  }

  .height3 {
    height: 808px;
  }

  .height4 {
    height: 1508px;
  }

  .height5 {
    height: 900px;
  }

  .height6 {
    height: 1600px;
  }


</style>
