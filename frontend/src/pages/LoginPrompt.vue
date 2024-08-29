<template>
  <div class="login-prompt">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>Access Restricted</span>
      </div>
      <div class="content">
        <el-alert
          title="Please log in"
          type="info"
          description="To access this page, please click the 'Login' button in the upper right corner."
          show-icon>
        </el-alert>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'LoginPrompt',
  methods: {
    handleLogin() {
      this.$router.push('/LoginPage');
    }
  }
}
</script>

<style scoped>
.login-prompt {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.box-card {
  width: 400px;
}

.content {
  text-align: center;
  padding: 20px;
}
</style>
