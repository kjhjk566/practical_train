<template>
  <div class="experiment">
    <div>
      <el-card class="box-card" shadow="hover">
        <el-form
          :inline="true"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="实验名称" prop="labName">
            <el-input class="input" v-model="ruleForm.labName"></el-input>
          </el-form-item>
          <el-form-item label="数据源" prop="sourceName">
            <el-input class="input" v-model="ruleForm.sourceName"></el-input>
          </el-form-item>
          <el-form-item >
            <el-button @click="getOptions">确定</el-button>
          </el-form-item>
        </el-form>
        <el-form
          :inline="true"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="全局点异常"> </el-form-item>
          <el-form-item label="注入指标">
            <el-select
              class="selector"
              v-model="globalIndex"
              multiple
              placeholder="请选择"
            >
              <el-option
                v-for="(item, index) in indexOptions[0]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="异常比率">
            <el-input class="input" v-model.number="ruleForm.globalRatio"></el-input>
          </el-form-item>
          <el-form-item label="影响范围">
            <el-input class="input" v-model.number="ruleForm.globalRadius"></el-input>
          </el-form-item>
          <el-form-item label="影响因子">
            <el-input class="input" v-model.number="ruleForm.globalFactor"></el-input>
          </el-form-item>
        </el-form>
        <el-form
          :inline="true"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="上下文点异常"> </el-form-item>
          <el-form-item label="注入指标">
            <el-select
              class="selector"
              v-model="contextIndex"
              multiple
              placeholder="请选择"
            >
              <el-option
                v-for="item in indexOptions[1]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="异常比率">
            <el-input class="input" v-model.number="ruleForm.contextRatio"></el-input>
          </el-form-item>
          <el-form-item label="影响范围">
            <el-input class="input" v-model.number="ruleForm.contextRadius"></el-input>
          </el-form-item>
          <el-form-item label="影响因子">
            <el-input class="input" v-model.number="ruleForm.contextFactor"></el-input>
          </el-form-item>
        </el-form>
        <el-form
          :inline="true"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="季节性异常"> </el-form-item>
          <el-form-item label="注入指标">
            <el-select
              class="selector"
              v-model="seasonalIndex"
              multiple
              placeholder="请选择"
            >
              <el-option
                v-for="item in indexOptions[2]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="异常比率">
            <el-input class="input" v-model.number="ruleForm.seasonalRatio"></el-input>
          </el-form-item>
          <el-form-item label="影响范围">
            <el-input
              class="input"
              v-model.number="ruleForm.seasonalRadius"
            ></el-input>
          </el-form-item>
          <el-form-item label="影响因子">
            <el-input
              class="input"
              v-model.number="ruleForm.seasonalFactor"
            ></el-input>
          </el-form-item>
        </el-form>
        <el-form
          :inline="true"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="趋势异常"> </el-form-item>
          <el-form-item label="注入指标">
            <el-select
              class="selector"
              v-model="trendIndex"
              multiple
              placeholder="请选择"
            >
              <el-option
                v-for="item in indexOptions[3]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="异常比率">
            <el-input class="input" v-model.number="ruleForm.trendRatio"></el-input>
          </el-form-item>
          <el-form-item label="影响范围">
            <el-input class="input" v-model.number="ruleForm.trendRadius"></el-input>
          </el-form-item>
          <el-form-item label="影响因子">
            <el-input class="input" v-model.number="ruleForm.trendFactor"></el-input>
          </el-form-item>
        </el-form>
        <el-form
          :inline="true"
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="形状异常"> </el-form-item>
          <el-form-item label="注入指标">
            <el-select
              class="selector"
              v-model="patternIndex"
              multiple
              placeholder="请选择"
            >
              <el-option
                v-for="item in indexOptions[4]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="异常比率">
            <el-input class="input" v-model.number="ruleForm.patternRatio"></el-input>
          </el-form-item>
          <el-form-item label="影响范围">
            <el-input class="input" v-model.number="ruleForm.patternRadius"></el-input>
          </el-form-item>
          <el-form-item label="影响因子">
            <el-input class="input" v-model.number="ruleForm.patternFactor"></el-input>
          </el-form-item>
        </el-form>
        <div class="patterns">
          <PatternChart
            @listenCharts="listenCharts"
            :propsData="[
              0.0,
              0.19834015220060802,
              0.3868834885934064,
              0.5566291765371674,
              0.7000980376310593,
              0.8119238674722211,
              0.8892583503544338,
              0.9319541427873157,
              0.942510484416573,
              0.9257868697054267,
              0.888510987494519,
              0.8386254620136858,
              0.7845322715356088,
              0.7343027990225349,
              0.6949244725172642,
              0.6716516140490221,
              0.6675187334295583,
              0.6830599163432245,
              0.7162594683595327,
              0.7627382401548686,
              0.8161589274260398,
              0.8688140001436573,
              0.9123435249907164,
              0.9385184669727867,
              0.9400191351675351,
              0.9111388030288694,
              0.8483491536129992,
              0.7506764838488584,
              0.6198544528519982,
              0.4602390725747093,
              0.2784928364737861,
              0.08306547026897773,
              -0.11648307050190722,
              -0.3102576587350224,
              -0.4888326645580129,
              -0.6440151476801765,
              -0.7694991866503497,
              -0.861353670309823,
              -0.9183007328309445,
              -0.9417606871569848,
              -0.9356601346414065,
              -0.9060210415196778,
              -0.860368090006542,
              -0.8070077903702244,
              -0.7542442356537746,
              -0.7096019754812233,
              -0.6791257804854296,
              -0.6668201279814616,
              -0.674278683319415,
              -0.7005370022433547,
              -0.7421616612774329,
              -0.7935678246175281,
              -0.847536771620339,
              -0.8958869693486312,
              -0.9302384830221435,
              -0.9428021063600996,
              -0.9271223157328223,
              -0.8787072109581854,
              -0.7954886395706342,
              -0.6780708147606304,
              -0.5297445804561512,
              -0.35626536748100557,
              -0.16541395672970335,
              0.033621463086622186,
              0.23098751445712656,
              0.41696661132768253,
              0.5827659426824139,
              0.7212247718325632,
              0.8273776511861654,
              0.8988240058889052,
              0.9358718115641411,
              0.9414432388426228,
              0.9207513610674665,
              0.8807774260285129,
              0.8295959556093728,
              0.7756084726212358,
              0.7267547533500716,
              0.6897724446037732,
              0.6695714926677219,
              0.6687795015407201,
              0.6874987926211739,
              0.7232969491524543,
              0.7714316932162318,
              0.8252899317804959,
              0.8770015989301067,
              0.9181732306308796,
              0.9406754385807922,
              0.9374135636615426,
              0.9030122243893277,
              0.8343521040768915,
              0.7309104613765909,
              0.594874329214479,
              0.43101562590570974,
              0.2463386302679344,
              0.04953057398147702,
              -0.1497363338574081,
              -0.3415898815631738,
              -0.5167604990938701,
              -0.6673306653219907,
              -0.7873649215268332
            ]"
            metricIndex="0"
          ></PatternChart>
          <PatternChart
            @listenCharts="listenCharts"
            :propsData="[0, 1, 2, 1, 0, 1, 2, 1, 0]"
            metricIndex="1"
          ></PatternChart>
        </div>

        <el-button type="primary" @click="submitForm('ruleForm')"
          >立即创建</el-button
        >
      </el-card>
    </div>
    <div>
      <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
        <span>是否确定以当前参数进行异常注入</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import PatternChart from "../component/PatternChart.vue";
