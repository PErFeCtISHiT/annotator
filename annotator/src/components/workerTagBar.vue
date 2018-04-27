<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" border-card>
      <el-tab-pane label="全部" name="total"></el-tab-pane>
      <el-tab-pane
        v-for="tag in $store.state.tags.tagMsg"
        :label="tag" :name="tag"></el-tab-pane>
    </el-tabs>
    <br>
  </div>
</template>

<script>

  export default {
    name: "worker-tag-bar",

    data () {
      return {
        activeName: 'total'
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
