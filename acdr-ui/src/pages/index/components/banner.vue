<template>
  <view class="carousel-container">
    <swiper class="carousel" autoplay="true" interval="3000" @change="onSwiperChange">
      <swiper-item>
        <view class="banner-content">
          <image :src="imgUrl('@/static/home/banner.png')" class="banner-image" mod="widthFix" />
        </view>
      </swiper-item>
      <swiper-item>
        <view class="banner-content">
          <image :src="imgUrl('@/static/home/banner.png')" class="banner-image" mod="widthFix" />
        </view>
      </swiper-item>
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

<script setup>
import { imgUrl } from '@/utils/commUtils'
import { ref } from 'vue'

const currentIndex = ref(0)

const onSwiperChange = (event) => {
  currentIndex.value = event.detail.current
}
</script>

<style scoped>
.carousel-container {
  position: relative;
  width: 100vw;
  height: 30vh;
  border-radius: 10px 10px 0 0;
  background-color: #ff8261;
}

.carousel {
  width: 100%;
  height: 100%;
}

.banner-content {
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.banner-image {
  width: 100%;
  object-fit: cover;
}

.custom-indicators {
  position: absolute;
  bottom: 20%;
  left: 5%;
  display: flex;
  justify-content: center;
}

.indicator {
  width: 10px;
  height: 5px;
  margin: 0 2px;
  background-color: #cccccc;
  border-radius: 5px;
  transition: width 0.3s;
}

.indicator.active {
  width: 18px;
  background-color: #ffc107;
}
</style>
