<route lang="json5">
{
  style: {
    navigationBarTitleText: '宠物详情',
    "navigationStyle": "custom"
  },
}
</route>

<template>
  <TopBar/>
  <image
    :src="imgUrl('@/static/push/bg.png')"
    class="object-cover absolute top-[-80px] left-0 w-full z-[-1]"
    mode="widthFix"
  ></image>
  <view class="w-full h-40"></view>
  <view class="w-full bg-[#ffff]">
    <!-- 顶部背景和头像 -->
    <view class="relative">
      <view class="absolute left-4 bottom-[-20px] z-10">
        <image
          :src="imgUrl('@/static/icons/cat.png')"
          class="w-20 h-20 object-cover rounded-full border-4 border-white"
        ></image>
      </view>
      <!-- 圆角遮罩 -->
      <view
        class="absolute bottom-[-5px] left-0 w-full h-6 bg-white rounded-tl-xl rounded-tr-xl z-0"
      ></view>
    </view>

    <!-- 数据统计 -->
    <view class="flex justify-around bg-white pt-4 rounded-lg">
      <view class="text-center">
        <text class="text-lg">0</text>
        <text class="block text-gray-600">粉丝</text>
      </view>
      <view class="text-center">
        <text class="text-lg">0</text>
        <text class="block text-gray-600">获猫币</text>
      </view>
      <view class="text-center">
        <text class="text-lg">0</text>
        <text class="block text-gray-600">获得赞</text>
      </view>
    </view>

    <view class="pl-4 pr-4">
      <!-- 宠物信息 -->
      <view class="bg-white p-4 rounded-lg">
        <text class="text-xl">cat</text>
        <view class="flex items-center mt-2">
          <wd-icon name="calendar" size="20" class="text-[#ffc107]"></wd-icon>
          <text class="ml-2 text-gray-600">距离生日还有12天哦</text>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="flex justify-between py-4">
        <button
          class="bg-[#F0985A] text-white rounded-lg py-2 px-6 h-9 flex-grow-[8] flex items-center justify-center"
        >
          修改头像
        </button>
        <button
          class="bg-[#ffc107] text-white rounded-lg py-2 px-6 h-9 flex-grow-[2] flex items-center justify-center ml-4"
        >
          分享
        </button>
      </view>

      <!-- 标签栏 -->
      <view class="flex justify-around bg-white py-2 rounded-lg">
        <view
          v-for="tab in tabs"
          :key="tab"
          :class="[
            'text-lg px-4',
            activeTab === tab
              ? 'text-[#ffc107] relative border-b-2 border-[#ffc107]'
              : 'text-gray-600',
          ]"
          @click="activeTab = tab"
        >
          {{ tab }}
          <view v-if="activeTab === tab" class="active-underline"></view>
        </view>
      </view>

      <!-- 内容提示 -->
      <EmptyState type="default" message="暂无内容" />
    </view>
  </view>
</template>

<script setup>
import EmptyState from '@/components/EmptyState.vue'
import TopBar from '@/components/TopBar.vue'
import { imgUrl } from '@/utils/commUtils'
import { ref } from 'vue'


const tabs = ref(['全部', '心情', '养护', '清洁'])
const activeTab = ref('全部')

const goBack = () => {
  uni.navigateBack()
}
</script>

<style scoped>
/* 使用 UnoCSS 定义样式 */
.view {
  @apply bg-[#F5F5F5] h-full;
}

.image {
  @apply w-full h-40 object-cover;
}

.button {
  @apply bg-[#ffc107] text-white rounded-lg py-2 px-6 h-12;
}

.text-gray-600 {
  @apply text-gray-600;
}

.text-gray-400 {
  @apply text-gray-400;
}

.border-b-2 {
  border-bottom-width: 2px;
}

.active-underline {
  @apply w-6 h-[0.20rem] bg-yellow-400 mt-1 absolute bottom-[-2px] left-[1.45rem] transform;
}
</style>
