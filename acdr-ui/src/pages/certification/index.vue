<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '宠托师认证页面',
    navigationStyle: 'custom',
  },
}
</route>

<template>
  <!-- 返回按钮 -->
  <TopBar />
  <image
    class="absolute w-full h-full"
    :src="imgUrl('@/static/certification/certification_bg.png')"
    mode="aspectFill"
  ></image>
  <view class="flex justify-center items-center h-screen bg-gray-100 pt-10">
    <view class="text-center flex gap-5 flex-col" v-if="!user.userInfo.isPetNursery">
      <text class="text-xl text-gray-700 mb-4">您还未认证，加入我们成为宠托师！</text>
      <image
        class="w-full h-[90px]"
        :src="imgUrl('@/static/certification/wy.png')"
        mode="scaleToFill"
        @click="goToApplication"
      />
      <image
        class="w-full h-[90px]"
        :src="imgUrl('@/static/certification/wyl.png')"
        mode="scaleToFill"
        @click="goToApplication"
      />
      <image
        class="w-full h-[90px]"
        :src="imgUrl('@/static/certification/mr.png')"
        mode="scaleToFill"
        @click="goToApplication"
      />
    </view>

    <view class="text-center bg-white rounded absolute z-1 w-[80vw] h-[80vh] top-2/6" v-else>
      <image
        class="absolute inset-0 w-full h-full object-cover z-[-1]"
        :src="imgUrl('@/static/certification/cert.png')"
        mode="widthFix"
      />
      <text
        class="text-[20px] mb-4 absolute top-[24%] left-[50%] text-[#5D392C] font-700 transform translate-x-[-50%] translate-y-[-50%]"
      >
        您的宠托师证书
      </text>
      <text class="absolute top-[32%] left-[50%] translate-x-[-50%]">证书详情</text>
      <view class="flex flex-col items-start z-2 absolute top-[38%] left-[21%] text-[#7E5B2E]">
        <text class="mb-2">证书编号: {{ certificate.card_id }}</text>
        <text class="mb-2">证书类型: {{ certificate.type }}</text>
        <text class="mb-2">服务次数: {{ certificate.service_number }}</text>
        <text class="mb-2">创建时间: {{ formatDate(certificate.create_time) }}</text>
        <text class="mb-2">过期时间: {{ formatDate(certificate.expired_time) }}</text>
        <text class="mb-2">最后更新: {{ formatDate(certificate.update_time) }}</text>
      </view>
    </view>
  </view>
  <wd-popup :close-on-click-modal="false" v-model="show" position="center" :style="{ 'width': '100%' }">
    <CertPopup @handleJoin="handleJoin" @handleReturn="handleReturn" />
  </wd-popup>

</template>

<script setup>
import TopBar from '@/components/TopBar.vue'
import { useUserStore } from '@/store'
import { httpGet } from '@/utils/http'
import { ref } from 'vue'
import CertPopup from './components/certPopup.vue'
import { imgUrl } from '@/utils/commUtils'

const user = useUserStore()
let certificate = ref({}) // 证书信息
const show = ref(!user.userInfo.isPetNursery)

const handleReturn = () => {
  show.value = false
  uni.navigateBack()
}

const handleJoin = () => {
  show.value = false
}

// 获取认证状态和证书信息
const getCertificationStatus = async () => {
  certificate.value = await httpGet('/petInfo/getExpertInfo')
}

// 格式化日期
const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString()
}

// 页面加载时获取认证状态和证书信息
onLoad(async () => {
  await getCertificationStatus()
})

// 跳转到申请页面
const goToApplication = () => {
  uni.navigateTo({
    url: '/pages/certification/pet-sitter',
  })
}
</script>

<style scoped>
/* 证书样式 */
.flex {
  display: flex;
}

.flex-col {
  flex-direction: column;
}

.items-start {
  align-items: flex-start;
}

.p-4 {
  padding: 1rem;
}

.bg-white {
  background-color: white;
}

.rounded {
  border-radius: 0.5rem;
}

.shadow {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.mb-2 {
  margin-bottom: 0.5rem;
}
</style>
