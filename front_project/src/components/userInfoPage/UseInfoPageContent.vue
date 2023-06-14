<script setup>
defineProps({
  userHistoryData: {
    type: Object,
  },
});
</script>

<template>
  <div class="card">
    <el-card class="box-card">
      <template #header>
        <div class="title">
          {{ userHistoryData.title }}
        </div>
      </template>
      <div>{{ userHistoryData.content }}</div>
    </el-card>
  </div>
</template>

<style scoped>
.card {
  display: flex;
  justify-content: center;
  margin-top: 1%;
}
.box-card {
  width: 60%;
}
.title {
  font-size: large;
  font-weight: bold;
}
</style>
