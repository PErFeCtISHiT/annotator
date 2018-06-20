<template>
  <div id>
    <!--<p>{{title}}</p>-->
    <!--<el-button type="text" @click="dialogVisible = true">点击打开 Dialog</el-button>-->

    <!--<el-dialog-->
      <!--v-dialogDrag-->
      <!--title="提示"-->
      <!--:visible.sync="dialogVisible"-->
      <!--width="30%">-->
      <!--<span>这是一段信息</span>-->
      <!--<span slot="footer" class="dialog-footer">-->
        <!--<el-button @click="dialogVisible = false">取 消</el-button>-->
        <!--<el-button type="primary" @click="dialogVisible = false">确 定</el-button>-->
      <!--</span>-->
    <!--</el-dialog>-->

    <marks-viewer>
    </marks-viewer>

    <!--<canvas id="canvas2" width=600 height=400 style="border: 1px solid #000;"></canvas>-->
    <!--<div id="test"></div>-->
    <!--<button @click="handleCanvas">再次测试</button>-->
    <!--<router-link :to="'/1-1'" replace>次级跳转测试</router-link>-->
    <!--<br>-->
    <!--<br>-->
    <!--<br>-->
    <!--<div id="test3"></div>-->
    <!--<task-item :task-msg="obj"></task-item>-->
  </div>
</template>

<script>
  import TaskItem from "./taskItem";
  import CanvasDrawer from "./drawer/canvasDrawer";
  import Tester from "./test/tester";
  import MarksViewer from "./requester/marksViewer";

  export default {
    components: {
      MarksViewer,
      Tester,
      CanvasDrawer,
      TaskItem
    },
    name: "test-draw",
    data() {
      let obj = {
        imgNum: 8,
        taskID: 123,
        taskName: "标出所有人物",
        description: "请标出图片中的所有人物",
        sponsorName: "蔡蔚霖",
        startDate: "2017-4-22",
        endDate: "2017-5-1",
        progress: 0.6
      };
      return {
        dialogVisible: false,
        title: "我是画图测试页",
        obj: obj,
        canvasTarget: null
      }
    },
    // mounted(){
    //   this.handleCanvas();
    // },
    // mounted(){
    //   this.$http({
    //     method:'post',
    //     url:"mark/checkImage",
    //     // data:JSON.stringify({taskID:taskID,user:[{username:user}],imgName:imgName}),
    //     data:{taskID:1,users:[{username:"a"},{username:"b"}],imgName:"c"},
    //     // contentType:'application/json',
    //     // dataType:'json'
    //   });
    // },
    methods: {
      handleCanvas() {
        // $("#test").append('<canvas id="canvas" width= 600 height=400 style="border: 1px solid #000;"></canvas>');
        // var temp1 = 600 * Math.random();
        // var temp2 = 400 * Math.random();
        // var id = new Date().getTime();
        $('#canvas2').drawRect({
          click() {
            console.log('counted');
          },
          layer: true,
          draggable: true,
          fillStyle: '#000',
          x: 150, y: 100,
          width: 200,
          height: 100
        });
        // $("#test3").load('../../src/temp/testCode.html');
      },
      // handleChange() {
      //   $("#test").empty();
      //   $("#test").load('../../src/temp/test.html');
      // }
    }
  }
</script>

<style scoped>

</style>
