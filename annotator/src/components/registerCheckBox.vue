<template>
  <el-radio-group v-model="radio">
    <el-radio label="发起者">
    </el-radio>
    <el-radio label="工人">
    </el-radio>
  </el-radio-group>
</template>

<script>
  export default {
    data () {
      return {
        radio: '发起者',
      };
    },
    methods:{
      getResult: function () {
        return [this.mapNum(this.radio)];
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
