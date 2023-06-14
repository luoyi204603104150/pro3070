<template>
  <div class="sidebar">
    <el-form :model="submitForm">
      <el-form-item label="邮箱">
        <el-input
          v-model="submitForm.email"
          placeholder="请输入你的登录邮箱"
          style="width: 200px"
        />
      </el-form-item>

      <el-form-item label="密码">
        <el-input
          v-model="submitForm.password"
          placeholder="请输入你的密码"
          style="width: 200px"
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button color="#303133" @click="onSubmit">登录</el-button>
        <RegisterBox></RegisterBox>
      </el-form-item>
    </el-form>

    <!--显示成功或者报错信息-->
    <div>{{ submitForm.info }}</div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { login } from "../../requests/api";
import RegisterBox from "./RegisterBox.vue";

const submitForm = reactive({ email: "", password: "", info: "" });
async function onSubmit() {
  submitForm.info = await login(submitForm.email, submitForm.password);
}
</script>

<style scoped>
.sidebar {
  /*background: #95a5a6;*/

  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 120px;

  /*圆角阴影边框 */
  border: 2px solid #ccc;
  border-radius: 5%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  padding: 2rem;
}
</style>
