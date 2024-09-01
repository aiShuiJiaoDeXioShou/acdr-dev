<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '服务订单',
    navigationBarBackgroundColor: '#ffff',
  },
}
</route>
<template>
  <view class="main-page">

    <!-- Tab 切换 -->
    <view class="flex justify-between text-xs items-center pb-2 mb-4 bg-[#ffff]">
      <view
        v-for="tab in tabs"
        :key="tab"
        :class="[
          'tab',
          {
            'text-yellow-500': activeTab === tab,
          },
        ]"
        @click="changeTab(tab)"
      >
        {{ tab }}
        <view v-if="activeTab === tab" class="active-underline"></view>
      </view>
    </view>

    <view class="h-0.5 w-11/12 bg-gray-100 my-4 mx-auto"></view>

    <!-- 订单列表 -->
    <view class="p-5">
      <view v-for="order in orders" :key="order.id">
        <order-item
          @click="fetchOrders"
          :key="order.id"
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
        />
      </view>
    </view>

    <!-- 无订单提示 -->
    <view
      v-if="orders.length === 0"
      class="flex flex-col items-center justify-center h-64 text-gray-400"
    >
      <EmptyState></EmptyState>
    </view>

  </view>
</template>

<script setup>
import { ref } from 'vue'
import OrderItem from './components/OrderItem.vue'
import { httpPost } from '@/utils/http'
import EmptyState from '@/components/EmptyState.vue'

const OrderStateMap = {
  待付款: 'WAIT_PAY',
  待接单: 'PAYED',
  已预约: 'SHIPPED',
  待评价: 'FINISHED',
}
const activeTab = ref('全部')
const tabs = ['全部', '待付款', '待接单', '已预约', '待评价']

const orders = ref([])

// 获取订单数据
const fetchOrders = async () => {
  try {
    const response = await httpPost(
      '/order/query',
      {
        current: 1,
        size: 1000,
      },
      {
        state: OrderStateMap[activeTab.value] || null,
      },
    )
    if (response.code === 200 || response.code === 204) {
      orders.value = response.records
    } else {
      console.error('订单数据获取失败', response.message)
    }
  } catch (error) {
    console.error('订单数据获取失败', error)
  }
}

// 切换 Tab 时刷新显示内容
const changeTab = async (tab) => {
  activeTab.value = tab
  await fetchOrders()
}

// 页面加载时获取订单数据
onLoad(async () => {
  await fetchOrders()
})
</script>

<style scoped>
page {
  background-color: #f3f4f6;
}
.main-page {
  background-color: #f3f4f6;
}
.tab {
  @apply px-4 py-2 text-gray-700 cursor-pointer;
  position: relative;
}
.tab.text-yellow-500 {
  @apply text-yellow-500;
}
.active-underline {
  @apply w-6 h-1 bg-yellow-400 mt-1 absolute bottom-0 left-1/2 transform -translate-x-1/2;
}
</style>
