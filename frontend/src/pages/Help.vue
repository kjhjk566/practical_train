<template>
  <div class="experiment-flow">
    <div class="process-selector">
      <el-button @click="selectProcess('normal')" :class="{ active: selectedProcess === 'normal' }">正常数据生成流程</el-button>
      <el-button @click="selectProcess('exception')" :class="{ active: selectedProcess === 'exception' }">异常注入流程</el-button>
    </div>

    <div v-if="selectedProcess === 'normal'" class="process-section">
      <h2>正常数据生成流程</h2>
      <ul>
        <li>用户启动系统。</li>
        <li>用户输入实验的基本信息，如实验名称、实验类型等。</li>
        <li>用户根据实验需求填写必要的参数。</li>
        <li>用户选择数据生成的模式（如实时生成、批量生成等）。</li>
        <li>系统根据用户输入的信息和选择的模式生成数据。</li>
        <li>用户查看生成的数据，并进行必要的观测和分析。</li>
        <li>用户完成正常数据生成流程。</li>
      </ul>
    </div>

    <div v-else-if="selectedProcess === 'exception'" class="process-section">
      <h2>异常注入流程</h2>
      <ul>
        <li>用户选择异常注入功能。</li>
        <li>用户指定需要注入的异常指标（如错误代码、异常类型等）。</li>
        <li>用户根据异常类型填写必要的参数。</li>
        <li>系统根据用户输入的异常指标和参数注入异常。</li>
        <li>用户查看注入异常后的数据，并进行必要的观测和分析。</li>
        <li>用户完成异常注入流程。</li>
      </ul>
    </div>

    <div class="process-section">
      <h2>注意事项</h2>
      <ul>
        <li>在每个步骤中，都可能有“验证”或“确认”的环节，以确保用户输入的信息准确无误。</li>
        <li>在“数据模式选择”和“填写异常参数”步骤中，可能需要用户提供额外的信息，如数据生成的时间范围、异常注入的触发条件等。</li>
        <li>在“注入异常”步骤之后，可能需要系统进行一定的处理，如记录异常注入的详细信息、通知相关人员等。</li>
      </ul>
    </div>

    <div class="process-section">
      <h2>结束</h2>
      <ul>
        <li>用户可以选择退出系统或继续其他操作。</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedProcess: 'normal',
    };
  },
  methods: {
    selectProcess(process) {
      this.selectedProcess = process;
    },
  },
};
</script>

<style scoped>
.experiment-flow {
    font-family: 'Roboto', sans-serif;
    max-width: 800px;
    margin: auto;
}

.process-selector button {
    background-color: #f0f0f0;
    border: none;
    padding: 10px 20px;
    margin: 5px;
    border-radius: 5px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s;
}

.process-selector button.active,
.process-selector button:hover {
    background-color: #007bff;
    color: white;
}

.process-section {
    border: 1px solid #ccc;
    padding: 20px;
    margin-top: 20px;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.process-section h2 {
    color: #0056b3;
}

ul {
    list-style: inside;
}

/* Responsive Design */
@media (max-width: 768px) {
    .process-selector button {
        width: 100%;
        margin-bottom: 10px;
    }
}
</style>
