<template>
  <el-col :span="24">

    <div id = "firstPageInput"></div>

    <!--<img src="../images/main.png" width="100%">-->

    <!--<br>-->
    <!--<br>-->

    <!--<el-row style="background-color: #e3eef3; border-radius: 10px">-->
      <!--<el-col :span="1">-->
        <!--<img src="../images/advantage.png" width="60" style="margin-top: 2px">-->
      <!--</el-col>-->
      <!--<el-col :span="22" style="margin-top: 14px; margin-left: 20px;">-->
        <!--<div style="font-size: 25px; color: #343434; font-weight: bolder">我们的优势</div>-->
      <!--</el-col>-->
    <!--</el-row>-->

    <!--<br>-->
    <!--<img src="../images/team.png" width="33%">-->
    <!--<img src="../images/data.png" width="33%">-->
    <!--<img src="../images/sys.png" width="33%">-->

    <!--<br>-->
    <!--<br>-->

    <!--<el-row style="background-color: #e3eef3; border-radius: 10px">-->
      <!--<el-col :span="1">-->
        <!--<img src="../images/pen.png" width="60" style="margin-top: 2px">-->
      <!--</el-col>-->
      <!--<el-col :span="22" style="margin-top: 14px; margin-left: 20px;">-->
        <!--<div style="font-size: 25px; color: #343434; font-weight: bolder">多样化标注方式</div>-->
      <!--</el-col>-->
    <!--</el-row>-->
    <!--<br>-->

    <!--<img src="../images/simple.png" width="33%">-->
    <!--<img src="../images/part.png" width="33%">-->
    <!--<img src="../images/total.png" width="33%">-->
  </el-col>


</template>

<script>
  export default {
    name: "first-part",

    mounted(){
      this.initFirstPage();
    },

    methods:{
      initFirstPage(){
        let area = $("#firstPageInput");
        area.empty();
        area.load('../../src/firstPage/index.html');

      }
    }
  }
</script>

<style scoped>

</style>
