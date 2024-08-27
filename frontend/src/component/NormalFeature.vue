<template>
  <div class="experiment">
    <!-- <div>
      <h4>实验名称：</h4>
      <el-input v-model="labName" placeholder="请输入内容"></el-input>
    </div>
    <div>
      <h4>指标数：</h4>
      <el-input v-model="metricsNum" placeholder="请输入内容"></el-input>
    </div>
    <div>
      <button @click="confirm">确定</button>
    </div> -->
    <div>
      <el-card class="box-card" shadow="hover">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="平滑度" prop="smoothness">
            <el-input v-model="ruleForm.smoothness"></el-input>
          </el-form-item>
          <el-form-item label="周期度" prop="periodicity">
            <el-input v-model="ruleForm.periodicity"></el-input>
          </el-form-item>
          <el-form-item label="关联度" prop="correlation">
            <el-input v-model="ruleForm.correlation"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
        
    </div>
    
  </div>
</template>

<script>
export default {
  name: "NormalFeature",
  props: ["propsMetric"],
  data() {
    return {
      ruleForm: {
        smoothness: "",
        periodicity: "",
        correlation: "",
      },
      rules: {
        smoothness: [
        { required: true, message: '请输入数据平滑度', trigger: 'blur' },
          { type: 'number', message: '请输入数字', trigger: 'blur', transform(value) { return Number(value); } },
          { 
            validator: (rule, value, callback) => {
              if (!/^\d+(\.\d+)?$/.test(value)) {
                callback(new Error('请输入有效的数字'));
              } else if (value < 0 || value > 1) {
                callback(new Error('请输入0到1之间的数字'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
        periodicity: [
        { required: true, message: '请输入数据平滑度', trigger: 'blur' },
          { type: 'number', message: '请输入数字', trigger: 'blur', transform(value) { return Number(value); } },
          { 
            validator: (rule, value, callback) => {
              if (!/^\d+(\.\d+)?$/.test(value)) {
                callback(new Error('请输入有效的数字'));
              } else if (value < 0 || value > 1) {
                callback(new Error('请输入0到1之间的数字'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
        correlation: [
        { required: true, message: '请输入数据平滑度', trigger: 'blur' },
          { type: 'number', message: '请输入数字', trigger: 'blur', transform(value) { return Number(value); } },
          { 
            validator: (rule, value, callback) => {
              if (!/^\d+(\.\d+)?$/.test(value)) {
                callback(new Error('请输入有效的数字'));
              } else if (value < 0 || value > 1) {
                callback(new Error('请输入0到1之间的数字'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
      }
    };
  },
  methods: {
    confirm() {
      console.log(this.ruleForm);
      this.$emit("listenFeature", this.ruleForm);
    },
    changeFeature() {
      const params = new URLSearchParams();
      params.append("smoothness", this.ruleForm.smoothness);
      params.append("periodicity", this.ruleForm.periodicity);
      params.append("correlation", this.ruleForm.correlation);
      params.append("metricNum", this.propsMetric);
      this.$http
        .post("http://localhost:5000/generateNormal/feature", params)
        .then((res) => {
          this.ruleForm.smoothness = res.data[0]
          this.ruleForm.periodicity = res.data[1]
          this.ruleForm.correlation = res.data[2]
          this.confirm()
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changeFeature();
          this.$notify({
            title: '成功',
            message: '表单提交成功,数据特征已重新计算',
            type: 'success'
          });
        } else {
          this.$notify.error({
            title: '错误',
            message: '表单提交失败'
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
.experiment {
  display: flex;
}
.box-card {
  width: 700px;
  border-radius: 4px
}
</style>
