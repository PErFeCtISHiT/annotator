<template>
  <div id="distributeNewTask">

    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="12"><div class="grid-content bg-purple-light main-div">

        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="7"><div class="grid-content bg-purple-light">
            <span id="title">新任务</span>
          </div></el-col>
        </el-row>
        <br>

        <el-form :model="newTask" status-icon :rules="myRule" ref="newTask">

          <el-form-item label="任务名称" prop="taskName">
            <el-input type="text" v-model="newTask.taskName" clearable style="width: 500px"></el-input>
          </el-form-item>

          <el-form-item label="任务描述" prop="taskDescription">
            <el-input type="text" v-model="newTask.taskDescription" auto-complete="false" clearable style="width: 500px"
                      maxlength=100></el-input>
          </el-form-item>

          <el-form-item label="任务类型标签" prop="checkedTags">
            <el-checkbox-group v-model="newTask.checkedTags">
              <el-checkbox v-for="tag in tags" :label="tag" :key="tag">{{ tag }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="开始时间" prop="taskStartDate">
            <el-date-picker type="date" v-model="newTask.taskStartDate" placeholder="请选择开始时间" style="width: 500px" ref="startTimePicker"
                            :picker-options="option1" @change="getSTime" format="yyyy-MM-dd"></el-date-picker>
          </el-form-item>

          <el-form-item label="结束时间" prop="taskEndDate">
            <el-date-picker type="date" v-model="newTask.taskEndDate" placeholder="请选择结束时间" style="width: 500px" ref="endTimePicker"
                            :picker-options="option2" @change="getETime" format="yyyy-MM-dd"></el-date-picker>
          </el-form-item>

          <el-form-item label="参与人数" prop="expectedNumber">
            <el-input type="text" v-model.number="newTask.expectedNumber" auto-complete="false" clearable style="width: 500px"></el-input>
          </el-form-item>

          <el-form-item label="最低工人等级">
            <el-rate v-model="newTask.workerLevel" style="margin-top: 10px"></el-rate>
          </el-form-item>

          <el-form-item label="奖励积分" prop="points">
            <el-input type="text" v-model.number="newTask.points" auto-complete="false" clearable style="width: 500px"></el-input>
          </el-form-item>

          <el-form-item>
            <el-upload multiple :limit="20" :on-exceed="handleExceed" ref="upload"
                       action="dummy" :auto-upload="false"
                       :file-list="newTask.fileList" list-type="" accept=".jpg,.png,.jpeg,.zip"
                       :beforeRemove="beforeRemove" :http-request="uploadImage"
                       show-file-list
                       style="width: 580px">

              <el-button size="medium" type="primary">点击上传</el-button>
              <div slot="tip">上传jpg/png文件，不超过20张。或选择只上传一个zip压缩文件</div>
            </el-upload>
          </el-form-item>

          <br>
          <el-form-item>
            <el-row :gutter="20">
              <el-col :span="6" :offset="6"><div>
                <el-button type="primary" @click="submitForm('newTask')">提交</el-button>
              </div></el-col>
              <el-col :span="6"><div>
                <el-button @click="resetForm('newTask')">重置</el-button>
              </div></el-col>
            </el-row>
          </el-form-item>

        </el-form>


      </div></el-col>
    </el-row>
  </div>
</template>

<script>
  import {mapActions} from 'vuex'

  const myTags = ['A', 'B', 'C', 'D', 'E'];
  let taskID = -100;

  export default {
    components: {

    },
    name: "distribute-new-task",

    data() {
      //某些函数内部修正指向问题
      let that = this;

      let checkDate = (rule, value, callback) => {
        let sd = this.$refs.endTimePicker.value;
        if (!sd) {
          callback();
        }else if (value >= sd) {
          return callback(new Error('不能晚于结束日期'));
        } else {
          callback();
        }
      };

      let checkPoints = (rule, value, callback) => {
        if(value > this.$store.state.user.userInfo.points)
          return callback(new Error("超出现有金额"));
        else
          callback();
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
            return time.getTime() <= sd || time.getTime() < Date.now();
          }
        },

        //表单的数值
        newTask: {
          taskName: "",
          taskDescription: "",
          checkedTags: [],
          taskStartDate: '',
          taskEndDate: '',
          expectedNumber: 0,
          workerLevel: 0,
          points: 0,
          fileList: []
        },



        //表单的验证规则
        myRule: {
          taskName: [
            { required: true, message: '请输入名称', trigger: 'blur'}
          ],
          taskDescription: [
            { required: true, message: '请输入描述', trigger: 'blur' }
          ],
          checkedTags: [
            { type: 'array', required: true, message: '请至少选择一个类别', trigger: 'change' }
          ],
          taskStartDate: [
            { validator: checkDate, trigger: 'blur' },
            { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
          ],
          taskEndDate: [
            { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
          ],
          expectedNumber: [
            { required: true, message: '预期参与人数不能为空'},
            { type: 'number', message: '预期参与人数必须为数字值'}
          ],
          points: [
            { required: true, message: '奖励积分不能为空'},
            { type: 'number', message: '奖励积分必须为数字值'},
            { validator: checkPoints, trigger: 'blur' }
          ]
        }
      };
    },

    methods: {
      ...mapActions(['updateWithoutPointer']),
      ...mapActions(['logOut']),

      //神奇的时间转换函数。没有她格式就不对……
      getSTime(val) {
        this.newTask.startDate = val;
      },

      getETime(val) {
        this.newTask.endDate = val;
      },

      submitForm: function (formName) {
        let that = this;

        //表单验证全部通过就发ajax请求
        this.$refs[formName].validate((valid) => {
          if(valid){
            console.log()
            this.$http.post('/task/releaseTask', {
              sponsorName: that.$store.state.user.userInfo.username,
              taskName: that.newTask.taskName,
              description: that.newTask.taskDescription,
              tag: that.newTask.checkedTags,
              startDate: that.newTask.startDate.getFullYear() + "-" + (that.newTask.startDate.getMonth()+1) + "-" + that.newTask.startDate.getDate(),
              endDate: that.newTask.endDate.getFullYear() + "-" + (that.newTask.endDate.getMonth()+1) + "-" + that.newTask.endDate.getDate(),
              workerLevel: that.newTask.workerLevel,
              expectedNumber: that.newTask.expectedNumber,
              points: that.newTask.points
            })
              .then(function (response) {
                //检查返回信息

                console.log('看看id是几',response.data.taskID);
                if(response.data.mes === true){
                  taskID = response.data.taskID;   //设定任务的id属性值
                  that.$refs.upload.submit();  //发送ajax请求
                }

                else {
                  that.$message.warning('上传失败');
                  console.log('2')
                }
              })
              .catch(function (error) {
                that.$message({
                  message: '上传失败' + error,
                  type: 'warning'
                });
                console.log('1');
              })

            /*console.log(this.newTask.taskName + " " + this.newTask.taskDescription);
            console.log(this.newTask.checkedTags);
            console.log(this.convertDate(this.newTask.taskStartDate));
            console.log(this.newTask.expectedNumber);
            console.log(this.newTask.workerLevel);
            console.log(typeof this.newTask.workerLevel);
            this.newTask.fileList.forEach(file => console.log(file.toString()));*/
          }else {
            this.$message({
              message: "您填写的内容不合规范",
              type: 'warning'
            });
          }
        })
      },

      resetForm (formName) {
        this.newTask.workerLevel = 0;
        this.$refs[formName].resetFields();
      },

      handleExceed (files, fileList) {
        this.$message.warning(`当前限制选择 20 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },

      beforeRemove (file) {
        return this.$confirm(`确定移除${ file.name }`)
      },

      uploadImage: function (item) {
        let that = this;

        let formData = new FormData();
        formData.append('file', item.file);
        formData.append('taskID', taskID + "");

        this.$http.request({
          method: 'post',
          data: formData,
          url: 'task/imagesUpload',
          headers: {'content-type': 'multipart/form-data'}
        })
          .then(function (response) {
            if (response.data.mes === true) {
              that.updateWithoutPointer();
            }
            else {
              that.$message.warning('图片上传失败。可能由网络引起');
              console.log('3');
            }
          })
          .catch(function (error) {
            that.$message({
              message: '图片上传失败' + error,
              type: 'warning'
            });
            console.log('4');
          });
      }

    }

  }
</script>

<style scoped>
  #title {
    font-family: Arial;
    font-size: xx-large;
  }

  .main-div {

  }

  #distributeNewTask {

  }
</style>
