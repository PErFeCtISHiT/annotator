<template>
  <el-col :span="24">
    <!--任务详情-->
    <el-col :span="6">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>任务名称: &nbsp;</span>
          <span>人物标注</span>
          <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
        </div>
        <div class="text">
          <div class="text">
            <span>任务名称: &nbsp;</span>
            <span>人物标注</span>
          </div>
          <div class="text">
            <span>任务描述: &nbsp;</span>
            <span>标出图片中的人</span>
          </div>
          <div class="text">
            <span>任务标签: &nbsp;</span>
            <el-tag type="success">人</el-tag>
            <el-tag type="warning">旅游</el-tag>
          </div>
          <div class="text">
            <span>发布时间: &nbsp;</span>
            <span>2018-04-23</span>
          </div>
          <div class="text">
            <span>结束时间: &nbsp;</span>
            <span>2018-05-02</span>
          </div>
          <div class="text">
            <span>图片数量: &nbsp;</span>
            <span>13</span>
          </div>
          <div class="text">
            <span>要求人数: &nbsp;</span>
            <span>30</span>
          </div>
          <div class="text">
            <span>任务奖励: &nbsp;</span>
            <span>12</span>
          </div>
          <div class="text">
            <span>已接受人数: &nbsp;</span>
            <span>30</span>
          </div>
          <div class="text">
            <span>已完成人数: &nbsp;</span>
            <span>20</span>
          </div>
          <div class="text">
            <span>任务进度: &nbsp;</span>
            <span>66%</span>
          </div>
        </div>
      </el-card>
    </el-col>
    <!--用户列表-->
    <el-col :span="17" :offset="1">
      <el-table
        :data="reportTableData"
        class="normal-table"
        height="300"
      >
        <el-table-column
          prop="reportTime"
          label="投诉时间"
          width="190">
        </el-table-column>
        <el-table-column
          prop="reporter"
          label="投诉人"
          width="190">
        </el-table-column>
        <el-table-column
          prop="respondent"
          label="被投诉人"
          width="190">
        </el-table-column>
        <el-table-column
          prop="taskID"
          label="投诉任务编号"
          width="190">
        </el-table-column>
        <el-table-column
          prop="description"
          label="投诉内容"
          width="250"
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          align="center"
          width="100">
          <template slot-scope="scope">
            <el-button @click="" type="text" size="small">处理</el-button>
            <el-button type="text" size="small">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-col>
  </el-col>

</template>

<script>
  export default {
    name: "task-detail"
  }
</script>

<style scoped>
  .text {
    font-size: 15px;
    margin: 10px 20px 10px 5px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
</style>
