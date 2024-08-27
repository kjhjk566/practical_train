<template>
    <div class="experiment">
      <div>
        <el-card class="box-card" shadow="hover">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="实验名称" prop="labName">
              <el-input v-model="ruleForm.labName"></el-input>
            </el-form-item>
            <el-form-item label="实体指标数" prop="metricsNum">
            <el-input v-model="ruleForm.metricsNum"></el-input>
          </el-form-item>
            <el-form-item>
              <el-button class="button-color" type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
              <el-button class="button-color" @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
          
      </div>
      
    </div>
  </template>
  
  <script>
  export default {
    name: "NormalExperiment",
    data() {
      return {
        ruleForm: {
          labName: "",
          metricsNum: "",
        },
        rules: {
          labName: [
            { required: true, message: '请输入实验名称', trigger: 'blur' },
            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
          ],
          metricsNum: [
            { required: true, message: '请输入实体指标数', trigger: 'blur' },
            { type: 'number', message: '请输入数字', trigger: 'blur', transform(value) { return Number(value); } },
            { 
              validator: (rule, value, callback) => {
                if (!/^\d+$/.test(value)) {
                  callback(new Error('请输入正整数'));
                } else if (value > 10) {
                  callback(new Error('请输入不超过10的数字'));
                } else {
                  callback();
                }
              },
              trigger: 'blur'
            }
          ]
        }
      };
    },
    methods: {
      confirm() {
        // console.log(this.metricsNum);
        this.$emit("listenExperiment", this.ruleForm);
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.confirm();
            // alert('submit!');
            this.$notify({
              title: '成功',
              message: '表单提交成功',
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
    border-radius: 4px
  }
  /* .button-color {
    background-color: #3f5b94;
    border-color: #3f5b94;
  } */
  </style>
  