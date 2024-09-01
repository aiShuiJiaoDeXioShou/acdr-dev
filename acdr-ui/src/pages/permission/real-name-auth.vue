<route lang="json5">
{
  style: {
    navigationBarTitleText: '实名认证',
  },
}
</route>

<template>
  <view>
    <view class="bg-[#F5F5F5] h-full" v-if="!user.userInfo.isRealName">
      <!-- 顶部图片和标题 -->
      <view class="w-full bg-blue-100 py-6 flex justify-center items-center">
        <image
          src="https://via.placeholder.com/300x150"
          class="w-2/3 h-40 object-cover rounded-lg"
        ></image>
      </view>

      <!-- 实名认证部分 -->
      <view class="p-4">
        <text class="text-lg text-gray-800 mb-2">请进行身份证实名认证</text>

        <view class="bg-white p-4 rounded-lg shadow mb-4 flex items-center justify-between">
          <view class="flex items-center">
            <image :src="frontImage" class="w-24 h-16 object-cover rounded"></image>
            <text class="ml-4">身份证人像面</text>
          </view>
          <button class="bg-[#ffc107] text-white rounded-full px-4 py-2" @click="chooseFrontImage">
            <wd-icon name="add" />
          </button>
        </view>

        <view class="bg-white p-4 rounded-lg shadow mb-4 flex items-center justify-between">
          <view class="flex items-center">
            <image :src="backImage" class="w-24 h-16 object-cover rounded"></image>
            <text class="ml-4">身份证国徽面</text>
          </view>
          <button class="bg-[#ffc107] text-white rounded-full px-4 py-2" @click="chooseBackImage">
            <wd-icon name="add" />
          </button>
        </view>

        <button
          class="w-full bg-pink-500 text-white text-center rounded-full py-4 mt-4"
          @click="submitAuth"
        >
          提交认证材料
        </button>
      </view>
    </view>
    <view class="bg-[#F5F5F5] h-full" v-else>您已经完成了实名认证！</view>
  </view>
</template>

<script setup lang="js">
import { ref } from 'vue'
import { httpUploadFile } from '@/utils/http'
import { useUserStore } from '@/store'

const frontImage = ref('https://via.placeholder.com/100')
const backImage = ref('https://via.placeholder.com/100')
const frontImagePath = ref(null)
const backImagePath = ref(null)

const user = useUserStore()

// 选择身份证人像面图片
const chooseFrontImage = () => {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      frontImagePath.value = res.tempFilePaths[0]
      frontImage.value = frontImagePath.value // 更新图片显示
    },
  })
}

// 选择身份证国徽面图片
const chooseBackImage = () => {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      backImagePath.value = res.tempFilePaths[0]
      backImage.value = backImagePath.value // 更新图片显示
    },
  })
}

// 提交认证逻辑
const submitAuth = async () => {
  // 提交的文件不能为空
  if (!frontImagePath.value) {
    uni.showToast({ title: '请选择身份证人像面图片', icon: 'none' })
    return
  }
  if (!backImagePath.value) {
    uni.showToast({ title: '请选择身份证国徽面图片', icon: 'none' })
    return
  }

  try {
    uni.showToast({ title: '提交认证中', icon: 'loading' })

    if (frontImagePath.value) {
      const uploadRes = await httpUploadFile('/auth', frontImagePath.value, 'file', { type: 'front' })
      console.log(uploadRes)
      user.setUserInfo({ isRealName: true })
    }

    // if (backImagePath.value) {
    //   await httpUploadFile('/auth', backImagePath.value, 'file', { type: 'back' })
    // }

    uni.showToast({ title: '提交认证成功', icon: 'none' })
  } catch (error) {
    console.error(error)
    if (error.statusCode == 413) {
      uni.showToast({ title: '图片大小不能超过 1MB', icon: 'none' })
      return
    }
    uni.showToast({ title: '提交认证失败', icon: 'none' })
  }
}
</script>

<style scoped>
/* 使用 UnoCSS 定义样式 */
.view {
  @apply bg-[#F5F5F5] h-full;
}

.button {
  @apply w-full bg-pink-500 text-white text-center rounded-full py-4 mt-4;
}
</style>
