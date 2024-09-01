<template>
  <view class="border-wrapper">
    <view class="info-text">
      <view class="line"></view>
      <text class="main-text">
        您的陪遛员
        <text class="highlight">{{ name }}</text>
        已出发
      </text>
      <text class="sub-text">
        距你
        <text class="highlight">{{ distance }}m</text>
        ，预计
        <text class="highlight">{{ time }}分钟</text>
        后到达~
      </text>
    </view>
    <view class="courier">
      <image :src="imgUrl('@/static/home/couriers.png')" class="delivery-image" />
      <div class="loading-dots">
        <div class="dot"></div>
        <div class="dot"></div>
        <div class="dot"></div>
      </div>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { imgUrl } from '@/utils/commUtils';
import { defineProps } from 'vue'

const props = defineProps<{
  name: string
  distance: number
  time: number
}>()
</script>

<style scoped>
.border-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 10px 5px 20px;
  background-color: #ff9775;
  border-radius: 15px;
}

.border-wrapper::before {
  left: -20px;
  border-right: none;
}

.border-wrapper::after {
  right: -20px;
  border-left: none;
}

.line {
  position: absolute;
  top: 50%;
  left: 0;
  width: 5px;
  height: 30px;
  margin: 0 10px;
  background-color: #fff;
  border-radius: 10px;
  transform: translateY(-50%);
}

.info-text {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  padding: 10px 0;
  padding-left: 15px;
  background-color: #fff;
  border-radius: 18px;
}

.main-text {
  font-size: 16px;
  color: #000;
}

.sub-text {
  font-size: 14px;
  color: #888;
}

.highlight {
  color: #ff7043;
}

.delivery-image {
  width: 50px;
  height: 50px;
  margin-left: 10px;
}

.courier {
  position: absolute;
  top: -15%;
  right: 12%;
  z-index: 999;
}

.loading-dots {
  position: absolute;
  right: -30px;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dot {
  width: 8px; /* 小点的大小 */
  height: 8px;
  margin: 0 2px;
  background-color: #ff7043; /* 小点的颜色 */
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.dot:nth-child(1) {
  animation-delay: -0.32s;
}
.dot:nth-child(2) {
  animation-delay: -0.16s;
}
.dot:nth-child(3) {
  animation-delay: 0s;
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}
</style>
