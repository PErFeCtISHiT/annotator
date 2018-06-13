<template>
  <div class="requester-tasks">
    <el-row type="flex" justify="center">
      <el-col :span="22">
        <div>
          <worker-tag-bar v-on:change="changeTabs"> </worker-tag-bar>
        </div>
      </el-col>
    </el-row>

    <el-row type="flex" justify="center">
      <el-col :span="20">

        <el-row :gutter="10">
          <el-col :span="24">

            <worker-task-item
              style="height: 542px;"
              v-for="(message, index) in messages"
              @remove="handleRemove(index)" @accept="handleAccept"
              :taskMsg="message" :theIndex="index" :key="message.taskID"> </worker-task-item>

          </el-col>
        </el-row>

      </el-col>
    </el-row>


  </div>
</template>

<script>
  import WorkerTagBar from "./workerTagBar";
  import WorkerTaskItem from "./workerTaskItem";

  const items = [];


  export default {
    components: {
      WorkerTaskItem,
      WorkerTagBar,
    },

    name: "worker-accept-tasks",

    mounted: function () {
      this.changeTabs("total");
    },


    data() {
      return {
        messages: items,
        tags: [],
        tabName: ""
      };
    },

    methods: {
      /**
       * 这里那个bar需要用到的。
       * tabName是你里面传回来的
       */
      changeTabs: function (tabName) {

        this.tabName = tabName;

        //检查类型
        let status = 1, that = this, tab = "", userRole = 4; //4是工人
        if (tabName === 'already') {
          status = 2;
        } else if (tabName === 'undergoing') {
          status = 1;
        } else if (tabName === 'total') {
          status = 0;
        } else {
          status = 1;
          tab = tabName;
        }

        //console.log('status and tag: ', status, tab);
        this.$http.post('/task/allTasks', {
            status: status,
            tag: tab,
            userRole: userRole
        })
          .then(function (response) {
            that.messages = response.data.tasks;
            console.log(response.data.tasks);
          })
          .catch(function (error) {
            that.$message({
              message: '请求分类失败' + error,
              type: 'warning',
              duration: 500
            });
            console.log('分类错误');
          });

      },

      handleAccept(uid, index) {
        let that = this;

        this.$confirm('接受此任务后，请到工人-进行中页面进行标注', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
          .then(() => {
            //确认的话发一个ajax请求
            console.log(uid);
            that.$http.get('/task/acceptTask', {
              params: {
                taskID: uid,
                username: that.$store.state.user.userInfo.username
              }
            })
              .then(function (response) {
                if (response.data.mes) {
                  that.$message({
                    message: '接受任务成功',
                    type: 'success',
                    duration: 500
                  });
                }
                else {
                  that.$message({
                    message: '接受失败，您已经接收过该任务',
                    type: 'warning',
                    duration: 500
                  });
                }
                that.messages.splice(index, 1);
              })
              .catch(function (error) {
                that.$message({
                  message: '接受失败：'+error,
                  type: 'warning',
                  duration: 500
                });
              })
          })
          .catch(() => {
            that.$message.info('已取消');
          })
      },

      /**
       * 删除任务。是从子组件emit过来的
       * */
      handleRemove(index) {
        this.messages.splice(index, 1);
      }

    }

  }
</script>

<style scoped>

</style>
