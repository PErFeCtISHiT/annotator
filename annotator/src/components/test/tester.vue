<template>
  <div
    style="border:1px solid #000; box-sizing: border-box; border-radius: 8px; background-color: #f9f8f7">
    <el-row style="border-bottom: 2px solid lightgrey">
      <h3 style="margin-left: 20px">{{'· 题目描述与要求'}}</h3>
      <p style="margin-left: 25px; color:darkgrey">{{description}}</p>
    </el-row>
    <el-row v-if="testType===typeChoose">
      <el-col :span="16">
        <el-row type="flex" justify="center" align="middle"
                style="width: 100%; height: 100%; margin-bottom: 5%; margin-top: 5%; border-right: 2px solid lightgrey">
          <img :src="imgURL" style="width:90%; height:90%; margin: 5% 5% 5% 5%">
        </el-row>
      </el-col>
      <el-col :span="8" style="height: inherit">
        <el-row type="flex" justify="center" align="middle"
                style="width: 100%; height: 100%; margin-bottom: 5%; margin-top: 5%;">
          <div>

            <h4 style="margin: 5% 5% 2% 5%;">作答提示</h4>
            <p style="margin: 0 5% 5% 5%; color: grey">
              本题有多个备选项，但在这些选项中，<u>只有一个是正确答案</u>，请务必仔细观察做侧的图片，并严格按照题目要求作答：
            </p>

            <h4 style="margin: 5% 5% 2% 5%;">作答区</h4>
            <p style="margin: 0 5% 5% 5%; color: grey">
              请在下面提供的选项中选择你认为符合题目要求的一个，在认为答案没有问题后请点击确认答案按钮
            </p>

            <div style="margin: 15% 5% 5% 5%;border: lightgrey 1px solid">
              <el-row style="margin: 10% 0 10% 0;" type="flex" justify="center" align="middle">
                <el-radio-group v-model="currentAnswer">
                  <template v-for="value in choices">
                    <el-radio :label="value">
                    </el-radio>
                  </template>
                </el-radio-group>
              </el-row>

              <el-row style="margin: 0 0 5% 0;" type="flex" justify="center" align="middle">
                <el-button type="primary">确认答案</el-button>
              </el-row>
            </div>

          </div>
        </el-row>
      </el-col>
    </el-row>


  </div>
</template>

<script>
  import ElRow from "element-ui/packages/row/src/row";

  const imgURL = '../../../src/infoImages/elephant-draw.jpg';
  const description = '图中的标记里，符合规范的标记有几个？';
  const choices = ['0个', '1个', '2个', '3个'];
  const typeChoose = 'choose';
  const typeDraw = 'draw';

  export default {
    components: {ElRow},
    name: "tester",
    data() {
      return {
        typeChoose,
        typeDraw,
        testType: typeChoose,
        description: description,
        choices: choices,
        answer: choices[choices.length - 1],
        x: 0,
        y: 0,
        width: 0,
        height: 0,
        faultTolerantRate: 0,
        imgURL: imgURL,
        currentAnswer: choices[0],
        tests: [],
        testResult: [],
      }
    },
    methods: {

      comfirmChoose() {

      },

      confirmDraw() {
        this.testResult.push(this.checkChoose());
        if (this.test.length > 0) {
          this.getTestFromTests();
        }else{
          this.handleTestFinished();
        }
      },

      handleTestFinished(){

      },

      getTestFromTests(){
        let tempObj = test.pop();
        this.testType = tempObj.testType;
        this.description = tempObj.description;
        this.choices = tempObj.choices;
        this.answer = tempObj.answer;
        this.x = tempObj.x;
        this.y = tempObj.y;
        this.width = tempObj.width;
        this.height = tempObj.height;
        this.faultTolerantRate = tempObj.faultTolerantRate;
        this.imgURL = tempObj.imgURL;
      },

      checkChoose() {
        return this.currentAnswer === this.answer;
      },

      checkDraw() {

      }
    }

  }
</script>

<style scoped>

</style>
