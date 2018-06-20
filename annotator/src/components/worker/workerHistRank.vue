<template>
  <el-col :span="24">
    <el-col :span="6" style="margin-left: 30px; margin-right: 70px">
      <el-card color="#EAF7E7" class="box-card">
        <div style="padding-top: 23px; margin-top: 10px">
          <span class="label-all">
            用户名:&nbsp
          </span>
          <span class="label-all" style="font-weight: bolder">
            {{$store.state.user.userInfo.username}}
          </span>
        </div>
        <div style="padding-top: 29px">
          <span class="label-all">
            积分数:&nbsp
          </span>
          <span class="label-all" style="font-weight: bolder">
            {{$store.state.user.userInfo.points}}
          </span>
        </div>
        <div style="padding-top: 29px">
          <span class="label-all">
            排名:&nbsp&nbsp&nbsp&nbsp
          </span>
          <span class="label-all" style="font-weight: bolder">
            {{trueRank!==0?trueRank:'error'}}
          </span>
        </div>
      </el-card>
    </el-col>
    <el-col :span="16">
      <el-row>
        <el-col :span="2">
          <img src="../../images/history.png" width="60px" style="margin-top: 10px">
        </el-col>
        <el-col :span="18">
          <div style="margin-top: 20px; color: #545457; font-size: 30px; font-weight: bolder">
            群体排名
          </div>
        </el-col>
      </el-row>

      <el-table
        height="250px"
        :data="rankMsg"
        style="width: 80%; padding-left: 40px">
        <el-table-column
          prop="username"
          label="用户名"
          align="center"
          width="155px">
        </el-table-column>
        <el-table-column
          prop="nickName"
          label="昵称"
          align="center"
          width="155px">
        </el-table-column>
        <el-table-column
          prop="rank"
          label="排名"
          align="center"
          width="155px">
        </el-table-column>
        <el-table-column
          prop="points"
          label="积分"
          align="center"
          width="155px">
        </el-table-column>
      </el-table>
    </el-col>
  </el-col>
</template>

<script>
  export default {
    name: "worker-hist-rank",
    data() {
      return {
        rank: 0,
        rankMsg: []
      }
    },
    computed: {
      trueRank(){
        return this.rank;
      }
    },
    created() {
      let that = this;

      that.$http.get('/statistic/checkRanking')
        .then(function (response) {
          that.rankMsg = response.data.workers;
          console.log(that.rankMsg);
          that.refreshRank();
          console.log(that.rank + '1');
        })
        .catch(function (error) {
          console.log(error);
          that.$message({
            message:'更新积分历史失败',
            type: 'error',
            duration: 1500
          });
        });
    },
    methods: {

      refreshRank() {
        let that = this;
        console.log(that.rankMsg);
        for (let i = 0; i < that.rankMsg.length; i++ ) {
          if (that.rankMsg[i].username === that.$store.state.user.userInfo.username) {
            console.log(that.rankMsg[i].rank);
            that.rank = that.rankMsg[i].rank;
            console.log(that.rank + '2');
            return;
          }
        }

        this.$message({
          message: '您的数据不在排名系统中，请联系管理员',
          type:'error',
          duration: 1500
        });
      }

    }
  }
</script>

<style scoped>
  .label-all {
    font-size: 25px;
    padding-left: 20px;
    color: #4f4f52;
  }

  .box-card {
    margin-top: 20px;
    background-color: #f8ffff;
    height: 300px;
  }
</style>
