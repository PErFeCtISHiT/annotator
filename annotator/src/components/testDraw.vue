<template>
  <div>
    <!--<p>{{title}}</p>-->
    <!--<canvas id="canvas" width= 600 height=400 style="border: 1px solid #000;"></canvas>-->
    <div id = "test"></div>
    <button @click="handleCanvas">再次测试</button>
    <button @click="handleChange">区块替换</button>
    <router-link :to="'/1-1'" replace>次级跳转测试</router-link>
  </div>
</template>

<script>
  export default {
    name: "test-draw",
    data() {
      return {
        title : "我是画图测试页"
      }
    },

    // mounted(){
    //   this.$http({
    //     method:'post',
    //     url:"mark/checkImage",
    //     // data:JSON.stringify({taskID:taskID,user:[{username:user}],imgName:imgName}),
    //     data:{taskID:123,user:[{username:"a"},{username:"b"}],imgName:"c.png"},
    //     // contentType:'application/json',
    //     // dataType:'json'
    //   });
    // },
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
