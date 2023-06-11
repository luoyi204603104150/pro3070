<script setup>
defineProps({
  userinfoData: {
    type: Object,
  },
});
</script>

<template>
  <el-page-header>
    <template #content>
      <div class="flex items-center">
        <el-avatar
          class="mr-3"
          :size="32"
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        />
        <span style="color: var(--el-text-color-regular)">用户信息</span>
      </div>
    </template>

    <el-descriptions :column="3" size="small" class="mt-4">
      <el-descriptions-item label="用户名">
        {{ userinfoData.username }}
      </el-descriptions-item>
      <el-descriptions-item label="性别">{{
        userinfoData.sex
      }}</el-descriptions-item>
      <el-descriptions-item label="生日">
        {{ userinfoData.birthday }}
      </el-descriptions-item>
      <el-descriptions-item label="ip属地"
        >{{ userinfoData.ip }}
      </el-descriptions-item>
      <el-descriptions-item label="标签">
        <el-tag size="small">{{ userinfoData.tag }}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <div>个人简介</div>
  </el-page-header>
</template>
