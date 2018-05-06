<template>
  <div style="height: 542px">
  <el-col :span="24">
    <!--任务详情-->
    <el-col :span="21">
      <el-card color="#EAF7E7" class="box-card">
        <div slot="header" class="clearfix">
          <el-col :span="7">
            <img src="../images/task.png" style="width: 70px; height: 70px"/>
          </el-col>
          <el-col :span="17">
            <div style="font-size: larger;">任务名称</div>
            <div
              style="text-align: center; margin-right: 20px; margin-top: 14px; font-size: larger; font-weight: bolder;">
              {{ detailInfo.taskName }}
            </div>
          </el-col>
          <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
        </div>
        <div>
          <div>
            <el-col :span="8" style="margin-top: 23px">
              <span class="label-all">任务编号:</span>
            </el-col>
            <el-col :span="13" class="label-all" style="text-align: right; margin-right: 10px">
              <span class="taskIDLabel">NO.</span>
              <span class="taskIDLabel" id="taskID">{{ uid }}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="8" class="label-all">
              <span>任务描述:</span>
            </el-col>
            <el-col :span="14" class="label-all label_detail" style="text-align: right;">
              <span>{{ detailInfo.description }}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="8" class="label-all" style="margin-top: 17px">
              <span>任务标签:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span v-for="(tag, index) in detailInfo.tag" :key="index">
                <el-tag >{{ tag }}</el-tag>&thinsp;
              </span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>发布时间:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>{{ detailInfo.startDate ? detailInfo.startDate.split(" ")[0] : detailInfo.startDate }}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>结束时间:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>{{ detailInfo.endDate ? detailInfo.endDate.split(" ")[0] : detailInfo.endDate }}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>图片数量:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>共 {{ detailInfo.imgNum }} 张</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>任务奖励:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>{{ detailInfo.points }}点/人</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>要求人数:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>{{ detailInfo.expectedNumber }}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>已接受人数:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>{{ detailInfo.acceptNum}}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all">
              <span>已完成人数:</span>
            </el-col>
            <el-col :span="12" class="label-all label_detail">
              <span>{{ detailInfo.completedNumber}}</span>
            </el-col>
          </div>
          <div>
            <el-col :span="10" class="label-all" style="margin-bottom: 20px">
              <span>任务进度:</span>
            </el-col>
            <el-col :span="12" class="label-all">
              <el-progress :text-inside="true" :stroke-width="18" :percentage="detailInfo.totalProgress * 100"></el-progress>
            </el-col>
          </div>
        </div>
      </el-card>
    </el-col>
    <!--图钉-->
    <el-col :span="3">
      <img src="../images/ping.png" height="60" width="60" style="margin-left: -38px; margin-top: -5px">
    </el-col>
  </el-col>
  </div>
</template>

<script>
  export default {
    name: "one-task",

    props: ['detailInfo', 'uid']
  }
</script>

<style scoped>
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

  .box-card {
    margin-top: 20px;
    background-color: #f8ffff
  }

  .label-all {
    font-size: 14px;
    margin-top: 12px;
    color: #4f4f52;
  }
  /*任务ID单独要放大显示*/
  .taskIDLabel {
    color: #e44030;
    font-size: 30px;
    font-weight: bolder;
    font-family: Chalkboard
    /*font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;*/
  }

  .label_detail {
    text-align: right;
    margin-right: 10px;
    margin-top: 12px;
  }
</style>
