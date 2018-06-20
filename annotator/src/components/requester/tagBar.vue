<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" border-card>
      <el-tab-pane label="全部" name="total"></el-tab-pane>
      <el-tab-pane label="已完成" name="already"></el-tab-pane>
      <el-tab-pane label="进行中" name="undergoing"></el-tab-pane>
      <el-tab-pane v-for="tag in $store.state.tags.tagMsg"
                   :label=tag :name="tag" :key="tag"></el-tab-pane>
      <!--<el-tab-pane label="A" name="A"></el-tab-pane>-->
      <!--<el-tab-pane label="B" name="B"></el-tab-pane>-->
      <!--<el-tab-pane label="C" name="C"></el-tab-pane>-->
      <!--<el-tab-pane label="D" name="D"></el-tab-pane>-->
      <!--<el-tab-pane label="E" name="E"></el-tab-pane>-->

    </el-tabs>
    <br>
  </div>
</template>

<script>

    import ElTabPane from "element-ui/packages/tabs/src/tab-pane";

    export default {
      components: {ElTabPane},
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
