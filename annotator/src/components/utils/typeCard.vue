<template>
  <div>
    <!--对话框平时是不显示的-->
    <el-dialog
      v-if="cardType===typeDraw"
      title="模式说明"
      width="65%"
      center
      top="5vh"
      :visible.sync="dialogVisible">
      <h3 slot="title" style="margin-top: 0; margin-bottom: 0">模式说明</h3>
      <instruction-part :total-title="coreMsg" :description="description" :details="details"
                        :img-u-r-l="imgURL">
      </instruction-part>
    </el-dialog>

    <el-card :body-style="{ padding: '0px' }" shadow="hover">
      <el-row>
        <!--非常的神奇，加上一个行容器就可以不让遮罩层过大了-->
        <div class="category-card">
          <div class="card-hover" :style="`cursor:${cardType===typeDraw?'pointer':''}`"
               @click="handleHoverClick">
            <el-row type="flex" justify="center" align="middle" style="width: 100%; height: 100%">
              <h3 v-if="cardType===typeDraw" style="color: white">预览模板</h3>
              <p v-if="cardType===typeTag" style="color: white; margin-left: 5%; margin-right: 5%">
                {{tagDescription}}</p>
            </el-row>
          </div>
          <img :src="previewImgURL" class="image">
        </div>
      </el-row>
      <div style="padding: 14px;">
        <span>{{coreMsg}}</span>
        <div class="bottom clearfix">
          <time class="briefDescription" :style="`font-size:${cardType===typeTag?'10px':'13px'}`">{{ briefDescription }}</time>
          <el-button type="text" class="button" @click="handleChoose">选中此项</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
  import ElRow from "element-ui/packages/row/src/row";
  import InstructionPart from "./instructionPart";

  const previewImgURL = '../../../src/testDrawImage/1.jpg';
  const imgURL = '../../../src/infoImages/elephant-draw.jpg';
  const typeTag = 'tag';
  const typeDraw = 'draw';
  const tagDescription = 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.';
  const coreMsg = '指定目标的标注';
  const briefDescription = '您被要求标出已经指定的目标';
  const exampleDescription = '您被指定标出所有的目标对象，如所有的大象';
  const title1 = '基本要求';
  const sentence1 = '请使用我们提供的矩形标注工具，将您在途中看到的目标对象标记出来。选定绘制矩形的功能后，只需点击画布并移动鼠标以绘制标记。';
  const title2 = '注意事项';
  const sentence2 = '您无需对标记进行任何的描述，我们亦不会提供此功能。如果图中的目标对象的重要识别部位被遮挡（如被母象挡住了头部的小象），请不要标记它';
  const title3 = '操作建议';
  const sentence3 = '请仔细确认图片的内容再进行标记；标记的收束处不要超出画布，因为我们会直接将终点设置为鼠标接触边缘的位置。';
  const details = [
    {
      title: title1,
      sentence: sentence1
    },
    {
      title: title2,
      sentence: sentence2
    },
    {
      title: title3,
      sentence: sentence3
    }
  ];

  export default {
    components: {
      InstructionPart,
      ElRow
    },
    name: "type-card",
    props: {
      cardType: {
        type: String,
        default: typeDraw
      },
      tagDescription: {
        type: String,
        default: tagDescription
      },
      coreMsg: {
        type: String,
        default: coreMsg
      },
      briefDescription: {
        type: String,
        default: briefDescription
      },
      description: {
        type: String,
        default: exampleDescription
      },
      details: {
        type: Array,
        default: function () {
          return details;
        }
      },
      imgURL: {
        type: String,
        default: imgURL
      },
      previewImgURL: {
        type: String,
        default: previewImgURL
      }
    },

    data() {
      return {
        typeTag,
        typeDraw,
        dialogVisible: false,
      };
    },

    methods: {
      handleChoose() {
        if (this.cardType === typeTag) {
          this.$emit('choose-tag', this.coreMsg);
        }
        if (this.cardType === typeDraw) {
          this.$emit('choose-type', this.coreMsg);
        }
      },

      handleHoverClick() {
        if (this.cardType === typeDraw) {
          this.dialogVisible = true;
        }
      }
    }
  }
</script>

<style scoped>
  .briefDescription {
    color: #999;
  }

  @media screen and (max-device-width: 1000px) {
    .briefDescription {
      display: none;
    }
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
    transition: all ease-in-out 0.3s 0.05s;
  }

  .category-card:hover .card-hover {
    opacity: 0.9;
  }
</style>
