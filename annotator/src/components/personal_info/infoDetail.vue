<template>
  <div>



    <el-row type="flex" justify="center" class="row-bg">
      <el-col :span="16">

        <!--个人信息部分-->
        <el-card class="box-card">
          <!--标题-->
          <div slot="header" class="clearfix" style="margin-left: 40%">
            <span class="my-card-header">个人信息</span>
            <el-button style="float: right; padding: 10px 25px 10px 25px; font-size: large"
                       type="primary" @click="$emit('handleOpen')">修改
            </el-button>
          </div>

          <!--内容-->
          <div style="font-size: large">
            <el-row style="margin-top: 25px; margin-bottom: 25px">
              <el-col :span="8" class="label-all">
                <span>用户名:</span>
              </el-col>
              <el-col :span="16" class="label-all">
                <span>{{ baseInfoDetail.username }}</span>
              </el-col>
            </el-row>

            <el-row style="margin-top: 25px; margin-bottom: 25px">
              <el-col :span="8" class="label-all">
                <span>昵称:</span>
              </el-col>
              <el-col :span="16" class="label-all">
                <span>{{ baseInfoDetail.nickname }}</span>
              </el-col>
            </el-row>

            <el-row style="margin-top: 25px; margin-bottom: 25px">
              <el-col :span="8" class="label-all">
                <span>当前拥有积分:</span>
              </el-col>
              <el-col :span="16" class="label-all">
                <span>{{ baseInfoDetail.points}}</span>
              </el-col>
            </el-row>

            <el-row v-if="baseInfoDetail.type === 'worker'" style="margin-top: 25px; margin-bottom: 25px">
              <el-col :span="8" class="label-all">
                <span>当前等级:</span>
              </el-col>
              <el-col :span="16" class="label-all">
                <el-rate v-model="baseInfoDetail.lev" disabled></el-rate>
              </el-col>
            </el-row>
          </div>
        </el-card>

      </el-col>
    </el-row>
  </div>
</template>

<script>
  import ElRow from "element-ui/packages/row/src/row";

  export default {
    components: {ElRow},
    name: "info-detail",

    props: ["baseInfoDetail"],

    data() {
      return {

      }
    },

    methods: {

    }

  }
</script>

<style scoped>
  .my-card-header {
    font-size: xx-large;
    font-weight: bolder;
    font-family: Chalkboard;
  }

  .clearfix {
    color: #4f4f52;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .label-all {
    font-size: 20px;
    color: #4f4f52;
    padding-left: 30px;
  }
</style>
