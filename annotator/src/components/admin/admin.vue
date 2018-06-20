<template>
  <div>
    <el-row>
      <el-col :span="12">
        <adminUserStat :info="userStat"></adminUserStat>
      </el-col>

      <el-col :span="12">
        <adminTaskStat :info="taskStat"></adminTaskStat>
      </el-col>
    </el-row>
    <!-- 总体信息部分结束 -->


    <br>
    <el-row>
      <el-col :span="24">
        <div class="grid-bg row-bg2">
          <!--标题-->
          <div class="big-label">用户详情</div>

          <el-table :data="userDetail" class="normal-table" height="300" style="width: 90%">

            <el-table-column prop="username" label="用户名" width="160">
            </el-table-column>

            <el-table-column prop="name" label="昵称" width="160">
            </el-table-column>

            <el-table-column label="角色" width="160">
              <template slot-scope="scope">
                <span v-for="oneRole in scope.row.role">
                  <el-tag type="info">{{ myShift(oneRole) }}</el-tag>&thinsp;
                </span>
              </template>
            </el-table-column>

            <el-table-column label="等级" width="220">
              <template slot-scope="scope">
                <el-popover trigger="click" placement="top">
                  <p>等级为: {{ scope.row.lev }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-rate v-model="scope.row.lev" disabled></el-rate>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

            <el-table-column prop="points" label="积分" width="80">
            </el-table-column>


            <el-table-column label="操作" width="280" fixed="right">
              <template slot-scope="scope">
                <el-button type="primary" size="medium" disabled>修改积分</el-button>
                <el-button type="danger" size="medium" @click="dealDelete(scope.$index, scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <!-- 用户信息结束 -->
    <br>

    <el-row>
      <el-col :span="24">
        <adminTaskDetail :info="taskDetail"></adminTaskDetail>
      </el-col>
    </el-row>
    <!--任务信息结束-->


    <!--<el-row>-->
    <!--<el-col :span="24">-->
    <!--<div class="grid-bg row-bg3">-->
    <!--<div class="big-label">投诉详情</div>-->
    <!--<el-table :data="reportTableData" class="normal-table" height="300">-->
    <!--<el-table-column prop="reportTime" label="投诉时间" width="190">-->
    <!--</el-table-column>-->
    <!--<el-table-column prop="reporter" label="投诉人" width="190">-->
    <!--</el-table-column>-->
    <!--<el-table-column prop="respondent" label="被投诉人" width="190">-->
    <!--</el-table-column>-->
    <!--<el-table-column prop="taskID" label="投诉任务编号" width="190">-->
    <!--</el-table-column>-->
    <!--<el-table-column prop="description" label="投诉内容" width="250"-->
    <!--&gt;-->
    <!--</el-table-column>-->
    <!--<el-table-column fixed="right" label="操作" align="center"-->
    <!--width="100">-->
    <!--<template slot-scope="scope">-->
    <!--<el-button @click="" type="text" size="small">处理</el-button>-->
    <!--<el-button type="text" size="small">拒绝</el-button>-->
    <!--</template>-->
    <!--</el-table-column>-->
    <!--</el-table>-->
    <!--</div>-->
    <!--</el-col>-->
    <!--</el-row>-->

    <!--投诉信息结束-->
  </div>
</template>

<script>
  import adminTaskStat from './adminTaskStat'
  import adminUserStat from './adminUserStat'
  import adminTaskDetail from './adminTaskDetail'

  export default {
    //id
    //userNum 用户总数 workerNum 工人总数 requestorNum 发起者总数
    //taskNum 任务总数 processNum 进行中任务数 finishNum 已结束任务数
    //userTableData 用户信息表 taskTableDate 任务信息表 reportTableData 举报信息表
    components: {
      adminTaskDetail,
      adminUserStat,
      adminTaskStat,
    },

    /**
     * 1.请求用户基本统计
     * 2.请求任务基本统计
     * 3.操作任务
     * 4.操作用户*/
    mounted() {
      let that = this;
      //1
      this.$http.get('/statistic/checkUserNum')
        .then(function (response) {
          that.userStat = response.data;
          //console.log('user', response.data);
        })
        .catch(function (error) {
          that.$message.warning('请求服务器数据失败' + error)
        });

      //2
      this.$http.get('/statistic/checkTaskNum')
        .then(function (response) {
          that.taskStat = response.data;
          //console.log('task', response.data);
        })
        .catch(function (error) {
          that.$message.warning('请求服务器数据失败' + error)
        });

      //3
      this.$http.get('/user/checkUser')
        .then(function (response) {
          that.userDetail = response.data.users;
          //console.log(that.userDetail);
        })
        .catch(function (error) {
          that.$message.warning('请求服务器数据失败' + error)
        });

      //4
      this.$http.post('/task/allTasks', {
        userRole: 2,
        tag: "",
        status: 0
      })
        .then(function (response) {
          that.taskDetail = response.data.tasks;
          console.log('正常点', that.taskDetail);
        })
        .catch(function (error) {
          that.$message.warning('请求服务器数据失败' + error)
        });
    },

    data() {
      return {
        userStat: {},
        taskStat: {},
        userDetail: [],
        taskDetail: []
      }
    },

    methods: {
      myShift(oneRole) {
        if (oneRole === 2) return "管理员";
        else if (oneRole === 3) return "发起者";
        else if (oneRole === 4) return "工人";
      },

      dealDelete(index, row) {
        let that = this;

        this.$http.get('/user/deleteUser', {
          params: {
            username: row.username
          }
        })
          .then(function (response) {
            if (response.data.mes === true) {
              that.$message.success('删除成功');
              that.userDetail.splice(index, 1);
            } else {
              that.$message.warning('删除失败');
            }
          })
          .catch(function (error) {
            that.$message.warning('服务器连接异常。删除失败' + error)
          })
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
