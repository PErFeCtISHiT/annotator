<template>
  <div>
    <p>{{msg}}</p>
    <el-button type="text" @click="logOut">登出</el-button>
    <navigation></navigation>
    <router-view></router-view>
  </div>
</template>

<script>
  import {mapActions} from 'vuex'
  import navigation from './navigation'
  import first from './firstcomponent'
  import second from './secondcomponent'

  export default {
    data() {
      return {msg: "请修改这一页"}
    },
    methods: {
      ...mapActions(['logOut']),
    },
    components: {
      navigation,
      first,
      second
    }

  }
</script>

<style scoped>

</style>
