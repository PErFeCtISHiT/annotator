<template>
  <div>
    <!--<p>{{title}}</p>-->
    <!--<canvas id="canvas" width= 600 height=400 style="border: 1px solid #000;"></canvas>-->
    <div id = "test"></div>
    <button @click="handleCanvas">再次测试</button>
    <button @click="handleChange">区块替换</button>
    <router-link :to="'/1-1'" replace>次级跳转测试</router-link>
    <br>
    <br>
    <br>
    <task-item :task-msg="obj"></task-item>
  </div>
</template>

<script>
  import TaskItem from "./taskItem";

  export default {
    components: {TaskItem},
    name: "test-draw",
    data() {
      return {
        title : "我是画图测试页",
        obj:{
          imgNum: 8,
          taskID: 123,
          taskName:"标出所有人物",
          description:"请标出图片中的所有人物",
          requestor:"蔡蔚霖",
          startDate:"2017-4-22",
          endDate:"2017-5-1",
          progress: 0.6
        }
      }
    },

    mounted(){
      this.$http({
        method:'post',
        url:"mark/checkImage",
        // data:JSON.stringify({taskID:taskID,user:[{username:user}],imgName:imgName}),
        data:{taskID:1,users:[{username:"a"},{username:"b"}],imgName:"c"},
        // contentType:'application/json',
        // dataType:'json'
      });
    },
    methods:{
      handleCanvas(){
        $("#test").append('<canvas id="canvas" width= 600 height=400 style="border: 1px solid #000;"></canvas>');
        var temp1 = 600 * Math.random();
        var temp2 = 400 * Math.random();
        var id = new Date().getTime();
        $("#canvas").drawPolygon({
          draggable: true,
          strokeStyle: "#6c3",
          x: temp1, y: temp2,
          radius: 50, sides: 5,
          layer:true,
          name: id,
        });
      },
      handleChange(){
        $("#test").empty();
        $("#test").load('../../src/temp/markLocality.html');
      }
    }
  }
</script>

<style scoped>

</style>
