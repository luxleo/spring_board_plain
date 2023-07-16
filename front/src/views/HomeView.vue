<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios'

const posts = ref([])
axios.get("/api/posts?page=1&size=10").then(res=>{
  res.data.forEach((r:any) => {
    posts.value.push(r)
  })
})

</script>

<template>
<ul>
  <li v-for="post in posts" :key="post.id">
    <div>
      <router-link :to="{name:'read', params: {postId:post.id}}" tag="a" class="title" >{{post.title}}</router-link>
    </div>
    <div>
      <router-link :to="{name:'read', params: {postId:post.id}}" class="content">{{post.content}}</router-link>
    </div>
  </li>
</ul>
</template>
<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

li {
  margin-bottom: 1rem;

.title {
  font-size: 1.1rem;
  color: #C8C8C8;
  text-decoration: none;

&:hover {
   text-decoration: underline;
 }
}

.content {
  font-size: 0.85rem;
  margin-top: 8px;
  line-height: 1.4;
  color: #7e7e7e;
  text-decoration: none;
}

&:last-child {
   margin-bottom: 0;
 }

.sub {
  margin-top: 8px;
  font-size: 0.78rem;

.regDate {
  margin-left: 10px;
  color: #6b6b6b;
}
}
}
}
</style>
