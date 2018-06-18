<template>
  <div class="requester-tasks">
    <el-row type="flex" justify="center">
      <el-col :span="22"><div>
        <tag-bar v-on:change="changeTabs"></tag-bar>
      </div></el-col>
    </el-row>

    <el-row type="flex" justify="center">
      <el-col :span="20">

        <el-row :gutter="10">
          <el-col :span="24">

            <requester-task-item v-for="(message, index) in messages"
                                 @download="handleDownload"
                                 :taskMsg="message" :theIndex="index" :key="message.taskID">
            </requester-task-item>

          </el-col>
        </el-row>

      </el-col>
    </el-row>



  </div>
</template>

<script>
  import tagBar from './tagBar'
  import requesterTaskItem from './requesterTaskItem'
  const items = [
    {
      taskID: '1',
      taskName: '静物之植物',
      description: '随便写一点',

      sponsorName: 'Ax',
      imgNum: 12,
      tag: ['植物'],
      startDate: '2018-03-21',
      endDate: '2018-08-08',
      status: 1
    },

    {
      taskID: '2',
      taskName: '静物之动物',
      description: '“风波一失所，各在天一隅”，这说的是多情人的离别；用来描述元稹写诗时候的感受也应不会有怎样的偏颇。元',

      sponsorName: 'Ba',
      imgNum: 18,
      tag: ['动物'],
      startDate: '2018-12-31',
      endDate: '2018-05-04',
      status: 2
    },

    {
      taskID: '3',
      taskName: 'test',
      description: '“风波一失所，各在天一隅”，这说的是多情人的离别；用来描述元稹写诗时候的感受也应不会有怎样的偏颇。元',

      sponsorName: 'Ba',
      imgNum: 18,
      tag: ['植物'],
      startDate: '2018-12-31',
      endDate: '2018-05-04',
      status: 1
    },

    // {
    //   taskID: 6,
    //   taskName: "成功了",
    //   description: "阿斯顿还是低啊随便丢撒比都把送都弄撒旦好似嗲上班都i啊班底哦那送你的撒都纳斯哦你滴哦啊索尼电视都",
    //   totalProgress: 0.67,
    //   tags: ['A', 'B', 'C'],
    //   startDate: "2018-04-25",
    //   endDate: "2018-04-27",
    // },
    // {
    //   taskID: 4,
    //   taskName: "失败了",
    //   description: "按时打开链接爱斯莫地方v王企鹅王企鹅女妇女被送女滴哦是计费的方式你",
    //   totalProgress: 1.00,
    //   tags: ['A', 'B', 'C'],
    //   startDate: "2018-04-25",
    //   endDate: "2018-04-27",
    // },
    // {
    //   taskID: 3,
    //   taskName: "1",
    //   description: "2351",
    //   totalProgress: 1.00,
    //   tags: ['A', 'B', 'C'],
    //   startDate: "2018-04-25",
    //   endDate: "2018-04-27",
    // },
    // {
    //   taskID: 2,
    //   taskName: "1",
    //   description: "2351",
    //   totalProgress: 0.67,
    //   tags: ['A', 'B', 'C'],
    //   startDate: "2018-04-25",
    //   endDate: "2018-04-27",
    // },
    // {
    //   taskID: 1,
    //   taskName: "1",
    //   description: "2351",
    //   totalProgress: 0.67,
    //   tags: ['A'],
    //   startDate: "2018-04-25",
    //   endDate: "2018-04-27",
    // }
  ];



  export default {
    components: {
      requesterTaskItem,
      tagBar
    },

    name: "requester-tasks",

    mounted: function () {
      this.changeTabs("total");
      //this.messages = items;
    },


    data () {
      return {
        messages: [],
        tags: [],

        tabName: ""
      };
    },

    methods: {
      /**
      * 这里那个bar需要用到的。
       * tabName是你里面传回来的
       */
      changeTabs: function (tabName) {

        this.tabName = tabName;

        //检查类型
        let status = 0, that = this, tab = "";
        if(tabName === 'already'){
          status = 2;
        }else if (tabName === 'undergoing'){
          status = 1;
        }else if (tabName === 'total'){
          status = 0;
        }else{
          status = 0;
          tab = tabName;
        }

        //console.log('status and tag: ', status, tab);
        this.$http.post('/task/myTasks', {
          username: this.$store.state.user.userInfo.username,
          status: status,
          tag: tab,
          userRole: 3
        })
          .then(function (response) {
            console.log(response.data);
            let data = response.data.tasks;

            that.messages = data;
          })
          .catch(function (error) {
            that.$message({
              message: '请求分类失败' + error,
              type: 'warning'
            });
            console.log('分类错误');
          });

      },

      // handleAggregation(uid, index) {
      //
      //   this.$confirm('结束此任务，积分无法退还。是否继续', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   })
      //     .then(() => {
      //
      //       //确认的话发一个ajax请求
      //       that.$http.get('/task/endTask',{
      //         params:{
      //           taskID: uid
      //         }
      //       })
      //         .then(function (response) {
      //           if(response.data.mes === true){
      //             that.messages.splice(index, 1);
      //             that.$message.success('已结束任务');
      //           }
      //           else{
      //             that.$message.warning('结束任务失败');
      //           }
      //         })
      //         .catch(function (error) {
      //           that.$message.warning('结束任务失败' + error);
      //         })
      //
      //     })
      //     .catch(() => {
      //       that.$message.info('已取消');
      //     })
      // },

      /**
       * 删除任务。是从子组件emit过来的
       * */
      handleDownload(payload) {
        let that = this;

        //确认的话发一个ajax请求
        this.$http.get('/task/getDataSet', {
          params: {
            taskID: payload.uid
          },
        })
          .then(function (response) {
            //let url = window.URL.createObjectURL(new Blob[response]);
            let link = document.createElement('a');
            link.style.display = 'none';
            //link.href = url;
            link.setAttribute('href', '/task/getDataSet?taskID='+ payload.uid);

            document.body.appendChild(link);
            link.click();
          })
          .catch(function (error) {
            that.$message.warning('下载失败' + error);
          })
      }
    }

  }
</script>

<style scoped>

</style>
