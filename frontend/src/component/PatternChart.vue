<template>
    <div id="container">
      <!--图表容器-->
      <div ref="Chart" style="width: 500px; height: 300px"></div>
      <el-radio v-model="selected" label="yes">选择</el-radio>
      <el-radio v-model="selected" label="no">取消</el-radio>
    </div>
  </template>
  <script>
  export default {
    name: "PatternChart",
    props: ["propsData", "metricIndex"],
    data() {
      return {
        selected: "no",
	    formatter: '{value}',
	  }
    },
    mounted() {
      this.showCharts();
      
    },
    watch: {
      propsData: {
        handler() {
            this.showCharts()
        }
      },
      selected: {
        handler() {
            this.confirm()
        }
      }
    },
    methods: {
      confirm(){
        this.$emit("listenCharts", [this.selected, this.metricIndex])
      },
      showCharts(){
        let myChart = this.$echarts.init(this.$refs.Chart);
        myChart.setOption({ 
          tooltip: {
            trigger: 'axis'
          },
          title: { text: 'Pattern: '+this.metricIndex },
          xAxis: {
            type: 'category',//坐标轴类型,类目轴，适用于离散的类目数据。为该类型时类目数据可自动从 series.data 或 dataset.source 中取，或者可通过 xAxis.data 设置类目数据
            //坐标轴刻度相关设置
            axisTick: {
              alignWithLabel: true,//为true时，可以让刻度跟底部的标签内容对齐
            },
            data: [...new Array(this.propsData.length).keys()],//X轴的刻度数据
            // name: "time",//坐标轴名称
            nameLocation: "start",//坐标轴名称显示位置
            //坐标轴名称的文字样式
            nameTextStyle: {
              align: "center",//文字水平对齐方式，默认自动
              verticalAlign: "top",//文字垂直对齐方式，默认自动
              lineHeight: 28,//行高
              fontSize: 10,//字体大小
              color: "rgba(0, 0, 0, 1)"//字体颜色
            },
            //坐标轴刻度标签的相关设置
            axisLabel: {
              interval: 'auto'//坐标轴刻度标签的显示间隔，在类目轴中有效。可以设置成 0 强制显示所有标签,如果设置为 1，表示『隔一个标签显示一个标签』，如果值为 2，表示『隔两个标签显示一个标签』，以此类推。
            }
          },
          // Y轴
          yAxis: {
            type: 'value',//坐标轴类型,'value' 数值轴，适用于连续数据
            //坐标轴刻度标签的相关设置
            axisLabel: {
              formatter: this.formatter//刻度标签的内容格式器，支持字符串模板和回调函数两种形式。简单讲就是可以自己格式化标签的内容。
            },
          },
          //直角坐标系内绘图网格,简单理解指的就是这个折线图。
          grid: {
            left: 50//grid 组件离容器左侧的距离
          },
          // 数据
          series: [
            {
              data: this.propsData,
              type: 'line'
            }
          ],
          
        })
      }
    }
  };
  </script>
  
  <style scoped>
</style>