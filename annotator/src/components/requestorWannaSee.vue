<template>
  <div>

  </div>
</template>

<script>
  export default {
    name: "requestor-wanna-see",

    mounted() {
      console.log(this.workerName, this.taskID, 'ok!!!!');
    },

    props: ['workerName', 'taskID']
  }
</script>

<style scoped>

</style>
