<template>
  <div class="sidebar">
    <el-form :model="form">
      <el-form-item label="用户名">
        <el-input
          v-model="form.user"
          placeholder="请输入你的用户名"
          style="width: 200px"
        />
      </el-form-item>

      <!--&#12288;是空白中文占位符-->
      <el-form-item label="密码&#12288;">
        <el-input
          v-model="form.pwd"
          placeholder="请输入你的密码"
          style="width: 200px"
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button color="#303133" @click="onSubmit">登录</el-button>
        <el-button color="#606266" @click="onRegister">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import axios from "axios";

const form = reactive({
  user: "",
  pwd: "",
});

const onSubmit = async () => {
  try {
    const res = await axios.post("后端登录接口url", {
      username: form.user,
      password: form.pwd,
    });
    if (res.data.success) {
      console.log("登录成功");
      // 如果登录成功，可以保存token并跳转到主页面
    } else {
      console.log("登录失败");
    }
  } catch (error) {
    console.error("登录错误：", error);
  }
};

const onRegister = async () => {
  try {
    const res = await axios.post("后端登录接口url", {
      username: form.user,
      password: form.pwd,
    });
    if (res.data.success) {
      console.log("登录成功");
      // 如果登录成功，可以保存token并跳转到主页面
    } else {
      console.log("登录失败");
    }
  } catch (error) {
    console.error("登录错误：", error);
  }
};
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
