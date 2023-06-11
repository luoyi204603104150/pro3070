<script setup>
const props = defineProps({
    contentData: {
        type: Object
    }
})
</script>

<template>
    <el-card class="box-card" style = "border-radius: 20px">
        <template #header>
            <div class="card-header">
                <span>
                    <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    {{ contentData.username }}
                </span>
                <el-button color="#FF0000" round>点赞</el-button>
            </div>
        </template>

        <div>{{ contentData.content }}</div>
    </el-card>
</template>
 
<style>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.text {
    font-size: 14px;
}

.box-card {
    height: auto;
    width: auto;
    padding: 3px;
    margin-bottom: 10px;
}
</style>