<route lang="json5">
{
  style: {
    navigationBarTitleText: '宠托师服务详情',
    navigationStyle: 'custom',
  },
}
</route>

<template>
  <TopBar />
  <view class="bg-[#F5F5F5] h-[135vh]">
    <!-- 顶部背景和头像 -->
    <view class="relative">
      <image
        :src="baseUrl + serviceData.bgUrl"
        class="w-full h-40 object-cover"
        mode="widthFix"
      ></image>
      <view class="absolute left-4 bottom-[-20px]">
        <image
          :src="baseUrl + serviceData.userAvatar"
          mode="aspectFill"
          class="w-20 h-20 object-cover rounded-full border-4 border-white"
        ></image>
      </view>
    </view>

    <!-- 宠托师信息 -->
    <view class="bg-white p-4">
      <text class="text-2xl font-bold">{{ serviceData.userName }}</text>
      <view class="flex items-center mt-2">
        <text class="text-sm text-gray-500 mr-2">认证1年10个月</text>
        <text class="text-sm text-gray-500">服务过200+次</text>
      </view>
      <view class="flex items-center mt-2">
        <wd-icon name="location" size="20" class="text-[#ffc107]"></wd-icon>
        <text class="ml-2 text-gray-600">{{ serviceData.address }}</text>
      </view>
      <text class="mt-4 text-gray-600">{{ serviceData.description }}</text>
    </view>

    <!-- 服务和费用 -->
    <view class="bg-white p-4">
      <text class="text-lg font-bold mb-2">{{ serviceData.serviceName }}服务费</text>
      <view class="flex justify-between items-center mb-4">
        <view class="flex items-center space-x-4">
          <view class="flex flex-col items-center">
            <wd-icon name="bowl" size="24" class="text-pink-500"></wd-icon>
            <text class="text-pink-500 text-sm">食具清洁</text>
          </view>
          <view class="flex flex-col items-center">
            <wd-icon name="water" size="24" class="text-pink-500"></wd-icon>
            <text class="text-pink-500 text-sm">添粮换水</text>
          </view>
          <view class="flex flex-col items-center">
            <wd-icon name="litter" size="24" class="text-pink-500"></wd-icon>
            <text class="text-pink-500 text-sm">铲屎添砂</text>
          </view>
        </view>
        <text class="text-lg text-red-500">¥{{ serviceData.price }}/次</text>
      </view>
    </view>

    <!-- 其他费用 -->
    <view class="bg-white p-4">
      <text class="text-lg font-bold mb-2">其他费用</text>
      <view class="flex justify-between items-center mb-4">
        <view class="flex flex-col items-center">
          <wd-icon name="home" size="24" class="text-gray-500"></wd-icon>
          <text class="text-gray-500 text-sm">基础上门费</text>
          <text class="text-gray-500 text-sm">1km以内23元</text>
        </view>
        <view class="flex flex-col items-center">
          <wd-icon name="scooter" size="24" class="text-gray-500"></wd-icon>
          <text class="text-gray-500 text-sm">每公里加价</text>
          <text class="text-gray-500 text-sm">每多1km加10元</text>
        </view>
      </view>
    </view>

    <!-- 用户评价 -->
    <view class="bg-white p-4">
      <text class="text-lg font-bold mb-2">用户评价</text>
      <view class="mb-4">
        <view class="flex items-center mb-2">
          <text class="text-pink-500 text-lg">5.0</text>
          <view class="flex items-center ml-2">
            <wd-icon name="star-on" size="20" class="text-[#ffc107]"></wd-icon>
            <wd-icon name="star-on" size="20" class="text-[#ffc107]"></wd-icon>
            <wd-icon name="star-on" size="20" class="text-[#ffc107]"></wd-icon>
            <wd-icon name="star-on" size="20" class="text-[#ffc107]"></wd-icon>
            <wd-icon name="star-on" size="20" class="text-[#ffc107]"></wd-icon>
          </view>
          <text class="text-gray-500 ml-2">(14条评论)</text>
        </view>
        <text class="text-gray-600">阿落超级细心！！下次一定还找阿落！！超级安心！</text>
      </view>
    </view>

    <!-- 底部地图展示 -->
    <view class="flex flex-col justify-center items-center bg-[#ffff] pb-[50px]">
      <text class="text-lg font-bold mb-2 w-full pl-[20px]" style="text-align: left">
        服务位置
      </text>
      <view id="mapContainer" class="w-[90%] h-40"></view>
    </view>

    <!-- 底部操作栏 -->
    <view
      class="fixed bottom-0 w-full bg-white flex justify-between items-center px-[10px] mt-4 py-[10px] z-10"
    >
      <text class="text-red-500 text-lg">¥{{ serviceData.price }}/次 起</text>
      <view class="flex space-x-4">
        <button @click="message" class="bg-gray-200 text-gray-600 rounded-full py-[2px] px-6">
          消息
        </button>
        <button
          class="bg-[#ffc107] text-white rounded-full py-[2px] px-6"
          @click="openReservationModal"
        >
          预约宠托师
        </button>
      </view>
    </view>
  </view>

  <!-- 预约弹窗 -->
  <view
    v-if="showReservationModal"
    class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
  >
    <view class="bg-white rounded-lg p-4 w-11/12">
      <view class="text-lg font-bold mb-4">选择预约信息</view>

      <!-- 服务宠物选择 -->
      <text class="text-sm text-gray-500 mb-2">服务宠物</text>
      <view class="flex space-x-4 mb-4 scroll-x overflow-x-auto" scroll-x>
        <view
          v-for="pet in pets"
          :key="pet.id"
          class="flex flex-col items-center"
          @click="selectPet(pet.id)"
          :class="selectedPetId == pet.id ? 'border-4 border-[#ffc107] color-[#ffc107]' : ''"
        >
          <image
            :src="baseUrl + pet.profileUrl"
            class="w-20 h-20 rounded-full object-cover"
          ></image>
          <text class="text-sm">{{ pet.name }}</text>
        </view>
      </view>

      <!-- 预约时间 -->
      <text class="text-sm text-gray-500 mb-2">预约时间</text>
      <picker mode="date" :start="today" :end="weekFromToday" @change="handleDateChange">
        <view class="bg-gray-100 p-2 rounded mb-4">{{ reservationDate || '选择日期' }}</view>
      </picker>

      <!-- 选择预约小时 -->
      <text class="text-sm text-gray-500 mb-2">选择服务时长</text>
      <view>
        <wd-input-number v-model="hours" @change="hoursHandleChange" :min="1" :max="12" />
      </view>

      <!-- 服务地址选择 -->
      <text class="text-sm text-gray-500 mb-2">选择地址</text>
      <picker
        v-if="addressList.length > 0"
        mode="selector"
        range-key="display"
        :range="addressList"
        @change="handleAddressChange"
      >
        <view class="bg-gray-100 p-2 rounded mb-4">
          {{ selectedAddress.display || '请选择地址' }}
        </view>
      </picker>
      <view v-else>请选择地址</view>

      <!-- 操作按钮 -->
      <view class="fixed bottom-0 left-0 w-full p-4 bg-white shadow-up">
        <view class="flex justify-between">
          <button
            class="flex-1 mx-2 py-2 px-6 bg-gray-200 text-gray-600 rounded-full"
            @click="closeReservationModal"
          >
            取消
          </button>
          <button
            class="flex-1 mx-2 py-2 px-6 bg-[#ffc107] text-white rounded-full"
            @click="confirmReservation"
          >
            确认预约
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import { httpGet } from '@/utils/http'
import { initMap, showLocationOnMap } from '@/utils/map-utils'
import { baseUrl, toast } from '@/utils/commUtils'
import { pay } from '@/logic/pay'
import TopBar from '@/components/TopBar.vue'

