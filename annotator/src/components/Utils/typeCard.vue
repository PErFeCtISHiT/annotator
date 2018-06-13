<template>
  <div>
      <el-card :body-style="{ padding: '0px' }" shadow="hover">
        <el-row>
          <!--非常的神奇，加上一个行容器就可以不让遮罩层过大了-->
          <div class="category-card">
            <div class="card-hover">
              <el-row type="flex" justify="center" align="middle" style="width: 100%; height: 100%">
                <h3 style="color: white">预览模板</h3>
              </el-row>
            </div>
            <img src="../../testDrawImage/1.jpg" class="image">
          </div>
        </el-row>
        <div style="padding: 14px;">
          <span>好吃的汉堡</span>
          <div class="bottom clearfix">
            <time class="time">{{ currentDate }}</time>
            <el-button type="text" class="button">操作按钮</el-button>
          </div>
        </div>
      </el-card>
  </div>
</template>

<script>
  import ElRow from "element-ui/packages/row/src/row";

  export default {
    components: {ElRow},
    name: "type-card",
    data() {
      return {
        currentDate: new Date(),
      };
    },
  }
</script>

<style scoped>
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    border-radius: 4px;
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .category-card .card-hover {
    position: absolute;
    opacity: 0;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(59, 89, 152, 0.8);
    background-color: #22326c;
    width: 100%;
    height: 100%;
    cursor: pointer;
    transition: all ease-in-out 0.3s 0.05s;
  }

  .category-card:hover .card-hover {
    opacity: 0.9;
  }
</style>
