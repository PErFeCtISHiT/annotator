<template>
  <div id="myChart" :style="{width: '300px', height: '300px'}">

  </div>
</template>

<script>
  export default {
    name: "echart",

    mounted() {
      this.drawLines();
    },

    methods: {
      drawLines: function (){
        let myChart = this.$echarts.init(document.getElementById('myChart'), 'customed');
        // 绘制图表
        myChart.setOption({
          title: { text: '在Vue中使用echarts' },
          tooltip: {},
          xAxis: {
            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
          },
          yAxis: {},
          series: [{
            name: '销量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20]
          }]
        });
      }


    }
  }
</script>

<style scoped>

</style>
