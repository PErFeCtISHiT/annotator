<template>
  <div id="distributeNewTask">
    <el-form :model="newTask" status-icon :rules="myRule" ref="newTask">
      <el-form-item :inline="true" label="任务名称" prop="proName">
        <el-input v-model="newTask.taskName" auto-complete="false" clearable></el-input>
      </el-form-item>

      <el-form-item label="任务描述" prop="proDescription">
        <el-input type="text" v-model="newTask.taskDescription" auto-complete="false" clearable maxlength=100></el-input>
      </el-form-item>

      <el-form-item label="任务类型标签" prop="proTag">
        <el-checkbox-group v-model="newTask.checkedTags">
          <el-checkbox v-for="tag in tags" :label="tag" :key="tag">{{ tag }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

    </el-form>

    <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
  </div>
</template>

<script>
  import ElCheckbox from "element-ui/packages/checkbox/src/checkbox";

  const myTags = ['A', 'B', 'C', 'D', 'E'];

  export default {
    components: {ElCheckbox},
    name: "distribute-new-task",

    data() {
      let checkDate = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('年龄不能为空'));
        }else{
          callback();
        }

      };

      return {
        tags: myTags,
        active: 1,
        newTask: {
          taskName: "" ,
          taskDescription: "",
          checkedTags: [],
          taskStartDate: "",
          taskEndDate: "",
          workerLevel: 0,
          expectedNumber: 0,
          points: 0
        },
        myRule: {
          startDate: [
            { validator: checkDate, trigger: 'blur'}
          ]
        }
      };
    },

    methods: {

    }

  }
</script>

<style scoped>

</style>
