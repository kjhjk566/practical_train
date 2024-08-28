<template>
  <footer class="footer">
    <div class="footer__main">
      <!-- <a href="https://git.code.tencent.com/Shiqi_Chen/monitoring_system">
        <h4 class="footer__nav-title">加入我们</h4>
      </a> -->
      <I><h4 class="footer__nav-title" @click="showModal">联系我们</h4></I>
      <el-dialog :visible.sync="visible" width="50%">
        <span class="information">团队信息</span>
        <el-table :data="data" style="width: 100%">
          <el-table-column prop="name" label="姓名" width="180">
          </el-table-column>
          <el-table-column prop="age" label="学号" width="180">
          </el-table-column>
          <el-table-column prop="address" label="邮箱"> </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="visible = false" class="button"
            >确 定</el-button
          >
        </span>
      </el-dialog>
    </div>

    <div class="footer__copyright">
      <h4>2024 NKU 研究生编程实训- KPI 异常数据模拟系统</h4>
    </div>
  </footer>
</template>

<script>
export default {
  name: "Footer",
  data() {
    return {
      visible: false,
      data: [
        {
          name: "匡俊骅",
          age: 2120240797,
          address: "2120240797@mail.nankai.edu.cn",
        },
        {
          name: "曹平川",
          age: 2120240819,
          address: "2120240819@mail.nankai.edu.cn",
        },
        {
          name: "陈世祺",
          age: 2120240791,
          address: "2120240791@mail.nankai.edu.cn",
        },
        {
          name: "陈子昂",
          age: 2120240792,
          address: "2120240792@mail.nankai.edu.cn",
        },
      ],
    };
  },
  methods: {
    showModal() {
      this.visible = true;
    },
    handleOk() {
      this.visible = false;
    },
  },
};
</script>

<style scoped>
.footer {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.footer__main {
  display: flex;
}

.footer__copyright {
  display: flex;
}

a {
  margin-right: 10px;
  text-decoration: none;
}

h4 {
  color: rgba(38, 35, 35, 0.8);
  cursor: pointer;
}

.information {
  display: flex;
  color: rgb(63, 91, 148);
  margin-left: 10px;
}
.button {
  background-color: rgb(63, 91, 148);
  border-color: #fff;
  color: #fff;
}
</style>
