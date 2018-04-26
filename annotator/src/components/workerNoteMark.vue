<template>
  <div>
    <p>{{taskID}}</p>
  </div>
</template>

<script>
  export default {
    name: "worker-note-mark",
    props: ['taskID'],
    data(){
      return {
        myTaskID:this.taskID
      }
    }
  }
</script>

<style scoped>

</style>
