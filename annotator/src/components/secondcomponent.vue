<template>
  <div id="secondcomponent">
    <h1>I am another page</h1>
    <a> written by {{ author }} </a>

	<ul>
	<li v-for="article in articles">
		{{article.title}}
	</li>
	</ul>

    <p> 感谢 <a href="https://github.com/showonne">showonne</a>大神的技术指导</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      author: "微信公众号 jinkey-love",
      articles: [],
    }
  },
  mounted: function() {

  }
}
</script>

<style>
</style>




翻页
多页应用
axios
element ui
