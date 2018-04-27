<template>
  <div>
    <p>{{taskID}}</p>
    <div class="block">
      <span class="demonstration">默认 Hover 指示器触发</span>
      <el-carousel height="200px">
        <el-carousel-item v-for="item in imgURLs" :key="item">
          <img :src="item" height="200" width="200" @click ="handleDrawing(item)"/>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div id="drawingArea"></div>
  </div>
</template>

<script>
  import {mapMutations} from 'vuex'
  import {mapActions} from 'vuex'

  export default {
    name: "worker-note-mark",

    props: ['taskID'],

    mounted() {
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
        sponsorName: '',
        taskName: '',
        description: '',
        imgNum: '',
        startDate: '',
        endDate: '',
        points: '',
        acceptNum: '',
        completedNum: '',
        totalProgress: 0,
        imgURLs:['../../src/testDrawImage/1.jpg','../../src/testDrawImage/2.jpg','../../src/testDrawImage/3.jpg','../../src/testDrawImage/4.jpg']
      }
    },
    methods: {
      ...mapMutations(['updateCurrentTaskID', 'updateCurrentImageURL','updateCurrentSponsor']),
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
            that.taskName = data.taskName;
            that.description = data.description;
            that.imgNum = data.imgNum;
            that.startDate = data.startDate;
            that.endDate = data.endDate;
            that.points = data.points;
            that.acceptNum = data.acceptNum;
            that.completedNum = data.completedNum;
            that.totalProgress = data.totalProgress;
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
        this.$http.get('/checkWorkerProgress', {
          params:{
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
              message:'加载图片数据失败',
              type:'error'
            });
            console.log(error);
          });
      },

      completeTask() {
        refreshSelfProgress();
        if (this.progress !== 1) {
          this.$alert('请完成后重试', '未完成所有项目', {
            confirmButtonText: '确定',
            callback: () => {
              this.$message({
                type: 'info',
                message: `已回到标注页面`
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
                    message: `任务已完成`
                  });
                }
              });
              that.updateWithoutPointer();
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                message: `请检查网络连接`
              });
              console.log(error);
            });
        }
      },

      handleDrawing(imgURL){
        this.updateCurrentImageURL(imgURL);
        this.updateCurrentSponsor(this.sponsorName);
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
