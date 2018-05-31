<template>
  <div class="grid-bg row-bg3">
    <div class="big-label">任务详情</div>
    <el-table :data="info" class="normal-table" height="300">
      <el-table-column label="任务编号" width="150">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top-start">
            <div style="width: 350px">
              <span>任务描述：{{ scope.row.description }}</span><br>
              <span>开始时间：{{ scope.row.startDate }}</span><br>
              <span>结束时间：{{ scope.row.endDate }}</span><br>
            </div>

            <div slot="reference">
              <span>{{ scope.row.taskID }}</span>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column prop="taskName" label="任务名称" width="220">
      </el-table-column>

      <el-table-column prop="sponsorName" label="发起人" width="220">
      </el-table-column>

      <el-table-column label="工人等级要求" width="250">
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <p>工人等级要求为: {{ scope.row.lev }}级</p>

            <div slot="reference" class="name-wrapper">
              <el-rate v-model="scope.row.lev" disabled></el-rate>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="任务总体进展" width="250">
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <p>预期任务完成量: {{ scope.row.expectedNumber }}</p>
            <p>当前完成量: {{ scope.row.completedNumber }}</p>

            <div slot="reference" class="name-wrapper">
              <el-progress progress="scope.row.totalProgress"></el-progress>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column prop="points" label="积分奖励" width="130">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

  export default {
    name: "admin-task-detail",

    mounted(){
      console.log('info',this.info);
    },

    props: ['info'],

    data() {
      return {

      }
    },

    methods: {

    }
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

  .row-bg2 {
    height: 400px;
  }

  .row-bg3 {
    height: 400px;
  }

  .big-label {
    margin-left: 30px;
    margin-top: 10px;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    font-size: 25px;
    font-weight: bold;
    color: #464646;
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

  /*表格*/
  .normal-table {
    width: 90%;
    margin-left: 5%;
    margin-top: 20px;

  }
</style>
