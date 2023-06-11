<template>
    <el-affix position="bottom" :offset="0">
        <el-row :gutter="20" justify="center">
            <el-col :span="15" :offset="1">
                <div><el-input v-model="textarea" :rows="3" type="textarea" placeholder="留下你的评论吧。" /></div>
            </el-col>
            <div class="button">
                <el-col :span="1">
                    <el-button color="blue" round>发送</el-button>

                </el-col>
            </div>
        </el-row>
    </el-affix>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
const textarea = ref('')
</script>

<style>
.button {
    padding-top: 20px;
}
</style>