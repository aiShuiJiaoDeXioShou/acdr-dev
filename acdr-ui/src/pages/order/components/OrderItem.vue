<template>
  <view class="order-item p-4 mb-4 bg-white shadow rounded-lg">
    <!-- 订单头部，包含标题和状态 -->
    <view class="order-header flex justify-between items-center mb-2">
      <text class="order-title font-bold text-lg">{{ serviceInfo.serviceName }}</text>
      <text class="order-status text-sm text-gray-500">{{ state }}</text>
    </view>

    <!-- 服务信息 -->
    <view class="order-info text-sm text-gray-700 mb-4">
      <view>
        <text>服务类型:</text>
        {{ serviceInfo.type }}
      </view>
      <view>
        <text>服务地址:</text>
        {{ serviceInfo.address }}
      </view>
      <view>
        <text>预约时间:</text>
        {{ reservationTime }}
      </view>
      <view>
        <text>服务时长:</text>
        {{ serviceHours }} 小时
      </view>
    </view>

    <!-- 宠物信息 -->
    <view class="pet-info flex items-center mb-4">
      <image
        :src="imgUrl(pet.profileUrl)"
        mode="aspectFill"
        class="w-16 h-16 rounded-full mr-4"
      ></image>
      <view>
        <view>
          <text>宠物名称:</text>
          {{ pet.name }}
        </view>
        <view>
          <text>宠物品种:</text>
          {{ pet.breed }}
        </view>
      </view>
    </view>

    <!-- 用户信息 -->
    <view class="user-info text-sm text-gray-700 mb-4">
      <view>
        <text>下单用户:</text>
        {{ user.nickname }} ({{ user.phone }})
      </view>
      <view>
        <text>用户地址:</text>
        {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detailAddress }}
      </view>
    </view>

    <!-- 支付信息 -->
    <view class="payment-info text-sm text-gray-700 mb-4">
      <view>
        <text>支付方式:</text>
        {{ paymentMethod }}
      </view>
      <view>
        <text>支付状态:</text>
        {{ isPay ? '已支付' : '未支付' }}
      </view>
      <view>
        <text>总价:</text>
        ¥{{ price }}
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="flex justify-between items-center">
      <text class="text-lg text-red-500">¥{{ price }} 共{{ serviceHours }}小时</text>
      <view class="flex">
        <button v-if="!isTake" class="btn" @click="toOrderDetail">查看详情</button>
        <button v-if="!isTake" class="btn ml-2" @click="cancel">取消订单</button>
        <button v-if="!isTake" class="btn-primary ml-2" @click="orderPay">支付</button>
        <button v-if="isTake" @click="bookingCancel">取消订单</button>
        <button v-if="isTake" @click="confirm">确认订单</button>
        <button v-if="state && state.includes('已预约') && user.userInfo.isPetNursery" @click="scan">扫描二维码</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { payOrder } from '@/logic/pay'
import { useUserStore } from '@/store'
import { imgUrl, scanCodeAsync, showModalAsync, toast } from '@/utils/commUtils'
import { httpPost } from '@/utils/http'
import { defineProps } from 'vue'

const user = useUserStore()

const props = defineProps({
  id: String,
  userId: String,
  reservationTime: String,
  serviceHours: String,
  personalServiceId: String,
  personalServiceUserId: String,
  price: Number,
  isPay: Boolean,
  feedback: String,
  star: Number,
  state: String,
  paymentMethod: String,
  qrcode: String,
  createTime: String,
  updateTime: String,
  address: Object,
  pet: Object,
  user: Object,
  psUser: Object,
  serviceInfo: Object,
  isTake: false,
})

const shopRemark = ref('')

const scan = async () => {
  // 扫描二维码，获取扫描信息发送给后端
  const scan = await scanCodeAsync()
  if (scan.result) {
    try {
      // 发送请求给后端
      const res = await httpPost('/order/scan/' + props.id, {}, { qrcode: scan.result })
      if (res.code == 200) {
        toast('支付成功')
        // 刷新数据
        uni.$emit('refresh')
      } else {
        toast(res.message)
      }
    } catch (e) {
      toast(e.data.codeStr)
    }
  } else {
    toast('扫码失败')
  }
}

const orderPay = async () => {
  try {
    const res = await payOrder(props.id)
    if (res.code == 200) {
      toast('支付成功')
    } else {
      toast(res.message)
    }
  } catch (e) {
    console.log(e)
    toast(e.data.codeStr)
  }
}

const cancel = async () => {
  const res = await httpPost('/order/cancel/' + props.id)
  if (res.code == 200) {
    toast('取消成功')
    // 刷新数据
    uni.$emit('refresh')
  } else {
    toast(res.message)
  }
}

const toOrderDetail = () => {
  uni.navigateTo({
    url: `/pages/order/order-detail?id=${props.id}`,
  })
}

const bookingCancel = async () => {
  try {
    // 弹出对话框让用户输入取消原因
    const res = await showModalAsync({
      title: '取消订单',
      content: '',
      editable: true,
      placeholderText: '请输入取消原因!',
    })

    if (res.confirm) {
      // 用户点击确定
      shopRemark.value = res.content

      // 进行订单取消操作
      const response = await httpPost(
        '/order/bookingCancel/' + props.id,
        {},
        { shopRemark: shopRemark.value },
      )
      if (response.code == 200) {
        toast('取消订单成功！')
        // 刷新数据
        uni.$emit('refresh')
      } else {
        toast(response.message)
      }
    } else if (res.cancel) {
      // 用户点击取消
      toast('取消操作已取消')
    }
  } catch (e) {
    toast('请求失败，请稍后再试')
    console.error(e)
  }
}

const confirm = async () => {
  try {
    const res = await httpPost('/order/confirm/' + props.id)
    if (res.code == 200) {
      toast('确认订单成功！')
      // 刷新数据
      uni.$emit('refresh')
    } else {
      toast(res.message)
    }
  } catch (e) {
    toast(e.data.message)
  }
}
</script>

<style scoped>
.order-item {
  @apply p-4 mb-4 bg-white shadow rounded-lg;
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

.order-info {
  @apply text-sm text-gray-700 mb-4;
}

.pet-info {
  @apply flex items-center mb-4;
}

.user-info {
  @apply text-sm text-gray-700 mb-4;
}

.payment-info {
  @apply text-sm text-gray-700 mb-4;
}

.btn {
  @apply px-2 py-1 bg-gray-100 rounded text-xs;
}

.btn-primary {
  @apply px-2 py-1 bg-[#ffc107] text-white rounded text-xs;
}

.text-red-500 {
  @apply text-red-500;
}
</style>
