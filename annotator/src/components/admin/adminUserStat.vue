<template>
  <div class="grid-bg row-bg1">
    <div class="big-label">系统用户统计</div>
    <el-col :span="6">
      <img src="../../images/user.png" class="im-big"/>
    </el-col>
    <el-col :span="6" :offset="2">
      <div class="total-label" style="font-size: 18px; color: dimgray; margin-top: 35px">总用户数</div>
      <div class="total-label" style="font-size: 30px; font-weight: bolder; alignment: center; margin-top: 10px"
           id="userNum">{{ info.sponsorNum + info.workerNum }}
      </div>
    </el-col>
    <el-col :span="6" :offset="1">
      <div>
        <el-col :span="15">
          <div class="normal-label">众包工人:</div>
        </el-col>
        <el-col :span="9">
          <div class="normal-figure" id="workerNum">{{ info.workerNum }}</div>
        </el-col>
      </div>
      <div>
        <el-col :span="15">
          <div class="normal-label">任务发起者:</div>
        </el-col>
        <el-col :span="9">
          <div class="normal-figure" id="requestorNum">{{ info.sponsorNum }}</div>
        </el-col>
      </div>
    </el-col>
  </div>
</template>

<script>
  export default {
    name: "admin-user-stat",

    props: ['info']
  }
</script>

<style scoped>
  .grid-bg {
    margin: 30px 30px 0 30px;
    border-radius: 4px;
    min-height: 36px;
    background-color: aliceblue;
    padding: 10px 0;
  }

  .row-bg1 {
    height: 200px;
  }

  .big-label {
    margin-left: 30px;
    margin-top: 10px;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    font-size: 25px;
    font-weight: bold;
    color: #464646;
  }
  /*每个大图标的设置*/
  .im-big {
    width: 80px;
    height: 80px;
    margin-left: 50px;
    margin-top: 30px;
  }

  /*每个合计统计数据的设置*/
  .total-label {
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  }

  /*普通统计数据，正常字体*/
  .normal-label {
    margin-top: 30px;
    color: #464646;
  }

  .normal-figure {
    margin-top: 30px;
    color: black;
    font-weight: bolder;
  }


</style>
