<template>
  <div id="distributeNewTask">
    <el-steps :active="active" space="500" finish-status="success">
      <el-step title="步骤 1" description="填写基本信息"></el-step>
      <el-step title="步骤 2" description="选择图片"></el-step>
    </el-steps>

    <div id="baseInfo" v-if="active"

    <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
  </div>
</template>

<script>
  export default {
    name: "distribute-new-task",

    data() {
      return {
        active: 1
      };
    },

    methods: {
      next() {
        this.active > 1 ? this.active = 1 : this.active++;
      }
    }

  }
</script>

<style scoped>

</style>
