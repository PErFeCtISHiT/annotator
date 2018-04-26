<template>
  <div>
    <p>{{taskID}}</p>
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
        taskData: {}
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
            this.taskData.requestors = data.requestors;
            this.taskData.taskName = data.taskName;
            this.taskData.description = data.description;
            this.taskData.imgNum = data.imgNum;
            this.taskData.startDate = data.startDate;
            this.taskData.endDate = data.endDate;
            this.taskData.points = data.points;
            this.taskData.acceptNum = data.acceptNum;
            this.taskData.completedNum = data.completedNum;
            this.taskData.totalProgress = data.totalProgress;
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
        let userName = this.$store.state.user.userInfo.username;
        let status = 1;
        let userRole = 3;
        let that = this;
        this.$http.post('/task/myTasks', {
          taskID,
          userName,
          status,
          userRole
        })
          .then(function (response) {
            this.taskData.progress = response.data.progress;
          })
          .catch(function (error) {
            that.$message({
              message: '刷新当前个人进度失败',
              type: 'error'
            });
            console.log(error);
          });
      },

      completeTask() {
        refreshSelfProgress();
        if (this.taskData.progress !== 1) {
          this.$alert('请完成后重试', '未完成所有项目', {
            confirmButtonText: '确定',
            callback: () => {
              this.$message({
                type: 'info',
                message: `已回到标注页面`
              });
            }
          });
        }else{        //正式提交
          let taskID = this.taskID;
          let username = this.$store.state.user.userInfo.username;
          let that = this;
          this.$http.get('/task/completeTask', {
            params: {
              taskID,
              username
            }
          })
            .then(function (response) {
              console.log(response);
            })
            .catch(function (error) {
              console.log(error);
            });
        }
      }
    }
  }
</script>

<style scoped>

</style>
