<template>
  <div>
    <el-row type="flex" justify="center" class="row-bg">
      <el-col :span="16" style="background-color: #3c763d">
        <div>
          <el-col :span="8" style="margin-top: 23px">
            <span class="label-all">用户名:</span>
          </el-col>
          <el-col :span="13" class="label-all" style="text-align: right; margin-right: 10px">
            <span class="taskIDLabel">{{ this.baseInfoDetail.username }}</span>
          </el-col>
        </div>
        <div>
          <el-col :span="8" class="label-all">
            <span>昵称:</span>
          </el-col>
          <el-col :span="14" class="label-all label_detail" style="text-align: right;">
            <span>{{this.baseInfoDetail.nickname}}</span>
          </el-col>
        </div>
        <div>
          <el-col :span="8" class="label-all" style="margin-top: 17px">
            <span>E-mail:</span>
          </el-col>
          <el-col :span="12" class="label-all label_detail">
            <span>{{ this.baseInfoDetail.email}}</span>
          </el-col>
        </div>
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
