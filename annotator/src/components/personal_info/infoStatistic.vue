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
                  <div id="myChart3" style="width: 400px; height: 400px; margin-left: -5%; margin-bottom: 15px"></div>
                </el-row>

              </el-card>
            </el-col>

            <!--发起者的图标-->
            <el-col :span="24" v-show="$store.state.user.isRequester">
              <el-card :body-style="{ padding: '0px' }">

                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart4" style="width: 550px; height: 650px; margin-left: -5%"></div>
                </el-row>

                <el-row type="flex" justify="center" style="padding-top: 20px">
                  <div id="myChart5" style="width: 600px; height: 650px; margin-left: -5%"></div>
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
      let that = this;

      //工人部分
      if (this.$store.state.user.isWorker) {
        this.$http.all([
          that.$http.get('statistic/getCorrect?username=' + that.$store.state.user.userInfo.username),
          that.$http.get('statistic/getMarkNum?username=' + that.$store.state.user.userInfo.username),
        ])
          .then(that.$http.spread(function (rsp1, rsp2) {
            that.score = [].concat(rsp1.data.mes);
            that.tagNum = [].concat(rsp2.data.mes.slice(3, 8));
            that.catNum = [].concat(rsp2.data.mes.slice(0, 3));
            console.log('score--------------------',that.score);
            console.log('tagNum--------------------',that.tagNum);
            console.log('catNum--------------------',that.catNum);
            that.drawLinesOfWorkers();
          }))
          .catch(function (error) {
            that.$message({
              message: 'Num网络异常' + error,
              type: 'warning'
            });
          });



        // this.$http.get('statistic/getCorrect', {
        //   params: {
        //     username: that.$store.state.user.userInfo.username
        //   }
        // })
        //   .then(function (response) {
        //     that.score = [].concat(response.data.mes);
        //   })
        //   .catch(function (error) {
        //     that.$message({
        //       message: '网络异常' + error,
        //       type: 'warning'
        //     });
        //   });
        //
        // this.$http.get('statistic/getMarkNum', {
        //   params: {
        //     username: this.$store.state.user.userInfo.username
        //   }
        // })
        //   .then(function (response) {
        //     that.tagNum = [].concat(response.data.mes.slice(0, 3));
        //     that.catNum = [].concat(response.data.mes.slice(3, 8));
        //   });
        //
        // this.drawLinesOfWorkers();
      }

      //发起者部分
      else {
        this.$http.all([
          that.$http.get('statistic/getTaskNum?username=' + that.$store.state.user.userInfo.username),
          that.$http.get('statistic/getTaskDetail?username=' + that.$store.state.user.userInfo.username),
        ])
          .then(that.$http.spread(function (rsp1, rsp2) {
            that.taskNum = [...rsp1.data.mes];
            that.taskType = [].concat(rsp2.data.mes);

            console.log(that.taskType);
            that.drawLinesOfRequester();
          }))
          .catch(function (error) {
            that.$message({
              message: 'Num网络异常' + error,
              type: 'warning'
            });
          });

        // this.$http.get('statistic/getTaskNum', {
        //   params: {
        //     username: this.$store.state.user.userInfo.username
        //   }
        // })
        //   .then(function (response) {
        //     that.taskNum = [...response.data.mes];
        //   })
        //   .catch(function (error) {
        //     console.log(error);
        //     that.$message({
        //       message: 'Num网络异常' + error,
        //       type: 'warning'
        //     });
        //   });
        //
        // this.$http.get('statistic/getTaskDetail', {
        //   params: {
        //     username: this.$store.state.user.userInfo.username
        //   }
        // })
        //   .then(function (response) {
        //     that.taskType = [].concat(response.data.mes);
        //   })
        //   .catch(function (error) {
        //     that.$message({
        //       message: 'Task网络异常' + error,
        //       type: 'warning'
        //     });
        //   });
        //
        // this.drawLinesOfRequester();
      }
    },

    data() {
      return {
        score: [],
        tagNum: [],
        catNum: [],
        taskNum: [],
        taskType: []
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
                let tempItem = [];
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
        console.log('start init');
        taskChart.setOption({

          title: {
            text: '任务完成情况',
            left: 'center',
            top: 20,
            textStyle: {
              color: '#1c1c1c',
              fontSize: 30
            }
          },

          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },

          visualMap: {
            show: false,
            min: 0,
            max: 50,
            inRange: {
              colorLightness: [0.1, 0.9]
            }
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: '55%',
              center: ['50%', '50%'],
              data: [
                {value: that.taskNum[0], name: '已完成'},
                {value: that.taskNum[1], name: '进行中'},
              ].sort(function (a, b) {
                return a.value - b.value;
              }),
              roseType: 'radius',
              label: {
                normal: {
                  textStyle: {
                    color: '#73a373',
                    fontSize: 20
                  }
                }
              },
              labelLine: {
                normal: {
                  lineStyle: {
                    color: '#73a373'
                  },
                  smooth: 0.2,
                  length: 30,
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

        let taskTotalChart = this.$echarts.init(document.getElementById('myChart5'), 'customed.js');
        taskTotalChart.setOption({
          title: {
            text: '发布任务类型分布',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: ['您发布的任务', '系统中的所有任务']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'value',
            boundaryGap: [0.2, 0.4]
          },
          yAxis: {
            type: 'category',
            data: ['制定目标', '描述子类', '划分区域']
          },
          series: [
            {
              name: '您发布的任务',
              type: 'bar',
              data: function () {
                let temp = that.taskType.slice(3, 6);
                return temp.map(x => (x / temp.reduce((x, y) => x + y)).toFixed(2));
              }()
            },
            {
              name: '系统中的所有任务',
              type: 'bar',
              data: function () {
                let temp = that.taskType.slice(0, 3);
                return temp.map(x => (x / temp.reduce((x, y) => x + y)).toFixed(2));
              }()
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
