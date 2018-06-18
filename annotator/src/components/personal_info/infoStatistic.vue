<template>
  <div>
    <el-row type="flex" justify="center" class="row-bg">
      <el-col :span="20" style="">

        <!--主要的div-->
        <div style="text-align: center; margin: auto">
          <el-row>

            <el-col :span="24">
              <el-card :body-style="{ padding: '0px' }">
                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart" style="width: 600px; height: 550px; margin-left: 50px"></div>
                </el-row>


                <div style="font-size: larger">
                  <span>综合评分为{{ score[5] * 100 }}</span>
                </div>
              </el-card>
            </el-col>

          </el-row>
        </div>

      </el-col>
    </el-row>
  </div>
</template>

<script>
  import ElRow from "element-ui/packages/row/src/row";

  export default {
    components: {ElRow},
    name: "info-statistic",

    mounted() {
      this.drawLines();
    },

    data() {
      return {
        score: [0.5, 0.5, 0.1, 0.1, 0.1, 0.77]
      }
    },

    methods: {
      drawLines: function () {
        let that = this;
        let myChart = this.$echarts.init(document.getElementById('myChart'), 'customed');
        // 绘制图表
        myChart.setOption({
          title: {
            text: '1. 个人能力评价雷达图'
          },
          tooltip: {},
          legend: {
            data: ['']
          },
          radar: {
            //shape: 'circle',
            name: {
              textStyle: {
                color: '#fff',
                backgroundColor: '#999',
                borderRadius: 3,
                padding: [3, 5]
              }
            },
            indicator: [
              {name: '人物', max: 100},
              {name: '风景', max: 100},
              {name: '动物', max: 100},
              {name: '植物', max: 100},
              {name: '文本', max: 100},
            ]
          },
          series: [{
            name: '预算 vs 开销（Budget vs spending）',
            type: 'radar',
            // areaStyle: {normal: {}},
            data: [
              {
                value: function () {
                  return that.score.slice(0, 5).map(x => x * 100);
                }(),
                name: '能力评分'
              },
            ]
          }]
        });
      }

    }
  }
</script>

<style scoped>
  .my-span {
    font-size: xx-large;
    font-weight: bolder;
    font-family: Chalkboard;
  }
</style>
