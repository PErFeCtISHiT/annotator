<template>
  <div>
    <p>{{msg}}</p>
    <el-button type="text" @click="logOut">登出</el-button>
  </div>
</template>

<script>
  import {mapActions} from 'vuex'

  export default {
    data() {
      return {msg: "请修改这一页 相关内容加到div里"}
    },
    methods: {
      ...mapActions(['logOut']),
    }
  }
</script>

<style scoped>

</style>
