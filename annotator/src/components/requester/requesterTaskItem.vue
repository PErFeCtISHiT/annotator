<template>


  <el-col :span="8" class="slot-border">

    <!--详细信息部分-->
    <el-dialog title="工人信息" :visible.sync="dialogTableVisible" center>
      <el-dialog
        title="整合结果"
        :visible.sync="innerVisible"
        width="80%"
        append-to-body>

        <div>
          <marks-viewer :taskID="taskMsg.taskID"></marks-viewer>
        </div>
      </el-dialog>


      <div style="alignment: center">
        <el-table
          :data="workers"
          height="300"
          style="margin-left: 24%; width: 60%;">

          <el-table-column label="用户名" width="180">
            <template slot-scope="scope">
              <i class="el-icon-info"></i>
              <span style="margin-left: 10px">{{ scope.row.username }}</span>
            </template>
          </el-table-column>

          <el-table-column label="等级" width="200">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                <p>等级: {{ scope.row.lev }}</p>
                <div slot="reference" class="name-wrapper">
                  <el-rate v-model="scope.row.lev" disabled></el-rate>
                </div>
              </el-popover>
            </template>
          </el-table-column>

        </el-table>
      </div>

      <el-button type="primary" style="margin-left: 40%; margin-top: 20px" @click="handleViewDetail">查看整合标注结果</el-button>


    </el-dialog>

    <!-- 最外面一层是给引用它的都看的 -->
    <div>
      <el-col :span="24">

        <!-- 主体 -->
        <el-col :span="21">
          <el-card color="#EAF7E7" class="box-card">


            <div slot="header" class="clearfix">
              <el-col :span="7">
                <img src="../../images/task.png" style="width: 55px; height: 55px"/>
              </el-col>
              <el-col :span="17">
                <!--<el-button type="text" @click="jump">-->
                <div style="font-size: larger;">任务名称</div>
                <div
                  style="text-align: center; margin-right: 20px; margin-top: 14px; font-size: larger; font-weight: bolder;">
                  {{ taskMsg.taskName }}
                </div>
                <!--</el-button>-->
              </el-col>
              <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
            </div>

            <div>
              <div>
                <el-col :span="8" style="margin-top: 23px">
                  <span class="label-all">任务编号:</span>
                </el-col>
                <el-col :span="13" class="label-all" style="text-align: right">
                  <span class="taskIDLabel">NO.</span>
                  <span class="taskIDLabel" id="taskID">{{ taskMsg.taskID }}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="8" class="label-all" style="margin-top: 45px">
                  <span>任务描述:</span>
                </el-col>
                <el-col :span="15" class="label-all label_detail">
                  <div style="text-align: center; margin: auto; height: 105px;
                  box-shadow: 5px 5px 5px #9d9d9d; padding: 5px 5px 5px 5px; background-color: rgba(223,248,251,0.35)">
                    <span style="padding-top: 10px">{{ taskMsg.description }}</span>
                  </div>
                </el-col>
              </div>

              <div>
                <el-col :span="8" class="label-all" style="margin-top: 30px">
                  <span>任务标签:</span>
                </el-col>
                <el-col :span="14" class="label-all label_detail">
                  <el-tag v-for="theTag in taskMsg.tag" style="margin: 10px 10px 10px 10px">{{ theTag }}</el-tag>&thinsp;
                </el-col>
              </div>

              <div>
                <el-col :span="10" class="label-all">
                  <span>发布时间:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span>{{ taskMsg.startDate.split(" ")[0] }}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="10" class="label-all">
                  <span>结束时间:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span>{{ taskMsg.endDate.split(" ")[0] }}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="10" class="label-all">
                  <span>图片数量:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span>{{ taskMsg.imgNum}}</span>
                </el-col>
              </div>

              <!--<div>-->
              <!--<el-col :span="10" class="label-all" style="margin-bottom: 20px">-->
              <!--<span>任务进度:</span>-->
              <!--</el-col>-->
              <!--<el-col :span="12" class="label-all">-->
              <!--<el-progress :text-inside="true" :stroke-width="18" :percentage="taskMsg.totalProgress.toFixed(2) * 100" :status="taskMsg.totalProgress===1?'success':''"></el-progress>-->
              <!--</el-col>-->
              <!--</div>-->
            </div>


            <el-col :span="24" style="margin-bottom: 20px; margin-top:23px">
              <el-col :span="6" :offset="2">
                <el-button type="warning" size="medium" @click="handleAgg">标注详情</el-button>
              </el-col>


              <el-col :span="6" :offset="5">
                <el-popover placement="top-start" trigger="hover"
                            title="温馨小贴士" content="任务还在分发标注中哦，请耐心等待。"
                            width="200"
                            :disabled="!isDisabled">

                  <div slot="reference">
                    <el-button type="primary" size="medium" @click="handleDw" :disabled="isDisabled">
                      下载数据集
                    </el-button>
                  </div>
                </el-popover>

              </el-col>
            </el-col>


          </el-card>

        </el-col>


        <!--图钉-->
        <el-col :span="3">
          <img src="../../images/ping.png" height="50" width="50" style="margin-left: -38px; margin-top: -20px">
        </el-col>
      </el-col>
    </div>

  </el-col>


</template>

<script>
  import marksViewer from './marksViewer'

  const workerMock = [
    {
      username: 'somnus',
      lev: 2
    },
    {
      username: 'keith',
      lev: 3
    },
    {
      username: 'whiskey',
      lev: 3
    },
    {
      username: 'somnus',
      lev: 2
    },

  ];

  export default {
    components: {marksViewer},
    name: "requester-task-item",
    props: ['taskMsg', 'theIndex'],

    component:{
      marksViewer
    },

    data() {
      return {
        workers: {},
        dialogTableVisible: false,
        innerVisible: false
      }
    },

    computed: {
      isDisabled() {
        return this.taskMsg.status === 1;
      }
    },

    methods: {
      handleAgg() {
        //console.log('2line----------------------------',this.theIndex, this.taskMsg.taskID, 'finish--------------');

        let that = this;

        this.$http.get('/task/checkTaskDetail', {
          params: {
            taskID: this.taskMsg.taskID,
          }
        })
          .then(function (response) {
            that.workers = response.data.workerInfo;
            //that.workers = workerMock;
            that.dialogTableVisible = true;
          })
          .catch(function (error) {
            that.$message({
              message: '网络请求失败' + error,
              type: 'warning'
            });
            console.log('网络请求错误');
          });
      },

      handleDw() {
        //console.log('2line----------------------------',this.theIndex, this.taskMsg.taskID, 'finish--------------');
        this.$emit('download', {
          uid: this.taskMsg.taskID,
          //status: this.taskMsg.
        });
      },

      handleViewDetail(){
        this.innerVisible = true;
      },

      jump() {
        this.$router.push({
          name: 'taskDetail',
          params: {
            taskID: this.taskMsg.taskID
          }
        });
      }

    }

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
    background-color: #f8ffff
  }

  .label-all {
    font-size: 14px;
    margin-top: 15px;
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
    margin-top: 20px;
  }

  .slot-border {
    margin-bottom: 50px;
  }
</style>
