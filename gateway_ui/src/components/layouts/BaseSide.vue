<template>
  <el-menu
    router
    :default-active="activeMenu"
    class="centered-menu"
  >
    <el-menu-item index="/gateway/">
      <el-icon>
        <Connection />
      </el-icon>
      <template #title>
        网关管理
      </template>
    </el-menu-item>

    <el-menu-item index="/devices/">
      <el-icon>
        <Odometer />
      </el-icon>
      <template #title>
        仪表管理
      </template>
    </el-menu-item>

    <el-menu-item index="/templates/">
      <el-icon>
        <IconMenu />
      </el-icon>
      <template #title>
        采集模板
      </template>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { Menu as IconMenu, Connection, Odometer } from '@element-plus/icons-vue'
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)


</script>

<style>
.centered-menu {
  width: 240px;
}

.centered-menu .el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  gap: 6px;
}
</style>