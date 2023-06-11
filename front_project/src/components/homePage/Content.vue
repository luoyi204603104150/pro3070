<template>
  <div class="homePageContent">
    <el-table
      :data="
        tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)
      "
      style="width: 100%"
      @row-click="click"
    >
      <el-table-column
        prop="date"
        label="发帖时间"
        width="180"
        align="center"
      />
      <!-- 居中显示，红色原因不明 -->
      <el-table-column prop="name" label="作者" width="180" align="center" />
      <el-table-column prop="address" label="内容简述" align="center" />
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handleCurrentChange"
      :total="tableData.length"
      style="margin-top: 20px"
    />
  </div>
</template>

<script setup>
const props = defineProps({
  tableData: {
    type: Object,
  },
});

import { useRouter } from "vue-router";
import { ref } from "vue";

const router = useRouter();
function click() {
  router.push("/info");
}

let currentPage = ref(1); // 当前页码
const pageSize = 5; // 每页显示的条数

// 改变每页显示条数时触发
const handleCurrentChange = (val) => {
  currentPage.value = val;
};
</script>

<style scoped>
el-table-column {
  text-align: center;
}

.homePageContent {
  border: 2px solid #ccc;
  border-radius: 1%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  padding: 2rem;
}
</style>
