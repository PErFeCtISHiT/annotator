<template>
  <div>

    <div :style="borderMsg+`height:${taskDescriptionHeight}px`">
      <h3 style="margin-left: 12px">{{'· 任务ID：'+taskID}}</h3>
    </div>

    <div :style="'height:' + componentHeight + 'px'">

      <!--对话框平时是不显示的-->
      <el-dialog
        v-dialogDrag="{screenX:eventScreenX,screenY:eventScreenY}"
        title="标记内容"
        @open="doFocus"
        :visible.sync="dialogVisible"
        :before-close="function(done) {
        if(handleClose()){
        done();
        }
      }"
        width="30%">
        <el-input
          :ref="inputRef"
          placeholder="请输入标记的文本内容"
          v-model="markInputMsg"
          @keyup.enter.native="handleRevise"
          autofocus
          clearable>
        </el-input>
        <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="warning" @click="handleDelete">删 除</el-button>
        <el-button type="primary" @click="handleRevise">修 改</el-button>
      </span>
      </el-dialog>


      <el-col :span="19">
        <div :ref="canvasOuterSaver">
          <el-row type="flex" justify="center" align="middle"
                  :style="borderMsg+'; width:100%; height:' + componentHeight + 'px'">
            <!--对齐方法：用flex模式-->
            <div v-if="loadCanvas" :style="getSaverStyle">
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
                :style="borderMsg"></canvas>
            </div>
          </el-row>
        </div>
      </el-col>

      <el-col :span="5" v-if="false" :style="borderMsg+'; height:100%'">
        <el-row :style="borderMsg+'; width:100%; height:20%'">
          <el-row style="margin-left: 5%"><h4>全局属性：</h4></el-row>

          <el-row style="margin: 0 5% 0 10%">
            <!--上 右 下 左-->
            <el-col :span="12" style="margin-top: 1.5%">
              <el-popover
                placement="top-start"
                title="提示"
                width="200"
                content="这里设置的颜色将用于表示标记被选中时表现出的颜色"
                trigger="hover">
                <span slot="reference" style="cursor: pointer">焦点颜色：</span>
              </el-popover>
            </el-col>

            <el-color-picker
              style="margin-left: 7%"
              v-model="strokeFocusColor"
              size="small"
              show-alpha
              :predefine="predefineColors">
            </el-color-picker>

          </el-row>

        </el-row>
        <el-row :style="borderMsg+'; width:100%; height:30%'">
          <el-row style="margin-left: 5%"><h4>标记属性：</h4></el-row>

          <el-row style="margin: 0 5% 0 10%">
            <!--上 右 下 左-->
            <el-col :span="12" style="margin-top: 1.5%">
              <el-popover
                placement="top-start"
                title="提示与设置"
                width="200"
                trigger="hover">
                <p>这是下一个标记的笔触颜色，默认会被自动更改</p>
                <el-checkbox v-model="strokeNormalColorAutoChange">自动更改</el-checkbox>
                <span slot="reference" style="cursor: pointer">线条颜色：</span>
              </el-popover>
            </el-col>
            <el-color-picker
              style="margin-left: 7%"
              v-model="strokeNormalColor"
              size="small"
              show-alpha
              :predefine="predefineColors">
            </el-color-picker>
          </el-row>

          <el-row style="margin: 5% 5% 0 10%">
            <el-col :span="12" style="margin-top: 1%">
              <el-popover
                placement="top-start"
                title="提示"
                width="200"
                content="这里设置的线条宽度将决定下一个标记的线宽，默认是2.5"
                trigger="hover">
                <span slot="reference" style="cursor: pointer">线条宽度：</span>
              </el-popover>
            </el-col>

            <el-popover
              placement="right"
              width="200"
              trigger="click">

              <el-slider v-model="strokeWidth" :min="100" :max="400" :format-tooltip="formatTooltip">
              </el-slider>

              <el-button slot="reference" type="normal" round size="mini">{{(strokeWidth/100).toFixed(2)}}</el-button>
            </el-popover>

          </el-row>
        </el-row>
        <el-row :style="borderMsg+'; width:100%; height:50%'">
          <el-row style="margin-left: 5%"><h4>图像控制：</h4></el-row>
          <el-scrollbar style="height: 77%; width: 100%; overflow-x: hidden">
            <el-collapse v-model="activeMode" accordion @change="handleCollapseChange">
              <el-collapse-item name="drawing">
                <template slot="title">
                  <div style="margin-left: 10%">
                    <el-radio v-model="activeMode" label="drawing">绘制</el-radio>
                  </div>
                </template>

                <el-row type="flex" justify="center" align="middle">
                  <el-radio-group v-model="drawingType" size="small" @change="handleDrawingTypeChange">
                    <el-tooltip class="item" effect="dark" content="进行矩形标注" placement="bottom-start">
                      <el-radio-button v-if="showRect" label="rect">
                        <slot><i class="el-icon-whisky-rect"></i></slot>
                      </el-radio-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="进行区域划分" placement="bottom-start">
                      <el-radio-button v-if="showPoly" label="poly">
                        <slot><i class="el-icon-whisky-poly"></i></slot>
                      </el-radio-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="进行标记移动" placement="bottom-start">
                      <el-radio-button label="move">
                        <slot><i class="el-icon-rank"></i></slot>
                      </el-radio-button>
                    </el-tooltip>
                  </el-radio-group>
                </el-row>

              </el-collapse-item>

              <el-collapse-item name="showing">
                <template slot="title">
                  <div style="margin-left: 10%">
                    <el-radio v-model="activeMode" label="showing">显示</el-radio>
                  </div>
                </template>

                <div style="margin-left: 15%;">
                  <el-checkbox v-if="showCheckAll" :indeterminate="isIndeterminate" v-model="checkAll"
                               @change="handleCheckAllChange">全选
                  </el-checkbox>

                  <el-checkbox-group v-model="checkedShowModes" @change="handleCheckedModesChange">
                    <el-checkbox v-if="shouldShow(showMode)" v-for="showMode in showModes" :label="showMode"
                                 :key="showMode"
                                 @change="checked=>handleSingleChange(checked,showMode)">
                      {{getModeName(showMode)}}
                    </el-checkbox>
                  </el-checkbox-group>

                </div>

              </el-collapse-item>

              <el-collapse-item name="reSizing">
                <template slot="title">
                  <div style="margin-left: 10%">
                    <el-radio v-model="activeMode" label="reSizing">缩放</el-radio>
                  </div>
                </template>

                <el-row type="flex" justify="center" align="middle">
                  <el-button-group>
                    <el-tooltip class="item" effect="dark" content="重新渲染并放大" placement="bottom-start">
                      <el-button type="primary" size="small" icon="el-icon-zoom-in" @click="handleSizeUp">
                      </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="重新渲染并缩小" placement="bottom-start">
                      <el-button type="primary" size="small" icon="el-icon-zoom-out" @click="handleSizeDown">
                      </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="重新渲染并回归初始大小" placement="bottom-start">
                      <el-button type="primary" size="small" icon="el-icon-search" @click="handleSizeBack">
                      </el-button>
                    </el-tooltip>
                  </el-button-group>
                </el-row>

              </el-collapse-item>

            </el-collapse>
          </el-scrollbar>
        </el-row>
      </el-col>

      <el-col :span="5" :style="borderMsg+'; height:100%'">

        <el-row v-if="false" :style="borderMsg+'; width:100%; height:30%'">
          <el-row style="margin-left: 5%"><h4>整体描述：</h4></el-row>
          <div style="width:80%; height:50%; margin: auto auto">
            <el-input
              type="textarea"
              :rows="3"
              placeholder="请输入整体描述"
              v-model="totalDescription">
            </el-input>
          </div>
        </el-row>

        <el-row :style="`${borderMsg}; width:100%; height:64%`">
          <el-row style="margin-left: 5% "><h4>标记汇总：</h4></el-row>
          <el-scrollbar style="height: 77%; width: 100%; overflow-x: hidden">
            <el-card
              :style="`background-color:${getHoverColor(getCanvasTarget().getLayer(key).data.strokeColor)};${borderMsg}`"
              v-for="(value,key) in marks"
              :key="key"
              @mousedown.native="layerMouseDownFunc(getCanvasTarget().getLayer(key),true)"
              @mouseup.native="labelUp(getCanvasTarget().getLayer(key))"
              @mouseover.native="layerMouseOverFunc(getCanvasTarget().getLayer(key),true)"
              @mouseout.native="layerMouseOutFunc(getCanvasTarget().getLayer(key),true)"
              style="margin: 5px 5px 5px 5px; cursor: pointer"
              shadow="hover">
              <!--要加上.native保证拿到，否则拿不到-->
              <span :style="`color:${value==='未标记'?'#707070':'#000'}`">{{value}}</span>
            </el-card>
          </el-scrollbar>
        </el-row>

        <el-row :style="borderMsg+'; width:100%; height:12%'" type="flex" justify="center" align="middle">
          <div>
            <el-checkbox v-if="showCheckAll" :indeterminate="isIndeterminate" v-model="checkAll"
                         @change="handleCheckAllChange">全选
            </el-checkbox>

            <el-checkbox-group v-model="checkedShowModes" @change="handleCheckedModesChange">
              <el-checkbox v-if="shouldShow(showMode)" v-for="showMode in showModes" :label="showMode"
                           :key="showMode"
                           @change="checked=>handleSingleChange(checked,showMode)">
                {{getModeName(showMode)}}
              </el-checkbox>
            </el-checkbox-group>

          </div>
        </el-row>

        <el-row :style="borderMsg+'; width:100%; height:12%'" type="flex" justify="center" align="middle">
          <el-button-group>
            <el-tooltip class="item" effect="dark" content="重新渲染并放大" placement="bottom-start">
              <el-button type="primary" size="normal" icon="el-icon-zoom-in" @click="handleSizeUp">
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="重新渲染并缩小" placement="bottom-start">
              <el-button type="primary" size="normal" icon="el-icon-zoom-out" @click="handleSizeDown">
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="重新渲染并回归初始大小" placement="bottom-start">
              <el-button type="primary" size="normal" icon="el-icon-search" @click="handleSizeBack">
              </el-button>
            </el-tooltip>
          </el-button-group>
        </el-row>

        <el-row :style="borderMsg+'; width:100%; height:12%'" type="flex" justify="center" align="middle">

          <el-col :span="12">
            <el-row type="flex" justify="center" align="middle">
              <el-button type="primary" @click="handlePre">
                上一张
              </el-button>
            </el-row>
          </el-col>

          <el-col :span="12">
            <el-row type="flex" justify="center" align="middle">
              <el-button type="primary" @click="handleNext">
                下一张
              </el-button>
            </el-row>
          </el-col>

        </el-row>

      </el-col>
    </div>
  </div>
