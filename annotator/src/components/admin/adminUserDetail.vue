<template>
  <div class="grid-bg row-bg2">
    <!--标题-->
    <div class="big-label">用户详情</div>

    <!--表格-->
    <el-table :data="detail" class="normal-table" height="300" style="width: 100%">

      <el-table-column prop="username" label="用户名" width="160">
      </el-table-column>

      <el-table-column prop="name" label="昵称" width="160">
      </el-table-column>

      <el-table-column label="角色" width="160">
        <template slot-scope="scope">
          <span v-for="oneRole in myShift(scope.row.role)">
            <el-tag type="info">{{ oneRole }}</el-tag>&thinsp;
          </span>
        </template>
      </el-table-column>

      <el-table-column label="等级" width="200">
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <p>等级为: {{ scope.row.level }}</p>
            <div slot="reference" class="name-wrapper">
              <el-rate v-model="scope.row.level" disabled></el-rate>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column prop="points" label="积分" width="90">
      </el-table-column>


      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="medium" disabled>修改积分</el-button>
          <el-button type="danger" size="medium" disabled>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

  export default {
    name: "admin-user-detail",

    props: ['detail'],

    methods: {
      myShift(roles) {
        roles.map(role => role === 2 ? "发起者" : "工人");
      }
    }

  }
</script>

<style scoped>
  .grid-bg {
    margin: 30px 30px 0 30px;
    border-radius: 4px;
    min-height: 36px;
    background-color: aliceblue;
    padding: 10px 0;
  }

  .row-bg2 {
    height: 400px;
  }

  .big-label {
    margin-left: 30px;
    margin-top: 10px;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    font-size: 25px;
    font-weight: bold;
    color: #464646;
  }

  /*表格*/
  .normal-table {
    width: 90%;
    margin-left: 5%;
    margin-top: 20px;

  }
</style>
