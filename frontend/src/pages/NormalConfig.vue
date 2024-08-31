<template>
  <div>
    <div>
      <component
        :is="this.$store.getters.getNormalState"
        @listenExperiment="listenExperiment"
        @listenFeature="listenFeature"
        @listenPattern="listenPattern"
        :propsMetric="metricsNum"
      ></component>
    </div>
    <div class="change-component">
      <el-button
        class="button-color"
        type="primary"
        icon="el-icon-arrow-left"
        @click="lastStep"
        v-if="this.$store.getters.getNormalState != 'NormalExperiment'"
        >上一页</el-button
      >
      <el-button
        class="button-color"
        type="primary"
        @click="nextStep"
        v-if="this.$store.getters.getNormalState != 'NormalPattern'"
        >下一页<i class="el-icon-arrow-right el-icon--right"></i
      ></el-button>
    </div>
    <div>
      <el-dialog title="实验参数" :visible.sync="dialogTableVisible">
        <el-table :data="gridData">
          <el-table-column property="date" label="参数"></el-table-column>
          <el-table-column property="name" label="设定"></el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogTableVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import NormalExperiment from "../component/NormalExperiment.vue";
import NormalFeature from "../component/NormalFeature.vue";
import NormalPattern from "../component/NormalPattern.vue";

//引入组件A以及组件B

export default {
  components: {
    NormalExperiment,
    NormalFeature,
    NormalPattern
  },
  data() {
    return {
      dialogTableVisible: false,
      labName: "default",
      metricsNum: 3,
      smoothness: 0.5,
      periodicity: 0.5,
      correlation: 0.5,
      patterns: [],
      steps: ["NormalExperiment", "NormalFeature", "NormalPattern"],
      gridData: []
    };
  },
  methods: {
    showFullScreenLoading() {
      const loading = this.$loading({
        lock: true,
        text: "正在生成数据...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      setTimeout(() => {
        loading.close();
        this.$notify({
          title: "成功",
          message: "数据生成成功",
          type: "success"
        });
      }, 2000);
    },
    confirm() {
      this.dialogTableVisible = false;
      const params = new URLSearchParams();
      params.append("username", this.$store.getters.getGlobalUserName);
      params.append("labName", this.labName);
      params.append("metricNum", this.metricsNum);
      params.append("smoothness", this.smoothness);
      params.append("periodicity", this.periodicity);
      params.append("correlation", this.correlation);
      params.append("patterns", this.patterns);
      this.showFullScreenLoading();
      this.$http
        .post("http://localhost:5000/generateNormal/generate", params)
        .then((res) => {
          console.log(res)
          this.$http
            .post("http://localhost:8080/data/insert", params)
            .then((res) => {
            console.log(res)
          });
        });
      
    },
    listenExperiment(data) {
      console.log(data);
      this.labName = data["labName"];
      this.metricsNum = data["metricsNum"];
    },
    listenFeature(data) {
      console.log(data);
      this.smoothness = data["smoothness"];
      this.periodicity = data["periodicity"];
      this.correlation = data["correlation"];
    },
    listenPattern(data) {
      console.log(data);
      this.patterns = data;
      this.gridData = [];
      this.gridData.push({
        date: "实验名称",
        name: this.labName
      });
      this.gridData.push({
        date: "指标数量",
        name: this.metricsNum
      });
      this.gridData.push({
        date: "平滑度",
        name: this.smoothness
      });
      this.gridData.push({
        date: "周期性",
        name: this.periodicity
      });
      this.gridData.push({
        date: "相关性",
        name: this.correlation
      });
      this.gridData.push({
        date: "模式",
        name: this.patterns
      });
      this.dialogTableVisible = true;
    },
    nextStep() {
      if (
        this.steps.indexOf(this.$store.getters.getNormalState) ===
        this.steps.length - 1
      ) {
        this.$store.commit("updateNormalState", this.steps[0]);
        this.$router.push("/" + this.$store.getters.getNormalState);
        console.log(this.$store.getters.getNormalState);
        return;
      }
      this.$store.commit(
        "updateNormalState",
        this.steps[this.steps.indexOf(this.$store.getters.getNormalState) + 1]
      );
      this.$router.push("/" + this.$store.getters.getNormalState);
      console.log(this.$store.getters.getNormalState);
    },
    lastStep() {
      if (this.steps.indexOf(this.$store.getters.getNormalState) === 0) {
        this.$store.commit(
          "updateNormalState",
          this.steps[this.steps.length - 1]
        );
        this.$router.push("/" + this.$store.getters.getNormalState);
        console.log(this.$store.getters.getNormalState);
        return;
      }
      this.$store.commit(
        "updateNormalState",
        this.steps[this.steps.indexOf(this.$store.getters.getNormalState) - 1]
      );
      this.$router.push("/" + this.$store.getters.getNormalState);
      console.log(this.$store.getters.getNormalState);
    }
  }
};
</script>

<style scoped>
.change-component {
  margin-top: 5%;
}
/* .button-color {
    background-color: #3f5b94;
    border-color: #3f5b94;
  } */
</style>
