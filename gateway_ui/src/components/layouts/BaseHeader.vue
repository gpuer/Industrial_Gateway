<script lang="ts" setup>
import { repository } from '~/../package.json'
</script>

<template>
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" router>
    <el-menu-item index="/gateway/">
      <div class="flex items-center justify-center gap-2">
        <img src="@/assets/logo.svg" alt="logo" class="h-6 w-6" />
        <span>工业数据采集网关平台</span>
      </div>
    </el-menu-item>
    <el-menu-item h="full">
      <a class="size-full flex items-center justify-center" :href="repository.url" target="_blank">
        <div i-ri-github-fill />
      </a>
    </el-menu-item>
  </el-menu>
</template>

<style lang="scss">
.el-menu-demo {
  &.ep-menu--horizontal > .ep-menu-item:nth-child(1) {
    margin-right: auto;
  }
}
</style>
