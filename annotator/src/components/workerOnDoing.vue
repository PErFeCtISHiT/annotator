<template>
  <div>
    <ul>
    <li
      is = "task-item"
      v-for="(todo, index) in onDoing"
      :key="todo.taskID"
      :taskMsg="todo"
      v-on:remove="handleRemove(index)"
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

    created(){
      for(let i = 0; i < this.onDoing.length; i++) {
        console.log(this.onDoing[i]);
      }
    },
    // created(){
    //   for(let item in temp){
    //     this.onDoing.push(item);
    //     console.log(this.onDoing);
    //   }
    // },

    data() {
      let temp = [
        {
          imgNum: 8,
          taskID: 123,
          taskName: "标出所有人物",
          description: "请标出图片中的所有人物",
          requestor: "蔡蔚霖",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.6
        },
        {
          imgNum: 12,
          taskID: 222,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          requestor: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
        {
          imgNum: 12,
          taskID: 155,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          requestor: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
        {
          imgNum: 12,
          taskID: 179,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          requestor: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
        {
          imgNum: 12,
          taskID: 444,
          taskName: "标出所有羊",
          description: "请标出图片中的所有羊",
          requestor: "曹嘉玮",
          startDate: "2017-4-22",
          endDate: "2017-5-1",
          progress: 0.8
        },
      ];

      return {
        onDoing:temp,
      }
    },

    methods:{
      handleRemove(index){
        this.onDoing.splice(index, 1);
      }
    }
  }
</script>

<style scoped>

</style>
