<template>
  <div>
    <el-container>
      <el-aside width="240px">
        <sidebar @menu="change"></sidebar>
      </el-aside>
      <el-container>
        <!-- <el-header>
          <selection-cascader
            :key="this.$store.getters.getObservedState"
            @submit="getChartsData"
            v-if="this.showComponent"
          ></selection-cascader>
        </el-header> -->
        <el-main>
          <!-- <el-empty description="请点击右上方选择器选择数据集" v-if="this.initialData.length == 0 && this.showComponent"></el-empty> -->
          <div class="main-information">
            <user-status></user-status>
            <!-- <router-view
              :initialData="initialData"
              @datazoom="dataZoomData"
              :key="initialData"
              @refresh="refreshData"
              @stop="stopData"
              ref="dataShow"
            ></router-view> -->
            <router-view></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Sidebar from "../component/Sidebar";
import UserStatus from '../component/UserStatus.vue';
import router from "../router";

export default {
  components: { Sidebar, UserStatus },
  data() {
    return {
      showComponent: false,
      initialData: [],
      selectPath: [],
    };
  },
  methods: {
    change(val) {
      this.showComponent = val == "2";
    },
  },
  watch: {
    "$store.getters.getObservedState": {
      deep: true,
      handler() {
        this.url = this.getPostPath();
        this.initialData = [];
      },
    },
  },
  mounted() {
    this.change(window.location.href.slice(-3,-2));
  },
};
</script>

<style scoped>
.el-header {
  background-color: #3f5b94;
  color: #fff;
  line-height: 7.5vh;
  display: flex;
  justify-content: flex-end;
}

.el-aside {
  background-color: #3f5b94;
  color: #333;
}
li.el-submenu {
  width: 240px;
}
.el-main {
  background-color: #fff;
  color: #333;
  height: 100vh;
  display: flex;
  justify-content: center;
  padding: 0;
}
.el-button {
  position: fixed;
  right: 3%;
  top: 15%;
}
.main-information {
}
</style>
