<template>
  <div
    style="border:1px solid #000; box-sizing: border-box; border-radius: 8px; background-color: #f9f8f7">
    <el-row style="border-bottom: 2px solid lightgrey">
      <h3 style="margin-left: 20px">{{'· '+totalTitle}}</h3>
      <p style="margin-left: 25px; color:darkgrey">{{description}}</p>
    </el-row>
    <el-row>
      <el-col :span="16">
        <el-row type="flex" justify="center" align="middle"
                style="width: 100%; height: 100%; margin-bottom: 5%; margin-top: 5%; border-right: 2px solid lightgrey">
          <img :src="imgURL" style="width:90%; height:90%; margin: 5% 5% 5% 5%">
        </el-row>
      </el-col>
      <el-col :span="8" style="height: inherit">
        <el-row type="flex" justify="center" align="middle"
                style="width: 100%; height: 100%; margin-bottom: 5%; margin-top: 5%;">
          <div>

            <template v-for="val in details">
              <h4 style="margin: 5% 5% 2% 5%;">{{val.title}}</h4>
              <p style="margin: 0 5% 5% 5%; color: grey">
                {{val.sentence}}
              </p>
            </template>

          </div>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  const imgURL = '../../../src/infoImages/elephant-draw.jpg';
  const totalTitle = '指定目标标注';
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
    name: "instruction-part",
    props: {
      totalTitle: {
        type: String,
        default: totalTitle
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
      imgURL:{
        type: String,
        default: imgURL
      }
    },
  }
</script>

<style scoped>

</style>
