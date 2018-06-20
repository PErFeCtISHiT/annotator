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
                <el-button type="primary" @click="confirmChoose">确认答案</el-button>
              </el-row>
            </div>

          </div>
        </el-row>
      </el-col>
    </el-row>

    <el-row v-if="testType===typeDraw">
      <el-col :span="16">
        <el-row type="flex" justify="center" align="middle"
                style="width: 100%; height: 100%; margin-bottom: 5%; margin-top: 5%; border-right: 2px solid lightgrey">
          <div :style="getSaverStyle" v-if="loadCanvas">
            <!--touch事件是给手机端用的-->
            <!--<img :src="imgSrc">-->
            <canvas
              v-if="loadCanvas"
              @mousedown="handleCanvasDownDistribute($event)"
              @mouseup="handleCanvasUpDistribute($event)"
              @mousemove="handleCanvasMoveDistribute($event)"
              @touchstart="handleCanvasDownDistribute($event)"
              @touchend="handleCanvasUpDistribute($event)"
              @touchmove="handleCanvasMoveDistribute($event)"
              :ref="canvasRef"
              :width="canvasWidth"
              :height="canvasHeight"
              style="border:1px solid #000; box-sizing: border-box;"></canvas>
          </div>
        </el-row>
      </el-col>
      <el-col :span="8" style="height: inherit">
        <el-row type="flex" justify="center" align="middle"
                style="width: 100%; height: 100%; margin-bottom: 5%; margin-top: 5%;">
          <div>

            <h4 style="margin: 5% 5% 2% 5%;">作答要求</h4>
            <p style="margin: 0 5% 5% 5%; color: grey">
              请在左侧的图片中用矩形标出唯一的标记。标记用的矩形应当尽可能多地包含目标对象，又要尽可能地小。
            </p>

            <h4 style="margin: 5% 5% 2% 5%;">作答提示</h4>
            <p style="margin: 0 5% 5% 5%; color: grey">
              你只能画出一个标记。如果你认为标记标得不够好，可以点击删除按钮。确认无误后，请点击确认按钮。
            </p>

            <div style="margin: 10% 5% 5% 5%">
              <el-row>
                <el-col :span="12">
                  <el-row type="flex" justify="center" align="middle">
                    <el-button type="warning" @click="drawDelete">删除</el-button>
                  </el-row>
                </el-col>
                <el-col :span="12">
                  <el-row type="flex" justify="center" align="middle">
                    <el-button type="primary" @click="confirmDraw">确认</el-button>
                  </el-row>
                </el-col>
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
  import {getIDByTime} from "../functions/referFunctions";

  const imgURL = '../../../src/infoImages/animal.jpg';
  const description = '图中的标记里，符合规范的标记有几个？';
  const choices = ['0个', '1个', '2个', '3个'];
  const typeChoose = 'choose';
  const typeDraw = 'draw';
  const registerMode = 'register';
  const normalMode = 'normal';
  const targetRate = 0.8;        //这个值要问清楚
  const originalHeight = 500;
  const padding = 50;
  const originalWidth = originalHeight + 2 * padding;
  const canvasRef = 'canvas';

  export default {
    components: {ElRow},
    name: "tester",
    props: {
      mode: {
        type: String,
        default: normalMode
      }
    },

    mounted() {
      this.getTestsFromServer(this.getTestFromTests);   //这里传入了一个回调函数
      // this.refreshComponent(this.imgURL);
    },

    data() {
      return {
        typeChoose,
        typeDraw,
        normalMode,
        registerMode,
        canvasRef,
        loadCanvas: false,
        testType: typeDraw,
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
        canvasWidth: 0,
        canvasHeight: 0,
        globalRate: 1,
        currentLayerName: '',
        isDown: false,
        currentRectX: 0,
        currentRectY: 0,
        lock: false,
      }
    },

    computed: {
      getSaverStyle() {
        return `width:${this.canvasWidth};height:${this.canvasHeight};background-image:url(${this.imgURL});background-repeat:no-repeat; background-size:${this.canvasWidth}px ${this.canvasHeight}px;`
      },
    },

    methods: {

      setCurrentLayerNameByTime() {
        this.currentLayerName = 'testLayer' + getIDByTime();  //这里要是文本
      },

      getCanvasTarget() {
        return $(this.$refs[this.canvasRef]);
      },

      getWrappedEvent(e) {
        if (e.changedTouches) {
          e.preventDefault();   //防止向上滚动
          return e.changedTouches[0];
        } else {
          return e;
        }
        //changeTouches而不是touches可以避免touchEnd中这一数组被清空
      },

      getLocXInCanvas(e) {
        return e.clientX - e.target.getBoundingClientRect().left;
      },

      getLocYInCanvas(e) {
        return e.clientY - e.target.getBoundingClientRect().top;
      },

      handleCanvasDownDistribute(e) {
        // console.log('callDown');
        if (this.getCanvasTarget().getLayers() && this.getCanvasTarget().getLayers().length === 0) {
          this.lock = false;
          this.isDown = true;
          let event = this.getWrappedEvent(e);
          this.setCurrentLayerNameByTime();
          let x = this.getLocXInCanvas(event);
          let y = this.getLocYInCanvas(event);
          this.currentRectX = x;
          this.currentRectY = y;

          let canvas = this.getCanvasTarget();

          let that = this;
          canvas.addLayer({
            type: 'rectangle',
            strokeStyle: '#ffff00',
            strokeWidth: 2.5,
            name: that.currentLayerName,
            fromCenter: false,
            x: that.currentRectX, y: that.currentRectY,
            width: 1,
            height: 1
          });
          canvas.drawLayers();
        }
      },

      handleCanvasMoveDistribute(e) {
        if (this.isDown) {
          let event = this.getWrappedEvent(e);
          // console.log('callMove');
          if (this.isNearlyOutOfBorder(event)) {
            this.handleCanvasUpDistribute(event);
          } else {
            // console.log(this.currentLayerName);

            let canvas = this.getCanvasTarget();

            let x = this.getLocXInCanvas(event);
            let y = this.getLocYInCanvas(event);

            let width = x - this.currentRectX;
            let height = y - this.currentRectY;

            // console.log(width);
            // console.log(height);

            let that = this;

            canvas.removeLayer(this.currentLayerName);

            canvas.addLayer({
              type: 'rectangle',
              strokeStyle: '#ffff00',
              strokeWidth: 2.5,
              name: that.currentLayerName,
              fromCenter: false,
              x: that.currentRectX, y: that.currentRectY,
              width: width,
              height: height
            });
            canvas.drawLayers();
          }
        }
      },

      handleCanvasUpDistribute(e) {
        // console.log('callUp');
        if (!this.lock) {
          this.lock = true;
          let event = this.getWrappedEvent(e);
          let canvas = this.getCanvasTarget();

          let x = this.getLocXInCanvas(event);
          let y = this.getLocYInCanvas(event);

          let width = x - this.currentRectX;
          let height = y - this.currentRectY;

          canvas.removeLayer(this.currentLayerName);

          if (Math.abs(width) >= 5 && Math.abs(height) >= 5) {

            let that = this;
            canvas.addLayer({
              type: 'rectangle',
              strokeStyle: '#ffff00',
              strokeWidth: 2.5,
              name: that.currentLayerName,
              fromCenter: false,
              x: that.currentRectX, y: that.currentRectY,
              width: width,
              height: height,
            });

          }
          canvas.drawLayers();
          this.isDown = false;
        }
      },


      isNearlyOutOfBorder(e) {
        let judgeRate = 0.025;
        let delta = 10;
        let x = this.getLocXInCanvas(e);
        let y = this.getLocYInCanvas(e);
        let canvasRect = e.target.getBoundingClientRect();
        let width = canvasRect.width;
        let height = canvasRect.height;
        let deltaX = width * judgeRate;
        let deltaY = height * judgeRate;
        deltaX = deltaX < delta ? deltaX : delta;
        deltaY = deltaY < delta ? deltaY : delta;
        return x <= deltaX || y <= deltaY || width - x <= deltaX || height - y <= deltaY;
      },

      refreshComponent(imgSrc) {

        this.loadCanvas = false;
        this.globalRate = 1;
        this.currentLayerName = '';
        this.loadCanvas = false;

        if (this.getCanvasTarget()) {
          this.getCanvasTarget().removeLayers();
        }

        let that = this;
        let image = new Image();
        image.src = imgSrc;

        let handle = function (image) {
          let width = image.width;
          let height = image.height;

          if (width > height) {
            that.globalRate = width / originalWidth;
            width = originalWidth;
            height /= that.globalRate;
          }
          else {
            that.globalRate = height / originalHeight;
            height = originalHeight;
            width /= that.globalRate;
          }

          that.canvasWidth = width;
          that.canvasHeight = height;

          that.loadCanvas = true;
        };

        if (image.complete) {
          handle(image);
          image.onload = function () {
          };
        } else {
          image.onload = function () {
            handle(image);
            // clear onLoad, IE behaves erratically with animated gifs otherwise
            image.onload = function () {
            };
          };
          image.onerror = function () {
            that.$message({
              message: '加载图片失败',
              type: 'error',
              duration: 1800
            });
          }
        }
      },        //为了保证有序，写到了一个函数里

      getTestsFromServer(recall = null) {
        let that = this;
        this.$http.get('/test/getTests')
          .then(function (response) {
            console.log(response.data.data);
            that.tests = response.data.data;
            that.testResult = [];
            if (recall && recall !== null) {
              recall();
            }
          })
          .catch(function (error) {
            that.$message({
              message: '网络错误',
              type: 'error',
              duration: 1800
            });
            console.log(error);
          });
      },

      confirmChoose() {
        this.testResult.push(this.checkChoose());
        this.doThingsAccordingToLength();
      },

      confirmDraw() {
        if (this.getCanvasTarget().getLayers() && this.getCanvasTarget().getLayers().length > 0) {
          this.testResult.push(this.checkDraw());
          this.doThingsAccordingToLength();
        } else {
          this.$message({
            message: '请在绘制矩形标记后点击确认',
            type: 'warning',
            duration: 1500
          });
        }
      },

      doThingsAccordingToLength() {
        if (this.tests.length > 0) {
          this.getTestFromTests();
        } else {
          this.handleTestFinished();
        }
      },

      handleTestFinished() {
        if (this.mode === this.normalMode) {
          //向后端询问结果
          //如果返回值为真，返回工人的页面
          //如果返回值为假，询问用户是否继续做题
          //  如果用户选择继续做题，再发起一次请求获取题目
          //  否则用户选择不再继续做题，页面返回首页
          this.handleNormalFinished();
        } else if (this.mode === this.registerMode) {
          //直接在前端检查正确率
          //如果正确率够，告诉父组件正确率够了，并且把本页面关闭
          //如果正确率不够，询问用户是否继续做题
          //  如果用户选择继续做题，再发起一次请求获取题目
          //  如果用户选择不继续做题，告诉父组件正确率不够，并且把本页面关闭
          this.handleRegisterFinished();
        }
      },

      getTestResult() {
        let num = 0;
        for (let i = 0; i < this.testResult.length; i++) {
          if (this.testResult[i]) {
            num++;
          }
        }
        let rate = num / this.testResult.length;
        return {rate, num};
      },

      handleNormalFinished() {
        let that = this;
        let testResult = this.getTestResult();
        this.$http.post('/test/postResult', {
          username: that.$store.state.user.userInfo.username,
          rate: testResult.rate,
          num: testResult.num
        })
          .then(function (response) {
            if (response.data.mes === true) {
              that.$router.push('/2-1');
            } else {
              that.handleNormalNotTrue();
            }
          })
          .catch(function (error) {
            that.$message({
              message: '网络错误，请检查网络连接',
              type: 'error',
              duration: 1800
            });
            console.log(error);
          });
      },

      handleNormalNotTrue() {
        this.$confirm('您的正确率仍未达标，是否继续答题？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'primary'
        }).then(() => {
          this.getTestsFromServer(this.getTestFromTests);
          this.$message({
            type: 'info',
            message: '已经开始新一轮的测试'
          });
        }).catch(() => {
          this.$router.push('/index');
          this.$message({
            type: 'info',
            message: '已取消继续作答'
          });
        });
      },

      handleRegisterFinished() {
        if (this.getTestResult().rate >= targetRate) {
          let testResult = this.getTestResult();
          this.$emit('update-pass', testResult.rate, testResult.num);
        } else {
          this.handleRegisterNotTrue();
        }
      },

      handleRegisterNotTrue() {
        this.$confirm('您的正确率仍未达标，是否继续答题？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'primary'
        }).then(() => {
          this.getTestsFromServer(this.getTestFromTests);
          this.$message({
            type: 'info',
            message: '已经开始新一轮的测试'
          });
        }).catch(() => {
          this.$emit('not-pass');
          this.$message({
            type: 'info',
            message: '已取消继续作答'
          });
        });
      },

      getTestFromTests() {
        let tempObj = this.tests.pop();
        console.log(this.tests.length);
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
        if (this.testType === typeDraw) {
          this.refreshComponent(this.imgURL);
        }
      },

      checkChoose() {
        let result = this.currentAnswer === this.answer;
        if (result) {
          this.$message({
            message: '回答正确',
            type: 'success',
            duration: 1500
          });
        } else {
          this.$message({
            message: '回答错误',
            type: 'warning',
            duration: 1500
          });
        }
        return result;
      },

      checkDraw() {
        let deltaX = this.width * this.faultTolerantRate;
        let deltaY = this.height * this.faultTolerantRate;

        let x = this.x;
        let y = this.y;
        let width = this.width;
        let height = this.height;

        let rect1 = this.getRect(x, y, deltaX, deltaY);  //左上
        let rect2 = this.getRect(x + width, y, deltaX, deltaY);    //右上
        let rect3 = this.getRect(x + width, y + height, deltaX, deltaY);     //右下
        let rect4 = this.getRect(x, y + height, deltaX, deltaY);   //左下

        let layer = this.getCanvasTarget().getLayer(this.currentLayerName);
        let layerX = layer.x;
        let layerY = layer.y;
        let layerWidth = layer.width;
        let layerHeight = layer.height;

        let result = this.checkOnePoint(layerX, layerY, rect1) && this.checkOnePoint(layerX + layerWidth, layerY, rect2) && this.checkOnePoint(layerX + layerWidth, layerY + layerHeight, rect3) && this.checkOnePoint(layerX, layerY + layerHeight, rect4);
        if (result) {
          this.$message({
            message: '绘图误差符合要求',
            type: 'success',
            duration: 1500
          });
        } else {
          this.$message({
            message: '绘图误差过大',
            type: 'warning',
            duration: 1500
          });
        }
        return result;
      },

      getRect(x, y, deltaX, deltaY) {
        let left = x - deltaX;
        let right = x + deltaX;
        let top = y - deltaY;
        let bottom = y + deltaY;
        return {top, right, bottom, left};
      },

      checkOnePoint(x, y, rect) {
        return x > rect.left && x < rect.right && y > rect.top && rect < rect.bottom;
      },

      drawDelete() {
        let canvas = this.getCanvasTarget();
        canvas.removeLayers();
        canvas.drawLayers();
      }
    }

  }
</script>

<style scoped>

</style>
