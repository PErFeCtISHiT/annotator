<template>
  <div id="distributeNewTask">

    <!--这个vue文件写的耦合度太高了。。。 将就一下了-->
    <!--显示一个step步骤条-->
    <el-row>

      <el-col :span="2">
        <el-row type="flex" justify="center" align="middle">
          <el-button style="margin-top: 23px;" @click="pre" icon="el-icon-arrow-left" circle="">
          </el-button>
        </el-row>
      </el-col>

      <el-col :span="20">
        <el-steps :active="isActive" finish-status="success" simple style="margin-top: 20px">
          <el-step :title="intType<0?'选择标注类型':`第${intType}种标注方式`" style="margin-left: 10%">
          </el-step>
          <el-step :title="isActive===1?'编辑中':'待发布任务详情'" style="margin-right: 10%">
          </el-step>
        </el-steps>
      </el-col>

      <el-col :span="2">
        <el-row type="flex" justify="center" align="middle">
          <el-button style="margin-top: 23px;" @click="next" icon="el-icon-arrow-right" circle="">
          </el-button>
        </el-row>
      </el-col>

    </el-row>



    <!--第一层的标记选择开始-->
    <el-row v-show="isActive === 0" :gutter="40" type="flex" justify="center" align="middle" :style="contentStyle">
      <el-col :span="6" :style="singleCardStyle">
        <type-card
          :card-type="typeDraw"
          :core-msg="typeMsg1"
          :brief-description="briefDescription1"
          :example-description="exampleDescription1"
          :details="details1"
          :img-u-r-l="imgURL1"
          :preview-img-u-r-l="previewImgURL1"
          @choose-type="chooseIntType(1)">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeDraw"
          :core-msg="typeMsg2"
          :brief-description="briefDescription2"
          :example-description="exampleDescription2"
          :details="details2"
          :img-u-r-l="imgURL2"
          :preview-img-u-r-l="previewImgURL2"
          @choose-type="chooseIntType(2)">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeDraw"
          :core-msg="typeMsg3"
          :brief-description="briefDescription3"
          :example-description="exampleDescription3"
          :details="details3"
          :img-u-r-l="imgURL3"
          :preview-img-u-r-l="previewImgURL3"
          @choose-type="chooseIntType(3)">
        </type-card>
      </el-col>
    </el-row>






    <!--第二层的表单部分开始-->
    <el-row v-loading="isLoading" v-show="isActive === 1" type="flex" class="row-bg" justify="center" style="margin-left: 5%">
      <el-col :span="12">
        <div class="grid-content bg-purple-light main-div">

          <br><br>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="7">
              <div class="grid-content bg-purple-light">
                <span id="title">新任务</span>
              </div>
            </el-col>
          </el-row>
          <br><br>

          <el-form :model="newTask" status-icon :rules="myRule" ref="newTask">

            <el-form-item label="任务名称" prop="taskName">
              <el-input type="text" v-model="newTask.taskName" clearable style="width: 500px">
              </el-input>
            </el-form-item>

            <el-form-item label="任务描述" prop="taskDescription">
              <el-input type="text" v-model="newTask.taskDescription" auto-complete="false" clearable
                        style="width: 500px"
                        maxlength=50>
              </el-input>
            </el-form-item>

            <el-form-item label="任务类型标签" prop="checkedTags">
              <el-radio-group v-model="newTask.checkedTags">
                <el-radio v-for="tag in $store.state.tags.tagMsg" :label="tag" :key="tag">{{ tag }}</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="开始时间" prop="taskStartDate">
              <el-date-picker type="date" v-model="newTask.taskStartDate" placeholder="请选择开始时间" style="width: 500px"
                              ref="startTimePicker"
                              :picker-options="option1" @change="getSTime" format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>

            <el-form-item label="结束时间" prop="taskEndDate">
              <el-date-picker type="date" v-model="newTask.taskEndDate" placeholder="请选择结束时间" style="width: 500px"
                              ref="endTimePicker"
                              :picker-options="option2" @change="getETime" format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>

            <!--<el-form-item label="参与人数" prop="expectedNumber">-->
              <!--<el-input type="text" v-model.number="newTask.expectedNumber" auto-complete="false" clearable-->
                        <!--style="width: 500px"></el-input>-->
            <!--</el-form-item>-->

            <el-form-item label="最低工人等级" prop="level">
              <el-rate v-model="newTask.workerLevel" style="margin-top: 10px">
              </el-rate>
            </el-form-item>

            <!--<el-form-item label="奖励积分" prop="points">-->
              <!--<el-input type="text" v-model.number="newTask.points" auto-complete="false"-->
                        <!--placeholder="发布任务扣除积分为此栏积分数与人数的乘积" clearable style="width: 500px"> </el-input>-->
            <!--</el-form-item>-->

            <el-form-item>
              <el-upload :limit="1" :on-exceed="handleExceed" ref="upload" drag
                         action="dummy" :auto-upload="false"
                         :file-list="newTask.fileList" accept=".zip"
                         :beforeRemove="beforeRemove" :http-request="uploadImage"
                         show-file-list
                         style="width: 580px">

                <!--<el-button size="medium" type="primary">点击上传</el-button>-->
                <div style="margin-top: 60px" class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div slot="tip">请选择上传一个zip压缩文件，内部图片格式为jpg/png</div>
              </el-upload>
            </el-form-item>

            <br>
            <el-form-item>
              <el-row :gutter="20">
                <el-col :span="6" :offset="4">
                  <div>
                    <el-button type="primary" @click="submitForm('newTask')" style="padding: 15px 30px 15px 30px">提交</el-button>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div>
                    <el-button @click="resetForm('newTask')" style="padding: 15px 30px 15px 30px">重置</el-button>
                  </div>
                </el-col>
              </el-row>
            </el-form-item>

          </el-form>


        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {mapActions} from 'vuex'
  import TypeCard from '../utils/typeCard'

  let taskID = -100;
  const contentStyle = 'margin: 3% 3% 3% 3%; background-color: #f5f7fa';
  const singleCardStyle = 'margin-top: 3%;margin-bottom: 3%';
  const typeTag = 'tag';
  const typeDraw = 'draw';

  const typeMsg1 = '指定目标的标注';
  const typeMsg2 = '描述子类的标注';
  const typeMsg3 = '划分区域的标注';

  const previewImgURL1 = '../../../src/infoImages/elephant-card.jpg';
  const imgURL1 = '../../../src/infoImages/elephant-draw.jpg';
  const briefDescription1 = '您被要求标出已经指定的目标';
  const exampleDescription1 = '您被指定标出所有的目标对象，在本次任务中，您需要标出所有的大象';
  const title1_1 = '基本要求';
  const sentence1_1 = '请使用我们提供的矩形标注工具，将您在图中看到的目标对象标记出来。选定绘制矩形的功能后，只需点击画布并移动鼠标以绘制标记。';
  const title1_2 = '注意事项';
  const sentence1_2 = '您无需对标记进行任何的描述，我们亦不会提供此功能。如果图中的目标对象的重要识别部位被遮挡（如被母象挡住了头部的小象），请不要标记它';
  const title1_3 = '操作建议';
  const sentence1_3 = '请仔细确认图片的内容再进行标记；标记的收束处不要超出画布，因为我们会直接将终点设置为鼠标接触边缘的位置。';
  const details1 = [
    {
      title: title1_1,
      sentence: sentence1_1
    },
    {
      title: title1_2,
      sentence: sentence1_2
    },
    {
      title: title1_3,
      sentence: sentence1_3
    }
  ];

  const previewImgURL2 = '../../../src/infoImages/tableware-card.jpg';
  const imgURL2 = '../../../src/infoImages/tableware-draw.jpg';
  const briefDescription2 = '您要对图中对象进行分类和标注';
  const exampleDescription2 = '您被指定标出所有的餐具，然后依次填写餐具的名称';
  const title2_1 = '基本要求';
  const sentence2_1 = '请使用我们提供的矩形标注工具，将您在图中看到的目标对象标记出来。标记后请描述标注的内容。';
  const title2_2 = '注意事项';
  const sentence2_2 = '您必须对标记进行描述，在您完成一个标记的描述前，不能进行下一个标注。如果图中的目标对象的重要识别部位被遮挡，请不要标记它';
  const title2_3 = '操作建议';
  const sentence2_3 = '请仔细确认图片的内容再进行标记；标记的收束处不要超出画布，因为我们会直接将终点设置为鼠标接触边缘的位置。';
  const details2 = [
    {
      title: title2_1,
      sentence: sentence2_1
    },
    {
      title: title2_2,
      sentence: sentence2_2
    },
    {
      title: title2_3,
      sentence: sentence2_3
    }
  ];

  const previewImgURL3 = '../../../src/infoImages/road-card.jpg';
  const imgURL3 = '../../../src/infoImages/road-draw.jpg';
  const briefDescription3 = '您被要求划分出特定的对象';
  const exampleDescription3 = '在本题中，您被要求将图中的公路划分出来';
  const title3_1 = '基本要求';
  const sentence3_1 = '请使用我们提供的多边形标注工具，将您在图中看到的目标对象标记出来。选定绘制多边形的功能后，您可以点击画布或者在按下鼠标后移动进行画图。';
  const title3_2 = '注意事项';
  const sentence3_2 = '如果目标对象有好几块，可以进行多次多边形标记，然后用同个名词来描述这些标记';
  const title3_3 = '操作建议';
  const sentence3_3 = '描边时请仔细观察图形，避免将不必要的内容圈点到标记当中。如果图形还未封闭，这个多边形的绘制就不会结束。';
  const details3 = [
    {
      title: title3_1,
      sentence: sentence3_1
    },
    {
      title: title3_2,
      sentence: sentence3_2
    },
    {
      title: title3_3,
      sentence: sentence3_3
    }
  ];

  export default {
    components: {TypeCard},
    name: "distribute-new-task",

    data() {
      //某些函数内部修正指向问题
      let that = this;

      let checkDate = (rule, value, callback) => {
        let sd = this.$refs.endTimePicker.value;
        if (!sd) {
          callback();
        } else if (value >= sd) {
          return callback(new Error('不能晚于结束日期'));
        } else {
          callback();
        }
      };

      let checkPoints = (rule, value, callback) => {
        if (value === "") return callback(new Error("请填写奖励积分"));
        else if (value === 0) return callback(new Error("请填写奖励积分"));
        else if (value * this.newTask.expectedNumber > this.$store.state.user.userInfo.points)
          return callback(new Error("超出现有金额"));
        else
          callback();
      };

      // let checkExpectedNumber = (rule, value, callback) => {
      //   if (value <= 0)
      //     return callback(new Error("请选择合适的人数"));
      //   else{
      //     callback();
      //   }
      //
      // };

      let checkLevel = (rule, value, callback) => {
        if (value === null || value < 1 )
          return callback(new Error("请选择最低等级"));
        else
          callback();
      };

      return {
        isLoading: false,
        isActive: 0,
        intType: -10,
        messageFlag: false, //默认认为上传不成功

        //第一部分的图片需要的东西
        tagMsg: '',
        typeMsg1,
        typeMsg2,
        typeMsg3,

        previewImgURL1,
        imgURL1,
        briefDescription1,
        exampleDescription1,
        details1,

        previewImgURL2,
        imgURL2,
        briefDescription2,
        exampleDescription2,
        details2,

        previewImgURL3,
        imgURL3,
        briefDescription3,
        exampleDescription3,
        details3,

        contentStyle,
        singleCardStyle,
        typeTag,
        typeDraw,


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
          checkedTags: "",
          taskStartDate: '',
          taskEndDate: '',
          //expectedNumber: 0,
          workerLevel: 0,
          //points: 0,
          fileList: []
        },


        //表单的验证规则
        myRule: {
          taskName: [
            {required: true, message: '请输入名称', trigger: 'blur'}
          ],
          taskDescription: [
            {required: true, message: '请输入描述', trigger: 'blur'}
          ],
          checkedTags: [
            {required: true, message: '请至少选择一个类别', trigger: 'change'}
          ],
          taskStartDate: [
            {validator: checkDate, trigger: 'blur'},
            {type: 'date', required: true, message: '请选择日期', trigger: 'change'}
          ],
          taskEndDate: [
            {type: 'date', required: true, message: '请选择日期', trigger: 'change'}
          ],
          // expectedNumber: [
          //   {validator: checkExpectedNumber, trigger: "blur"},
          //   {required: true, message: '预期参与人数不能为空'},
          //   {type: 'number', message: '预期参与人数必须为数字值'}
          // ],
          level: [
            {validator: checkLevel, trigger: 'change'},
          ],
          points: [
            {required: true, message: '奖励积分不能为空'},
            {type: 'number', message: '奖励积分必须为数字值'},
            {validator: checkPoints, trigger: 'blur'}
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
          if (valid) {
            //准备开始发送请求
            this.isLoading = true;

            this.$http.post('/task/releaseTask', {
              sponsorName: that.$store.state.user.userInfo.username,
              taskName: that.newTask.taskName,
              description: that.newTask.taskDescription,
              tag: [that.newTask.checkedTags],
              startDate: that.newTask.startDate.getFullYear() + "-" + (that.newTask.startDate.getMonth() + 1) + "-" + that.newTask.startDate.getDate(),
              endDate: that.newTask.endDate.getFullYear() + "-" + (that.newTask.endDate.getMonth() + 1) + "-" + that.newTask.endDate.getDate(),
              workerLevel: that.newTask.workerLevel,
              type: that.intType
              // expectedNumber: that.newTask.expectedNumber,
              // points: that.newTask.points
            })
              .then(function (response) {
                //检查返回信息

                console.log('看看id是几', response.data.taskID);
                if (response.data.mes === true) {
                  taskID = response.data.taskID;   //设定任务的id属性值

                  setTimeout(function () {
                    that.$refs.upload.submit();  //发送ajax请求
                  }, 700);


                  //全部提交完成
                  that.resetForm('newTask');

                  setTimeout(function () {
                    that.isLoading = false;
                    that.$refs.upload.clearFiles();
                    if(that.messageFlag) {

                      that.$confirm('文件成功上传, 继续留在本页面发布任务?', '提示', {
                        confirmButtonText: '留在此页',
                        cancelButtonText: '查看其他',
                        type: 'warning'
                      }).then(() => {
                        //nothing to do
                      }).catch(() => {
                        that.$router.push('1-1');
                      });
                    }

                    that.messageFlag = false;
                  }, 3500);

                }else {
                  that.isLoading = false;
                  that.$message({
                    message: "任务上传失败",
                    type: 'warning'
                  });
                }

              })
              .catch(function (error) {
                that.isLoading = false;
                that.$message({
                  message: '任务上传失败，服务器异常' + error,
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
          } else {
            this.$message({
              message: "您填写的内容不合规范",
              type: 'warning'
            });
          } //外层嵌套检查valid
        });

      },

      resetForm(formName) {
        this.newTask.workerLevel = 0;
        this.$refs[formName].resetFields();
      },

      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },

      beforeRemove(file) {
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
              that.messageFlag = true;
              that.updateWithoutPointer();
            }
            else {
              that.$message.warning('图片上传失败。网络异常');
            }
          })
          .catch(function (error) {
            that.$message({
              message: '图片上传失败' + error,
              type: 'warning'
            });
          });
      },



      //新加入的解决选择类型问题的部分
      next(){
        if(this.isActive !== 0){
          this.$message({
            message: '尊敬的用户，已经到底了哟~',
            duration: 1800
          });
        }
        else if(this.intType <= 0){
            this.$message({
              message: '请选择类型~',
              duration: 1800
            });
        }
        else{
          this.isActive = 1;
        }

      },

      pre() {
        if(this.isActive === 1){
          this.resetForm('newTask');
          this.$refs.upload.clearFiles();
          this.isActive = 0;
          this.intType = -10;
        }
        else{
          this.$message({
            message: '尊敬的用户，已经到头了哟~',
            duration: 1800
          });
        }
      },

      chooseIntType(cat){
        this.intType = cat;
        console.log(cat);
        console.log(this.isActive);
        this.next();
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
