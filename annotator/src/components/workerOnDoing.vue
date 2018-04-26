<template>
  <div>
    <ul>
      <li
        is="task-item"
        v-for="(todo, index) in onDoing"
        :key="todo.taskID"
        :taskMsg="todo"
        @remove="handleRemove(index)"
      ></li>
    </ul>

    <ul id="example-1">
      <li v-for="item in onDoing">
        {{ item.imgNum }}
      </li>
    </ul>

  </div>
</template>

<script>
  import TaskItem from "./taskItem";


  export default {
    components: {TaskItem},
    name: "worker-on-doing",

    mounted(){
      let taskID = 0;
      let username = this.$store.state.user.userInfo.username;  //这里的name首字母小写
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
          if(response.data.tasks){
            this.onDoing = response.data.tasks;
          }else{
            that.$message({
              message:'回传数据格式出错',
              type:'error'
            });
          }
          console.log(response);
        })
        .catch(function (error) {
          that.$message({
            message:'请检查您的网络',
            type:'error'
          });
          console.log(error);
        });
    },

    data() {
      let temp = [
        {
          imgNum: 8,
          taskID: 123,
          taskName: "标出所有人物",
          description: "请标出图片中的所有人物",
          sponsorName: "蔡蔚霖",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.6
        },
        {
          imgNum: 12,
          taskID: 222,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          sponsorName: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
        {
          imgNum: 12,
          taskID: 155,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          sponsorName: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
        {
          imgNum: 12,
          taskID: 179,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          sponsorName: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
        {
          imgNum: 12,
          taskID: 444,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          sponsorName: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
      ];

      return {
        onDoing: temp,
      }
    },

    methods: {
      handleRemove(index) {

        if(this.$store.state.user.userInfo.userName==='admin'){
          that.onDoing.splice(index, 1);
          return;
        }


        let target = this.onDoing[index];
        let taskID = target.taskID;
        let workerName = this.$store.state.user.userInfo.userName;
        let that = this;
        this.$http.get('/task/abortTask', {
          params: {
            taskID,
            workerName
          }
        })
          .then(function (response) {
            if(response.data.mes){
              that.$message({
                message: '删除任务成功',
                type: 'success'
              });
              that.onDoing.splice(index, 1);
            }else{
              that.$message({
                message: '删除任务失败',
                type: 'error'
              });
            }
            console.log(response);
          })
          .catch(function (error) {
            that.$message({
              message: '请检查你的网络',
              type: 'error'
            });
            console.log(error);
          });
      }
    }
  }
</script>

<style scoped>

</style>