// 获取当前日期
const nowday = new Date()
const year = nowday.getFullYear()
const month = (nowday.getMonth() + 1).toString().padStart(2, '0') // 月份从0开始，需加1
const day = nowday.getDate().toString().padStart(2, '0')
const reservationDate = ref(`${year}-${month}-${day}`)

const serviceData = ref({})
const pets = ref([])
const addressList = ref([])
const showReservationModal = ref(false)
const selectedPetId = ref('')
const today = ref('')
const weekFromToday = ref('')
const personalServiceId = ref('')
const hours = ref(1)
const selectedAddress = ref({})

const hoursHandleChange = ({ value }) => {
  if (value >= 1 && value <= 12) {
    hours.value = value
  } else {
    toast('选择的服务时长在1-12小时')
  }
}

const message = () => {}

const detailPay = async () => {
  const order = {
    reservationTime: `${reservationDate.value} 00:00:00`,
    personalServiceId: personalServiceId.value,
    personalServiceUserId: serviceData.value.serviceUserId,
    price: serviceData.value.price * hours.value,
    paymentMethod: 'wxpay',
    address: selectedAddress.value.id,
    pet: selectedPetId.value,
    serviceHours: hours.value,
  }
  await pay(order)
  closeReservationModal()
}

// 获取当前日期并计算一周后的日期
const setDateRange = () => {
  const now = new Date()
  today.value = now.toISOString().split('T')[0]

  const weekLater = new Date(now)
  weekLater.setDate(now.getDate() + 7)
  weekFromToday.value = weekLater.toISOString().split('T')[0]
}

