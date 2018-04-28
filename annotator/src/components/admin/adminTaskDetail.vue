<template>
  <div class="grid-bg row-bg2">
    <!--标题-->
    <div class="big-label">用户详情</div>

    <el-table :data="userDetail" class="normal-table" height="300" style="width: 90%">

      <el-table-column prop="username" label="用户名" width="160">
      </el-table-column>

      <el-table-column prop="name" label="昵称" width="160">
      </el-table-column>

      <el-table-column label="角色" width="160">
        <template slot-scope="scope">
                <span v-for="oneRole in scope.row.role">
                  <el-tag type="info">{{ myShift(oneRole) }}</el-tag>&thinsp;
                </span>
        </template>
      </el-table-column>

      <el-table-column label="等级" width="220">
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <p>等级为: {{ scope.row.level }}</p>
            <div slot="reference" class="name-wrapper">
              <el-rate v-model="scope.row.level" disabled></el-rate>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column prop="points" label="积分" width="80">
      </el-table-column>


      <el-table-column label="操作" width="280" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="medium" disabled>修改积分</el-button>
          <el-button type="danger" size="medium" @click="dealDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "admin-task-detail"
  }
</script>

<style scoped>

</style>
