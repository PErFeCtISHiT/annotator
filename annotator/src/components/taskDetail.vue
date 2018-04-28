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
        :data="workers"
        height="300"
        style="width: 100%">

        <el-table-column
          label="用户名"
          width="180">
          <template slot-scope="scope">
            <i class="el-icon-info"></i>
            <span style="margin-left: 10px">{{ scope.row.username }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="等级"
          width="200">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>姓名: {{ scope.row.level }}</p>
              <div slot="reference" class="name-wrapper">
                <el-rate v-model="scope.row.level"></el-rate>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
          label="进度"
          width="350">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.completedNumber * 100 / response.imgNum" status="success"></el-progress>
          </template>

        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleComplain(scope.$index, scope.row)">举报</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleView(scope.row)">查看</el-button>
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
        workers: [],
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
          that.workers = response.data.workerInfo;

          console.log('response:---------\n');
          console.log(that.response);
        })
        .catch(function (error) {
          that.$message({
            message: '网络请求失败' + error,
            type: 'warning'
          });
          console.log('网络请求错误');
        })
    },

    methods: {
      handleView(row){
        this.$router.push({
          name: 'forTest',
          params: {
            taskID: this.response.taskID,
            workerName: row.username
          }
        });
      }
    }
  }
</script>

<style scoped>

</style>
