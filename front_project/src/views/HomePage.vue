<script setup>
import Sidebar from "../components/homePage/Sidebar.vue";
import Header from "../components/homePage/HomePageHeader.vue";
import RotationChart from "../components/homePage/RotationChart.vue";
import Content from "../components/homePage/Content.vue";

const props = defineProps({
  tabletData: {
    type: Object,
  },
});

const tableData = [
  { date: "2016-05-03", name: "Tom", address: "阿巴巴巴吧" },
  { date: "2016-05-02", name: "Tom", address: "666666666666666666666666666" },
  { date: "2016-05-04", name: "Tom", address: "快看何润生在天上飞" },
  {
    date: "2016-05-01",
    name: "Tom",
    address: "111111111111111111111111111111",
  },
  {
    date: "2016-05-01",
    name: "Tom",
    address: "111111111111111111111111111111",
  },
  {
    date: "2016-05-01",
    name: "Tom",
    address: "111111111111111111111111111111",
  },
];
</script>

<template>
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
      <Sidebar></Sidebar>
    </el-aside>
  </el-container>

  <Content class="interval" :tableData="tableData"></Content>
</template>

<style>
html {
  background-color: #f6f6f6;
}
.interval {
  margin-top: 20px;
}
</style>
