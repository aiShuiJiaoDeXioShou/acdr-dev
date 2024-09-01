<template>
  <view class="carousel-container">
    <swiper class="carousel" autoplay="true" interval="3000" @change="onSwiperChange">
      <swiper-item>
        <view class="quick-services">
          <view class="quick-service-card">
            <image :src="imgUrl('@/static/home/book.png')" class="quick-service-image" />
            <text class="quick-service-text">宠物宝典</text>
          </view>
          <view class="quick-service-card">
            <image :src="imgUrl('@/static/home/pb.png')" class="quick-service-image" />
            <text class="quick-service-text">宠物陪伴</text>
          </view>
          <view class="quick-service-card" @click="toPath('/pages/mall/index')">
            <image :src="imgUrl('@/static/home/yl.png')" class="quick-service-image" />
            <text class="quick-service-text">定制商城</text>
          </view>
          <view class="quick-service-card">
            <image :src="imgUrl('@/static/home/mr.png')" class="quick-service-image" />
            <text class="quick-service-text">宠物美容</text>
          </view>
        </view>
      </swiper-item>
      <!-- <swiper-item>
        <view class="quick-services">
          <view class="quick-service-card">
            <image src="@/static/home/book.png" class="quick-service-image" />
            <text class="quick-service-text">宠物宝典2</text>
          </view>
          <view class="quick-service-card">
            <image src="@/static/home/陪伴.png" class="quick-service-image" />
            <text class="quick-service-text">宠物陪伴2</text>
          </view>
          <view class="quick-service-card">
            <image src="@/static/home/医疗.png" class="quick-service-image" />
            <text class="quick-service-text">宠物医疗2</text>
          </view>
          <view class="quick-service-card">
            <image src="@/static/home/美容.png" class="quick-service-image" />
            <text class="quick-service-text">宠物美容2</text>
          </view>
        </view>
      </swiper-item> -->
    </swiper>
    <view class="custom-indicators">
      <view
        v-for="(item, index) in 2"
        :key="index"
        :class="['indicator', { active: currentIndex === index }]"
      ></view>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import {imgUrl, toPath} from '@/utils/commUtils'

const currentIndex = ref(0)


const onSwiperChange = (event) => {
  currentIndex.value = event.detail.current
}
</script>

<style scoped>
.carousel-container {
  position: relative;
  width: 100%;
  height: 12vh;
  margin-top: 20px;
  background-color: #ffffff;
  border-radius: 10px;
}

.carousel {
  width: 100%;
}

.quick-services {
  display: flex;
  justify-content: space-around;
  padding: 10px;
}

.quick-service-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 25%;
  padding: 10px;
}

.quick-service-image {
  width: 40px;
  height: 40px;
}

.quick-service-text {
  margin-top: 5px;
  font-size: 14px;
  text-align: center;
}

.custom-indicators {
  position: absolute;
  bottom: 5%;
  left: 50%;
  display: flex;
  justify-content: center;
  transform: translate(-50%);
}

.indicator {
  width: 10px;
  height: 5px;
  margin: 0 1px;
  background-color: #cccccc;
  border-radius: 5px;
  transition: width 0.3s;
}

.indicator.active {
  width: 18px;
  background-color: #ffc107;
}
</style>
