<template>
  <el-button color="#606266" @click="dialogFormVisible = true">注册 </el-button>
  <el-dialog v-model="dialogFormVisible" title="注册" width="30%">
    <el-form :model="registerFrom">
      <!--&#12288;是中文占位符 -->
      <el-form-item class="interval" label="邮箱&#12288;">
        <el-input v-model="registerFrom.email" />
      </el-form-item>

      <el-form-item class="interval" label="用户名">
        <el-input v-model="registerFrom.username" />
      </el-form-item>

      <el-form-item class="interval" label="密码&#12288;">
        <el-input v-model="registerFrom.password" />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button color="#606266" @click="onRegister"> 注册 </el-button>
      </span>
      <div>{{ registerFrom.info }}</div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import { register } from "../../requests/api";

const dialogFormVisible = ref(false);

const registerFrom = reactive({
  email: "",
  username: "",
  password: "",
  info: "",
});
async function onRegister() {
  registerFrom.info = await register(
    registerFrom.email,
    registerFrom.username,
    registerFrom.password
  );
}
</script>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
.interval {
  margin-top: 5px;
}
</style>
