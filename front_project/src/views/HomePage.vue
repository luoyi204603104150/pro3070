<script setup>
import LoginBox from "../components/homePage/LoginBox.vue";
import Header from "../components/homePage/HomePageHeader.vue";
import RotationChart from "../components/homePage/RotationChart.vue";
import Content from "../components/homePage/Content.vue";
import ForumPost from "../components/homePage/ForumPost.vue";

defineProps({ tabletData: { type: Object } });

const tableData = [
  { date: "2023-05-03", name: "Tom", address: "阿巴巴巴吧", id: "1" },
  { date: "2023-05-03", name: "Tom", address: "6666666666666", id: "2" },
  { date: "2023-05-03", name: "Tom", address: "快看何润生在天上飞", id: "3" },
  { date: "2023-05-03", name: "Tom", address: "111111111111", id: "4" },
  { date: "2016-05-01", name: "Tom", address: "111111111111", id: "5" },
  { date: "2016-05-01", name: "Tom", address: "1111111111111", id: "6" },
];
</script>

<template>
  <ForumPost></ForumPost>
  <el-container>
    <el-container>
      <el-header>
        <Header class="interval"></Header>
      </el-header>

      <el-main>
        <RotationChart class="interval"></RotationChart>
      </el-main>
    </el-container>

    <el-aside width="350px">
      <LoginBox></LoginBox>
    </el-aside>
  </el-container>

  <Content class="interval"></Content>
</template>

<style>
html {
  background-color: #f6f6f6;
}
.interval {
  margin-top: 20px;
}
</style>
