<template>
  <div class="login-page">
    <div class="login-box">
      <h2>登录</h2>
      <el-form :model="loginForm" @submit.native.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister">登录</el-button>
          <a @click="handleRegister">注册</a>//
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
    handleRegister(){

    },
    handleLogin() {
      // 模拟登录逻辑
      // if (this.loginForm.username === 'admin' && this.loginForm.password === '1234') {
      //   this.$message.success('登录成功');
      //   this.$root.$emit('login-success'); // 触发全局登录成功事件

      //   // 获取原始页面路径并跳转回去
      //   const redirect = this.$route.query.redirect || '/';
      //   this.$router.push(redirect);
      // } else {
      //   this.$message.error('用户名或密码错误');
      // }
      const params = new URLSearchParams();
      params.append("username", this.loginForm.username);
      params.append("password", this.loginForm.password);
      this.$http
        .post("http://localhost:5000/", params)
        .then((res) => {
          if (res.data == "Success"){
             this.$message.success('登录成功');
              this.$root.$emit('login-success'); // 触发全局登录成功事件

              // 获取原始页面路径并跳转回去
              const redirect = this.$route.query.redirect || '/';
              this.$router.push(redirect);
          }
          else if (res.data == "No user"){
              this.$message.error('无此用户');
          }
          else{
            this.$message.error('无此用户');
          }
            // 
        });
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
}
</style>
