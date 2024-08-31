<template>
  <div>
    <div>
      <el-input
        placeholder="请输入内容"
        v-model="labName"
        class="input-with-select"
      >
        <template slot="prepend">Lab Name</template>
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="load"
        ></el-button>
      </el-input>
    </div>
    <div class="data-feature" v-if="data.length != 0">
      <el-input
        :placeholder="'smoothness:' + smoothness"
        :disabled="true"
      ></el-input>
      <el-input
        :placeholder="'periodicity:' + periodicity"
        :disabled="true"
      ></el-input>
      <el-input
        :placeholder="'correlation:' + correlation"
        :disabled="true"
      ></el-input>
    </div>
    <MetricChart
      v-for="(item, index) in data"
      :propsData="item"
      :metricIndex="index"
      :propsLabel="label"
    ></MetricChart>
  </div>
</template>

<script>
import { ElMessage } from "element-ui";
import { fommatData } from "../js/utils";
import MetricChart from "../component/MetricChart.vue";
// import Test from "../components/Test.vue";

export default {
  name: "ViewAnomaly",
  components: { MetricChart },
  data() {
    return {
      labName: "test6",
      metrics: 10,
      smoothness: 0,
      periodicity: 0,
      correlation: 0,
      graphics: [],
      data: [],
      label: []
      // time_index: 1,
    };
  },
  methods: {
    load() {
      const params = new URLSearchParams();
      params.append("username", this.$store.getters.getGlobalUserName);
      params.append("labName", this.labName);
      this.$http
        .post("http://localhost:8080/data/get_data", params)
        .then(response => {
          let data =  JSON.parse(response.data["data"]["timeSeriesData"]);
          console.log(data);
          console.log(data[this.labName]["data"]);
          console.log(data[this.labName]["data"][0]);
          console.log(data[this.labName]["label"]);
          this.data = [];
          for (let i = 0; i < data[this.labName]["data"].length; i++) {
            this.data.push(data[this.labName]["data"][i]);
          }
          this.label = data[this.labName]["label"];
          // this.label.splice(0,2,1,1)
          this.smoothness = data[this.labName]["smoothness"];
          this.periodicity = data[this.labName]["periodicity"];
          this.correlation = data[this.labName]["correlation"];
          this.$children.showCharts;
        })
        .catch(err => {
          console.log(err);
          // this.$finishLoading();
          ElMessage({
            showClose: true,
            message: "文件未找到！",
            type: "error"
          });
        });
    }
  }
};
</script>

<style scoped>
.head {
  display: flex;
  width: 80%;
  text-align: center;
  margin: auto;
}

.main-content {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.show-echarts {
  width: 99%;
  height: 180px;
  margin: auto;
  margin-left: -20px;
  /*background-color: #1883e0;*/
}
.data-feature {
  display: flex;
}
</style>
