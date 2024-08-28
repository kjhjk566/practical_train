<template>
  
  <div class="info">
    <user-status  class="fixed-component"></user-status>
    <div class="advantage">
      <div>
        <h2>通过全面的测试和验证，确保系统的稳定性与可靠性</h2>
        <p>
          我们进行充分的测试和验证，以确保系统在不同条件下的卓越稳定性，从而为用户提供无与伦比的可靠性保障。
        </p>
      </div>
      <div>
        <h2>数据可视化与用户界面设计的简洁直观</h2>
        <p>
          我们采用先进的数据可视化技术和简洁直观的用户界面设计，以提供令人舒适的用户体验，让复杂数据呈现变得直观易懂。
        </p>
      </div>
      <div>
        <h2>提供丰富的数据生成和故障模拟功能，覆盖多领域需求</h2>
        <p>
          我们提供多样化的数据生成和故障模拟功能，涵盖多领域的需求，为用户提供广泛的应用前景，满足不同行业的需要。
        </p>
      </div>
    </div>
    <div>
      <img
        alt="立即使用"
        src="https://assets.code.tencent.com/official/1692153622093/static/img/use-right.be3d7f9.png"
        style=""
      />
    </div>

    <div class="content">
      <h2>多指标数据生成：</h2>
      <p>
        网站提供强大的多指标数据生成功能，用户可以根据自己的需要生成各种类型的模拟数据。用户可以根据自己的测试需求自定义生成的数据内容和规模。
      </p>

      <h2>故障注入模拟：</h2>
      <p>
        用户可以选择不同类型的故障注入模式，模拟系统在各种压力下的表现，以评估系统在异常情况下的表现和恢复能力。
      </p>

      <h2>数据可视化与分析：</h2>
      <p>
        网站提供直观的可视化界面和实时监控功能，以便用户能够时刻了解系统的性能和状态，帮助用户更好地理解数据特征和异常情况，全面评估系统的可靠性和鲁棒性。
      </p>

      <h2>定制化配置：</h2>
      <p>
        用户能够根据自己的需求，灵活配置生成数据的参数，以及故障注入的规模和方式，使得系统更具适用性和灵活性。无论用户是经验丰富的测试专家还是初学者，都能够从该网站中受益。
      </p>

      <h2>数据导出与共享：</h2>
      <p>支持将生成的数据导出为常见的数据格式，方便用户在其他系统中使用。</p>
    </div>
    <div>
      <img
        alt="工蜂"
        src="https://assets.code.tencent.com/official/1692153622093/static/img/banner-right-bg.52523c4.png"
        style=""
      />
      <div class="radio" style=""></div>
    </div>
    <div id="circle-btn" class="btn-container">
      <button @click="toUse">立即使用</button>
      <!-- <svg width="48" height="48" xmlns="http://www.w3.org/2000/svg"></svg> -->
    </div>
  </div>
</template>

<script>
import UserStatus from '../component/UserStatus.vue';

export default {
  name: "SystemInformation",
  methods: {
    toUse() {
      this.$store.commit("updateNormalState", "NormalExperiment");
      this.$router.push("/NormalExperiment");
    }
  },
  components: {
    UserStatus, // 注册组件
   
  },
};
</script>

<style scoped>
.text {
  text-align: center; /* 让文字居中对齐 */
  max-width: 940px; /* 设置最大宽度，可以根据需要调整 */
  margin: 0 auto; /* 使用 auto 来使元素在水平方向上居中 */
  padding: 20px; /* 设置内边距，增加可读性 */
  font-size: 16px; /* 设置字体大小，可以根据需要调整 */
  line-height: 1.5; /* 设置行高，增加可读性 */
}

.text h2 {
  margin-bottom: 20px; /* 设置标题底边距，增加段落之间的间隔 */
  color: #333; /* 设置标题文字颜色，可以根据需要调整 */
}
.advantage {
  display: flex;
  justify-content: space-around; /* 将子元素在水平方向上平均分布 */
  align-items: center; /* 将子元素在垂直方向上居中对齐 */
  padding: 20px;
}

.advantage div {
  text-align: center;
  max-width: 300px; /* 限制每个子元素的最大宽度 */
}

.advantage h2 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #333; /* 标题颜色 */
}

.advantage p {
  font-size: 16px;
  color: #666; /* 段落颜色 */
  line-height: 1.5;
}
.content {
  max-width: 940px;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin: 20px;
}

h2 {
  /* font-size: 20px; */
  line-height: 1.5;
  margin-bottom: 10px;
}

p {
  margin-bottom: 10px;
}

#circle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  /* height: 100vh; */
}
.btn-container {
  position: relative;
}

button {
  border: 0;
  border-radius: 50px;
  color: white;
  background: #3f5b94;
  padding: 15px 40px 16px 40px;
  text-transform: uppercase;
  background: linear-gradient(to right, #f72585 50%, #3f5b94 50%);
  background-size: 200% 100%;
  background-position: right bottom;
  transition: all 2s ease;
}

svg {
  background: #f72585;
  padding: 8px;
  border-radius: 50%;
  position: absolute;
  left: 42.3%;
  top: 0%;
}

button:hover {
  background-position: left bottom;
}

</style>