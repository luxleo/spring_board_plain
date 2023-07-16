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
      <router-link :to="{name:'read', params: {postId:post.id}}">{{post.title}}</router-link>
    </div>
    <div>
      <router-link to="/read">{{post.content}}</router-link>
    </div>
  </li>
</ul>
</template>
