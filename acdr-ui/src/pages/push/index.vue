<route lang="json5">
{
  style: {
    navigationBarTitleText: "发布与分享",
    navigationStyle: "custom",
  },
}
</route>
<template>
  <image
    class="background"
    :src="imgUrl('@/static/push/bg.png')"
    mode="aspectFill"
  ></image>
  <view class="container h-[100vh]">
    <scroll-view class="flex mt-2 space-x-4 h-[100%] p-t[100%]" scroll-x="true">
      <view
        @click="handleService('door')"
        class="flex flex-col items-center w-full relative"
      >
        <image
          class="bg-image absolute inset-0 w-full h-[100px] rounded-lg"
          :src="imgUrl('@/static/push/pushshop.png')"
          mode="widthFix"
        ></image>
        <view
          class="h-12 rounded-lg flex items-center justify-center text-black relative z-10 font-500 text-size-[25px]"
        >
          发布上门服务
        </view>
        <view
          class="h-2 rounded-lg flex items-center justify-center text-black relative z-10 text-size-[15px]"
          >发布我的专属上门服务</view
        >
      </view>

      <view
        @click="handleService('shop')"
        class="flex flex-col items-center w-full mt-[15%] relative"
      >
        <image
          class="bg-image absolute inset-0 w-full h-[95px] rounded-lg"
          :src="imgUrl('@/static/push/pushdoor.png')"
          mode="widthFix"
        ></image>
        <view
          class="h-12 rounded-lg flex items-center justify-center text-black relative z-10 font-500 text-size-[25px]"
        >
          发布商品服务
        </view>
        <view
          class="h-2 rounded-lg flex items-center justify-center text-black relative z-10 text-size-[15px]"
          >发布我的专属商品服务</view
        >
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { imgUrl } from "@/utils/commUtils";

const handleService = (type) => {
  if (type === "door") {
    toPath("/pages/push/door");
  } else if (type === "shop") {
    toPath("/pages/push/goods");
  }
};
const toPath = (path) => {
  uni.navigateTo({
    url: path,
  });
};
</script>

<style>
.background {
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  width: 100%;
  height: 100%;
  background-color: #facc15;
}
/* 在这里添加任何自定义的样式 */
.container {
  padding: 16px;
}

button {
  display: flex;
  align-items: center;
  justify-content: center;
  /* 调整字体大小以适应卡片 */
  font-size: 14px;
  text-align: center;
  /* 使按钮的文字不换行 */
  white-space: nowrap;
}
</style>
