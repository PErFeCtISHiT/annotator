<template>
  <div>
    <el-row type="flex" justify="center" class="row-bg">
      <el-col :span="20" style="">

        <!--主要的div-->
        <div style="text-align: center; margin: auto">
          <el-row>

            <!--工人的图表-->
            <el-col :span="24" v-show="$store.state.user.isWorker">
              <el-card :body-style="{ padding: '0px' }">
                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart1" style="width: 500px; height: 550px; margin-left: -5%"></div>
                </el-row>


                <div style="font-size: x-large">
                  <span>综合评分为<em>{{ score[5] * 100 }}</em></span>
                </div>

                <br/><br/>

                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart2" style="width: 600px; height: 550px; margin-left: -5%"></div>
                </el-row>

                <br/><br/>

                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart3" style="width: 400px; height: 400px; margin-left: -5%"></div>
                </el-row>

              </el-card>
            </el-col>

            <!--发起者的图标-->
            <el-col :span="24" v-show="$store.state.user.isRequester">
              <el-card :body-style="{ padding: '0px' }">

                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart4" style="width: 500px; height: 550px; margin-left: -5%"></div>
                </el-row>


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
      if(!this.$store.state.user.isRequester) {
        this.drawLinesOfWorkers();
      }
      else {
        this.drawLinesOfRequester();
      }
    },

    data() {
      return {
        score: [0.5, 0.5, 0.1, 0.1, 0.1, 0.77],
        tagNum: [500, 25, 400, 150, 100],
        catNum: [100, 35, 48]
      }
    },

    methods: {

      drawLinesOfWorkers: function () {
        let that = this;
        let scoreChart = this.$echarts.init(document.getElementById('myChart1'), 'customed.js');
        // 绘制图表
        scoreChart.setOption({
          title: {
            text: '个人能力评价雷达图'
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

        let inclinationChart = this.$echarts.init(document.getElementById('myChart2'), 'customed.js');
        inclinationChart.setOption({
          title: {
            text: '标注的标签分布倾向',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter: function (params) {
              let tar = params[1];
              return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            splitLine: {show: false},
            data: ['总标注', '人物', '风景', '动物', '植物', '文本']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '辅助',
              type: 'bar',
              stack: '总量',
              itemStyle: {
                normal: {
                  barBorderColor: 'rgba(0,0,0,0)',
                  color: 'rgba(0,0,0,0)'
                },
                emphasis: {
                  barBorderColor: 'rgba(0,0,0,0)',
                  color: 'rgba(0,0,0,0)'
                }
              },
              data: function () {
                let tempItem = []
                for (let i = 1; i <= 4; i++) {
                  tempItem.push(that.tagNum.slice(i, 5).reduce((x, y) => x + y));
                }
                console.log(tempItem);
                return [0, ...tempItem, 0];
              }()
            },
            {
              name: '标注量',
              type: 'bar',
              stack: '总量',
              label: {
                normal: {
                  show: true,
                  position: 'inside'
                }
              },
              data: function () {
                return [that.tagNum.reduce((x, y) => x + y), ...that.tagNum];
              }()
            }
          ]
        });

        let categoryChart = this.$echarts.init(document.getElementById('myChart3'), 'customed.js');
        // 绘制图表
        categoryChart.setOption({
          title: {
            text: '标注的画法倾向',
          },
          xAxis: {
            type: 'category',
            data: ['指定目标', '描述子类', '划分区域']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            data: that.catNum,
            type: 'bar',
            color: ['#759aa0', '#e69d87', '#8dc1a9', '#ea7e53', '#eedd78', '#73a373', '#73b9bc', '#7289ab', '#91ca8c', '#f49f42'],
          }]
        });


      },//方法结束


      drawLinesOfRequester() {
        let that = this;
        let taskChart = this.$echarts.init(document.getElementById('myChart4'), 'customed.js');
        taskChart.setOption({
          //backgroundColor: ,

          title: {
            text: 'Customized Pie',
            left: 'center',
            top: 20,
            textStyle: {
              color: '#ccc'
            }
          },

          tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },

          visualMap: {
            show: false,
            min: 80,
            max: 600,
            inRange: {
              colorLightness: [0, 1]
            }
          },
          series : [
            {
              name:'访问来源',
              type:'pie',
              radius : '55%',
              center: ['50%', '50%'],
              data:[
                {value:335, name:'直接访问'},
                {value:310, name:'邮件营销'},
                {value:274, name:'联盟广告'},
                {value:235, name:'视频广告'},
                {value:400, name:'搜索引擎'}
              ].sort(function (a, b) { return a.value - b.value; }),
              roseType: 'radius',
              label: {
                normal: {
                  textStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                  }
                }
              },
              labelLine: {
                normal: {
                  lineStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                  },
                  smooth: 0.2,
                  length: 10,
                  length2: 20
                }
              },
              itemStyle: {
                normal: {
                  color: '#c23531',
                  shadowBlur: 200,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },

              animationType: 'scale',
              animationEasing: 'elasticOut',
              animationDelay: function (idx) {
                return Math.random() * 200;
              }
            }
          ]
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
