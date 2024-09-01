<route lang="json5" type="page">
{
  layout: "default",
  style: {
    navigationBarTitleText: "地图组件",
    // 隐藏该组件的头部;
    navigationStyle: "custom",
  },
}
</route>

<template>
  <view class="objView">
    <u-row class="myInfoRow">
      <u-col span="12" style="font-size: 17px">
        经度：{{ longitude }} &nbsp&nbsp 纬度：{{ latitude }}
      </u-col>
    </u-row>
    <u-row class="myInfoRow">
      <u-col span="12">
        <textarea
          v-show="geocode"
          disabled="true"
          :value="position"
          placeholder="未加载出位置信息"
        ></textarea>
      </u-col>
    </u-row>
    <u-row>
      <u-col span="6">
        <u-button type="success" shape="square" @click="changePosition"
          >刷新当前位置</u-button
        >
      </u-col>
      <u-col span="6">
        <u-button type="success" shape="square" @click="returnPosition"
          >返回定位位置</u-button
        >
      </u-col>
    </u-row>
    <br />
    <u-row>
      <!-- style内嵌标签的写法才能让app端地图全屏展示 -->
      <u-col span="12">
        <map
          id="myMap"
          ref="myMap"
          :longitude="longitude"
          :latitude="latitude"
          :scale="scale"
          @tap="clickmap"
          :markers="covers"
          style="width: 100%; height: 100vh"
        ></map>
      </u-col>
    </u-row>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";

const longitude = ref(0); // 记录实时点击位置
const latitude = ref(0);
const originalLongitude = ref(0); // 记录用户当前真实位置随时返回
const originalLatitude = ref(0);
const scale = ref("16");
const geocode = ref(true);
const position = ref(""); // 获取的地址信息
const originalPosition = ref(""); // 记录用户当前真实位置随时返回

const covers = reactive([
  {
    longitude: longitude.value,
    latitude: latitude.value,
    iconPath: "/static/map/self.png",
    width: 30,
    height: 30,
    label: {
      content: "当前位置",
      textAlign: "center",
      color: "#FB3109",
    },
  },
]);

const clickmap = () => {
  uni.chooseLocation({
    success: (res) => {
      position.value = "位置名称:" + res.name + "  详细地址:" + res.address;
      longitude.value = res.longitude;
      latitude.value = res.latitude;
      covers[0].longitude = res.longitude;
      covers[0].latitude = res.latitude;
    },
    fail: (err) => {
      uni.showToast({
        title: "获取位置失败",
      });
    },
  });
};

const returnPosition = () => {
  position.value = originalPosition.value;
  longitude.value = originalLongitude.value;
  latitude.value = originalLatitude.value;
  covers[0].longitude = originalLongitude.value;
  covers[0].latitude = originalLatitude.value;
};

const changePosition = () => {
  console.log("坐标刷新成功");
  uni.showToast({
    title: "坐标刷新成功",
  });
};

const getLocation = () => {
  uni.showLoading({
    title: "正在获取定位",
  });
  uni.getLocation({
    type: "gcj02",
    timeout: 1000,
    geocode: geocode.value,
    success: (res) => {
      console.log("@@@@", res);
      uni.hideLoading();
      longitude.value = res.longitude;
      latitude.value = res.latitude;
      originalLongitude.value = res.longitude;
      originalLatitude.value = res.latitude;
      covers[0].longitude = res.longitude;
      covers[0].latitude = res.latitude;

      const { address } = res;
      const { country, province, city, district, street, streetNum, poiName } = address;
      const myPosition = `${country}-${province}-${city}-${district}-${street}-${streetNum}-${poiName}`;
      position.value = myPosition;
      originalPosition.value = myPosition;
    },
    fail: (err) => {
      console.log(err);
      uni.hideLoading();
      uni.showModal({
        title: "提示",
        content: "位置信息获取失败（请确定定位功能是否打开）",
        showCancel: false,
      });
    },
  });
};

onMounted(() => {
  getLocation();
});
</script>

<style lang="scss" scoped>
#myMap {
  width: 750rpx;
}
/* 信息栏高度 */
.myInfoRow {
  height: 50px;
}
/* 让地址栏通栏展示 */
textarea {
  width: 100%;
}
</style>
