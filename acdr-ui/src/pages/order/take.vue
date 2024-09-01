<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '宠托师接单页面',
  },
}
</route>

<template>
  <view class="main-page p-4">
    <!-- 订单列表 -->
    <view v-for="order in orders" :key="order.id">
      <order-item
        :id="order.id"
        :userId="order.userId"
        :reservationTime="order.reservationTime"
        :serviceHours="order.serviceHours"
        :personalServiceId="order.personalServiceId"
        :personalServiceUserId="order.personalServiceUserId"
        :price="order.price"
        :isPay="order.isPay"
        :feedback="order.feedback"
        :star="order.star"
        :state="order.state"
        :paymentMethod="order.paymentMethod"
        :qrcode="order.qrcode"
        :createTime="order.createTime"
        :updateTime="order.updateTime"
        :address="order.address"
        :pet="order.pet"
        :user="order.user"
        :psUser="order.psUser"
        :serviceInfo="order.serviceInfo"
        :isTake="true"
        @refresh="refresh"
      />
    </view>

    <!-- 无订单提示 -->
    <view
      v-if="orders.length === 0"
      class="flex flex-col items-center justify-center h-64 text-gray-400"
    >
      <image
        src="https://via.placeholder.com/150/FFFFFF/000000?text=No+Orders"
        class="w-32 h-32 mb-4"
      ></image>
      <text>还没有相关的订单呢</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { httpPost } from '@/utils/http'
import OrderItem from './components/OrderItem.vue'

const orders = ref([])

const refresh = async () => {
  await fetchOrders()
}

// 获取宠托师的订单信息
const fetchOrders = async () => {
  try {
    const response = await httpPost('/order/ptOrdersInfo')
    if (response.code === 200) {
      orders.value = response.records
    } else {
      console.error('订单数据获取失败', response.message)
    }
  } catch (error) {
    console.error('订单数据获取失败', error)
  }
}

// 页面加载时获取订单数据
onMounted(() => {
  fetchOrders()
})
</script>

<style lang="scss" scoped>
.main-page {
  @apply p-4;
}

.flex {
  @apply flex;
}

.text-gray-400 {
  @apply text-gray-400;
}

.h-64 {
  @apply h-64;
}

.w-32 {
  @apply w-32;
}

.mb-4 {
  @apply mb-4;
}
</style>
