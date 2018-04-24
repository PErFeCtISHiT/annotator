<template>
  <div id="distributeNewTask">

    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="6"><div class="grid-content bg-purple-light">
        <span id="title">新任务</span>
      </div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple-light"></div></el-col>
    </el-row>
    <br>

    <el-form :model="newTask" status-icon :rules="myRule" ref="newTask">

      <el-form-item label="任务名称" prop="proName">
        <el-input type="text" v-model="newTask.taskName" clearable style="width: 500px"></el-input>
      </el-form-item>

      <el-form-item label="任务描述" prop="proDescription">
        <el-input type="text" v-model="newTask.taskDescription" auto-complete="false" clearable style="width: 500px"
                  maxlength=100></el-input>
      </el-form-item>

      <el-form-item label="任务类型标签" prop="proTag">
        <el-checkbox-group v-model="newTask.checkedTags">
          <el-checkbox v-for="tag in tags" :label="tag" :key="tag">{{ tag }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="开始时间" prop="proStartTime">
        <el-date-picker v-model="newTask.taskStartDate" placeholder="请选择开始时间" style="width: 500px" ref="startTimePicker"
                        :picker-options="option1"></el-date-picker>
      </el-form-item>

      <el-form-item label="结束时间" prop="proEndTime">
        <el-date-picker v-model="newTask.taskEndDate" placeholder="请选择结束时间" style="width: 500px"
                        :picker-options="option2"></el-date-picker>
      </el-form-item>

      <el-form-item label="参与人数" prop="proNumber">
        <el-input type="text" v-model.number="newTask.expectedNumber" auto-complete="false" clearable style="width: 500px"></el-input>
      </el-form-item>

      <el-form-item label="最低工人等级">
        <el-rate v-model="newTask.workerLevel" style="margin-top: 10px"></el-rate>
      </el-form-item>

      <el-form-item label="奖励积分" prop="proPoints">
        <el-input type="text" v-model.number="newTask.points" auto-complete="false" clearable style="width: 500px"></el-input>
      </el-form-item>

    </el-form>


  </div>
</template>

<script>
  import ElFormItem from "element-ui/packages/form/src/form-item";

  const myTags = ['A', 'B', 'C', 'D', 'E'];

  export default {
    components: {
      ElFormItem

    },
    name: "distribute-new-task",

    data() {
      //某些函数内部修正指向问题
      let that = this;

      let checkDate = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('年龄不能为空'));
        } else {
          callback();
        }

      };

      return {
        tags: myTags,
        /**
         * 这一段是给日期选择器设定参数的
         */
        option1: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 86400000;
          }
        },
        option2: {
          disabledDate(time) {
            let sd = that.$refs.startTimePicker.value;
            return time.getTime() <= sd || time.getTime() < Date.now() - 86400000;
          }
        },

        //表单的数值
        newTask: {
          taskName: "",
          taskDescription: "",
          checkedTags: [],
          taskStartDate: "",
          taskEndDate: "",
          workerLevel: 0,
          expectedNumber: 0,
          points: 0
        },

        //表单的验证规则
        myRule: {
          startDate: [
            {validator: checkDate, trigger: 'blur'}
          ]
        }
      };
    },

    methods: {


    }

  }
</script>

<style scoped>
  #title {
    font-family: Arial;
    font-size: xx-large;
  }

  #distributeNewTask {

  }
</style>
