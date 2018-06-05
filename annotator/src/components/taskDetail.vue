<template>
  <el-col :span="24">
    <!--任务详情-->
    <el-col :span="7">
      <el-row type="flex" id="navigation-div2" justify="left">
        <el-col :span="24">
          <div>
            <oneTask :detailInfo="response" :uid="taskID"></oneTask>
          </div>
        </el-col>
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
              <p>等级: {{ scope.row.lev }}</p>
              <div slot="reference" class="name-wrapper">
                <el-rate v-model="scope.row.lev" disabled></el-rate>
              </div>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column
          label="进度"
          width="350">
          <template slot-scope="scope">
            <el-progress :percentage="(scope.row.completedNumber/ response.imgNum).toFixed(2) * 100 " :status="scope.row.completedNumber/ response.imgNum===1?'success':''"> </el-progress>
          </template>

        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleComplain(scope.row)">举报
            </el-button>

            <el-dialog title="投诉举报" :visible.sync="dialogVisible" close-on-press-escape show-close="false" :before-close="handleClose">
              <el-form ref="reqComplaint" :model="complaintInfo" label-width="100px">
                <el-form-item label="举报原因">
                  <el-input type="textarea" column="22" row="4" v-model="complaintInfo.content" clearable
                            style="width: 500px"></el-input>
                </el-form-item>
              </el-form>

              <div slot="footer">
                <el-button type="primary" @click="submitComplaint('reqComplaint')">提交</el-button>
              </div>
            </el-dialog>

            <el-button
              size="mini"
              type="danger"
              @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="block" style="width:800px; height:200px; margin:auto">
        <el-button @click = "handleTotal(taskID)">查看整体标注结果</el-button>
        <!--<el-button :disabled="response.totalProgress!==1" @click="handleMixed">查看矩形整合结果</el-button>-->
      </div>

    </el-col>

  </el-col>

</template>

<script>
  import oneTask from './oneTask'

  export default {
    components: {
      oneTask,
    },

    props: ['taskID'],

    data() {
      return {
        response: {},
        workers: [],  // 自定义表格的内容
        dialogVisible: false,
        currentRowInfo: {
          respondent: ""
        },   //提交的时候得知道的被举报人信息
        complaintInfo: {
          content: ""
        }
      }
    },

    mounted() {
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
      handleView(row) {
        console.log(row.completedNumber);

        this.$router.push({
          name: 'forTest',
          params: {
            taskID: this.response.taskID,
            workerName: row.username
          }
        });
      },

      handleComplain(row) {
        this.dialogVisible = true;
        this.currentRowInfo = {
          respondent: row.username,
        }
      },


      submitComplaint(formName) {
        let that = this;

        this.$http.post('/task/reportWorker', {
          respondent: this.currentRowInfo.respondent,
          reporter: this.$store.state.user.userInfo.username,
          taskID: this.response.taskID,
          taskName: this.response.taskName,
          description: this.complaintInfo.content
        })
          .then(function (response) {
            if (response.data.mes === true) {
              that.$refs[formName].resetFields();
              that.$message.success('投诉成功');
              that.dialogVisible = false;

            } else {
              that.$message({
                type: 'warning',
                message: '网络异常，投诉举报失败'
              })
            }
          })
          .catch(function (error) {
            that.$message.error('网络异常，投诉举报失败' + error);
          })
      },

      handleClose(done) {
        let that = this;

        this.$confirm('确认关闭？')
          .then(() => {
            that.$refs.reqComplaint.resetFields();
            done();
          })
          .catch(() => {});
      },

      handleTotal(taskID){
        this.$router.push({
          name: 'forReTotal',
          params: {
            taskID: taskID,
          }
        });
      },
    }//所有方法结束

  }
</script>

<style scoped>

</style>
