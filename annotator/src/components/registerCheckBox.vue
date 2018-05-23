<template>
  <el-checkbox-group v-model="checkList">
    <el-checkbox label="发起者"></el-checkbox>
    <el-checkbox label="工人"></el-checkbox>
  </el-checkbox-group>
</template>

<script>
  //这是一个多选组件？？
  export default {
    data () {
      return {
        checkList: ['发起者','工人']
      };
    },
    methods:{
      getResult: function () {
        return this.checkList.map(this.mapNum);
      },
      mapNum: function (str) {
        if(str==='发起者'){
          return 3;
        }else if(str==='工人'){
          return 4;
        }
      }
    }
  };
</script>

<style scoped>

</style>
