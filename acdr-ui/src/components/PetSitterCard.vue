<template>
  <view @click="toServiceDetail" class="pet-sitter-card bg-white p-4 rounded-lg shadow flex">
    <!-- 左侧头像和认证标识 -->
    <view class="flex-shrink-0 mr-4 relative">
      <image :src="imgUrl(petSitter.url)" class="w-16 h-16 object-cover rounded-full"></image>
      <view class="absolute top-0 left-0 bg-[#ffc107] text-white rounded-full text-xs px-2 py-1">
        V 超赞
      </view>
    </view>

    <!-- 右侧内容 -->
    <view class="flex-1">
      <view class="flex justify-between items-center">
        <text class="text-lg font-bold">{{ petSitter.serviceName }}</text>
        <text class="text-red-500 text-lg">{{ petSitter.price }}￥/小时</text>
      </view>

      <view class="flex items-center mt-1">
        <view class="flex">
          <wd-icon v-for="n in 5" :key="n" name="star-on" size="20" class="text-[#ffc107]"></wd-icon>
        </view>
        <text class="ml-2 text-[#ffc107]">5星好评</text>
      </view>

      <text class="text-gray-600 mt-2">{{ petSitter.description }}</text>

      <view class="flex justify-between items-center mt-2">
        <view class="flex items-center">
          <text class="text-xs text-gray-400 mr-2">专业宠托师</text>
          <text class="text-xs text-gray-400">多次服务，信赖</text>
        </view>
        <text class="text-xs text-gray-400">专业宠托师</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { imgUrl } from '@/utils/commUtils'
import { defineProps } from 'vue'

const props = defineProps({
  petSitter: {
    type: Object,
    required: true,
  },
})

const toServiceDetail = () => {
  uni.navigateTo({
    url: '/pages/service/detail?id=' + props.petSitter.id,
  })
}
</script>

<style scoped>
.pet-sitter-card {
  @apply bg-white p-4 rounded-lg shadow flex;
}

.flex-shrink-0 {
  @apply flex-shrink-0 mr-4 relative;
}

.flex-1 {
  @apply flex-1;
}

.text-lg {
  @apply text-lg;
}

.font-bold {
  @apply font-bold;
}

.text-red-500 {
  @apply text-red-500;
}

.flex {
  @apply flex;
}

.text-[#ffc107] {
  @apply text-[#ffc107];
}

.ml-2 {
  @apply ml-2;
}

.text-gray-600 {
  @apply text-gray-600;
}

.mt-2 {
  @apply mt-2;
}

.text-xs {
  @apply text-xs;
}

.text-gray-400 {
  @apply text-gray-400;
}

.bg-[#ffc107] {
  @apply bg-[#ffc107];
}
</style>
