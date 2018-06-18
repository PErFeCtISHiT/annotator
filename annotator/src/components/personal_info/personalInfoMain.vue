<template>
  <div style="width: 100%;" v-bind:class="{height5: h[0], height6: h[1]}">
    <div style="background-image: url(../../../src/images/bgp.jpg); opacity: 0.2;
        position: absolute; z-index: -10; width: 96%;
        box-shadow: 10px 10px 5px #9d9d9d"
         v-bind:class="{height3: h[0], height4: h[1]}"></div>

    <el-dialog
      title="修改个人信息" width="30%" v-dialog-drag center
      :visible.sync="dialogVisible"
      :before-close="handleClose">
      <div>
        <el-row type="flex" justify="center" align="center" style="margin-top: 20px; margin-bottom: 20px">
          <el-col class="my-dialog" :span="6">
            <span>昵称</span>
          </el-col>
          <el-col :span="15">
            <el-input clearable placeholder="请输入新的昵称" v-model="nickname"></el-input>
          </el-col>
        </el-row>

        <!--<el-row type="flex" justify="center" style="margin-top: 20px; margin-bottom: 20px">-->
        <!--<el-col class="my-dialog" :span="6">-->
        <!--<span>邮箱</span>-->
        <!--</el-col>-->
        <!--<el-col :span="15">-->
        <!--<el-input clearable placeholder="请输入新的绑定邮箱" v-model="newMail"></el-input>-->
        <!--</el-col>-->
        <!--</el-row>-->

        <el-row type="flex" justify="center" style="margin-top: 20px; margin-bottom: 20px">
          <el-col class="my-dialog" :span="6">
            <span>新密码</span>
          </el-col>
          <el-col :span="15">
            <el-input clearable placeholder="输入新的密码" v-model="passwr1" type="password"></el-input>
          </el-col>
        </el-row>

        <el-row type="flex" justify="center" style="margin-top: 20px; margin-bottom: 20px">
          <el-col class="my-dialog" :span="6">
            <span>确认密码</span>
          </el-col>
          <el-col :span="15">
            <el-input clearable placeholder="输入新的密码" v-model="passwr2" type="password"></el-input>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmInfoChange">确认修改</el-button>
        &nbsp;&nbsp;
        <el-button style="padding-left: 32px; padding-right: 32px" type="primary"
                   @click="cancelInfoChange">取消</el-button>
      </span>
    </el-dialog>


    <div style="position: absolute; z-index: 0;
        width: 105%; margin-left: -5%; margin-top: 20px;">
      <el-row type="flex" justify="center" class="row-bg">
        <el-col :span="18">
          <div class="grid-content bg-purple">

            <el-tabs type="border-card" v-model="activePane" @tab-click="handleTabEvent"
                     style="margin-top: 30px" v-bind:class="{height1: h[0], height2: h[1]}">
              <el-tab-pane name="information">
                <span slot="label" class="my-tag"><i class="el-icon-date"></i>&nbsp;个人信息修改</span>
                <div class="grid-content bg-purple">
                  <info-detail @handleOpen="dialogVisible = true" style="margin-top: 50px"
                               :baseInfoDetail="personalInfo"></info-detail>
                </div>
              </el-tab-pane>


              <el-tab-pane name="statistics">
                <span slot="label" class="my-tag"><i class="el-icon-document"></i>&nbsp;统计数据</span>
                <div class="grid-content bg-purple">
                  <info-statistic style="margin-top: 10px; margin-left: 20px"></info-statistic>
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
  import ElRow from "element-ui/packages/row/src/row";


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
      ElRow,
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
      let that = this;

      this.$http.get('user/getCurrentUser')
        .then(function (response) {
          that.personalInfo = response.data;
        })
        .catch(function (error) {
          that.$message({
            message: '网络异常' + error,
            type: 'warning'
          });
        });
    },

    data() {
      return {
        personalInfo: {},
        personalStatistic: {},

        nickname: '',
        newMail: '',
        passwr1: '',
        passwr2: '',

        //为了设置CSS
        activePane: 'information',
        h: [true, false],
        dialogVisible: false
      }
    },

    methods: {
      handleTabEvent(tab) {
        //console.log(tab);
        //
        // this.h1 ? this.h1 = false : this.h1 = true;
        // this.h2 ? this.h2 = false : this.h2 = true;
        // this.h3 ? this.h3 = false : this.h3 = true;
        // this.h4 ? this.h4 = false : this.h4 = true;

        for (let i = 0; i < 2; i++) {
          this.h[i] ? this.h[i] = false : this.h[i] = true;
        }
      },

      clearScreen() {
        this.nickname = '';
        this.newMail = '';
        this.passwr1 = '';
        this.passwr2 = '';
      },

      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            this.clearScreen();
            done();
          })
          .catch(_ => {
          });
      },

      cancelInfoChange() {
        this.clearScreen();
        this.dialogVisible = false;
      },

      confirmInfoChange() {
        let that = this;

        // if(!this.newMail.match( /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/))
        //   this.$message({
        //     showClose: true,
        //     message: '邮箱格式错误',
        //     type: 'warning'
        //   });
        if (this.passwr1 !== this.passwr2)
          this.$message({
            showClose: true,
            message: '两次密码不一致',
            type: 'warning'
          });

        else {
          this.$http.post('/user/modify', {
            username: this.$store.state.user.userInfo.username,
            nickname: this.nickname,
            passwr: this.passwr1,
          })
            .then(function (response) {
              //if(response.data.mes === true) {
              that.$message({
                showClose: true,
                message: '修改成功',
                type: 'success',
              });
              that.personalInfo.nickname = that.nickname;
              that.clearScreen();
              that.dialogVisible = false;
              //}
            })
            .catch(function (error) {
              that.$message({
                message: '网络异常' + error,
                type: 'warning',
              });
            });

        }
      }//此方法结束
    }


  }
</script>

<style scoped>
  .my-tag {
    font-size: large;
    font-family: "等线 Light";
  }

  .height1 {
    height: 600px;
  }

  .height2 {
    height: 1800px;
  }

  .height3 {
    height: 708px;
  }

  .height4 {
    height: 1908px;
  }

  .height5 {
    height: 800px;
  }

  .height6 {
    height: 2000px;
  }

  .my-dialog {
    margin-left: 10px;
    margin-top: 8px;
    font-size: medium;
    font-family: "等线 Light";
  }

</style>
