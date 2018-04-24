<template>
  <div>
    <p>{{title}}</p>
    <!--<canvas id="canvas" width= 600 height=400 style="border: 1px solid #000;"></canvas>-->
    <div id = "test"></div>
    <button @click="handleCanvas">再次测试</button>
    <button @click="handleChange">区块替换</button>
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
    //   this.handleCanvas();
    // },
    methods:{
      handleCanvas(){
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
        $("#test").load('../../src/temp/markLocality.html');
      }
    }
  }
</script>

<style scoped>

</style>
