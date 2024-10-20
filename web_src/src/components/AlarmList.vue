<template>
  <div id="app" style="width: 100%">
    <div class="page-header">
      <div class="page-title">设备列表</div>
      <div class="page-header-btn">
        <el-input @input="getAlarmList()" style="margin-right: 1rem; width: auto;" size="mini" placeholder="设备编码"
                  prefix-icon="el-icon-search" v-model="deviceId" clearable></el-input>
        <el-button icon="el-icon-refresh-right" circle size="mini" :loading="getAlarmListLoading"
                   @click="getAlarmList()"></el-button>
      </div>
    </div>
    <!--设备列表-->
    <el-table :data="alarmList" style="width: 100%;font-size: 12px;" :height="winHeight" header-row-class-name="table-header">
<!--      <el-table-column prop="id" label="编号" min-width="50"/>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="alarmDescription" label="报警内容" min-width="200" ></el-table-column>-->
      <el-table-column prop="alarmTime" label="报警时间" min-width="200" ></el-table-column>
      <el-table-column label="报警类型" min-width="200" >
        <template slot-scope="scope">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium" type="danger" v-if="scope.row.alarmType==='1'">人工视频报警</el-tag>
            <el-tag size="medium" type="danger" v-if="scope.row.alarmType==='2'">运动目标检测报警</el-tag>
            <el-tag size="medium" type="danger" v-if="scope.row.alarmType==='3'">遗留物检测报警</el-tag>
            <el-tag size="medium" type="danger" v-if="scope.row.alarmType==='4'">物体移除检测报警</el-tag>
            <el-tag size="medium" type="danger" v-if="scope.row.alarmType==='5'">绊线检测报警</el-tag>
            <el-tag size="medium" type="danger" v-if="scope.row.alarmType==='6'">入侵检测报警</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="报警方式" min-width="200" >
        <template slot-scope="scope">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium" type="info" v-if="scope.row.alarmMethod==='1'">电话报警</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmMethod==='2'">设备报警</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmMethod==='3'">短信报警</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmMethod==='4'">GPS报警</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmMethod==='5'">视频报警</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmMethod==='6'">设备故障报警</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="优先级" min-width="200" >
        <template slot-scope="scope">
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium" type="info" v-if="scope.row.alarmPriority==='1'">一级警情</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmPriority==='2'">二级警情</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmPriority==='3'">三级警情</el-tag>
            <el-tag size="medium" type="info" v-if="scope.row.alarmPriority==='4'">四级警情</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="channelId" label="视频通道" min-width="200" ></el-table-column>
      <el-table-column prop="deviceId" label="设备编码" min-width="200" ></el-table-column>
      <el-table-column label="报警抓图" min-width="200">
        <template v-slot:default="scope">
          <el-image
            :src="getSnap(scope.row)"
            :preview-src-list="getBigSnap(scope.row)"
            :fit="'contain'"
            style="width: 60px">
          </el-image>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="float: right"
      @size-change="handleSizeChange"
      @current-change="currentChange"
      :current-page="currentPage"
      :page-size="count"
      :page-sizes="[15, 25, 35, 50]"
      layout="total, sizes, prev, pager, next"
      :total="total">
    </el-pagination>

    <!-- 模态框用于显示全屏图片 -->
<!--    <div-->
<!--      v-if="imageModalVisible"-->
<!--      @click="handleClose"-->
<!--      class="transparent-dialog"-->
<!--    >-->
<!--      <img :src="currentImageUrl" alt="全屏图片" class="fullscreen-image" />-->
<!--    </div>-->


    <div v-if="imageModalVisible" class="overlay" @click="handleClose">
      <div class="modal">
        <el-image
          style="width: 80%; height: auto; display: block; margin: 0 auto;"
          :src="currentImageUrl">
        </el-image>
        <!-- 可以添加关闭按钮 -->
<!--        <button @click="imageModalVisible = false">关闭</button>-->
      </div>
    </div>
  </div>
</template>

