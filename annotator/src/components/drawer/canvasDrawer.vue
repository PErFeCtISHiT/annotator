<template>
  <canvas :ref = "canvasRef"></canvas>
</template>

<script>
  import colorPicker from '../vue-color-picker/colorPicker'

  export default {
    name: "canvas-drawer",

    components: {colorPicker},

    data() {
      return {
        canvasRef: 'canvas',

        strokeWidth: 0,
        strokeNormalColor: '',
        strokeFocusColor: '',

        currentLayer: null,
      }
    },

    computed:{
      canvasTarget(){
        return $(this.$refs[this.canvasRef]);
      }
    }
  }
</script>

<style scoped>

</style>
