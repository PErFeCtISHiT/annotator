<template>
  <div>
    <el-button type="text" @click="dialogVisible = true">点击打开 Dialog</el-button>

    <el-dialog title="投诉举报" :visible.sync="dialogVisible">
      <el-form ref="reqComplaint" :model="complaintInfo" label-width="100px">
        <el-form-item label="举报原因" prop="reason"
                      :rules="[
                                    { required: true, message: '举报信息不能为空', trigger: 'change'}
                                ]">
          <el-input type="textarea" column="20" v-model="complaintInfo.content" clearable
                    style="width: 500px"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitComplaint">提交</el-button>
      </div>
  </div>
</template>

<script>
  export default {
    name: "requestor-wanna-see",

    mounted() {
      console.log(this.workerName, this.taskID, 'ok!!!!');
    },

    props: ['workerName', 'taskID'],

    data() {
      return {
        dialogVisible: false,
        complaintInfo: {
          content: "sadsad"
        }
      };
    },
    methods: {
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      }
    }
  }
</script>

<style scoped>

</style>
