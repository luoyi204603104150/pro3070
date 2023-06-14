import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
  { path: "/hello", component: () => import("../views/HomePage.vue") },
  { path: "/info/:id", component: () => import("../views/InfoPage.vue") },
  { path: "/userinfo", component: () => import("../views/UserInfoPage.vue") },
  { path: "/test", component: () => import("../views/ATest.vue") },
  { path: "/", redirect: "/hello" },
  // 路由配置
];

const router = createRouter({
  routes,
  history: createWebHashHistory(),
});

export default router;
