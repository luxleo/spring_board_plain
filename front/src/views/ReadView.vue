<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from 'axios'

const props = defineProps({
  postId: {
    type: [Number,String],
    required: true
  }
});

const post = ref({id:-1,title:"",content:""})

onMounted(()=>{
  axios.get(`/api/posts/${props.postId}`).then(res=>{
    post.value = res.data
  })
})
</script>
<template>
  <h1>{{post.title}}</h1>
  <h1>{{post.content}}</h1>
</template>
<style>

</style>