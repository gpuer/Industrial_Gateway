<script setup lang="ts">
</script>

<template>
  <el-config-provider namespace="ep">
    <BaseHeader />
    <div class="main-container flex">
      <BaseSide class="sidebar" />
      <div w="full" py="4">
        <RouterView />
      </div>
    </div>
  </el-config-provider>
</template>

<style>
#app {
  text-align: center;
  color: var(--ep-text-color-primary);
}
/* 设置侧边栏宽度 */
.sidebar {
  width: 240px; /* 可以根据实际需求设置 */
  min-width: 200px;
  background-color: #fff; /* 可选 */
}

.main-container {
  height: calc(100vh - var(--ep-menu-item-height) - 4px);
}
</style>
