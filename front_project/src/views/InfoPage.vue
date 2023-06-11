<script setup>
import { getName } from "../requests/index";
import Breadcrumb from "../components/infoPage/Breadcrumb.vue";
import Content from "../components/infoPage/InfoPageContent.vue";
import Input from "../components/infoPage/Input.vue";

const contentData = {
  username: "何润生",
  content: "何润生不想理你并向你丢了一块石头",
};

const props = defineProps({
  contentData: {
    type: Object,
  },
});
</script>

<template>
  <Breadcrumb></Breadcrumb>

  <div v-for="i in 10">
    <Content :contentData="contentData"></Content>
  </div>

  <Input></Input>
</template>
