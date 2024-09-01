<route lang="json5">
{
  style: {
    navigationBarTitleText: 'AI互动空间',
  },
}
</route>

<template>
  <view class="h-full bg-gray-100">
    <!-- 主内容 -->
    <scroll-view class="flex-1 p-5">
      <view class="bg-white p-5 rounded-lg shadow mb-4">
        <text class="text-gray-800 text-xl mb-2">宠物问问</text>
        <view class="bg-gray-100 p-4 rounded-lg mb-4">
          <text class="text-gray-600">猫不肯喝水是怎么回事？</text>
          <button class="bg-[#ffc107] text-white rounded-full mt-4 px-4 py-2">提问</button>
        </view>
        <view class="flex items-center justify-center">
          <image
            src="https://via.placeholder.com/300x150"
            class="w-full h-40 object-cover rounded-lg mb-4"
          ></image>
        </view>
      </view>

      <view class="bg-white p-5 rounded-lg shadow mb-4">
        <text class="text-gray-800 text-xl mb-2">AI图像识别</text>
        <view class="bg-[#ffc107] text-white p-4 rounded-lg mb-4 text-center">
          深度学习算法和海量宠物数据训练而成
        </view>
        <view class="grid grid-cols-2 gap-4">
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="smile" size="24" />
            <text class="mt-2">表情分析</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="cat" size="24" />
            <text class="mt-2">品种识别</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="tooth" size="24" />
            <text class="mt-2">口腔评估</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="poop" size="24" />
            <text class="mt-2">排泄物</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="vomit" size="24" />
            <text class="mt-2">呕吐物</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="skin" size="24" />
            <text class="mt-2">皮肤检测</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="ear" size="24" />
            <text class="mt-2">耳道检查</text>
          </view>
          <view class="bg-gray-100 p-4 rounded-lg flex flex-col items-center">
            <wd-icon name="eye" size="24" />
            <text class="mt-2">眼睛检查</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 底部拍照按钮 -->
    <view class="absolute bottom-0 w-full flex justify-center py-4 bg-white shadow-md">
      <button
        class="w-16 h-16 bg-[#ffc107] text-white text-center rounded-full text-lg flex items-center justify-center shadow-lg"
        @click="takePhoto"
      >
        <wd-icon name="camera" size="24" />
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const goBack = () => {
  uni.navigateBack()
}

const takePhoto = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['original', 'compressed'],
    sourceType: ['camera'],
    success: (res) => {
      console.log('Photo taken:', res.tempFilePaths[0])
      uni.showToast({
        title: '拍照成功',
        icon: 'success',
      })
    },
    fail: (err) => {
      console.error('Error taking photo:', err)
      uni.showToast({
        title: '拍照失败',
        icon: 'none',
      })
    },
  })
}
</script>

<style scoped>
/* 使用 UnoCSS 定义样式 */
button {
  @apply px-2 py-1 rounded text-sm;
}
button.primary {
  @apply px-2 py-1 bg-[#ffc107] text-white rounded text-sm;
}
</style>
