<template>
  <div>

    <el-row>

      <el-col :span="2">
        <el-row type="flex" justify="center" align="middle">
          <el-button style="margin-top: 23px;" @click="pre" icon="el-icon-arrow-left" circle="">
          </el-button>
        </el-row>
      </el-col>

      <el-col :span="20">
        <el-steps :active="activeStep" finish-status="success" simple style="margin-top: 20px">
          <el-step :title="tagNotEmpty?tagMsg:'选择标签'" style="cursor: pointer" @click.native="handleGoToStepOne">
          </el-step>
          <el-step :title="typeNotEmpty?getTypeMsg(type):'画法选择'" style="cursor: pointer"
                   @click.native="handleGoToStepTwo">
          </el-step>
          <el-step :title="tagNotEmpty&&typeNotEmpty?'绘制中':'开始绘制'" style="cursor: pointer"
                   @click.native="handleGoToDraw">
          </el-step>
        </el-steps>
      </el-col>

      <el-col :span="2">
        <el-row type="flex" justify="center" align="middle">
          <el-button style="margin-top: 23px;" @click="next" icon="el-icon-arrow-right" circle="">
          </el-button>
        </el-row>
      </el-col>

    </el-row>

    <el-row v-show="activeStep===0" :gutter="40" type="flex" justify="center" align="middle" :style="contentStyle">
      <el-col :span="6" :style="singleCardStyle">
        <!--emit的事件名字要小写-->
        <type-card
          :card-type="typeTag"
          :core-msg="$store.state.tags.tagMsg[0]"
          :tag-description="peopleTagDescription"
          :preview-img-u-r-l="peoplePreviewImgURL"
          @choose-tag="chooseTag"
          :brief-description="peopleBriefDescription">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeTag"
          :core-msg="$store.state.tags.tagMsg[1]"
          :tag-description="sceneryTagDescription"
          :preview-img-u-r-l="sceneryPreviewImgURL"
          @choose-tag="chooseTag"
          :brief-description="sceneryBriefDescription">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeTag"
          :core-msg="$store.state.tags.tagMsg[2]"
          :tag-description="animalTagDescription"
          :preview-img-u-r-l="animalPreviewImgURL"
          @choose-tag="chooseTag"
          :brief-description="animalBriefDescription">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeTag"
          :core-msg="$store.state.tags.tagMsg[3]"
          :tag-description="botanyTagDescription"
          :preview-img-u-r-l="botanyPreviewImgURL"
          @choose-tag="chooseTag"
          :brief-description="botanyBriefDescription">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeTag"
          :core-msg="$store.state.tags.tagMsg[4]"
          :tag-description="textTagDescription"
          :preview-img-u-r-l="textPreviewImgURL"
          @choose-tag="chooseTag"
          :brief-description="textBriefDescription">
        </type-card>
      </el-col>
    </el-row>

    <el-row v-show="activeStep===1" :gutter="40" type="flex" justify="center" align="middle" :style="contentStyle">
      <el-col :span="6" :style="singleCardStyle">
        <type-card
          :card-type="typeDraw"
          :core-msg="typeMsg1"
          :brief-description="briefDescription1"
          :description="exampleDescription1"
          :details="details1"
          :img-u-r-l="imgURL1"
          :preview-img-u-r-l="previewImgURL1"
          @choose-type="chooseType">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeDraw"
          :core-msg="typeMsg2"
          :brief-description="briefDescription2"
          :description="exampleDescription2"
          :details="details2"
          :img-u-r-l="imgURL2"
          :preview-img-u-r-l="previewImgURL2"
          @choose-type="chooseType">
        </type-card>
      </el-col>
      <el-col :span="6">
        <type-card
          :card-type="typeDraw"
          :core-msg="typeMsg3"
          :brief-description="briefDescription3"
          :description="exampleDescription3"
          :details="details3"
          :img-u-r-l="imgURL3"
          :preview-img-u-r-l="previewImgURL3"
          @choose-type="chooseType">
        </type-card>
      </el-col>
    </el-row>

    <el-row v-show="activeStep===2" :style="singleCardStyle">
      <canvas-drawer v-if="activeStep===2" :tag-msg="tagMsg" :mark-type="type" style="margin: 0 5% 0 5%">
      </canvas-drawer>
    </el-row>

  </div>
</template>

