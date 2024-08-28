<template>
  <div class="user-status" @click="handleClick" @mouseover="showTooltip" @mouseleave="hideTooltip">
    <img :src="userImage" alt="User Avatar" class="avatar" />
    <div v-show="tooltipVisible" class="tooltip">
      <el-button
        type="danger"
        @click.stop="logout"
        class="logout-button"
      >
        登出
      </el-button>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations } from 'vuex';

export default {
  data() {
    return {
      tooltipVisible: false // 控制tooltip的显示
    };
  },
  computed: {
    ...mapState({
      userLoggedIn: state => state.isLoggedIn,
      userImage: state => state.userImage
    })
  },
  methods: {
    ...mapMutations(['logout']),
    handleClick() {
      if (!this.userLoggedIn) {
        // 保存当前页面的路径和查询参数
        const currentRoute = this.$route.fullPath;
        this.$router.push({ name: 'LoginPage', query: { redirect: currentRoute } });
      }
    },
    showTooltip() {
      if (this.userLoggedIn) {
        this.tooltipVisible = true;
      }
    },
    hideTooltip() {
      this.tooltipVisible = false;
    }
  },
  watch: {
    '$route.query.redirect': function (newVal) {
      if (newVal) {
        this.userLoggedIn = this.$store.state.isLoggedIn;
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

.tooltip {
  position: absolute;
  top: 60px;
  right: 0;
  background-color: white;
  border: 1px solid #ccc;
  padding: 5px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logout-button {
  font-size: 14px; /* 增大字体大小 */
  padding: 8px 16px; /* 增加按钮的内边距 */
}

.tooltip .el-button {
  margin: 0;
}
</style>
