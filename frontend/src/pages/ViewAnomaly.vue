<template>
  <div>
    <div>
      <el-input
        placeholder="请输入内容"
        v-model="entity"
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
      entity: "anomaly",
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
      // this.$showLoading();
      // this.$http.get(`/data/data_${this.metrics}_${this.time_span}_${this.sample_freq}_${this.metric_similiarity_per}_${this.metric_cyclicity_per}_${this.noise_amp}_${this.time_index}.csv`)
      this.$http
        .get("/static/synthetic/" + this.entity + ".json")
        .then(response => {
          console.log(response);
          console.log(response.data[this.entity]["data"].length);
          console.log(response.data[this.entity]["data"][0]);
          console.log(response.data[this.entity]["label"]);
          this.data = [];
          for (let i = 0; i < response.data[this.entity]["data"].length; i++) {
            this.data.push(response.data[this.entity]["data"][i]);
          }
          this.label = response.data[this.entity]["label"];
          // this.label.splice(0,2,1,1)
          this.smoothness = response.data[this.entity]["smoothness"];
          this.periodicity = response.data[this.entity]["periodicity"];
          this.correlation = response.data[this.entity]["correlation"];
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
