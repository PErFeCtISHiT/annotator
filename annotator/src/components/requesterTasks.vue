<template>
  <div class="requester-tasks">
    <el-row type="flex" justify="center">
      <el-col :span="22"><div>
        <tag-bar v-on:change="changeTabs"></tag-bar>
      </div></el-col>
    </el-row>

    <el-row type="flex" justify="center">
      <el-col :span="20">

        <el-row :gutter="10">
          <el-col :span="24">

            <requester-task-item v-for="(message, index) in messages"
                                 @remove="handleRemove" @complete="handleComplete"
                                 :taskMsg="message" :theIndex="index" :key="message.taskID"> </requester-task-item>

          </el-col>
        </el-row>

      </el-col>
    </el-row>



  </div>
</template>

<script>
  import tagBar from './tagBar'
  import requesterTaskItem from './requesterTaskItem'
  const items = [
    {
      taskID: 6,
      taskName: "成功了",
      description: "阿斯顿还是低啊随便丢撒比都把送都弄撒旦好似嗲上班都i啊班底哦那送你的撒都纳斯哦你滴哦啊索尼电视都",
      totalProgress: 0.67,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 4,
      taskName: "失败了",
      description: "按时打开链接爱斯莫地方v王企鹅王企鹅女妇女被送女滴哦是计费的方式你",
      totalProgress: 1.00,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 3,
      taskName: "1",
      description: "2351",
      totalProgress: 1.00,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 2,
      taskName: "1",
      description: "2351",
      totalProgress: 0.67,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 1,
      taskName: "1",
      description: "2351",
      totalProgress: 0.67,
      tags: ['A'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    }
  ];



  export default {
    components: {
      requesterTaskItem,
      tagBar
    },

    name: "requester-tasks",

    mounted: function () {
      this.changeTabs("total");
    },


    data () {
      return {
        messages: [],
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
        let status = 0, that = this, tab = "";
        if(tabName === 'already'){
          status = 2;
        }else if (tabName === 'undergoing'){
          status = 1;
        }else if (tabName === 'total'){
          status = 0;
        }else{
          status = 0;
          tab = tabName;
        }

        //console.log('status and tag: ', status, tab);
        this.$http.post('/task/myTasks', {
          username: this.$store.state.user.userInfo.username,
          status: status,
          tag: tab,
          userRole: 2
        })
          .then(function (response) {
            let data = response.data.tasks;

            that.messages = data;
          })
          .catch(function (error) {
            that.$message({
              message: '请求分类失败' + error,
              type: 'warning'
            });
            console.log('分类错误');
          });

      },

      handleAccept(uid, index) {
        let that = this;

        this.$confirm('结束此任务，积分无法退还。是否继续', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {

            //确认的话发一个ajax请求
            console.log(uid);
            that.$http.get('/task/endTask',{
              params:{
                taskID: uid
              }
            })
              .then(function (response) {
                if(response.data.mes === true){
                  that.messages.splice(index, 1);
                  that.$message.success('已结束任务');
                }
                else{
                  that.$message.warning('结束任务失败');
                }
              })
              .catch(function (error) {
                that.$message.warning('结束任务失败' + error);
              })

          })
          .catch(() => {
            that.$message.info('已取消');
          })
      },

      /**
       * 删除任务。是从子组件emit过来的
       * */
      handleRemove(payload){
        let that = this;

        this.$confirm('删除此任务，积分无法退还。是否继续', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {

            //确认的话发一个ajax请求
            console.log(payload.uid);
            that.$http.get('/task/deleteTask', {
              params:{
                taskID: payload.uid
              }
            })
              .then(function (response) {
                if(response.data.mes === true){
                  that.messages.splice(payload.index, 1);
                  that.$message.success('删除成功');
                }
                else{
                  that.$message.warning('删除失败');
                }
              })
              .catch(function (error) {
                that.$message.warning('删除失败' + error);
              })

          })
          .catch(() => {
            this.$message.info('已取消');
          })
      }

    }

  }
</script>

<style scoped>

</style>
