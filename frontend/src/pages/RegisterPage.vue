<template>
  <div class="login-page">
    <div class="login-box">
      <h2>注册</h2>
      <el-form :model="loginForm" @submit.native.prevent="handleRegister">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
            v-model="loginForm.confirmPassword"
            type="password"
            placeholder="请输入确认密码"
            @blur="checkPasswordMatch"
          ></el-input>
          <div v-if="passwordMismatch" class="error-message">请保持确认密码一致</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <a @click="goBackLogin">返回登录</a>
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
        password: '',
        confirmPassword: ''
      },
      passwordMismatch: false
    };
  },
  methods: {
    handleRegister() {
      // 检查密码是否一致
      if (this.loginForm.password !== this.loginForm.confirmPassword) {
        this.loginForm.password = '';
        this.loginForm.confirmPassword = '';
        return;
      }

      // 如果密码一致，向后端发送请求
      const params = new URLSearchParams();
      params.append("username", this.loginForm.username);
      params.append("password", this.loginForm.password);
      this.$http
        .post("http://localhost:5000/", params)
        .then((res) => {
          if (res.data === "Success") {
            this.$message.success('注册成功');
            this.$router.push({ name: 'LoginPage' });
          } else {
            this.$message.error('用户名已经被使用');
          }
        });
    },
    checkPasswordMatch() {
      this.passwordMismatch = this.loginForm.password !== this.loginForm.confirmPassword;
    },
    goBackLogin() {
      this.$router.push({ name: 'LoginPage' });
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

.error-message {
  color: red;
  font-size: 12px;
  margin-top: 5px;
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
  color: #409EFF;
}
</style>
