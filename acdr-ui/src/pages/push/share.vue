<route lang="json5">
{
  style: {
    navigationBarTitleText: '发布页面',
  },
}
</route>

<template>
  <view class="flex flex-col h-full p-5 bg-[#f8f8f8]">
    <!-- 顶部导航栏 -->
    <view class="flex items-center py-2">
      <icon type="back" size="20" @click="goBack"></icon>
      <view class="ml-3 text-lg font-bold">发布笔记</view>
    </view>

    <!-- 图片上传区域 -->
    <view class="flex flex-nowrap overflow-x-scroll mt-4">
      <view
        class="w-25 h-25 bg-white border border-[#eaeaea] flex items-center justify-center mr-2"
        v-for="(image, index) in images"
        :key="index"
      >
        <image :src="image.url" mode="aspectFill" class="w-full h-full"></image>
      </view>
      <view
        class="w-25 h-25 bg-white border-dashed border-[#eaeaea] flex items-center justify-center mr-2 cursor-pointer"
        @click="chooseImage"
      >
        <wd-icon name="add" size="22px" color="#888"></wd-icon>
      </view>
    </view>

    <!-- 文本输入区域 -->
    <view class="mt-5">
      <input
        type="text"
        placeholder="填写标题会有更多赞哦~"
        class="w-full h-10 mt-2 p-2 border border-[#eaeaea] rounded-md"
        v-model="title"
      />
      <textarea
        placeholder="添加正文"
        class="w-full h-25 mt-2 p-2 border border-[#eaeaea] rounded-md"
        v-model="content"
      ></textarea>
    </view>

    <!-- 标签区域 -->
    <view class="flex flex-wrap mt-5">
      <view
        class="bg-[#f0f0f0] rounded-full px-3 py-1 m-1 text-sm"
        v-for="(tag, index) in tags"
        :key="index"
      >
        {{ tag }}
      </view>
    </view>

    <!-- 添加地点和公开可见选项 -->
    <view class="mt-5">
      <view class="flex justify-between items-center py-2 border-b border-[#eaeaea]">
        <text>添加地点</text>
        <input
          type="text"
          placeholder="请输入地点"
          class="text-sm text-[#888]"
          v-model="location"
        />
      </view>
      <view class="flex justify-between items-center py-2 border-b border-[#eaeaea]">
        <text>公开可见</text>
        <switch :checked="isPublic" @change="togglePublic"></switch>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="flex justify-center mt-5">
      <button
        class="w-full font-bold bg-[#FCD038] text-white rounded-md py-2 text-lg"
        @click="postNote"
      >
        发布笔记
      </button>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import { httpPost, httpUploadFile } from '@/utils/http'
import { useUserStore } from '@/store/user'

const images = ref([])
const title = ref('')
const content = ref('')
const location = ref('')
const isPublic = ref(true) // 公开可见的状态
const tags = ref(['#搞笑日常', '#快乐无限供应', '#生活随拍', '#日常碎片'])

const userStore = useUserStore()

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 选择图片
const chooseImage = () => {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      images.value.push({ url: res.tempFilePaths[0] })
    },
  })
}

// 切换公开状态
const togglePublic = (event) => {
  isPublic.value = event.detail.value
}

// 上传图片文件并获取URL
const uploadImage = async (filePath) => {
  try {
    const response = await httpUploadFile('/file/upload', filePath)
    if (response.code === 200) {
      return response.data.url
    } else {
      throw new Error('图片上传失败')
    }
  } catch (error) {
    uni.showToast({
      title: '图片上传失败，请稍后重试',
      icon: 'none',
    })
    throw error
  }
}

// 发布笔记
const postNote = async () => {
  if (!title.value || !content.value) {
    uni.showToast({
      icon: 'none',
      title: '请填写标题和内容',
    })
    return
  }

  try {
    // 1. 上传每张图片并获取其URL
    const uploadedImages = []
    for (const image of images.value) {
      const imageUrl = await uploadImage(image.url)
      uploadedImages.push(imageUrl)
    }

    // 2. 组织帖子数据并创建帖子
    const postData = {
      title: title.value,
      content: content.value,
      location: location.value,
      isPublic: isPublic.value,
      images: uploadedImages,
    }

    // 发送发布帖子请求
    const postResponse = await httpPost('/posts/create', postData)
    if (postResponse.code === 200) {
      uni.showToast({
        title: '发布成功',
        icon: 'success',
      })
      goBack()
    } else {
      uni.showToast({
        title: postResponse.message || '发布失败，请稍后再试',
        icon: 'none',
      })
    }
  } catch (error) {
    uni.showToast({
      title: '发布失败，请检查网络',
      icon: 'none',
    })
  }
}
</script>

<style scoped>
.border-dashed {
  border-style: dashed !important;
}
</style>
