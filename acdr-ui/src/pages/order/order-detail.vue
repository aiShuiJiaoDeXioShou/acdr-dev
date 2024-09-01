<template>
  <view class="order-detail p-4 bg-white shadow rounded-lg" v-if="orderDetail">
    <!-- 订单头部，包含标题和状态 -->
    <view class="order-header flex justify-between items-center mb-2">
      <text class="order-title font-bold text-lg">{{ orderDetail.serviceInfo.serviceName }}</text>
      <text class="order-status text-sm text-gray-500">{{ orderDetail.state }}</text>
    </view>

    <!-- 服务信息 -->
    <view class="order-info text-sm text-gray-700 mb-4">
      <view>
        <text>服务类型:</text>
        {{ orderDetail.serviceInfo.type }}
      </view>
      <view>
        <text>服务地址:</text>
        {{ orderDetail.serviceInfo.address }}
      </view>
      <view>
        <text>预约时间:</text>
        {{ orderDetail.reservationTime }}
      </view>
      <view>
        <text>服务时长:</text>
        {{ orderDetail.serviceHours }} 小时
      </view>
    </view>

    <!-- 宠物信息 -->
    <view class="pet-info flex items-center mb-4">
      <image
        :src="imgUrl(orderDetail.pet.profileUrl)"
        mode="aspectFill"
        class="w-16 h-16 rounded-full mr-4"
      ></image>
      <view>
        <view>
          <text>宠物名称:</text>
          {{ orderDetail.pet.name }}
        </view>
        <view>
          <text>宠物品种:</text>
          {{ orderDetail.pet.breed }}
        </view>
      </view>
    </view>

    <!-- 用户信息 -->
    <view class="user-info text-sm text-gray-700 mb-4">
      <view>
        <text>下单用户:</text>
        {{ orderDetail.user.nickname }} ({{ orderDetail.user.phone }})
      </view>
      <view>
        <text>用户地址:</text>
        {{ orderDetail.address.province }} {{ orderDetail.address.city }}
        {{ orderDetail.address.district }} {{ orderDetail.address.detailAddress }}
      </view>
    </view>

    <!-- 支付信息 -->
    <view class="payment-info text-sm text-gray-700 mb-4">
      <view>
        <text>支付方式:</text>
        {{ orderDetail.paymentMethod }}
      </view>
      <view>
        <text>支付状态:</text>
        {{ orderDetail.isPay ? '已支付' : '未支付' }}
      </view>
      <view>
        <text>总价:</text>
        ¥{{ orderDetail.price }}
      </view>
    </view>

    <!-- 二维码信息 -->
    <view v-if="orderDetail.qrcode" class="qrcode-info text-center mb-4">
      <image :src="imgUrl(orderDetail.qrcode)" mode="aspectFit" class="w-32 h-32 mx-auto"></image>
      <view class="text-xs text-gray-500 mt-2">订单二维码</view>
    </view>
  </view>
  <view v-else class="text-center text-gray-500 mt-4">加载中...</view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import { imgUrl, toast } from '@/utils/commUtils'
import { httpGet } from '@/utils/http'

const orderDetail = ref(null)

onLoad(async (options) => {
  const orderId = options.id // 获取传递的订单ID
  try {
    const response = await httpGet(`/order/info/${orderId}`)
    if (response.code === 200) {
      orderDetail.value = response.data
      // console.log('@@',orderDetail.value)
    } else {
      toast('获取订单详情失败 ' + response.message)
      console.error('获取订单详情失败:', response.message)
    }
  } catch (error) {
    toast('请求失败')
    console.error('请求失败:', error)
  }
})
</script>

<style lang="scss" scoped>
.order-detail {
  @apply p-4 bg-white shadow rounded-lg;
}

.order-header {
  @apply flex justify-between items-center mb-2;
}

.order-title {
  @apply font-bold text-lg;
}

.order-status {
  @apply text-sm text-gray-500;
}

.order-info,
.pet-info,
.user-info,
.payment-info {
  @apply text-sm text-gray-700 mb-4;
}

.qrcode-info {
  @apply text-center mb-4;
}

.qrcode-info image {
  @apply w-32 h-32 mx-auto;
}
</style>