<script>
  import CanvasDrawer from "../drawer/canvasDrawer";
  import TypeCard from "../utils/typeCard";
  import ElRow from "element-ui/packages/row/src/row";
  import InstructionPart from "../utils/instructionPart";

  const contentStyle = 'margin: 3% 3% 3% 3%; background-color: #f5f7fa';
  const singleCardStyle = 'margin-top: 3%;margin-bottom: 3%';
  const typeTag = 'tag';
  const typeDraw = 'draw';

  const peopleBriefDescription = '各色人物是标注的重点';
  const peopleTagDescription = '在此项人物中，你的工作与人物相关，如标出行人或标出人脸，亦可能涉及人物分类';
  const peoplePreviewImgURL = '../../../src/infoImages/people.jpg';


  const sceneryBriefDescription = '提供的图片是风景图';
  const sceneryTagDescription = '你可能被要求标注出一些特定的景观，或是对它们进行一定的说明';
  const sceneryPreviewImgURL = '../../../src/infoImages/scenery.jpg';

  const animalBriefDescription = '关注的重点是动物';
  const animalTagDescription = '任务涉及动物对象的标注、动物的识别与分类等等';
  const animalPreviewImgURL = '../../../src/infoImages/animal.jpg';

  const botanyBriefDescription = '材料与各种植物相关';
  const botanyTagDescription = '任务涉及各色植物的标注及对它们的识别与分类等等';
  const botanyPreviewImgURL = '../../../src/infoImages/botany.jpg';

  const textBriefDescription = '你将接触到各种各样的文本';
  const textTagDescription = '你的任务是进行文本识别和文本解析';
  const textPreviewImgURL = '../../../src/infoImages/text.jpg';

  const typeMsg1 = '指定目标的标注';
  const typeMsg2 = '描述子类的标注';
  const typeMsg3 = '划分区域的标注';

  const previewImgURL1 = '../../../src/infoImages/elephant-card.jpg';
  const imgURL1 = '../../../src/infoImages/elephant-draw.jpg';
  const briefDescription1 = '您被要求标出已经指定的目标';
  const exampleDescription1 = '您被指定标出所有的目标对象，在本次任务中，您需要标出所有的大象';
  const title1_1 = '基本要求';
  const sentence1_1 = '请使用我们提供的矩形标注工具，将您在图中看到的目标对象标记出来。选定绘制矩形的功能后，只需点击画布并移动鼠标以绘制标记。';
  const title1_2 = '注意事项';
  const sentence1_2 = '您无需对标记进行任何的描述，我们亦不会提供此功能。如果图中的目标对象的重要识别部位被遮挡（如被母象挡住了头部的小象），请不要标记它';
  const title1_3 = '操作建议';
  const sentence1_3 = '请仔细确认图片的内容再进行标记；标记的收束处不要超出画布，因为我们会直接将终点设置为鼠标接触边缘的位置。';
  const details1 = [
    {
      title: title1_1,
      sentence: sentence1_1
    },
    {
      title: title1_2,
      sentence: sentence1_2
    },
    {
      title: title1_3,
      sentence: sentence1_3
    }
  ];

  const previewImgURL2 = '../../../src/infoImages/tableware-card.jpg';
  const imgURL2 = '../../../src/infoImages/tableware-draw.jpg';
  const briefDescription2 = '您要对图中对象进行分类和标注';
  const exampleDescription2 = '您被指定标出所有的餐具，然后依次填写餐具的名称';
  const title2_1 = '基本要求';
  const sentence2_1 = '请使用我们提供的矩形标注工具，将您在图中看到的目标对象标记出来。标记后请描述标注的内容。';
  const title2_2 = '注意事项';
  const sentence2_2 = '您必须对标记进行描述，在您完成一个标记的描述前，不能进行下一个标注。如果图中的目标对象的重要识别部位被遮挡，请不要标记它';
  const title2_3 = '操作建议';
  const sentence2_3 = '请仔细确认图片的内容再进行标记；标记的收束处不要超出画布，因为我们会直接将终点设置为鼠标接触边缘的位置。';
  const details2 = [
    {
      title: title2_1,
      sentence: sentence2_1
    },
    {
      title: title2_2,
      sentence: sentence2_2
    },
    {
      title: title2_3,
      sentence: sentence2_3
    }
  ];

  const previewImgURL3 = '../../../src/infoImages/road-card.jpg';
  const imgURL3 = '../../../src/infoImages/road-draw.jpg';
  const briefDescription3 = '您被要求划分出特定的对象';
  const exampleDescription3 = '在本题中，您被要求将图中的公路划分出来';
  const title3_1 = '基本要求';
  const sentence3_1 = '请使用我们提供的多边形标注工具，将您在图中看到的目标对象标记出来。选定绘制多边形的功能后，您可以点击画布或者在按下鼠标后移动进行画图。';
  const title3_2 = '注意事项';
  const sentence3_2 = '如果目标对象有好几块，可以进行多次多边形标记，然后用同个名词来描述这些标记';
  const title3_3 = '操作建议';
  const sentence3_3 = '描边时请仔细观察图形，避免将不必要的内容圈点到标记当中。如果图形还未封闭，这个多边形的绘制就不会结束。';
  const details3 = [
    {
      title: title3_1,
      sentence: sentence3_1
    },
    {
      title: title3_2,
      sentence: sentence3_2
    },
    {
      title: title3_3,
      sentence: sentence3_3
    }
  ];

  export default {
    components: {
      InstructionPart,
      ElRow,
      TypeCard,
      CanvasDrawer
    },

    name: "worker-get-task",
    data() {
      return {
        activeStep: 0,    //实际步骤减一
        tagMsg: '',
        type: 0,

        typeMsg1,
        typeMsg2,
        typeMsg3,

        previewImgURL1,
        imgURL1,
        briefDescription1,
        exampleDescription1,
        details1,

        previewImgURL2,
        imgURL2,
        briefDescription2,
        exampleDescription2,
        details2,

        previewImgURL3,
        imgURL3,
        briefDescription3,
        exampleDescription3,
        details3,

        contentStyle,
        singleCardStyle,
        typeTag,
        typeDraw,

        peopleBriefDescription,
        peopleTagDescription,
        peoplePreviewImgURL,

        sceneryBriefDescription,
        sceneryTagDescription,
        sceneryPreviewImgURL,

        animalBriefDescription,
        animalTagDescription,
        animalPreviewImgURL,

        botanyBriefDescription,
        botanyTagDescription,
        botanyPreviewImgURL,

        textBriefDescription,
        textTagDescription,
        textPreviewImgURL,
      }
    },

    computed: {
      tagNotEmpty() {
        return this.tagMsg && this.tagMsg !== null && this.tagMsg !== '';
      },

      typeNotEmpty() {
        return this.type && this.type !== null && this.type !== '' && this.type !== 0;
      },
    },

    methods: {
      getTypeMsg(val) {
        switch (val) {
          case 1:
            return typeMsg1;
          case 2:
            return typeMsg2;
          case 3:
            return typeMsg3;
          default:
            this.$message({
              message: '数据出现错误',
              type: 'error',
              duration: 1800
            });
        }
      },

      getType(val) {
        switch (val) {
          case typeMsg1:
            return 1;
          case typeMsg2:
            return 2;
          case typeMsg3:
            return 3;
          default:
            this.$message({
              message: '数据出现错误',
              type: 'error',
              duration: 1800
            });
        }
      },

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
            this.$message({
              message: '尊敬的用户，已经到底了哟~',
              duration: 1800
            });
            break;
        }
      },

      pre() {
        switch (this.activeStep - 1) {
          case 0:
            this.handleGoToStepOne();
            break;
          case 1:
            this.handleGoToStepTwo();
            break;
          case -1:
          default:
            this.$message({
              message: '尊敬的用户，已经到头了哟~',
              duration: 1800
            });
            break;
        }
      },

      handleGoToStepOne() {   //去除它本身和后续的信息
        if (this.activeStep !== 0) {
          this.tagMsg = '';
          this.type = '';
          this.activeStep = 0;
        }
      },

      handleGoToStepTwo() {
        if (this.activeStep !== 1) {
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
        }
      },

      handleGoToDraw() {
        if (this.activeStep !== 2) {
          let that = this;
          this.$http.get('/user/checkAccuracy', {
            params: {
              username: that.$store.state.user.userInfo.username,
              tag: that.tagMsg
            }
          })
            .then(function (response) {
              let result = response.data.mes;
              if (result === false) {
                console.log('正确率不达标，跳');
                that.$router.push('/testPage');
                that.$message({
                  message: '您似乎有些疲倦了，正确率不达标',
                  type: 'warning',
                  duration: 1500
                });
              }else{
                that.actualGoToDraw();
              }
            })
            .catch(function (error) {
              that.$message({
                message: '网络错误',
                type: 'error',
                duration: 1500
              });
              console.log(error);
            });
        }
      },

      actualGoToDraw() {
        if (this.tagNotEmpty && this.typeNotEmpty) {     //computed属性复用不加()
          this.activeStep = 2;
        } else {
          this.$message({
            message: '请先完成前置的标记方式选择，再进行标记工作',
            type: 'warning',
            duration: 1800
          });
        }
      },

      chooseTag(val) {
        this.tagMsg = val;
        this.next();
      },


      chooseType(val) {
        this.type = this.getType(val);
        this.next();
      }
    }
  }
</script>

<style scoped>
</style>
