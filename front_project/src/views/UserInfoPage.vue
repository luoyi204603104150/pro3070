<template>
  <UseInfoPageContent :userinfoData="userinfoData"></UseInfoPageContent>
</template>

<script setup>
import UseInfoPageContent from "../components/userInfoPage/UseInfoPageContent.vue";

const userinfoData = {
  username: "何润生",
  sex: "男",
  birthday: "2023",
  ip: "北理珠",
  tag: "后端程序员",
};

const props = defineProps({
  userinfoData: {
    type: Object,
  },
});
</script>
