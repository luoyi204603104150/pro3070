<script setup>
import Sidebar from '../components/homePage/Sidebar.vue';
import Header from '../components/homePage/Header.vue';
import RotationChart from '../components/homePage/RotationChart.vue';
import Content from '../components/homePage/Content.vue';
import Pagination from '../components/homePage/Pagination.vue'

</script>

<template>
  <div class="common-layout" aria-setsize="default">
    <el-container>
      <el-aside width="350px">
        <Sidebar></Sidebar>
      </el-aside>

      <el-container>
        <el-header>
          <el-row>
            <Header></Header>
          </el-row>
        </el-header>

        <el-main>
          <RotationChart></RotationChart>
          <div aria-setsize="default"></div>
          <Content></Content>
        </el-main>

        <el-footer>
          <Pagination></Pagination>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>




<style></style>
