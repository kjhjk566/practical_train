<template>
  <div class="login-page">
    <div class="login-box">
      <i class="el-icon-arrow-left" @click="goback">返回</i>
      <h2>登录</h2>
      <el-form :model="loginForm" @submit.native.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <a @click="handleRegister">注册</a>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    handleRegister() {
      // 跳转到注册页面
      this.$router.push({ name: 'RegisterPage' });
    },
    handleLogin() {
      // 发送登录请求
      const requestData = {
        username: this.loginForm.username,
        password: this.loginForm.password
      };

        this.$http
        .post("http://localhost:8080/user/login", requestData) // 修改为正确的 API 端点
        .then((res) => {
          const { status, message } = res.data;
          
          if (status === 200) {
            this.$message.success('登录成功');
            this.$store.commit('login',this.loginForm.username);
            // this.$root.$emit('login-success'); // 触发全局登录成功事件

            // 获取原始页面路径并跳转回去
            const redirect = this.$route.query.redirect || '/';
            this.$router.push(redirect);
          } else {
            this.$message.error(message || '登录失败');
          }
        })
        .catch((error) => {
          this.$message.error('网络错误，请重试');
        });
    },
    goback() {
      const redirect = this.$route.query.redirect || '/';
      this.$router.push(redirect);
    }
  }
};
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-box {
  width: 300px;
  padding: 20px;
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.login-box h2 {
  text-align: center;
  margin-bottom: 20px;
}

.el-form-item {
  margin-bottom: 15px;
}

a {
  color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: flex-end;
  flex-direction: column;
  cursor: pointer;
  text-decoration: none;
}

a:hover {
  color: #409EFF; /* 更改颜色以反映链接被悬停 */
}

.el-icon-arrow-left {
  display: flex;
  color: rgba(0, 0, 0, 0.7);
  cursor: pointer;
}
</style>
