<template>
  <div>

    <el-row>

      <el-col :span="22">
        <el-steps :active="activeStep" finish-status="success" simple style="margin-top: 20px">
          <el-step :title="tagNotEmpty?tagMsg:'选择标签'" style="cursor: pointer" @click.native="handleGoToStepOne">
          </el-step>
          <el-step :title="typeNotEmpty?type:'画法选择'" style="cursor: pointer" @click.native="handleGoToStepTwo">
          </el-step>
          <el-step :title="tagNotEmpty&&typeNotEmpty?'绘制中':'开始绘制'" style="cursor: pointer" @click.native="handleGoToDraw">
          </el-step>
        </el-steps>
      </el-col>

      <el-col :span="2">
        <el-row type="flex" justify="center" align="middle">
          <el-button style="margin-top: 23px;" @click="next">下一步</el-button>
        </el-row>
      </el-col>

    </el-row>

    <el-row v-show="activeStep===0">
    </el-row>

    <el-row v-show="activeStep===1">
    </el-row>

    <el-row v-show="activeStep===2">
      <canvas-drawer v-if="activeStep===2">
      </canvas-drawer>
    </el-row>

  </div>
</template>

<script>
  import CanvasDrawer from "../drawer/canvasDrawer";

  export default {
    components: {CanvasDrawer},
    name: "worker-get-task",
    data() {
      return {
        activeStep: 0,
        tagMsg: '',
        type: '',
      }
    },

    computed: {
      tagNotEmpty(){
        return this.tagMsg && this.tagMsg !== null && this.tagMsg !== '';
      },

      typeNotEmpty(){
        return this.type && this.type !== null && this.type !== '';
      },
    },

    methods: {
      next() {
        switch (this.activeStep + 1) {
          case 1:
            this.handleGoToStepTwo();
            break;
          case 2:
            this.handleGoToDraw();
            break;
          case 3:
          default:
            this.handleGoToStepOne();
            break;
        }
      },

      handleGoToStepOne() {   //去除它本身和后续的信息
        this.tagMsg = '';
        this.type = '';
        this.activeStep = 0;
      },

      handleGoToStepTwo() {
        if (this.tagNotEmpty) {
          this.type = '';
          this.activeStep = 1;
        } else {
          this.$message({
            message: '请先完成前置的条件选择',
            type: 'warning',
            duration: 1800
          });
        }
      },

      handleGoToDraw() {
        if (this.tagNotEmpty&&this.typeNotEmpty) {     //computed属性复用不加()
          this.activeStep = 2;
        } else {
          this.$message({
            message: '请先完成前置的标记方式选择，再进行标记工作',
            type: 'warning',
            duration: 1800
          });
        }
      }


    }
  }
</script>

<style scoped>

</style>
