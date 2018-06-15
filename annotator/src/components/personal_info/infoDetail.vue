<template>
  <div>
    <el-row type="flex" justify="center" class="row-bg">
      <el-col :span="16" style="background-color: #3c763d">

        <!--个人信息部分-->
        <el-card class="box-card">
          <!--标题-->
          <div slot="header" class="clearfix">
            <span>个人信息</span>
            <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
          </div>

          <!--内容-->
          <div>
            <div>
              <el-col :span="8" style="margin-top: 23px">
                <span class="label-all">用户名:</span>
              </el-col>
              <el-col :span="16" class="label-all" style="text-align: right; margin-right: 10px">
                <span class="taskIDLabel">{{ this.baseInfoDetail.username }}</span>
              </el-col>
            </div>
            <div>
              <el-col :span="8" class="label-all">
                <span>昵称:</span>
              </el-col>
              <el-col :span="16" class="label-all label_detail" style="text-align: right;">
                <span>{{this.baseInfoDetail.nickname}}</span>
              </el-col>
            </div>
            <div>
              <el-col :span="8" class="label-all" style="margin-top: 17px">
                <span>E-mail:</span>
              </el-col>
              <el-col :span="16" class="label-all label_detail">
                <span>{{ this.baseInfoDetail.email}}</span>
              </el-col>
            </div>
          </div>
        </el-card>

      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "info-detail",
    props: ["baseInfoDetail"],


  }
</script>

<style scoped>

</style>
