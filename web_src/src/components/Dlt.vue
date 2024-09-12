<template>
  <div id="app" style="width: 100%">
<!--    <div class="page-header">-->
<!--      <div class="page-title">折线图</div>-->
      <!--      <div class="page-header-btn">-->
      <!--        <el-button icon="el-icon-refresh-right" circle size="mini" :loading="getAlarmListLoading"-->
      <!--                   @click="getDltList()"></el-button>-->
      <!--      </div>-->
<!--    </div>-->
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>

</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'LineChart',
  data() {
    return {
      chart: null,
    };
  },
  mounted() {
    this.initChart();
    this.fetchData();
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chartRef);
    },
    fetchData() {
      // 假设你有一个函数来调用后台API
      this.$axios({
        method: 'get',
        url: `/api/dlt/all`
      }).then((res) => {
        // 假设data是List<Map<time, data>>的JSON表示形式
        // 这里你需要处理数据以匹配ECharts的格式
        const xAxisData = []; // 时间轴数据
        const seriesData = []; // 折线数据
        let dataList = res.data.data;
        dataList.forEach(item => {
          // 假设item是Map的JSON对象，具有time和data属性
          xAxisData.push(new Date(item.time).toLocaleTimeString()); // 添加时间到x轴数据
          seriesData.push(item.ua); // 添加数据到折线数据
        });

        // 设置图表的配置项和数据
        const option = {
          title: {
            text: '抄表数据折线图'
          },
          tooltip: {},
          xAxis: {
            data: xAxisData,
            // type: 'time',
            // splitLine: {
            //   show: true
            // },
            // axisLabel: {
            //   formatter: function (time, index) {
            //     // 这里可以根据需要自定义时间格式
            //     return echarts.format.formatTime('hh:mm', time);
            //   },
            //   interval: 300000 // 0 表示自动计算间隔，也可以指定具体的数字来控制标签间隔
            // },
            // 如果是ECharts 5+版本，可以使用 intervalUnit 和 interval 来控制时间间隔
            // interval: 300000, // 5分钟，单位毫秒
            // intervalUnit: 'minute' // 分钟为单位
          },
          yAxis: {},
          series: [{
            name: '报警数据',
            type: 'line',
            data: seriesData
          }]
        };

        // 使用刚指定的配置项和数据显示图表。
        this.chart.setOption(option);
      })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    }
  }
};
</script>

<style scoped>
/* 这里可以添加你的样式 */
</style>
