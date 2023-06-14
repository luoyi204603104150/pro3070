<template>
  <el-button
    class="forumPostButton"
    color="#606266"
    @click="dialogFormVisible = true"
    >发帖
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="发帖" width="30%">
    <el-form :model="forumPostFrom">
      <!--&#12288;是中文占位符 -->
      <el-form-item label="发帖标题">
        <el-input v-model="forumPostFrom.title" />
      </el-form-item>

      <el-form-item label="发帖内容">
        <el-input
          v-model="forumPostFrom.content"
          :autosize="{ minRows: 5 }"
          type="textarea"
          placeholder="请输入帖子内容"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button color="#606266" @click="onForumPost"> 发帖 </el-button>
      </span>
      <div>{{ forumPostFrom.info }}</div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { forumPost } from "../../requests/api";

const dialogFormVisible = ref(false);

const forumPostFrom = reactive({ title: "", content: "", info: "" });
async function onForumPost() {
  forumPostFrom.info = await forumPost(
    forumPostFrom.title,
    forumPostFrom.content
  );
}
</script>

<style scoped>
.forumPostButton {
  position: fixed;
  right: 20px;
  top: 20px;
  z-index: 1000;
}
</style>
