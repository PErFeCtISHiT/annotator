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
        var result = this.checkList.map(this.mapNum);
        return result;
      },
      mapNum: function (str) {
        if(str==='发起者'){
          return 2;
        }else if(str==='工人'){
          return 3;
        }
      }
    }
  };
</script>

<style scoped>

</style>