// Handle the date change event
const handleDateChange = (e) => {
  reservationDate.value = e.detail.value
}

// Handle the address change event
const handleAddressChange = (e) => {
  selectedAddress.value = addressList.value[e.detail.value]
}

// 打开预约弹窗
const openReservationModal = () => {
  showReservationModal.value = true
}

// 关闭预约弹窗
const closeReservationModal = () => {
  showReservationModal.value = false
}

// 选择宠物
const selectPet = (id) => {
  selectedPetId.value = id
}

// 确认预约
const confirmReservation = async () => {
  if (!selectedPetId.value || !reservationDate.value || !selectedAddress.value.display) {
    toast('请选择宠物、日期和地址')
    return
  }
  await detailPay()
  closeReservationModal()
}

// 页面加载时获取服务详情数据、宠物信息和地址信息并初始化地图
onLoad(async (options) => {
  if (!options.id) {
    toast('该服务不存在！')
    // 返回到上一级页面
    uni.navigateBack()
    return
  }
  const id = options.id
  personalServiceId.value = options.id
  setDateRange()

  // 获取服务详情
  const serviceResponse = await httpGet(`/personal-service/service/${id}`)
  if (serviceResponse.code === 200) {
    serviceData.value = serviceResponse.data

    // 初始化地图（H5 和小程序分别处理）
    const mapContextOrMap = await initMap(
      'mapContainer',
      [serviceData.value.longitude, serviceData.value.latitude],
      15,
    )
    console.log(mapContextOrMap)
    // 显示当前位置
    showLocationOnMap(mapContextOrMap, {
      latitude: serviceData.value.latitude,
      longitude: serviceData.value.longitude,
    })
  } else {
    uni.showToast({ title: '加载服务数据失败', icon: 'none' })
  }

  // 获取宠物信息
  const petsResponse = await httpGet('/petInfo/my')
  if (petsResponse.code === 200) {
    pets.value = petsResponse.data
    selectedPetId.value = pets.value[0].id
  } else {
    uni.showToast({ title: '加载宠物数据失败', icon: 'none' })
  }

  // 获取地址信息
  const addressResponse = await httpGet('/china-address/my')
  if (addressResponse.code === 200) {
    addressList.value = addressResponse.data
    addressList.value.forEach((item) => {
      item['display'] = `${item.province} ${item.city} ${item.district} ${item.detailAddress}`
    })
    selectedAddress.value = addressList.value[0]
  } else {
    uni.showToast({ title: '加载地址数据失败', icon: 'none' })
  }
})
</script>

<style scoped>
.options {
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: #fff;
}
.mapContainer {
  width: 100%;
  height: 20vh;
}

.fixed {
  position: fixed;
}

.bottom-0 {
  bottom: 0;
}

.left-0 {
  left: 0;
}

.right-0 {
  right: 0;
}

.bg-white {
  background-color: white;
}

.rounded-full {
  border-radius: 9999px;
}

.shadow {
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.text-red-500 {
  color: #f56565;
}

.border-4 {
  border-width: 4px;
}

.border-[#ffc107] {
  border-color: #ffc107;
}
</style>