// import showFullScreenLoading from "../js/utils.js";

export default {
  name: "AnomalyConfig",
  data() {
    return {
      dialogVisible: false,
      globalIndex: [],
      contextIndex: [],
      seasonalIndex: [],
      trendIndex: [],
      patternIndex: [],
      indexOptions: [],
      options: [{}],
      ruleForm: {
        labName: "",
        sourceName: "",
        globalRatio: "",
        contextRatio: "",
        seasonalRatio: "",
        trendRatio: "",
        patternRatio: "",
        globalRadius: "",
        contextRadius: "",
        seasonalRadius: "",
        trendRadius: "",
        patternRadius: "",
        globalFactor: "",
        contextFactor: "",
        seasonalFactor: "",
        trendFactor: "",
        patternFactor: "",
        patterns: []
      },
      rules: {
        labName: [
          { required: true, message: "请输入", trigger: "blur" },
          { min: 3, max: 10, message: "长度在 3 到 10 个字符", trigger: "blur" }
        ],
        sourceName: [
          { required: true, message: "请输入", trigger: "blur" },
          { min: 3, max: 10, message: "长度在 3 到 10 个字符", trigger: "blur" }
        ],
        smoothness: [
          { required: true, message: "请输入数据平滑度", trigger: "blur" },
          {
            type: "number",
            message: "请输入数字",
            trigger: "blur",
            transform(value) {
              return Number(value);
            }
          },
          {
            validator: (rule, value, callback) => {
              if (!/^\d+(\.\d+)?$/.test(value)) {
                callback(new Error("请输入有效的数字"));
              } else if (value < 0 || value > 1) {
                callback(new Error("请输入0到1之间的数字"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
      }
    };
  },
  methods: {
    showFullScreenLoading() {
      const loading = this.$loading({
        lock: true,
        text: "正在注入异常...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      setTimeout(() => {
        loading.close();
        this.$notify({
          title: "成功",
          message: "异常注入成功",
          type: "success"
        });
      }, 2000);
    },
    confirm() {
      this.dialogVisible = false;
      const params = {
        labName: this.ruleForm.labName,
        sourceName: this.ruleForm.sourceName,
        globalRatio: this.ruleForm.globalRatio,
        contextRatio: this.ruleForm.contextRatio,
        seasonalRatio: this.ruleForm.seasonalRatio,
        trendRatio: this.ruleForm.trendRatio,
        patternRatio: this.ruleForm.patternRatio,
        globalRadius: this.ruleForm.globalRadius,
        contextRadius: this.ruleForm.contextRadius,
        seasonalRadius: this.ruleForm.seasonalRadius,
        trendRadius: this.ruleForm.trendRadius,
        patternRadius: this.ruleForm.patternRadius,
        globalFactor: this.ruleForm.globalFactor,
        contextFactor: this.ruleForm.contextFactor,
        seasonalFactor: this.ruleForm.seasonalFactor,
        trendFactor: this.ruleForm.trendFactor,
        patternFactor: this.ruleForm.patternFactor,
        globalIndex: this.globalIndex,
        contextIndex: this.contextIndex,
        seasonalIndex: this.seasonalIndex,
        trendIndex: this.trendIndex,
        patternIndex: this.patternIndex,
        patterns: this.ruleForm.patterns
      };
      console.log(params)
      this.showFullScreenLoading();
      this.$http
        .post("http://localhost:8080/anomaly/inject", params)
        .then((res) => {
          console.log(res)
        });
    },
    submitForm(formName) {
      this.dialogVisible = true;
      // const forms = this.$refs[formName];
      // for (let i = 0; i < forms.length; i++) {
      //   forms[i].validate(valid => {
      //     if (valid) {
      //       // this.confirm();
      //       // alert('submit!');
      //       this.$notify({
      //         title: "成功",
      //         message: "表单提交成功,数据特征已重新计算",
      //         type: "success"
      //       });
      //     } else {
      //       this.$notify.error({
      //         title: "错误",
      //         message: "表单提交失败"
      //       });
      //       return false;
      //     }
      //   });
      // }
    },
    resetForm(formName) {
      // this.$refs[formName].resetFields();
      // 获取所有相同 formName 的表单
      const forms = this.$refs[formName];

      // 遍历所有表单并执行重置操作
      for (let i = 0; i < forms.length; i++) {
        forms[i].resetFields();
      }
    },
    getOptions() {
      this.$http
        .get("/static/synthetic/"+this.ruleForm.sourceName+".json")
        .then(response => {
          console.log(response);
          console.log(response.data);
          console.log(response.data[this.ruleForm.sourceName]);
          let length = response.data[this.ruleForm.sourceName]["metricsNum"]
          console.log(length)
          this.options = []
          for (let i = 0; i < length; i++) {
            this.options.push({
              value: i,
              label: "指标" + i
            });
          }
          for (let i = 0; i < 5; i++) {
            this.indexOptions.push(this.options);
          }
        })
        .catch(err => {
          console.log(err);
          this.$notify.error({
            title: "错误",
            message: "文件未找到！"
          });
        });
    },
    listenCharts(data) {
      // console.log(data)
      if (data[0] == "yes" && this.ruleForm.patterns.indexOf(data[1]) == -1) {
        this.ruleForm.patterns.push(data[1]);
        this.ruleForm.patterns.sort();
      } else if (
        data[0] == "no" &&
        this.ruleForm.patterns.indexOf(data[1]) != -1
      ) {
        this.ruleForm.patterns.splice(
          this.ruleForm.patterns.indexOf(data[1]),
          1
        );
      }
    }
  },
  mounted() {
    // this.getOptions();
  },
  watch: {
    // 'ruleForm.sourceName': function() {
    //   this.getOptions();
    // }
  },
  components: { PatternChart }
};
</script>

<style scoped>
.experiment {
  display: flex;
}
.box-card {
  width: 1000px;
  border-radius: 4px;
}
.input {
  width: 70px;
}
.selector {
  width: 100px;
}
.patterns {
  display: flex;
}
</style>
