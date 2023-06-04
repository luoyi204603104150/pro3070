import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
  
  { path: "/hello", component: () => import("../views/HomePage.vue") },
  { path: "/info", component: () => import("../views/InfoPage.vue") },
  { path: "/", redirect: "/hello" },
  // 路由配置
];

const router = createRouter({
  routes,
  history: createWebHashHistory(),
});

export default router;
