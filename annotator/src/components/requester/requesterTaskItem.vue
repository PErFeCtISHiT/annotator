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
                <el-button type="warning" size="medium" @click="handleAgg" disabled>删除任务</el-button>
              </el-col>

              <el-col :span="6" :offset="5">
                <el-button type="primary" size="medium" @click="handleDw">下载数据集</el-button>
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
    name: "requester-task-item",
    props: ['taskMsg', 'theIndex'],

    computed: {
      isDisabled() {
        return this.taskMsg.totalProgress >= 0.999;
      }
    },

    methods: {
      handleAgg() {
        //console.log('2line----------------------------',this.theIndex, this.taskMsg.taskID, 'finish--------------');
        this.$emit('complete', this.taskMsg.taskID, this.theIndex);
      },

      handleDw() {
        //console.log('2line----------------------------',this.theIndex, this.taskMsg.taskID, 'finish--------------');
        this.$emit('download', {
          uid: this.taskMsg.taskID,
          //index: this.theIndex
        });
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