<script>
import uiHeader from '../layout/UiHeader.vue'

export default {
  name: 'app',
  components: {
    uiHeader
  },
  data() {
    return {
      deviceId: '', //设备编码
      alarmList: [], //设备列表
      currentAlarm: {}, //当前操作设备对象
      videoComponentList: [],
      updateLooper: 0, //数据刷新轮训标志
      currentAlarmChannelsLenth: 0,
      winHeight: window.innerHeight - 200,
      currentPage: 1,
      count: 15,
      total: 0,
      getAlarmListLoading: false,
      imageModalVisible: false,
      currentImageUrl: '',
    };
  },
  computed: {
  },
  mounted() {
    this.initData();
    this.updateLooper = setInterval(this.initData, 10000);
  },
  destroyed() {
    this.$destroy('videojs');
    clearTimeout(this.updateLooper);
  },
  methods: {
    getSnap: function (row) {
      let baseUrl = window.baseUrl ? window.baseUrl : "";
      return ((process.env.NODE_ENV === 'development') ? process.env.BASE_API : baseUrl) + '/api/device/query/snap/alarm/' + row.deviceId + '/' + row.channelId + '/' + row.alarmTime;
    },
    getBigSnap: function (row) {
      return [this.getSnap(row)]
    },
    // 这是一个方法，用于生成alarmImg的URL
    generateAlarmImgUrl(alarmTime) {
      // 提取时间部分并格式化
      const timePart = alarmTime.split(' ')[1].split(':').join('.'); // 假设alarmTime的格式总是"YYYY-MM-DD HH:mm:ss"
      // 构造完整的URL
      return `http://81.71.95.175/img/AB009D0PHA9E076/${alarmTime.split(' ')[0]}/pic_001/${timePart.split('.')[0]}/${timePart}[M][0@0][0][28800].jpg`;
      // 注意：这里我假设了日期和时间的分割方式，并且简单地将时间部分作为目录和文件名的一部分
      // 实际应用中，你可能需要根据实际的URL结构进行调整
    },
    showImageModal(imageUrl) {
      this.currentImageUrl = imageUrl;
      this.imageModalVisible = true;
    },
    handleClose() {
      this.imageModalVisible = false;
    },
    initData: function () {
      this.getAlarmList();
    },
    currentChange: function (val) {
      this.currentPage = val;
      this.getAlarmList();
    },
    handleSizeChange: function (val) {
      this.count = val;
      this.getAlarmList();
    },
    getAlarmList: function () {
      this.getAlarmListLoading = true;
      this.$axios({
        method: 'get',
        url: `/api/alarm/all`,
        params: {
          deviceId: this.deviceId,
          page: this.currentPage,
          count: this.count
        }
      }).then( (res)=> {
        if (res.data.code === 0) {
          this.total = res.data.data.total;
          this.alarmList = res.data.data.list;
        }
        this.getAlarmListLoading = false;
      }).catch( (error)=> {
        console.error(error);
        this.getAlarmListLoading = false;
      });
    },

  }
};
</script>

<style>
.videoList {
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.video-item {
  position: relative;
  width: 15rem;
  height: 10rem;
  margin-right: 1rem;
  background-color: #000000;
}

.video-item-img {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: 100%;
  height: 100%;
}

.video-item-img:after {
  content: "";
  display: inline-block;
  position: absolute;
  z-index: 2;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: 3rem;
  height: 3rem;
  background-image: url("../assets/loading.png");
  background-size: cover;
  background-color: #000000;
}

.video-item-title {
  position: absolute;
  bottom: 0;
  color: #000000;
  background-color: #ffffff;
  line-height: 1.5rem;
  padding: 0.3rem;
  width: 14.4rem;
}

.alarm-image {
  width: 100px; /* 或其他你希望的默认宽度 */
  height: auto;
  transition: transform 0.3s ease;
}

.transparent-dialog  {
   position: absolute;
}

.fullscreen-image {
  width: 100%;
  height: 100vh;
  object-fit: contain;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明遮罩 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

</style>
