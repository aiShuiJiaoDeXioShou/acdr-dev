<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '我的服务',
  },
}
</route>

<template>
  <view class="p-4">
    <view class="card mt-4" v-for="service in services" :key="service.id">
      <view class="flex justify-between items-center mb-4">
        <text class="text-xl">{{ service.serviceName }}</text>
        <view class="flex space-x-4">
          <button class="btn bg-blue-500 text-white" @click="editService(service)">编辑</button>
          <button class="btn bg-red-500 text-white" @click="deleteService(service.id)">删除</button>
        </view>
      </view>
      <text class="text-gray-500">{{ service.description }}</text>
      <text class="text-gray-500">价格: ¥{{ service.price }}</text>
      <text class="text-gray-500">地点: {{ service.address }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { httpPost } from '@/utils/http'

const services = ref([])

// 获取所有已发布的服务
const fetchServices = async () => {
  try {
    const response = await httpPost('/personal-service/list', {
      pageNo: 1,
      pageSize: 10,
    })
    if (response.code == 200) {
      services.value = response.records || []
    } else {
      uni.showToast({ title: '获取服务列表失败', icon: 'none' })
    }
  } catch (error) {
    uni.showToast({ title: '获取服务列表失败', icon: 'none' })
    console.error('Error fetching services:', error)
  }
}

// 编辑服务
const editService = (service) => {
  // 将服务数据保存到全局状态或使用查询参数传递到发布服务页面
  uni.navigateTo({
    url: `/pages/push/door?id=${service.id}`,
  })
}

// 删除服务
const deleteService = async (serviceId) => {
  try {
    const response = await httpPost('/personal-service/delete', { id: serviceId })
    if (response && response.data) {
      services.value = services.value.filter((service) => service.id !== serviceId)
      uni.showToast({ title: '服务已删除', icon: 'success' })
    } else {
      uni.showToast({ title: '删除服务失败', icon: 'none' })
    }
  } catch (error) {
    uni.showToast({ title: '删除服务失败', icon: 'none' })
    console.error('Error deleting service:', error)
  }
}

onMounted(() => {
  fetchServices()
})
</script>

<style lang="scss" scoped>
.card {
  padding: 1rem;
  margin-bottom: 1rem;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  text-align: center;
  border-radius: 0.5rem;
}

.bg-blue-500 {
  background-color: #4299e1;
}

.bg-red-500 {
  background-color: #f56565;
}

.text-white {
  color: white;
}

.text-gray-500 {
  color: #a0aec0;
}
</style>
