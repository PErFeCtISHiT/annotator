<template>
  <div>
    <ul>
      <li
        style="margin-top: 10px; margin-bottom: 10px"
        is="task-item"
        v-for="(todo, index) in onDoing"
        :key="todo.taskID"
        :taskMsg="todo"
        @remove="handleRemove(index)"
      ></li>
    </ul>

    <!--<ul id="example-1">-->
      <!--<li v-for="item in onDoing">-->
        <!--{{ item.imgNum }}-->
      <!--</li>-->
    <!--</ul>-->

  </div>
</template>

<script>
  import TaskItem from "../taskItem";


  export default {
    components: {TaskItem},
    name: "worker-on-doing",

    mounted(){
      let username = this.$store.state.user.userInfo.username;  //这里的name首字母小写
      let tag = "";    //这里不需要tag
      let status = 1;
      let userRole = 4;
      let that = this;
      this.$http.post('/task/myTasks', {
        tag,
        username,
        status,
        userRole
      })
        .then(function (response) {
          if(response.data.tasks){
            that.onDoing = response.data.tasks;
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
      let temp = [];

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
        let username = this.$store.state.user.userInfo.username;
        let that = this;
        console.log("传用户");
        console.log(username);
        this.$http.get('/task/abortTask', {
          params: {
            taskID,
            username
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