</template>


<!--export default {-->
<!--name: "marks-viewer"-->
<!--}-->


<script>
  import ElRow from "element-ui/packages/row/src/row";
  import '../../assets/icon/iconfont.css'      //在要用的页面引入icon
  const canvasMinWidth = 100;
  const canvasMinHeight = canvasMinWidth;
  const canvasOuterSaver = 'canvasOuterSaver';
  const deltaToChange = 40;
  const rectMode = 'rect';
  const polyMode = 'poly';
  const moveMode = 'move';
  const rectLayer = 'rectangle';
  const polyLayer = 'line';
  const showOptions = [rectMode, polyMode];
  const closeDelta = 14;
  const originalHeight = 500;
  const padding = 50;
  const originalWidth = originalHeight + 2 * padding;
  const taskDescriptionHeight = 80;
  const checkCircleNum = 100;
  const testDrawImages = ['src/testDrawImage/1.jpg', 'src/testDrawImage/2.jpg', 'src/testDrawImage/3.jpg', 'src/testDrawImage/4.jpg', 'src/testDrawImage/elephant.jpg', 'src/testDrawImage/tableware.jpg', 'src/testDrawImage/road.jpg'];

  import {
    getOffset,
    getAbsolute,
    getIDByTime,
    getRandomIntInclusive,
    testIfCloseEnough,
    getPolygonAreaCenter,
    getMinAndMaxXY
  } from '../functions/referFunctions'

  class NoteRectangle {
    constructor(author, left, top, width, height, mark, id, strokeColor, strokeWidth) {
      this.author = author;
      this.left = left;
      this.top = top;
      this.width = width;
      this.height = height;
      this.mark = mark;
      this.id = id;
      this.strokeColor = strokeColor;
      this.strokeWidth = strokeWidth;
    }
  }

  class NotePolygon {
    constructor(author, points, mark, id, strokeColor, strokeWidth) {
      this.author = author;
      this.points = points;
      this.mark = mark;
      this.id = id;
      this.strokeColor = strokeColor;
      this.strokeWidth = strokeWidth;
    }
  }

  class Point {
    constructor(x, y) {
      this.x = x;
      this.y = y;
    }
  }

  export default {
    props: {
      taskID: {
        type: Number,
        default: 1
      }

    },

    name: "marks-viewer",

    components: {
      ElRow,
    },

    mounted() {
      if (this.inDebugMode) {
        this.marksViewImages = testDrawImages;
        this.marksViewIndex = 0;
        this.refreshComponent(this.marksViewImages[this.marksViewIndex]);
      } else {
        let that = this;
        this.$http.get('/task/checkImages', {
          params: {
            taskID: that.taskID
          }
        })
          .then(function (response) {
            that.marksViewImages = response.data.imgURLs;
            that.marksViewIndex = 0;
            that.refreshComponent(that.marksViewImages[that.marksViewIndex]);
          })
          .catch(function (error) {
            that.$message({
              message: '网络错误，无法加载图片',
              type: 'error',
              duration: 1500
            });
            console.log(error);
          });
      }
    },

    data() {
      return {
        inDebugMode: false,
        marksViewImages: [],
        marksViewIndex: 0,

        tagMsg: '',
        markType: 0,
        canvasOuterSaver,

        checkImages: [],
        lengthFirstToZero: false,
        imgCounter: 1,
        recheckCircleNum: checkCircleNum,

        inputRef: 'myInput',

        eventScreenX: 0,
        eventScreenY: 0,
        dialogVisible: false,

        currentWorkerName: this.$store.state.user.userInfo.username,
        currentLayerName: '',
        currentRectX: 0,
        currentRectY: 0,

        taskDescriptionHeight,
        componentHeight: originalHeight + padding,
        borderMsg: 'border:1px solid #000; box-sizing: border-box;',

        canvasRef: 'canvas',
        canvasWidth: originalWidth,       //初值800
        canvasHeight: originalHeight,     //初值500
        loadCanvas: false,

        globalRate: 1,
        originGlobalRate: 1,

        imgSrc: '',
        currentDescription: '请按照给定的要求进行标注',

        strokeWidth: 250,
        strokeNormalColor: 'rgba(255, 255, 0, 1)',
        strokeFocusColor: 'rgba(0, 0, 0, 1)',

        strokeNormalColorAutoChange: true,

        currentLayer: null,

        activeMode: 'drawing',
        drawingType: this.markType === 3 ? polyMode : rectMode,

        predefineColors: [
          'rgba(255, 69, 0, 1)',
          'rgba(250, 255, 0, 1)',
          'rgba(13, 255, 0, 1)',
          'rgba(120, 40, 94, 1)',
          'rgba(181, 100, 37, 1)',
          'rgba(209, 100, 56, 1)',
          'rgba(255, 69, 0, 0.68)',
          'rgba(250, 255, 0, 0.73)',
          'rgba(13, 255, 0, 0.5)',
          'rgba(120, 40, 94, 0.5)',
        ],

        isIndeterminate: false,             //给多选器用的
        checkAll: true,
        checkedShowModes: [rectMode, polyMode],
        showModes: showOptions,
        totalDescription: '',

        // marks: ['蘑菇', '火腿', '干锅', '云吞', '鸡蛋'],
        marks: {},

        isDown: false,
        layerIsLocked: false,
        lockIntervalID: 0,

        markInputMsg: '',
        lastX: 0,
        lastY: 0,

        firstPoint: true,
        secondPoint: false,

        polyObj: {},

        points: [],
      }
    },

    computed: {
      getSaverStyle() {
        return `width:${this.canvasWidth};height:${this.canvasHeight};background-image:url(${this.imgSrc});background-repeat:no-repeat; background-size:${this.canvasWidth}px ${this.canvasHeight}px;`
      },

      showRect() {
        return this.markType === 0 || this.markType === 1 || this.markType === 2;
      },

      showPoly() {
        return this.markType === 0 || this.markType === 3;
      },

      showCheckAll() {
        return this.markType === 0;
      },

      showTotal() {
        return this.markType === 0;
      },

      markRequireStatus() {            //标记的填写要求 0:一定不填 1：可填可不填 2：必须填
        switch (this.markType) {
          case 1:
            return 0;
          case 2:
            return 2;
          case 0:
          case 3:
          default:
            return 1;
        }
      },

    },

    methods: {
      canvasOuterSaverRect() {
        return this.$refs[this.canvasOuterSaver].getBoundingClientRect();     //不能是vue组件，要绑定在div或其他原生组件上
      },

      shouldShow(mode) {
        if (mode === rectMode) {
          return this.showRect;
        }
        if (mode === polyMode) {
          return this.showPoly;
        }
      },

      poly_handleMouseEvent(e) {
        if (this.firstPoint && !this.secondPoint) {
          // console.log('first point');
          this.initPoly();
          this.setCurrentLayerNameByTime();
          this.polyObj.name = this.currentLayerName;

          this.points.push(new Point(this.getLocXInCanvas(e), this.getLocYInCanvas(e)));
          this.points.push(new Point(this.getLocXInCanvas(e), this.getLocYInCanvas(e)));
          //重复两次，两点才有办法画线

          // draw a dot
          this.drawLines();

          this.firstPoint = false;
          this.secondPoint = true;
        } else if (!this.firstPoint && this.secondPoint) {
          // console.log('second point');

          this.points[1] = new Point(this.getLocXInCanvas(e), this.getLocYInCanvas(e));
          this.drawLines();
          this.secondPoint = false;
        } else {
          if (testIfCloseEnough(this.getLocXInCanvas(e), this.points[0].x, closeDelta) && testIfCloseEnough(this.getLocYInCanvas(e), this.points[0].y, closeDelta) && (e.type === 'mouseup' || !e.type)) {
            //(e.type==='mouseup'||!e.type)表示鼠标抬起或者这是一个手机上的触摸事件
            // console.log('close_enough,last one');
            this.polyObj['closed'] = true;

            this.polyObj['mouseover'] = this.layerMouseOverFunc;
            this.polyObj['mouseout'] = this.layerMouseOutFunc;
            this.polyObj['mousedown'] = this.layerMouseDownFunc;
            this.polyObj['mouseup'] = this.layerMouseUpFunc;


            let tempPoints = [];
            for (let i = 0; i < this.points.length; i++) {
              tempPoints.push(new Point(this.points[i].x * this.globalRate, this.points[i].y * this.globalRate));
            }

            //把note注入图层的data中
            this.polyObj['data'] = new NotePolygon(this.currentWorkerName, tempPoints, '', this.currentLayerName, this.strokeNormalColor, this.getActualStrokeWidth());

            this.drawLines();

            this.getCanvasTarget().getLayer(this.currentLayerName).type = polyLayer;

            this.lastX = e.eventX;
            this.lastY = e.eventY;

            this.handleAfterDrawingOneNoteFinished();
            this.initPoly();
          } else {
            //这里是中间状态
            // console.log('meanWhile');
            this.points.push(new Point(this.getLocXInCanvas(e), this.getLocYInCanvas(e)));
            //redraw the lines
            this.drawLines();
          }
        }
      },

      initPoly() {
        this.firstPoint = true;
        this.secondPoint = false;

        this.points = [];

        this.polyObj = {
          strokeStyle: this.strokeNormalColor,
          strokeWidth: this.getActualStrokeWidth(),
          rounded: true,
          layer: true,
          name: '',
          type: 'line'
        };
      },

      drawLines() {
        let jqTemp = this.getCanvasTarget();
        jqTemp.removeLayer(this.currentLayerName);
        for (let p = 0; p < this.points.length; p += 1) {
          this.polyObj['x' + (p + 1)] = this.points[p].x;
          this.polyObj['y' + (p + 1)] = this.points[p].y;
        }
        jqTemp.drawLine(this.polyObj);
      },

      doFocus() {
        if (this.$refs[this.inputRef]) { //惰性渲染一开始可能没有这个元素
          this.$refs[this.inputRef].handleFocus(); //似乎没有用。。。
          //TODO 自动聚焦的问题无法解决
        }
      },

      labelUp(layer) {
        this.setCenterPosition(layer);
        this.layerMouseUpFunc(layer, true);
      },

      setCenterPosition(layer) {
        let centerMsg = this.getLayerCenter(layer);
        layer.eventX = centerMsg.x;
        layer.eventY = centerMsg.y;
      },

      handleCollapseChange(value) {
        if (value !== 'drawing') {
          this.setCurrentLayerUnlocked();
        }
      },

      addOrReviseMark(layerName) {
        console.log(this.getCanvasTarget().getLayer(layerName));
        let mark = this.getCanvasTarget().getLayer(layerName).data.mark;
        let toAdd = '';
        if (!mark || mark === null || mark === '') {
          toAdd = '未标记'
        } else {
          toAdd = mark;
        }
        this.marks[layerName] = toAdd;
      },

      deleteMark(layerName) {
        delete this.marks[layerName];
      },

      getActualStrokeWidth() {
        return this.strokeWidth / 100;
      },

      getCanvasTarget() {
        return $(this.$refs[this.canvasRef]);
      },

      handleCanvasDownDistribute(e) {
        // console.log('callDown');
        this.setCurrentLayerUnlocked();
        if (this.activeMode === 'drawing' && !this.isMoving() && this[this.drawingType + 'DownHandler']) {
          this.isDown = true;
          if (this.drawingType === rectMode) {
            this.rectDownHandler(this.getWrappedEvent(e));
          }
          if (this.drawingType === polyMode) {
            this.polyDownHandler(this.getWrappedEvent(e));
          }
        }
      },

      handleCanvasUpDistribute(e) {
        // console.log('callUp');
        if (this.activeMode === 'drawing' && !this.isMoving() && this[this.drawingType + 'UpHandler']) {
          if (this.drawingType === rectMode) {
            this.rectUpHandler(this.getWrappedEvent(e));
          }
          if (this.drawingType === polyMode) {
            this.polyUpHandler(this.getWrappedEvent(e));
          }
          this.isDown = false;
        }
      },

      handleCanvasMoveDistribute(e) {
        // console.log('callMove');
        let event = this.getWrappedEvent(e);
        if (this.activeMode === 'drawing' && !this.isMoving() && this[this.drawingType + 'MoveHandler'] && this.isDown) {
          if (this.isNearlyOutOfBorder(event)) {
            this.handleCanvasUpDistribute(event);
          } else {
            if (this.drawingType === rectMode) {
              this.rectMoveHandler(this.getWrappedEvent(e));
            }
            if (this.drawingType === polyMode) {
              this.polyMoveHandler(this.getWrappedEvent(e));
            }
          }
        }
      },

      handleAfterDrawingOneNoteFinished() {
        this.addOrReviseMark(this.currentLayerName);
        if (this.strokeNormalColorAutoChange) {
          this.changeStrokeColor();
        }
        this.setCurrentLayerLocked();
        if (this.markRequireStatus === 2) {
          this.openInputDialog(this.getCanvasTarget().getLayer(this.currentLayerName));
        }
      },

      rectDownHandler(e) {

        this.setCurrentLayerNameByTime();
        let x = this.getLocXInCanvas(e);
        let y = this.getLocYInCanvas(e);
        this.currentRectX = x;
        this.currentRectY = y;

        let canvas = this.getCanvasTarget();

        let that = this;
        canvas.addLayer({
          type: rectLayer,
          strokeStyle: that.strokeNormalColor,
          strokeWidth: that.getActualStrokeWidth(),
          name: that.currentLayerName,
          fromCenter: false,
          x: that.currentRectX, y: that.currentRectY,
          width: 1,
          height: 1
        });
        canvas.drawLayers();
        canvas.saveCanvas();       //save之后可以恢复，这里不一定要保存
      },

      rectMoveHandler(e) {
        let canvas = this.getCanvasTarget();

        let x = this.getLocXInCanvas(e);
        let y = this.getLocYInCanvas(e);

        let width = x - this.currentRectX;
        let height = y - this.currentRectY;

        let that = this;

        canvas.removeLayer(that.currentLayerName);
        canvas.addLayer({
          type: rectLayer,
          strokeStyle: that.strokeNormalColor,
          strokeWidth: that.getActualStrokeWidth(),
          name: that.currentLayerName,
          fromCenter: false,
          x: that.currentRectX, y: that.currentRectY,
          width: width,
          height: height
        });

        canvas.drawLayers();
        canvas.saveCanvas();
      },

      rectUpHandler(e) {
        let canvas = this.getCanvasTarget();

        let x = this.getLocXInCanvas(e);
        let y = this.getLocYInCanvas(e);

        let width = x - this.currentRectX;
        let height = y - this.currentRectY;

        canvas.removeLayer(this.currentLayerName);

        if (Math.abs(width) >= 5 && Math.abs(height) >= 5) {
          let dataObj = new NoteRectangle(this.currentWorkerName, this.currentRectX * this.globalRate,
            this.currentRectY * this.globalRate, width * this.globalRate, height * this.globalRate, '',
            this.currentLayerName, this.strokeNormalColor, this.getActualStrokeWidth());

          let that = this;
          canvas.addLayer({
            type: rectLayer,
            strokeStyle: that.strokeNormalColor,
            strokeWidth: that.getActualStrokeWidth(),
            name: that.currentLayerName,
            fromCenter: false,
            x: that.currentRectX, y: that.currentRectY,
            width: width,
            height: height,
            data: dataObj,
            mouseover: that.layerMouseOverFunc,
            mouseout: that.layerMouseOutFunc,
            mousedown: that.layerMouseDownFunc,
            mouseup: that.layerMouseUpFunc,
          });
          this.lastX = x;
          this.lastY = y;
          canvas.drawLayers();
          this.handleAfterDrawingOneNoteFinished();
        } else {
          canvas.drawLayers();
        }
      },

      polyDownHandler(e) {

      },

      polyMoveHandler(e) {
        this.poly_handleMouseEvent(e);
      },

      polyUpHandler(e) {
        this.poly_handleMouseEvent(e);
      },

      setCurrentLayerNameByTime() {
        this.currentLayerName = this.currentWorkerName + getIDByTime();
      },

      getLocXInCanvas(e) {
        return e.clientX - e.target.getBoundingClientRect().left;
      },

      getLocYInCanvas(e) {
        return e.clientY - e.target.getBoundingClientRect().top;
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

      isMoving() {
        return this.activeMode === 'drawing' && this.drawingType === 'move';
      },

      getModeName(val) {
        switch (val) {
          case 'rect':
            return '矩形';
          case 'poly':
            return '多边形';
          default:
            return '';
        }
      },

      refreshComponent(imgSrc, description = '请按照要求进行标注') {
        this.loadCanvas = false;
        this.imgSrc = imgSrc;
        this.currentDescription = description;
        this.globalRate = 1;
        this.originGlobalRate = 1;
        this.currentLayerName = '';
        this.lengthFirstToZero = false;

        this.marks = {};
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

          that.originGlobalRate = that.globalRate;
          that.loadCanvas = true;

          //TODO 发起者查看需要新加这句话
          that.loadByOneImageURL(imgSrc);
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

      formatTooltip(val) {
        return val / 100;
      },

      handleCheckAllChange(val) {
        this.checkedShowModes = val ? showOptions : [];
        this.isIndeterminate = false;
        if (val) {
          this.showLayer(rectLayer);
          this.showLayer(polyLayer);
        } else {
          this.hideLayer(rectLayer);
          this.hideLayer(polyLayer);
        }
      },

      handleCheckedModesChange(value) {
        let checkedCount = value.length;
        this.checkAll = checkedCount === this.showModes.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.showModes.length;
      },

      getHoverColor(color) {
        let arr = color.split(',');
        let alpha = parseInt(arr[3].split(')')[0]);
        alpha /= 2;
        arr.pop();
        arr.push(' ' + alpha.toString() + ')');
        return arr.join(',');
      },

      layerMouseOverFunc(layer, signal = false) {
        if ((this.activeMode !== 'drawing' || signal || this.isMoving()) && !(this.layerIsLocked && layer.name === this.currentLayerName)) {
          this.handleLayerMouseOver(layer);
          let centerMsg = this.getLayerCenter(layer);
          this.getCanvasTarget().drawText({
            fillStyle: '#000',
            x: centerMsg.x, y: centerMsg.y,
            fontSize: 15,
            fontFamily: 'Arial',
            text: layer.data.mark ? layer.data.mark : '',
            layer: true,
            name: 'toDeleteHover',
          });
        }
      },

      getLayerCenter(layer) {
        let result = {x: 0, y: 0};
        if (layer.type === rectLayer) {
          result.x = layer.x + layer.width / 2;
          result.y = layer.y + layer.height / 2;
        } else {
          result = getPolygonAreaCenter(layer.data.points);
        }
        return result;
      },

      handleLayerMouseOver(layer) {
        this.getCanvasTarget().animateLayer(layer, {
          fillStyle: this.getHoverColor(layer.data.strokeColor),
        }, 100);
      },

      layerMouseOutFunc(layer, signal = false) {
        if (this.activeMode !== 'drawing' || signal || this.isMoving()) {
          this.getCanvasTarget().animateLayer(layer, {
            fillStyle: 'transparent',
            scale: 1
          }, 0);
          this.getCanvasTarget().removeLayer('toDeleteHover');
        }
      },

      layerMouseDownFunc(layer, signal = false) {
        if (this.activeMode !== 'drawing' || signal) {
          this.setCurrentLayerUnlocked();
          this.getCanvasTarget().animateLayer(layer, {
            fillStyle: this.getHoverColor(this.getRevertColor(layer.data.strokeColor)),
            scale: 1.25
          }, 250);
        }
      },

      layerMouseUpFunc(layer, signal = false) {
        if (this.activeMode !== 'drawing' || signal) {
          this.getCanvasTarget().animateLayer(layer, {
            fillStyle: this.getHoverColor(layer.data.strokeColor),
            scale: 1
          }, 250);
          this.currentLayerName = layer.name;
          this.setCurrentLayerLocked();
          if (this.markRequireStatus !== 0) {
            this.openInputDialog(layer);
          }
        }

        if (this.isMoving()) {
          this.updateDataPosition(layer);
        }
      },

      updateDataPosition(layer) {
        if (layer.type === rectLayer) {
          let target = layer.data;
          target.left = layer.x * this.globalRate;
          target.top = layer.y * this.globalRate;
          target.width = layer.width * this.globalRate;
          target.height = layer.height * this.globalRate;
        }
        if (layer.type === polyLayer) {
          layer.data.points = this.getPointsFromLayer(layer);
        }
      },

      getPointsFromLayer(layer) {
        let deltaX = layer.x;
        let deltaY = layer.y;
        let length = layer.data.points.length;
        let tempPoints = [];
        for (let i = 0; i < length; i++) {
          let x = layer['x' + (i + 1)] + deltaX;
          let y = layer['y' + (i + 1)] + deltaY;
          tempPoints.push(new Point(x, y));
        }
        return tempPoints;
      },

      setCurrentLayerLocked() {
        if (!this.layerIsLocked && this.currentLayerName) {
          this.layerIsLocked = true;
          let canvas = this.getCanvasTarget();
          let layer = canvas.getLayer(this.currentLayerName);
          let duration = 250;
          let that = this;
          let delay = 25;

          this.setShiningEffect(canvas, layer, duration);     //先做一次

          this.lockIntervalID = setInterval(function () {
            that.setShiningEffect(canvas, layer, duration);
          }, 2 * duration + delay);
        }
      },

      setShiningEffect(canvas, layer, duration) {
        canvas.animateLayer(layer, {
          strokeStyle: this.strokeFocusColor,
        }, duration);
        canvas.animateLayer(layer, {
          strokeStyle: layer.data.strokeColor ? layer.data.strokeColor : '#000',
        }, duration);
      },

      setCurrentLayerUnlocked() {
        if (this.layerIsLocked && this.currentLayerName) {
          clearInterval(this.lockIntervalID);
          this.layerIsLocked = false;
          let canvas = this.getCanvasTarget();
          let layer = canvas.getLayer(this.currentLayerName);
          canvas.animateLayer(layer, {
            strokeStyle: layer.data.strokeColor ? layer.data.strokeColor : '#000',
            fillStyle: 'transparent'
          }, 0);
        }
      },

      changeStrokeColor() {
        let numArr = [0, 1, 2];
        let num1 = getRandomIntInclusive(0, 2);
        let num2 = getRandomIntInclusive(0, 2);
        while (num2 === num1) {
          num2 = getRandomIntInclusive(0, 2);
        }
        numArr.splice(numArr.indexOf(num1), 1);
        numArr.splice(numArr.indexOf(num2), 1);
        let num3 = numArr[0];

        let arr = this.strokeNormalColor.split(',');
        let newVal = getRandomIntInclusive(0, 255);

        let R = parseInt(arr[0].split('(').pop());
        let G = parseInt(arr[1].split(' ').pop());
        let B = parseInt(arr[2].split(' ').pop());
        let result = [];
        result.push(R, G, B);

        if (result[num1] === 0) {
          num1 = (num1 + 1) % 3;
          num2 = (num2 + 1) % 3;
          num3 = (num3 + 1) % 3;
        }

        result[num1] = 0;
        result[num2] = 255;
        result[num3] = newVal;

        for (let i = 0; i < result.length; i++) {
          result[i] = result[i].toString();
        }
        this.strokeNormalColor = 'rgba(' + result.join(', ') + ', 1)';
      },

      getRevertColor(color) {
        let arr = color.split(',');
        let R = parseInt(arr[0].split('(').pop());
        let G = parseInt(arr[1].split(' ').pop());
        let B = parseInt(arr[2].split(' ').pop());
        let result = [];
        result.push(R, G, B);
        for (let i = 0; i < result.length; i++) {
          result[i] = 255 - result[i];
        }
        return 'rgba(' + result.join(', ') + ', 1)';
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

      openInputDialog(layer) {
        let boundingClientRect = this.getCanvasTarget()[0].getBoundingClientRect();
        this.dialogVisible = true;
        this.markInputMsg = layer.data.mark ? layer.data.mark : '';
        if (layer.eventX === null && layer.eventY === null) {
          this.eventScreenX = boundingClientRect.left + this.lastX;
          this.eventScreenY = boundingClientRect.top + this.lastY;
        } else {
          this.eventScreenX = boundingClientRect.left + layer.eventX;
          this.eventScreenY = boundingClientRect.top + layer.eventY;
        }
      },

      handleClose() {
        let mark = this.getCanvasTarget().getLayer(this.currentLayerName).data.mark;
        if (this.markRequireStatus === 2 && (!mark || mark === '' || mark === null)) {
          this.$message({
            message: '此项任务的标记内容是必填项，请在填写后点击修改',
            type: 'warning',
            duration: 1500
          });
          return false;
        } else {
          this.dialogVisible = false;
          this.setCurrentLayerUnlocked();
          return true;
        }
      },

      handleRevise() {
        if (!this.markInputMsg || this.markInputMsg === '' || this.markInputMsg === null) {
          //必须填又没有填
          this.$message({
            message: '请不要在未填写任何内容的情况下点击修改',
            type: 'warning',
            duration: 1500
          });
        } else {
          this.getCanvasTarget().getLayer(this.currentLayerName).data.mark = this.markInputMsg;
          this.dialogVisible = false;
          this.setCurrentLayerUnlocked();
          this.addOrReviseMark(this.currentLayerName);
        }
      },

      handleDelete() {
        this.setCurrentLayerUnlocked();
        this.deleteMark(this.currentLayerName);
        this.getCanvasTarget().removeLayer(this.currentLayerName);
        this.$message({
          message: '删除成功',
          type: 'success',
          duration: 1500
        });
        this.dialogVisible = false;
        this.currentLayerName = '';
      },

      hideLayer(type) {
        let canvasJQ = this.getCanvasTarget();
        canvasJQ.getLayers(function (layer) {
          if (layer.type === type) {
            layer.visible = false;
          }
          return false; // do not generate the array
        });
        canvasJQ.drawLayers();
      },

      showLayer(type) {
        let canvasJQ = this.getCanvasTarget();
        canvasJQ.getLayers(function (layer) {
          if (layer.type === type) {
            layer.visible = true;
          }
          return false; // do not generate the array
        });
        canvasJQ.drawLayers();
      },

      handleSingleChange(value, mode) {
        if (mode === rectMode) {
          value ? this.showLayer(rectLayer) : this.hideLayer(rectLayer);
        }
        if (mode === polyMode) {
          value ? this.showLayer(polyLayer) : this.hideLayer(polyLayer);
        }
      },

      makeLayersMovable() {
        let that = this;
        let obj = {
          bringToFront: true,
          draggable: true,
          drag: function (layer) {
            let xLJudgement = false;
            let xRJudgement = false;
            let yTJudgement = false;
            let yBJudgement = false;
            if (layer.type === rectLayer) {
              xLJudgement = layer.x <= 0;
              xRJudgement = layer.x + layer.width >= that.canvasWidth;
              yTJudgement = layer.y <= 0;
              yBJudgement = layer.y + layer.height >= that.canvasHeight;
            }
            if (layer.type === polyLayer) {
              let points = that.getPointsFromLayer(layer);
              let saver = getMinAndMaxXY(points);
              xLJudgement = saver.xMin <= 0;
              xRJudgement = saver.xMax >= that.canvasWidth;
              yTJudgement = saver.yMin <= 0;
              yBJudgement = saver.yMax >= that.canvasHeight;
            }
            if (xLJudgement) {
              layer.x += 6;
              layer.restrictDragToAxis = 'y'
            }
            if (xRJudgement) {
              layer.x -= 6;
              layer.restrictDragToAxis = 'y'
            }
            if (yTJudgement) {
              layer.y += 6;
              layer.restrictDragToAxis = 'x'
            }
            if (yBJudgement) {
              layer.y -= 6;
              layer.restrictDragToAxis = 'x'
            }
          },
          dragstop: function (layer) {
            layer.restrictDragToAxis = null;
          },
        };
        this.getCanvasTarget().setLayers(obj).drawLayers();
      },

      makeLayersUnmovable() {
        this.getCanvasTarget().setLayers({draggable: false}).drawLayers();
      },

      handleDrawingTypeChange(val) {
        val === moveMode ? (this.setCurrentLayerUnlocked() && false || this.makeLayersMovable()) : this.makeLayersUnmovable();
      },

      handleSubmit() {
        this.setCurrentLayerUnlocked();
        let that = this;
        let mark = this.getMark();
        if (this.markType !== 0) {
          if (mark.noteRectangle.length > 0 || mark.notePolygon.length > 0 || mark.noteTotal.length > 0) {
            this.$http.post('/mark/postMark', mark)
              .then(function () {
                that.$message({
                  message: '您本张图片的标注信息已经成功提交',
                  type: 'success',
                  duration: 1500
                });
                that.handleSubmitAfterSuccess();
              })
              .catch(function (error) {
                console.log(error);
              });
          } else {
            this.$message({
              message: '请不要在未填写任何内容的情况下点击提交',
              type: 'warning',
              duration: 1500
            });
          }
        } else {
          if (mark.noteRectangle.length > 0 || mark.notePolygon.length > 0 || mark.noteTotal.length > 0) {
            that.handleSubmitAfterSuccess();
          } else {
            this.$message({
              message: '请不要在未填写任何内容的情况下点击提交',
              type: 'warning',
              duration: 1500
            });
          }
        }
      },

      getMark() {
        let noteRectangle = [];
        let notePolygon = [];
        let noteTotal = [];

        this.getCanvasTarget().getLayers(function (layer) {
          if (layer.type === rectLayer) {
            noteRectangle.push(layer.data);
          }
          if (layer.type === polyLayer) {
            notePolygon.push(layer.data);
          }
          return false; // do not generate the array
        });

        let description = this.totalDescription;
        if (description && description != null && description !== '') {
          noteTotal.push({mark: this.totalDescription, id: this.currentWorkerName + getIDByTime()});
        }

        return {imgURL: this.imgSrc, workerName: this.currentWorkerName, noteRectangle, notePolygon, noteTotal};
      },

      handleSubmitAfterSuccess() {
        let isAlreadySet = false;
        if (this.imgCounter % this.recheckCircleNum === 0 && (this.markType === 1 || this.markType === 2)) {              //补充用于测试的图片
          this.imgCounter %= this.recheckCircleNum;
          this.recheckCircleNum += getRandomIntInclusive(0, 20);
          this.getCheckImgsAndRefreshCanvas();
          isAlreadySet = true;
        }

        if (this.checkImages && this.checkImages.length && this.checkImages.length > 0) {               //有测试用的图片
          if (!isAlreadySet) {
            let tempObj = this.checkImages.pop();
            this.refreshComponent(tempObj.imgURL, tempObj.description);                            //优先进行测试
          }
          if (this.checkImages.length === 0) {
            this.lengthFirstToZero = true;
          }
        } else {
          if (this.lengthFirstToZero) {
            this.lengthFirstToZero = false;
            this.checkIfGoodJob();
          } else {
            this.imgCounter++;                                                                    //否则进行普通测试
            this.getNormalImgAndRefreshCanvas();
          }
        }
      },

      checkIfGoodJob() {
        let that = this;
        this.$http.get('/mark/test', {
          params: {
            tag: that.tagMsg,
            type: that.markType,
            username: that.currentWorkerName
          }
        })
          .then(function (response) {
            let isDoingGoodJob = response.data['mes'];
            if (!isDoingGoodJob) {
              that.$router.push('/testPage');
              that.$message({
                message: '您似乎有些疲倦了，标注的正确率有所下降',
                type: 'warning',
                duration: 1500
              });
            }
          })
          .catch(function (error) {
            that.$message({
              message: '请检查您的网络',
              type: 'error',
              duration: 1500
            });
            console.log(error);
          });
      },

      getCheckImgsAndRefreshCanvas() {
        let that = this;
        this.$http.get('/mark/markTest', {
          params: {
            tag: that.tagMsg,
            type: that.markType,
            username: that.currentWorkerName
          }
        })
          .then(function (response) {
            console.log('回传的图像数据');
            console.log(response.data);
            that.checkImages = response.data['imgs'];
            let tempObj = that.checkImages.pop();
            that.refreshComponent(tempObj.imgURL, tempObj.description);
          })
          .catch(function (error) {
            that.$message({
              message: '加载图片数据失败',
              type: 'error',
              duration: 1500
            });
            console.log(error);
          });
      },

      getNormalImgAndRefreshCanvas() {
        let that = this;

        if (this.markType === 0) {
          this.refreshComponent(testDrawImages[(this.imgCounter - 1) % testDrawImages.length]);
          this.$message({
            message: '请注意：您现在处于测试模式',
            duration: 1800
          });
          return;
        }

        console.log(this.tagMsg);
        console.log(this.markType);
        this.$http.get('/mark/markOne', {
          params: {
            tag: that.tagMsg,
            type: that.markType,
            username: that.currentWorkerName
          }
        })
          .then(function (response) {
            let ImageMsg = response.data;
            console.log('图片路径');
            console.log(ImageMsg.imgURL);
            that.refreshComponent(ImageMsg.imgURL, ImageMsg.description);
            console.log('更新后');
            console.log(that.imgSrc);
          })
          .catch(function (error) {
            that.$message({
              message: '加载图片数据失败',
              type: 'error',
              duration: 1500
            });
            console.log(error);
          });
      },

      handleSizeUp() {
        let oldWidth = this.canvasWidth;
        let newWidth = oldWidth + deltaToChange;
        let changeRate = newWidth / oldWidth;
        let newHeight = this.canvasHeight * changeRate;

        let maxWidth = this.canvasOuterSaverRect().width;
        let maxHeight = this.canvasOuterSaverRect().height;

        if (newWidth < maxWidth && newHeight < maxHeight && newWidth > canvasMinWidth && newHeight > canvasMinHeight) {
          this.canvasWidth = newWidth;
          this.canvasHeight = newHeight;
          this.globalRate *= changeRate;
          this.refreshLayerSize(changeRate);
        }
      },

      handleSizeDown() {
        let oldWidth = this.canvasWidth;
        let newWidth = oldWidth - deltaToChange;
        let changeRate = newWidth / oldWidth;
        let newHeight = this.canvasHeight * changeRate;

        let maxWidth = this.canvasOuterSaverRect().width;
        let maxHeight = this.canvasOuterSaverRect().height;

        if (newWidth < maxWidth && newHeight < maxHeight && newWidth > canvasMinWidth && newHeight > canvasMinHeight) {
          this.canvasWidth = newWidth;
          this.canvasHeight = newHeight;
          this.globalRate *= changeRate;
          this.refreshLayerSize(changeRate);
        }
      },

      handleSizeBack() {
        let changeRate = this.originGlobalRate / this.globalRate;
        this.canvasWidth *= changeRate;
        this.canvasHeight *= changeRate;
        this.globalRate = this.originGlobalRate;
        this.refreshLayerSize(changeRate);
      },

      refreshLayerSize(changeRate) {
        let canvasJQ = this.getCanvasTarget();
        canvasJQ.getLayers(function (layer) {
          if (layer.type === rectLayer) {
            layer.x *= changeRate;
            layer.y *= changeRate;
            layer.width *= changeRate;
            layer.height *= changeRate;
          }
          if (layer.type === polyLayer) {
            layer.x *= changeRate;
            layer.y *= changeRate;
            for (let i = 0; i < layer.data.points.length; i++) {
              layer['x' + (i + 1)] *= changeRate;
              layer['y' + (i + 1)] *= changeRate;
            }
          }
          return false; // do not generate the array
        });
        this.getCanvasTarget().drawLayers();
      },

      handlePre() {
        if (this.marksViewIndex - 1 >= 0) {
          this.refreshComponent(this.marksViewImages[--this.marksViewIndex]);
        } else {
          this.$message({
            message: '已经到达最前面了',
            type: 'info',
            duration: 1500
          });
        }
      },

      handleNext() {
        if (this.marksViewIndex + 1 <= this.marksViewImages.length - 1) {
          this.refreshComponent(this.marksViewImages[++this.marksViewIndex]);
        } else {
          this.$message({
            message: '已经到达最后面了',
            type: 'info',
            duration: 1500
          });
        }
      },

      //TODO 加载器全部在下面！！！！！

      loadByOneImageURL(imgURL) {
        let that = this;
        this.$http.post('/mark/checkImage', {
          imgURL
        })
          .then(function (response) {
            let marks = response.data.marks;
            if (marks && marks.length > 0) {
              that.loadFromMarks(marks);
            }
          })
          .catch(function (error) {
            that.$message({
              message: '网络错误，无法加载图片',
              type: 'error',
              duration: 1500
            });
            console.log(error);
          });
      },

      loadFromMarks(marks) {
        for (let mark of marks) {
          this.loadFromOneMark(mark);
        }
      },

      loadFromOneMark(mark) {
        for (let note of mark.noteRectangle) {
          this.loadOneRect(note);
        }
        for (let note of mark.notePolygon) {
          this.loadOnePolygon(note);
        }
      },

      loadOneRect(noteRectangle) {
        let canvasJQ = this.getCanvasTarget();
        canvasJQ.addLayer({
          type: rectLayer,
          strokeStyle: noteRectangle.strokeColor,
          strokeWidth: noteRectangle.strokeWidth,
          name: noteRectangle.id,
          fromCenter: false,
          x: noteRectangle.left, y: noteRectangle.top,
          width: noteRectangle.width,
          height: noteRectangle.height,
          data: noteRectangle,
          mouseover: this.layerMouseOverFunc,
          mouseout: this.layerMouseOutFunc,
          mousedown: this.layerMouseDownFunc,
          mouseup: this.layerMouseUpFunc,
        });
        canvasJQ.drawLayers();
        this.addOrReviseMark(noteRectangle.id);
      },

      loadOnePolygon(notePolygon) {
        let polyObj = {};
        let canvasJQ = this.getCanvasTarget();
        for (let p = 0; p < notePolygon.data.points.length; p += 1) {
          polyObj['x' + (p + 1)] = notePolygon.data.points[p].x;
          polyObj['y' + (p + 1)] = notePolygon.data.points[p].y;
        }
        polyObj['strokeStyle'] = notePolygon.strokeStyle;
        polyObj['strokeWidth'] = notePolygon.strokeWidth;
        polyObj['rounded'] = true;
        polyObj['layer'] = true;
        polyObj['name'] = notePolygon.id;
        polyObj['closed'] = true;
        polyObj['mouseover'] = this.layerMouseOverFunc;
        polyObj['mouseout'] = this.layerMouseOutFunc;
        polyObj['mousedown'] = this.layerMouseDownFunc;
        polyObj['mouseup'] = this.layerMouseUpFunc;
        polyObj['data'] = notePolygon;
        canvasJQ.drawLine(polyObj);
        canvasJQ.getLayer(notePolygon.id).type = polyLayer;
        this.addOrReviseMark(notePolygon.id);
      },
    },

    beforeDestroy() {
      this.setCurrentLayerUnlocked();
    },
  }
</script>

<style scoped>

</style>
