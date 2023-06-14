<script setup>
import UserInfoPageHeader from "../components/userInfoPage/UserInfoPageHeader.vue";
import UseInfoPageContent from "../components/userInfoPage/UseInfoPageContent.vue";

const userinfoData = {
  username: "何润生",
  sex: "男",
  birthday: "2023",
  ip: "北理珠",
  tag: "后端程序员",
  profile: "传说风中有座无人的桥，仅供灵魂一步一拜倒。",
};

const userHistoryData = {
  title: "震惊，有个外星人在天上飞。",
  content: "楼主是吉尼斯大水怪。",
};

const props = defineProps({
  userinfoData: { type: Object },
  userHistoryData: { type: Object },
});
</script>

<template>
  <UserInfoPageHeader :userinfoData="userinfoData"></UserInfoPageHeader>

  <div class="content">
    <div v-for="i in 10">
      <UseInfoPageContent
        :userHistoryData="userHistoryData"
      ></UseInfoPageContent>
    </div>
  </div>
</template>

<style scoped>
.content {
  width: 80%;
  margin-left: 10%;

  border: 2px solid #ccc;
  border-radius: 3%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
</style>
