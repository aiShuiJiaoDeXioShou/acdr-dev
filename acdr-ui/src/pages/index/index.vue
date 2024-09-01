<route lang="json5">
{
  style: {
    navigationBarTitleText: "首页",
    navigationStyle: "custom",
  },
}
</route>

<template>
  <view class="container">
    <Banner />
    <loading-animation v-model="isLoading" />
    <view class="index">
      <view class="profileRoot" @click="toPath('/pages/pet/pet-detail-page')">
        <ProfileInfo
          profileIcon="@/static/icons/cat.png"
          profileName="萌萌"
          :gender="0"
          :profileTags="['柴犬', '妹妹', '已绝育']"
        />
      </view>
      <hurry name="萌萌哒" :distance="25" :time="45" />
      <view class="services">
        <view class="service-card large-card" @click="toPath('/pages/service/search')">
          <image
            :src="imgUrl('@/static/home/cwpb.png')"
            class="service-image"
            mode="widthFix"
          />
        </view>
        <view class="right-column">
          <view
            class="service-card small-card"
            @click="toPath('/pages/service/slippery')"
          >
            <image
              :src="imgUrl('@/static/home/cwpl.png')"
              class="service-image"
              mode="widthFix"
            />
          </view>
          <view class="service-card small-card" @click="toPath('/pages/service/door')">
            <image
              :src="imgUrl('@/static/home/smfw.png')"
              class="service-image"
              mode="widthFix"
            />
          </view>
        </view>
      </view>

      <view class="quick">
        <view class="head">
          <view class="quick-title">
            <text>快捷服务</text>
          </view>
          <view @click="toExtended" class="quick-more">
            <text>查看全部</text>
            <wd-icon name="arrow-right"></wd-icon>
          </view>
        </view>

        <QuickServiceCarousel />
      </view>

      <RecommendedServices />
    </view>
  </view>
</template>

<script setup>
import ProfileInfo from "./components/profileInfo.vue";
import hurry from "./components/hurry.vue";
import QuickServiceCarousel from "./components/quickServiceCarousel.vue";
import RecommendedServices from "./components/recommendedServices.vue";
import Banner from "./components/banner.vue";
import LoadingAnimation from "@/components/LoadingAnimation.vue";
import { imgUrl } from "@/utils/commUtils";

let isLoading = ref(false);

// // 暂停两秒
// setTimeout(() => {
//   isLoading.value = false
// }, 2000)

const toExtended = () => {
  // 跳转到extended 页面
  uni.navigateTo({
    url: "/pages/extended/index",
  });
};

const toPath = (path) => {
  uni.navigateTo({
    url: path,
  });
};
</script>

<style lang="scss" scoped>
page {
  background-color: #f9f9f9;
}
.profileRoot {
  z-index: 999;
  margin-top: -30px;
}

.container {
  z-index: -999;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
}

.index {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 90%;
  height: 100%;
}

.order-status {
  width: 100%;
  padding: 10px;
  background-color: #fff3e0;
}

.status-text {
  font-size: 16px;
  color: #ff7043;
}

.services {
  box-sizing: border-box;
  display: flex;
  /* 设置子组件之间的宽度 */
  gap: 15px;
  justify-content: space-between;
  width: 100%;
  height: 22vh;
  background-color: #ffffff;
}

.service-card {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 10px;
}

.large-card {
  width: 50%;
}

.small-card {
  width: 100%;
  margin-bottom: 10px;
}

.right-column {
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 50%;
}

.service-image {
  width: 100%;
  height: 100%;
  border-radius: 10px;
}

.head {
  display: flex;
  justify-content: space-between;
}

.quick-title {
  font-weight: 600;
}

.quick-more {
  color: #a2a2a2;
}
</style>
