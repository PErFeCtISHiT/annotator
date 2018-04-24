<template>
  <el-col :span="24">
    <div class="grid-bg row-bg1">
      <!--最上面提示-->
      <el-col :span="2">
        <img src="../images/recharge.png" style="width: 50px; height: 50px; margin-left: 30px"/>
      </el-col>
      <el-col :span="2">
        <div style="margin-top: 12px; color: #6b6b6b; font-weight: bolder; font-size: 19px"> 充值中心</div>
      </el-col>
      <el-col :span="20" offset="3"></el-col>
      <el-col :span="20" offset="1" >
        <div class="prompt-label">1.充值获得的积分可以在ImgAnnotator系统中用于发布任务。</div>
        <div class="prompt-label">2.积分换算方法是 1元=10积分，你可以选择支付宝或者微信的付款方式进行充值，10元起充</div>
      </el-col>
    </div>
    <!--充值金额选项-->
    <el-row :gutter="15" style="margin-top: 50px">
      <el-col :span="6" offset="1" style="margin-left: 70px">
        <el-card style="" shadow="hover">
          ￥ &nbsp;10元
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card shadow="hover">
          ￥ &nbsp;20元
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card shadow="hover">
          ￥ &nbsp;50元
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card shadow="hover">
          ￥ &nbsp;100元
        </el-card>
      </el-col>
    </el-row>
    <!--充值方式选项-->
    <el-row  type="flex" class="prompt-label" justify="center">
      <el-col :span="8" offset="6" class="prompt-label" >
        <div>充值方式:</div>
      </el-col>
    </el-row>
    <!--金额提示-->
    <el-row type="flex" class="prompt-label" justify="center">
      <el-col :span="8" offset="6" class="prompt-label">
        <div>应付金额：</div>
      </el-col>
    </el-row>


  </el-col>
</template>

<script>
    export default {
        name: "recharge"
    }
</script>

<style scoped>
  .grid-bg {
    margin: 30px 30px 0 30px;
    border-radius: 4px;
    min-height: 36px;
    background-color: aliceblue;
    padding: 10px 0;
    height: 180px;
  }

  .prompt-label {
    margin: 20px 10px 10px 0px;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    font-size: 18px;
    color: #343434;
  }
</style>
