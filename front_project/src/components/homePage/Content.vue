<template>
  <div class="homePageContent">
    <el-table
      :data="
        articleListData.slice(
          (currentPage - 1) * pageSize,
          currentPage * pageSize
        )
      "
      style="width: 100%"
      row-key="articleId"
      @row-click="click"
    >
      <el-table-column
        prop="postTime"
        label="发帖时间"
        width="180"
        align="center"
      />

      <!-- align="center"意为居中显示，红色原因不明 -->
      <el-table-column
        prop="nickName"
        label="作者"
        width="180"
        align="center"
      />
      <el-table-column prop="summary" label="内容简述" align="center" />
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handleCurrentChange"
      :total="articleListData.length"
      style="margin-top: 20px"
    />
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

const router = useRouter();
function click(row) {
  router.push({ path: `/info/${row.articleId}` });
}

let currentPage = ref(1); // 当前页码
const pageSize = 5; // 每页显示的条数

// 改变每页显示条数时触发
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

import { articleList } from "../../requests/api";

const articleListData = ref("");
async function getdata() {
  articleListData.value = await articleList();
}
getdata();
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
