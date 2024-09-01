<route lang="json5">
{
  style: {
    navigationBarTitleText: '宠屋微信公众号',
  },
}
</route>

<template>
  <view class="bg-white h-full">
    <!-- 顶部标题 -->
    <view class="flex items-center px-4 py-2 border-b border-gray-200">
      <wd-icon name="left" size="20" @click="goBack"></wd-icon>
      <text class="ml-3 text-lg font-bold">宠屋微信公众号</text>
    </view>

    <!-- 宣传图片 -->
    <view class="flex justify-center my-5">
      <image
        src="https://via.placeholder.com/300x150"
        class="w-3/4 h-auto object-cover rounded-lg shadow-md"
      ></image>
    </view>

    <!-- 宣传内容 -->
    <view class="px-4">
      <text class="text-center text-lg text-gray-800 mb-4">长按识别右侧二维码</text>
      <view class="bg-[#FFC0CB] p-4 rounded-lg shadow-md text-center">
        <text class="text-lg font-bold mb-2">你将获得</text>
        <view class="grid grid-cols-2 gap-4 text-left">
          <view class="flex items-center">
            <wd-icon name="newspaper" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">宠物行业一手资讯</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="bell" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">官方活动重要通知</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="discount" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">享受平台下单优惠</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="book" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">养猫常见问题科普</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="medal" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">宠托师课程及培训</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="gift" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">文末留言互动有礼</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="strategy" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">养护技巧攻略分享</text>
          </view>
          <view class="flex items-center">
            <wd-icon name="envelope" class="text-[#F56C6C]"></wd-icon>
            <text class="ml-2">每月定期宠粉福利</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
const goBack = () => {
  uni.navigateBack()
}
</script>

<style scoped>
/* 使用 UnoCSS 定义样式 */
.grid {
  @apply grid grid-cols-2 gap-4;
}
.view {
  @apply bg-white h-full;
}
.button {
  @apply w-full bg-[#FFC0CB] text-white text-center rounded-full py-4 mt-4;
}
</style>
