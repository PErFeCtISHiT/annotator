<template>


  <el-col :span="8" class="slot-border">
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
                <div style="font-size: larger;">任务名称</div>
                <div
                  style="text-align: center; margin-right: 20px; margin-top: 14px; font-size: larger; font-weight: bolder;">
                  {{ taskMsg.taskName }}
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
                  <span class="taskIDLabel" id="taskID">{{ taskMsg.taskID }}</span>
                </el-col>
              </div>
              <div>
                <el-col :span="8" class="label-all" style="margin-top: 45px">
                  <span>任务描述:</span>
                </el-col>
                <el-col :span="14" class="label-all label_detail"
                        style="text-align: left; height: 50px; background-color: #d3dce6">
                  <span>{{ taskMsg.description }}</span>
                </el-col>
              </div>
              <div>
                <el-col :span="8" class="label-all" style="margin-top: 17px">
                  <span>任务标签:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span v-for="theTag in taskMsg.tag">
                    <el-tag>{{ theTag }}</el-tag>&thinsp;
                  </span>
                </el-col>
              </div>
              <div>
                <el-col :span="10" class="label-all">
                  <span>发布时间:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span>{{(taskMsg.startDate)?taskMsg.startDate.split(' ')[0]:""}}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="10" class="label-all">
                  <span>结束时间:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span>{{(taskMsg.endDate)?taskMsg.endDate.split(' ')[0]:""}}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="10" class="label-all">
                  <span>任务奖励:</span>
                </el-col>
                <el-col :span="12" class="label-all label_detail">
                  <span>{{taskMsg.points}}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="16" class="label-all">
                  <span>标注者最低等级:</span>
                </el-col>
                <el-col :span="6" class="label-all label_detail" :style="$store.state.user.userInfo.level<taskMsg.workerLevel?'color: #e44030;':''">
                  <span>{{taskMsg.workerLevel}}</span>
                </el-col>
              </div>

              <div>
                <el-col :span="10" class="label-all" style="margin-bottom: 20px">
                  <span>任务进度:</span>
                </el-col>
                <el-col :span="12" class="label-all">
                  <el-progress :text-inside="true" :stroke-width="18"
                               :percentage="taskMsg.totalProgress.toFixed(2) * 100"> </el-progress>
                </el-col>
              </div>
            </div>




            <el-col :span="24" style="margin-bottom: 20px; margin-top:5px">
              <el-col :span="6" :offset="2">
                <el-button type="primary" size="small" @click="handleFinish"
                           :disabled="taskMsg.totalProgress >= 0.999||($store.state.user.userInfo.level<taskMsg.workerLevel)">
                  接受任务
                </el-button>
              </el-col>

              <el-col :span="6" :offset="5">
                <el-button size="small" @click="handleDelete">移出视图</el-button>
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

  export default {
    name: "worker-task-item",
    props: ['taskMsg', 'theIndex'],

    mounted(){
      console.log(this.taskMsg.totalProgress);
    },

    methods: {
      handleDelete() {
        this.$emit('remove');
      },

      handleFinish() {
        this.$emit('accept', this.taskMsg.taskID, this.theIndex);
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
    margin-top: 20px;
  }

  .slot-border {
    margin-bottom: 50px;
  }
</style>
