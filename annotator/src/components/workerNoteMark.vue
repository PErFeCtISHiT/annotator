<template>
  <div>
    <p>{{taskID}}</p>
    <div class="block">
      <span class="demonstration">默认 Hover 指示器触发</span>
      <el-carousel height="150px">
        <el-carousel-item v-for="item in 4" :key="item">
          <h3>{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
  import {mapMutations} from 'vuex'

  export default {
    name: "worker-note-mark",

    props: ['taskID'],

    mounted() {
      this.refreshTaskData();
      this.refreshSelfProgress();
      this.updateCurrentTaskID(taskID);
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
        imgURLs:[]
      }
    },
    methods: {
      ...mapMutations(['updateCurrentTaskID', 'updateCurrentImageURL']),

      refreshTaskData() {
        let that = this;
        this.$http.get('/task/taskDetail', {
          params: {
            taskID: this.taskID
          }
        })
          .then(function (response) {
            let data = response.data;
            this.sponsorName = data.sponsorName;
            this.taskName = data.taskName;
            this.description = data.description;
            this.imgNum = data.imgNum;
            this.startDate = data.startDate;
            this.endDate = data.endDate;
            this.points = data.points;
            this.acceptNum = data.acceptNum;
            this.completedNum = data.completedNum;
            this.totalProgress = data.totalProgress;
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
        let username = this.$store.state.user.userInfo.username;
        let status = 1;
        let userRole = 3;
        let that = this;
        this.$http.post('/task/myTasks', {
          taskID,
          username,
          status,
          userRole
        })
          .then(function (response) {
            this.progress = response.data.progress;
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
            this.imgURLs =
            console.log(response);
          })
          .catch(function (error) {
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
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                message: `请检查网络连接`
              });
              console.log(error);
            });
        }
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
