<template>
  <el-col :span="24">
    <!--任务详情-->
    <el-col :span="7">
      <el-row type="flex" id="navigation-div2" justify="left">
        <el-col :span="24"><div>
          <oneTask :detailInfo="response" :uid="taskID"></oneTask>
        </div></el-col>
      </el-row>
    </el-col>

    <!--用户列表-->
    <el-col :span="17">
      <el-table
        :data="reportTableData"
        height="470"
      >
        <el-table-column
          prop="username"
          label="用户名"
          align="center"
          width="200">
        </el-table-column>
        <el-table-column
          prop="userLevel"
          label="用户等级"
          align="center"
          width="120">
        </el-table-column>

        <el-table-column
          prop="completeNum"
          align="center"
          label="已完成数量"
          width="120">
          <!--可以加进度条-->
          <!--<el-progress :text-inside="true" :stroke-width="18" :percentage="66.7" id="progress"></el-progress>-->
        </el-table-column>
        <el-table-column
          label="投诉"
          align="center"
          width="120">
          <el-button
            size="small"
            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        </el-table-column>
        <el-table-column
          label="查看详情"
          align="center"
          width="120">
          <template slot-scope="scope">
            <el-button @click="" type="text" size="small">处理</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-col>
  </el-col>

</template>

<script>
  import oneTask from './oneTask'

  export default {
    components: {
      oneTask
    },

    props: ['taskID'],

    data () {
      return {
        response: {},
      }
    },

    mounted () {
      let that = this;
      //console.log(this.$route.params.taskID);
      console.log(this.taskID);
      this.$http.get('/task/checkTaskDetail', {
        params: {
          taskID: this.taskID,
        }
      })
        .then(function (response) {
          that.response = response.data;
          console.log('response:---------\n');
        })
        .catch(function (error) {
          that.$message({
            message: '网络请求失败' + error,
            type: 'warning'
          });
          console.log('网络请求错误');
        })
    },

  }
</script>

<style scoped>

</style>
