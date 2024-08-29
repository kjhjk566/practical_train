<template>
  <div class="user-status" @click="handleClick">
    <img :src="userImage" alt="User Avatar" class="avatar" />
  </div>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
import { Message } from 'element-ui';

export default {
  computed: {
    ...mapState({
      userLoggedIn: state => state.isLoggedIn,
      userImage: state => state.userImage
    })
  },
  methods: {
    ...mapMutations(['logout']),
    handleClick() {
      if (this.userLoggedIn) {
        // 如果已经登录，则登出
        this.logout();
        // 显示“登出成功”消息
        Message.success('登出成功');
      } else {
        // 如果未登录，跳转到登录页面
        const currentRoute = this.$route.fullPath;
        this.$router.push({ name: 'LoginPage', query: { redirect: currentRoute } });
      }
    }
  }
};
</script>

<style scoped>
.user-status {
  position: fixed;
  top: 10px;
  right: 23px;
  cursor: pointer;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}
</style>
