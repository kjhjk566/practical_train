<template>
  <div>
    <el-menu :default-active="$route.path"
             class="el-menu-vertical-demo"
             background-color="#3f5b94"
             text-color="#FFFFFF"
             router>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-info"></i>
          <span>系统简介</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/menu/1-1" @click="handleSelect('/menu/1-1')">系统信息</el-menu-item>
          <el-menu-item index="/menu/1-2" @click="handleSelect('/menu/1-2')">系统指南</el-menu-item>
          <el-menu-item index="/menu/1-3" @click="handleSelect('/menu/1-3')">开发团队</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-setting"></i>
          <span>数据模拟</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/NormalExperiment" @click="handleSelect('/NormalExperiment')">实验信息</el-menu-item>
          <el-menu-item index="/NormalFeature" @click="handleSelect('/NormalFeature')">特征参数</el-menu-item>
          <el-menu-item index="/NormalPattern" @click="handleSelect('/NormalPattern')">模式选择</el-menu-item>
          <el-menu-item index="/ViewData" @click="handleSelect('/ViewData')">数据观测</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-setting"></i>
          <span>异常注入</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/AnomalyFeature" @click="handleSelect('/AnomalyFeature')">异常参数</el-menu-item>
          <el-menu-item index="/ViewAnomaly" @click="handleSelect('/ViewAnomaly')">数据观测</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
export default {
  computed: {
    isLoggedIn() {
      return this.$store.state.isLoggedIn;
    }
  },
  methods: {
    handleSelect(route) {
      if (!this.isLoggedIn && (route.startsWith('/Normal') || route.startsWith('/Anomaly')|| route.startsWith('/View'))) {
        this.$router.push({ name: 'LoginPage' });
      } else {
        this.$router.push(route);
      }
    }
  },
}
</script>

<style scoped>
.el-menu-item.is-active {
  color: #fff !important;
  background: rgb(50,73,118) !important;
}
</style>
