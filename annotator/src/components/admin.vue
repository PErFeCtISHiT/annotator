<template>
    <div>
      <el-row>
        <el-col :span="12">
          <div class="grid-bg row-bg1">
            <div class="big-label">系统用户统计</div>
            <el-col :span="6">
              <img src="../images/user.png" class="im-big"/>
            </el-col>
            <el-col :span="6" :offset="2">
              <div class="total-label" style="font-size: 18px; color: dimgray; margin-top: 35px">总用户数</div>
              <div class="total-label" style="font-size: 30px; font-weight: bolder; alignment: center; margin-top: 10px" id="userNum">200</div>
            </el-col>
            <el-col :span="6" :offset="1">
              <div>
                <el-col :span="15">
                  <div class="normal-label">众包工人:</div>
                </el-col>
                <el-col :span="9">
                  <div class="normal-figure" id="workerNum">300</div>
                </el-col>
              </div>
              <div>
                <el-col :span="15">
                  <div class="normal-label">任务发起者:</div>
                </el-col>
                <el-col :span="9">
                  <div class="normal-figure" id="requestorNum">300</div>
                </el-col>
              </div>
            </el-col>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-bg row-bg1">
            <div class="big-label">系统任务统计</div>
            <el-col :span="6">
              <img src="../images/task.png" class="im-big"/>
            </el-col>
            <el-col :span="6" :offset="2">
              <div class="total-label" style="font-size: 18px; color: dimgray; margin-top: 35px">总任务数</div>
              <div class="total-label" style="font-size: 30px; font-weight: bolder; alignment: center; margin-top: 10px" id="taskNum">200</div>
            </el-col>
            <el-col :span="6" :offset="1">
              <div>
                <el-col :span="15">
                  <div class="normal-label">正在进行中:</div>
                </el-col>
                <el-col :span="9">
                  <div class="normal-figure" id="processNum">300</div>
                </el-col>
              </div>
              <div>
                <el-col :span="15">
                  <div class="normal-label">已结束:</div>
                </el-col>
                <el-col :span="9">
                  <div class="normal-figure" id="finishNum">300</div>
                </el-col>
              </div>
            </el-col>
          </div>
        </el-col>
      </el-row>

      <br>
      <el-row>
        <el-col :span="24">
          <div class="grid-bg row-bg2">
            <div class="big-label">用户详情</div>
            <el-table
              :data="userTableData"
              class="normal-table"
              height="300"
            >
              <el-table-column
                prop="username"
                label="用户名"
                width="300">
              </el-table-column>
              <el-table-column
                prop="name"
                label="昵称"
                width="300">
              </el-table-column>
              <el-table-column
                prop="role"
                label="角色"
                width="300">
              </el-table-column>
              <el-table-column
                prop="points"
                label="积分">
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>

      <br>

      <el-row>
        <el-col :span="24">
          <div class="grid-bg row-bg3">
            <div class="big-label">任务详情</div>
            <el-table
              :data="taskTableData"
              class="normal-table"
              height="300"
            >
              <el-table-column
                prop="taskID"
                label="任务编号"
                width="200">
              </el-table-column>
              <el-table-column
                prop="taskName"
                label="任务名称"
                width="250">
              </el-table-column>
              <el-table-column
                prop="sponsorName"
                label="任务发起者"
                width="250">
              </el-table-column>
              <el-table-column
                prop="startDate"
                label="开始时间"
                width="280">
              </el-table-column>
              <el-table-column
                prop="endDate"
                label="结束时间">
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <div class="grid-bg row-bg3">
            <div class="big-label">投诉详情</div>
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
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<script>
    export default {
      //id
      //userNum 用户总数 workerNum 工人总数 requestorNum 发起者总数
      //taskNum 任务总数 processNum 进行中任务数 finishNum 已结束任务数
      //userTableData 用户信息表 taskTableDate 任务信息表 reportTableData 举报信息表
      data() {
        return {
          tableData: [{
            name: '2016-05-02',
            role: '王小虎',
            points: '上海市普陀区金沙江路 1518 弄'
          }, {
            role: '2016-05-04',
            name: '王小虎',
            points: '上海市普陀区金沙江路 1517 弄'
          }, {
            role: '2016-05-01',
            name: '王小虎',
            points: '上海市普陀区金沙江路 1519 弄'
          }, {
            role: '2016-05-03',
            name: '王小虎',
            points: '上海市普陀区金沙江路 1516 弄'
          }, {
            role: '2016-05-03',
            name: '王小虎',
            points: '上海市普陀区金沙江路 1516 弄'
          }]
        }
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
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
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
  .total-label{
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }

  /*普通统计数据，正常字体*/
  .normal-label{
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
