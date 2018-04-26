<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" border-card>
      <el-tab-pane label="全部" name="total"></el-tab-pane>
      <el-tab-pane label="A" name="A"></el-tab-pane>
      <el-tab-pane label="B" name="B"></el-tab-pane>
      <el-tab-pane label="C" name="C"></el-tab-pane>
      <el-tab-pane label="D" name="D"></el-tab-pane>
      <el-tab-pane label="E" name="E"></el-tab-pane>

    </el-tabs>
    <br>
  </div>
</template>

<script>

    export default {
      name: "tag-bar",

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
