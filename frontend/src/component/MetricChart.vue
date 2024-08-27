<template>
  <div id="container">
    <!--图表容器-->
    <div ref="Chart" style="width: 1000px; height: 300px"></div>
  </div>
</template>
<script>
export default {
  name: "MetricChart",
  props: ["propsData", "metricIndex", "propsLabel"],
  data() {
    return {
      testName: "test",

      formatter: "{value}"
    };
  },
  mounted() {
    this.showCharts();
  },
  watch: {
    propsData: {
      handler() {
        this.showCharts();
      }
    }
  },
  methods: {
    showCharts() {
      let continuousOnes = [];
      let start = -1;
      for (let i = 0; i < this.propsLabel.length; i++) {
        if (this.propsLabel[i] === 1 && start === -1) {
          start = i;
        } else if (this.propsLabel[i] === 0 && start !== -1) {
          continuousOnes.push({ start, end: i - 1 });
          start = -1;
        }
      }
      // 处理结尾处的1
      if (start !== -1) {
        continuousOnes.push({ start, end: this.propsLabel.length - 1 });
      }

      // 找到连续的0的段
      const continuousZeros = [];
      let startZeros = -1;
      for (let i = 0; i < this.propsLabel.length; i++) {
        if (this.propsLabel[i] === 0 && startZeros === -1) {
          startZeros = i;
        } else if (this.propsLabel[i] === 1 && startZeros !== -1) {
          continuousZeros.push({ start: startZeros, end: i - 1 });
          startZeros = -1;
        }
      }
      // 处理结尾处的0
      if (startZeros !== -1) {
        continuousZeros.push({ start: startZeros, end: this.propsLabel.length - 1 });
      }

      let visualMapPieces = [];
      continuousOnes.forEach(({ start, end }) => {
        visualMapPieces.push({
          lte: end,
          gte: start,
          color: "red"
        });
      });
      continuousZeros.forEach(({ start, end }) => {
        visualMapPieces.push({
          lte: end,
          gte: start,
          color: "#3f5b94"
        });
      });
      // let visualMapPieces = continuousOnes.map(({ start, end }) => ({
      //   lte: end,
      //   gte: start,
      //   color: "red"
      // }));
      // visualMapPieces.splice(0, 0, {
      //   gte: 0,
      //   lte: this.propsLabel.length - 1,
      //   color: "green"
      // })

      let myChart = this.$echarts.init(this.$refs.Chart);
      myChart.setOption({
        tooltip: {
          trigger: "axis"
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: "none"
            }
          }
        },
        title: { text: "Metric: " + this.metricIndex },
        xAxis: {
          type: "category", //坐标轴类型,类目轴，适用于离散的类目数据。为该类型时类目数据可自动从 series.data 或 dataset.source 中取，或者可通过 xAxis.data 设置类目数据
          //坐标轴刻度相关设置
          axisTick: {
            alignWithLabel: true //为true时，可以让刻度跟底部的标签内容对齐
          },
          data: [...new Array(this.propsData.length).keys()], //X轴的刻度数据
          // name: "time",//坐标轴名称
          nameLocation: "start", //坐标轴名称显示位置
          //坐标轴名称的文字样式
          nameTextStyle: {
            align: "center", //文字水平对齐方式，默认自动
            verticalAlign: "top", //文字垂直对齐方式，默认自动
            lineHeight: 28, //行高
            fontSize: 10, //字体大小
            color: "rgba(0, 0, 0, 1)" //字体颜色
          },
          //坐标轴刻度标签的相关设置
          axisLabel: {
            interval: "auto" //坐标轴刻度标签的显示间隔，在类目轴中有效。可以设置成 0 强制显示所有标签,如果设置为 1，表示『隔一个标签显示一个标签』，如果值为 2，表示『隔两个标签显示一个标签』，以此类推。
          }
        },
        // Y轴
        yAxis: {
          type: "value", //坐标轴类型,'value' 数值轴，适用于连续数据
          //坐标轴刻度标签的相关设置
          axisLabel: {
            formatter: this.formatter //刻度标签的内容格式器，支持字符串模板和回调函数两种形式。简单讲就是可以自己格式化标签的内容。
          }
        },
        //直角坐标系内绘图网格,简单理解指的就是这个折线图。
        grid: {
          left: 50 //grid 组件离容器左侧的距离
        },
        // 数据

        visualMap: {
          show: false,
          dimension: 0,
          pieces: visualMapPieces
        },
        series: [
          {
            data: this.propsData,
            // data: this.propsData.map((value, index) => ({
            //   value,
            //   itemStyle: {
            //     normal: {
            //       color: this.propsLabel[index] === 1 ? "red" : undefined
            //     }
            //   }
            // })),
            // markAera: {
            //   itemStyle: {
            //     color: "rgba(255, 173, 177, 0.4)"
            //   },
            //   data: [
            //     [
            //       {
            //         name: "Morning Peak",
            //         xAxis: "0"
            //       },
            //       {
            //         xAxis: "100"
            //       }
            //     ],
            //     [
            //       {
            //         name: "Evening Peak",
            //         xAxis: "200"
            //       },
            //       {
            //         xAxis: "300"
            //       }
            //     ]
            //   ]
            // },
            type: "line",
            smooth: true
            // showSymbol: false,
          }
        ]
      });
    }
  }
};
</script>

<style scoped></style>
