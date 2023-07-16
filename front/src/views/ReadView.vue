<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from 'axios'
import {useRouter} from "vue-router";

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
const router = useRouter()
const moveToEdit = ()=>{
  router.push({name:"edit",params:{postId:props.postId}})
}
</script>
<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>

      <div class="sub d-flex">
        <div class="category">생성일</div>
        <div class="regDate">2022-06-01 23:59:59</div>
      </div>
    </el-col>
  </el-row>
  <hr/>
  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  margin: 0;
}

.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
  }
}

.content {
  font-size: 0.95rem;
  margin-top: 12px;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>