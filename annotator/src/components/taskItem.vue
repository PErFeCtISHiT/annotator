<template>
  <div>
    <el-popover
      ref="popover1"
      placement="top-start"
      title="任务描述"
      width="200"
      trigger="hover"
      :content="taskMsg.description">
    </el-popover>


    <el-row>
      <el-col :span="2">
        <el-badge :value="taskMsg.imgNum" class="item">
          <el-button type="warning" icon="el-icon-document" v-popover:popover1 @click="handleShow"> </el-button>
        </el-badge>
      </el-col>

      <el-col :span="20">

        <el-row>
          <span>ID: {{taskMsg.taskID}} |</span>
          <span>名称: {{taskMsg.taskName}} |</span>
          <span>发起人: {{taskMsg.sponsorName}} |</span>
          <span>发起时间: {{taskMsg.startDate}} |</span>
          <span>结束时间: {{taskMsg.endDate}}</span>
        </el-row>

        <el-row>
          <el-progress :stroke-width="14" :status = "myMsg.progress===1?'success':''" :percentage="myMsg.progress.toFixed(2)*100"> </el-progress>
        </el-row>
      </el-col>

      <el-col :span="2">
        <el-tooltip class="item" effect="dark" content="点击将移除此任务" placement="top-end">
          <el-button type="danger" icon="el-icon-delete" @click="$emit('remove')" circle> </el-button>
        </el-tooltip>
      </el-col>
    </el-row>

  </div>
</template>

<script>

  export default {
    name: "task-item",
    props: ['taskMsg'],
    data() {
      return {
        myMsg: this.taskMsg
      }
    },
    methods: {
      handleShow() {
        this.$router.push({name: 'noteAndMark', params: {taskID: this.taskMsg.taskID}});
      }
    }
  }
</script>

<style scoped>

</style>
