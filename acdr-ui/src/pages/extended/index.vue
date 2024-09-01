<route lang="json5">
{
  style: {
    navigationBarTitleText: '更多应用',
  },
}
</route>
<script lang="ts"></script>

<template>
  <view class="container">
    <view class="quick-services">
      <view class="quick-service-card">
        <image :src="imgUrl('@/static/home/book.png')" class="quick-service-image" />
        <text class="quick-service-text">宠物宝典</text>
      </view>
      <view class="quick-service-card">
        <image :src="imgUrl('@/static/home/yl.png')" class="quick-service-image" />
        <text class="quick-service-text">商城选择</text>
      </view>
      <view class="quick-service-card">
        <image :src="imgUrl('@/static/home/yl.png')" class="quick-service-image" />
        <text class="quick-service-text">宠物医疗</text>
      </view>
      <view class="quick-service-card">
        <image :src="imgUrl('@/static/home/mr.png')" class="quick-service-image" />
        <text class="quick-service-text">宠物美容</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { imgUrl } from '@/utils/commUtils';


</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  padding: 20px;
  .quick-services {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 100%;

    .quick-service-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .quick-service-image {
        width: 40px;
        height: 40px;
        margin-bottom: 5px;
      }
    }
  }
}
</style>
