<template>
  <div>
    <el-menu
      :default-active="$route.path"
      class="el-menu-vertical-demo"
      background-color="#3f5b94"
      text-color="#FFFFFF"
      router
    >
      <el-submenu index="1">
        <template #title>
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
        <template #title>
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
        <template #title>
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
    },
  },
  methods: {
    handleSelect(route) {
      const protectedRoutes = [
        '/NormalExperiment',
        '/NormalFeature',
        '/NormalPattern',
        '/ViewData',
        '/AnomalyFeature',
        '/ViewAnomaly',
      ];

      if (!this.isLoggedIn && protectedRoutes.includes(route)) {
        // 未登录且尝试访问受保护的路由，跳转到登录页面
        this.$router.push({ name: 'LoginPage' });
      } else {
        // 根据不同的路由执行对应的状态更新
        switch (route) {
          case '/NormalExperiment':
            this.$store.commit('updateNormalState', 'NormalExperiment');
            console.log(this.$store.getters.getNormalState)
            break;
          case '/NormalFeature':
            this.$store.commit('updateNormalState', 'NormalFeature');
            console.log(this.$store.getters.getNormalState)
            break;
          case '/NormalPattern':
            this.$store.commit('updateNormalState', 'NormalPattern');
            console.log(this.$store.getters.getNormalState)
            break;
          default:
            break;
        }
        // 导航到目标路由
        // this.$router.push(route);
      }
    },
  },
};
</script>

<style scoped>
.el-menu-item.is-active {
  color: #fff !important;
  background: rgb(50, 73, 118) !important;
}
</style>
