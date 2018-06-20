<template>
  <div>

    <el-col :span="24" id = "pics">

      <!--任务详情-->
      <el-col :span="7">
        <el-row type="flex" id="navigation-div2" justify="left">
          <el-col :span="24">
            <div>
              <oneTask :detailInfo="taskData" :uid="taskID"> </oneTask>
            </div>
          </el-col>
        </el-row>
      </el-col>


      <el-col :span="17">

        <div class="block" style="width:800px; height:50px; margin:3% auto">
          <el-row :gutter="15">
            <el-col :span="4" style="margin-top: 10px">
              <span>我的进度：</span>
            </el-col>
            <el-col :span="18" style="margin-top: 10px">
              <el-progress :stroke-width="14"  :status="progress===1?'success':''" :percentage="progress.toFixed(2)*100"> </el-progress>
            </el-col>
            <el-col :span="2">
              <el-button :disabled="progress < 1" @click="completeTask">完成</el-button>
            </el-col>
          </el-row>
        </div>

        <div class="block" style="width:800px; height:600px; margin:auto">
          <el-carousel height="400px" :autoplay="false">
            <el-carousel-item v-for="item in imgURLs" :key="item">
              <img :src="item" height="600" width="800" @click="handleDrawing(item)"/>
            </el-carousel-item>
          </el-carousel>
        </div>
      </el-col>

    </el-col>

    <div id="drawingArea"></div>
  </div>
</template>

<script>
  import {mapMutations} from 'vuex'
  import {mapActions} from 'vuex'
  import OneTask from "../oneTask";

  export default {
    components: {OneTask},
    name: "worker-note-mark",

    props: ['taskID'],

    mounted() {
      window.refreshSelfProgress = this.refreshSelfProgress;
      this.refreshTaskData();
      this.refreshSelfProgress();
      this.refreshImgURL();
      this.updateCurrentTaskID(this.taskID);
    },

    data() {
      return {
        myTaskID: this.taskID,
        taskData: {},
        progress: 0,
        imgURLs: []
      }
    },
    methods: {
      ...mapMutations(['updateCurrentTaskID', 'updateCurrentImageURL', 'updateCurrentSponsor']),
      ...mapActions(['updateWithoutPointer']),
      refreshTaskData() {
        let that = this;
        let taskID = this.taskID;
        this.$http.get('/task/checkTaskDetail', {
          params: {
            taskID,
          }
        })
          .then(function (response) {
            let data = response.data;
            that.sponsorName = data.sponsorName;
            that.taskData = response.data;
            console.log("写入store的sponsorName:" + that.sponsorName);
            that.updateCurrentSponsor(that.sponsorName);
            console.log(that.$store.state.workerTask.currentSponsor);
          })
          .catch(function (error) {
            that.$message({
              message: '刷新当前任务数据失败',
              type: 'error'
            });
            console.log(error);
          });
      },

      refreshSelfProgress() {
        let taskID = this.taskID;
        let that = this;
        this.$http.get('task/checkWorkerProgress', {
          params: {
            taskID
          }
        })
          .then(function (response) {
            that.progress = response.data.progress;
          })
          .catch(function (error) {
            that.$message({
              message: '刷新当前个人进度失败',
              type: 'error'
            });
            console.log(error);
          });
      },

      refreshImgURL() {
        let taskID = this.taskID;
        let that = this;
        this.$http.get('/task/checkImages', {
          params: {
            taskID
          }
        })
          .then(function (response) {
            that.imgURLs = response.data.imgURLs;
          })
          .catch(function (error) {
            that.$message({
              message: '加载图片数据失败',
              type: 'error'
            });
            console.log(error);
          });
      },

      completeTask() {
        this.refreshSelfProgress();
        if (this.progress < 1) {
          this.$alert('请完成后重试', '未完成所有项目', {
            confirmButtonText: '确定',
            callback: () => {
              this.$message({
                type: 'info',
                message: '已回到标注页面',
                duration: 1200,
              });
            }
          });
        } else {        //正式提交
          let taskID = this.taskID;
          let username = this.$store.state.user.userInfo.username;
          let that = this;
          this.$http.get('/task/completeTask', {
            params: {
              taskID,
              username
            }
          })
            .then(function () {
              that.$alert('收到您的完成提交请求', '系统反馈', {
                confirmButtonText: '确定',
                callback: () => {
                  that.$message({
                    type: 'success',
                    message: '任务已完成',
                    duration: 1200,
                  });
                  that.$router.push('/2-2')
                }
              });
              that.updateWithoutPointer();
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                message: '请检查网络连接',
                duration: 1200,
              });
              console.log(error);
            });
        }
      },

      handleDrawing(imgURL) {
        this.updateCurrentImageURL(imgURL);

        let drawingArea = $("#drawingArea");
        drawingArea.empty();
        drawingArea.load('../../src/temp/markLocality.html');
      }
    }
  }
</script>

<style scoped>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: auto;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
