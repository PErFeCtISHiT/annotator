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

            <requester-task-item v-for="(message, index) in messages" @remove="handleRemove(index)"
                                 :taskMsg="message" :key="message.taskID"></requester-task-item>

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
      taskID: 6,
      taskName: "失败了",
      description: "按时打开链接爱斯莫地方v王企鹅王企鹅女妇女被送女滴哦是计费的方式你",
      totalProgress: 0.67,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 6,
      taskName: "1",
      description: "2351",
      totalProgress: 0.67,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 6,
      taskName: "1",
      description: "2351",
      totalProgress: 0.67,
      tags: ['A', 'B', 'C'],
      startDate: "2018-04-25",
      endDate: "2018-04-27",
    },
    {
      taskID: 6,
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
      this.askForTasks("all");
    },


    data () {
      return {
        messages: []
      };
    },

    methods: {
      changeTabs: function (tabName) {
        console.log(tabName, "ok");
      },

      askForTasks (category) {
        this.messages = items;
      },

      handleRemove(index){

      }

    }

  }
</script>

<style scoped>

</style>
