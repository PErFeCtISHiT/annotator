<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" border-card>
      <el-tab-pane label="进行中" name="undergoing"> </el-tab-pane>
      <el-tab-pane label="已完成" name="already"> </el-tab-pane>
      <el-tab-pane label="全部" name="total"> </el-tab-pane>
      <el-tab-pane
        v-for="tag in $store.state.tags.tagMsg"
        :label="'标签：'+tag+'(进行中)'" :name="tag" :key="tag"> </el-tab-pane>
    </el-tabs>
    <br>
  </div>
</template>

<script>

  export default {
    name: "worker-tag-bar",

    data () {
      return {
        activeName: 'undergoing'
      }
    },

    methods: {
      handleClick (tab) {
        this.$emit('change', tab.name);
      }
    }
  }
</script>

<style scoped>

</style>
