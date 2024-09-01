<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '宠托师资格证申请页面',
    // 设置为白色
    navigationBarBackgroundColor: '#ffffff', // 设置导航栏背景颜色为白色
  },
}
</route>

<template>
  <view class="bg-gray-100">
    <view class="h-full w-full bg-white p-6">
      <view class="mb-4 flex items-center justify-between">
        <label for="type" class="block">申请职位</label>
        <picker
          mode="selector"
          :range="positionOptions"
          :value="application.typeIndex"
          @change="pickerTypeChange"
        >
          <view class="w-full pl-3 border rounded py-2 w-full">
            {{ positionOptions[application.typeIndex] || '请选择职位' }}
          </view>
        </picker>
      </view>

      <view class="mb-4">
        <label for="content" class="block mb-2">审核内容</label>
        <textarea
          v-model="application.content"
          id="content"
          placeholder="请输入审核内容"
          class="w-full p-3 border rounded bg-gray-100"
          rows="5"
        ></textarea>
      </view>

      <view class="mb-4">
        <label for="image_url" class="block mb-2">审核材料</label>
        <view class="flex flex-wrap gap-4">
          <view v-for="(image, index) in application.imageUrls" :key="index" class="w-20 h-20">
            <image :src="image" class="w-full h-full object-cover rounded" />
          </view>
          <button
            v-if="application.imageUrls.length < 6"
            class="text-size-3xl ml-0 w-20 h-20 bg-gray-200 flex justify-center items-center rounded"
            @click="chooseImages"
          >
            +
          </button>
        </view>
      </view>

      <button
        class="w-full py-3 bg-[#FCCB30] text-black text-lg rounded-full flex items-center justify-center"
        @click="submitApplication"
      >
        提交申请
      </button>
    </view>
  </view>
</template>


<script setup>
import { ref } from 'vue'
import { httpPost, httpPostMultipart, httpUploadMultipleFiles } from '@/utils/http' // 使用httpPostMultipart方法

const positionOptions = [
  '宠托师',
  '宠物营养师',
  '宠物陪伴师',
  '宠物心理健康指导员',
  '宠物达人',
  '其他宠物师',
]

const application = ref({
  typeIndex: 0, // 存储选择的职位索引
  content: '',
  imageUrls: [], // 存储图片文件路径
})

const pickerTypeChange = (e) => {
  application.value.typeIndex = e.detail.value
}

const chooseImages = () => {
  uni.chooseImage({
    count: 6 - application.value.imageUrls.length, // 最多选择的图片数量
    success: (res) => {
      application.value.imageUrls.push(...res.tempFilePaths)
    },
  })
}

const submitApplication = async () => {
  try {
    // 将本地文件上传到服务器上
    application.value.imageUrls = await httpUploadMultipleFiles(
      '/file/upload',
      application.value.imageUrls,
    )
    application.value.type = positionOptions[application.value.typeIndex]

    // 上传文件和表单数据
    const response = await httpPost('/petInfo/apply', application.value)
    console.log(response)

    if (response.code == 200) {
      uni.showToast({ title: '申请已提交', icon: 'none' })
      application.value = {
        typeIndex: 0,
        content: '',
        imageUrls: [],
      }
    } else {
      uni.showToast({ title: '提交失败 ' + response.message, icon: 'none' })
    }
  } catch (err) {
    uni.showToast({ title: '提交失败', icon: 'none' })
  }
}
</script>

<style scoped>
/* 调整图片容器和布局 */
.flex {
  display: flex;
}

.flex-wrap {
  flex-wrap: wrap;
}

.gap-4 {
  gap: 1rem;
}

.w-20 {
  width: 5rem;
}

.h-20 {
  height: 5rem;
}
</style>
