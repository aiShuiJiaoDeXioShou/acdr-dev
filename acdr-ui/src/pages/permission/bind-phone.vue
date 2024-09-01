<route lang="json5">
{
  style: {
    navigationBarTitleText: '手机号绑定',
  },
}
</route>

<template>
  <view class="bg-[#F5F5F5] h-full">
    <!-- 顶部背景和标题 -->
    <view class="w-full bg-pink-100 py-6 flex justify-center items-center">
      <text class="text-2xl text-white">输入新的手机号</text>
    </view>

    <!-- 手机号绑定部分 -->
    <view class="p-4">
      <view class="bg-white p-4 rounded-lg shadow mb-4 flex items-center">
        <wd-icon name="search" size="24" class="text-gray-400"></wd-icon>
        <input class="ml-4 flex-1 text-lg" placeholder="请输入手机号" v-model="phoneNumber" />
      </view>
      <button
        class="w-full bg-gray-300 text-white text-center rounded-full py-4 mt-4"
        @click="getVerificationCode"
      >
        获取验证码
      </button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const phoneNumber = ref('')

const getVerificationCode = () => {
  // 获取验证码逻辑
  if (!phoneNumber.value) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }
  uni.showToast({ title: '验证码已发送', icon: 'success' })
}
</script>

<style scoped>
/* 使用 UnoCSS 定义样式 */
.view {
  @apply bg-[#F5F5F5] h-full;
}

.button {
  @apply w-full bg-gray-300 text-white text-center rounded-full py-4 mt-4;
}
</style>
