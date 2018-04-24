<template>
  <div>
    <p>{{title}}</p>
    <canvas id="canvas" width= 640 height=427 style="border: 1px solid #000;"></canvas>
    <button @click="handleCanvas">再次测试</button>
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
        $("#canvas").drawPolygon({
          draggable: true,
          fillStyle: "#6c3",
          x: 100, y: 100,
          radius: 50, sides: 5
        });
      }
    }
  }
</script>

<style scoped>

</style>
