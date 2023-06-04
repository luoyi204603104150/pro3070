<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="date" label="发帖时间" width="180" />
    <el-table-column prop="name" label="发帖人" width="180" />
    <el-table-column prop="address" label="内容简述" />
  </el-table>
</template>

<script lang="ts" setup>
const tableData = [
  {
    date: '2016-05-03',
    name: 'Tom',
    address: '阿巴巴巴吧',
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    address: '666666666666666666666666666',
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    address: '快看何润生在天上飞',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: '111111111111111111111111111111',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: '111111111111111111111111111111',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: '111111111111111111111111111111',
  },
]
</script>
