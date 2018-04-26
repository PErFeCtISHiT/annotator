<template>
  <div class="requester-tasks">
    <el-row type="flex" justify="center">
      <el-col :span="20"><div>

        <tag-bar v-on:change="changeTabs"></tag-bar>
        <requester-task-item :taskMsg="message"></requester-task-item>

      </div></el-col>
    </el-row>



  </div>
</template>

<script>
  import tagBar from './tagBar'
  import requesterTaskItem from './requesterTaskItem'
  const items = [
    {
      "taskID": 6,
      "taskName": "1",
      "description": "2351",
      "totalProgress": "0.67",
      "startDate": "2018-04-25 21:36:58",
      "endDate": "2018-04-27 00:00:00",
    }
  ];

  export default {
    components: {
      requesterTaskItem,
      tagBar
    },

    name: "requester-tasks",

    mounted: function () {

    },


    data () {
      return {
        message: items[0]
      };
    },

    methods: {
      changeTabs: function (tabName) {
        console.log(tabName, "ok");
      },

      askForTasks (category) {

      }

    }

  }
</script>

<style scoped>

</style>
