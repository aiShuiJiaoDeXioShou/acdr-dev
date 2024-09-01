<route lang="json5" type="page">
{
  layout: "default",
  style: {
    navigationBarTitleText: "发布上门服务",
  },
}
</route>

<template>
  <view class="p-4 flex flex-col h-[120vh]">
    <view class="card mt-4 flex-grow">
      <!-- 服务名称 -->
      <view class="mb-4">
        <input
          v-model="serviceName"
          class="input w-full"
          placeholder="输入服务名称（例如：宠物看护）"
        />
      </view>

      <!-- 服务类型 -->
      <view class="mb-4">
        <label class="label block mb-2">选择服务类型</label>
        <picker mode="selector" :range="serviceTypes">
          <view class="input w-full bg-gray-100 p-2 rounded">
            {{ serviceTypes[selectedServiceType] }}
          </view>
        </picker>
      </view>

      <!-- 服务描述 -->
      <view class="mb-4">
        <textarea
          v-model="serviceDescription"
          class="input h-24 w-full"
          placeholder="描述服务内容，例如：包含宠物喂养、遛狗等..."
        ></textarea>
      </view>

      <!-- 服务价格 -->
      <view class="mb-4">
        <label class="label block mb-2">服务价格（每小时）</label>
        <view class="flex w-[140px] items-center">
          <button
            class="btn w-10 bg-gray-200 text-black text-center"
            @touchstart="startIncrement(false)"
            @touchend="stopIncrement"
            @click="incrementPrice(1)"
          >
            -
          </button>
          <input
            v-model="servicePrice"
            type="number"
            class="input text-center"
            readonly
          />
          <button
            class="btn w-12 bg-gray-200 text-black text-center"
            @touchstart="startIncrement(true)"
            @touchend="stopIncrement"
            @click="incrementPrice(1)"
          >
            +
          </button>
        </view>
      </view>

      <!-- 服务地址 -->
      <view class="flex justify-between mb-4">
        <text class="text-gray-500">服务地点（范围）</text>
        <text class="text-gray-500">{{ serviceLocation }}</text>
      </view>

      <!-- 地图容器 -->
      <view v-show="loadmap" class="mb-4">正在定位......</view>
      <view id="mapContainer" class="mb-4 mapContainer"></view>

      <!-- 服务状态 -->
      <view class="flex justify-between mb-4">
        <label class="label">服务状态(开启/关闭)</label>
        <switch :checked="serviceState" @change="switchChange" color="#42b983"></switch>
      </view>

      <!-- 图片上传 -->
      <view class="mb-4">
        <label class="label block mb-2">上传服务封面</label>
        <button class="btn bg-gray-200 text-black mb-2" @click="chooseImage">
          选择图片
        </button>
        <view v-if="uploadedImages.length > 0" class="flex flex-wrap">
          <view
            v-for="(image, index) in uploadedImages"
            :key="index"
            class="w-24 h-24 m-2 bg-cover bg-center"
            :style="{ backgroundImage: 'url(' + baseUrl + image + ')' }"
          ></view>
        </view>
      </view>
    </view>

    <!-- 发布按钮固定在页面底部 -->
    <view class="fixed bottom-0 left-0 right-0 p-4 bg-white border-t border-gray-200">
      <button class="btn w-full bg-yellow-500 text-black" @click="submitService">
        {{ isEditMode ? "更新服务" : "发布上门服务" }}
      </button>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref, onMounted } from 'vue'
import { httpPost, httpGet, httpUploadFile } from '@/utils/http'
import { baseUrl } from '@/utils/commUtils'
import { getCurrentLocation, showLocationOnMap, initMap } from '@/utils/map-utils'
import config from '@/utils/config'

let incrementInterval = null

// 数据和状态变量
const serviceId = ref(null)
const isEditMode = ref(!!serviceId.value)
const serviceName = ref('')
const serviceTypes = ref(['宠物看护', '宠物洗澡', '宠物训练', '宠物寄养'])
const selectedServiceType = ref(0)
const serviceDescription = ref('')
const servicePrice = ref(0)
const serviceLocation = ref('')
const serviceState = ref(true)
const uploadedImages = ref([])
const loadmap = ref(true)
const location = ref({})

// 如果是编辑模式，获取服务数据
const fetchServiceData = async () => {
  try {
    const response = await httpPost('/personal-service/get', { id: serviceId.value })
    if (response.code == 200) {
      const service = response.data
      serviceName.value = service.serviceName
      selectedServiceType.value = serviceTypes.value.indexOf(service.type)
      serviceDescription.value = service.description
      servicePrice.value = service.price
      serviceLocation.value = service.serviceHost
      serviceState.value = service.state === 1
      uploadedImages.value = [service.url]
      location.value = {
        latitude: service.latitude,
        longitude: service.longitude,
        address: service.address,
      }
    }
  } catch (error) {
    uni.showToast({ title: '获取服务数据失败', icon: 'none' })
    console.error('Error fetching service data:', error)
  }
}

// 页面加载时获取位置信息并显示在地图上
onLoad(async (options) => {
  if(options.id) {
    serviceId.value = options.id
  }
  // #ifdef H5
  window._AMapSecurityConfig = {
    securityJsCode: config.MapSecurityJsCode,
  }
  // #endif
  if (isEditMode.value) {
    await fetchServiceData()
    try {
      // 初始化地图（H5 和小程序分别处理）
      const mapContextOrMap = await initMap(
        'mapContainer',
        [location.value.longitude, location.value.latitude],
        15,
      )
      // 显示当前位置
      showLocationOnMap(mapContextOrMap, location.value)
      loadmap.value = false
    } catch (error) {
      console.error('获取位置信息失败:', error)
    } finally {
      loadmap.value = false
    }
  } else {
    try {
      const locationInfo = await getCurrentLocation()
      serviceLocation.value = locationInfo.address
      location.value = locationInfo
      console.log('获取位置信息成功:', locationInfo)

      // 初始化地图（H5 和小程序分别处理）
      const mapContextOrMap = await initMap(
        'mapContainer',
        [locationInfo.longitude, locationInfo.latitude],
        15,
      )

      // 显示当前位置
      showLocationOnMap(mapContextOrMap, locationInfo)
    } catch (error) {
      console.error('获取位置信息失败:', error)
    } finally {
      loadmap.value = false
    }
  }
})

// 增加或减少服务价格
const incrementPrice = (increment) => {
  servicePrice.value = Math.max(servicePrice.value + increment, 0)
}

// 长按开始
const startIncrement = (isIncrement) => {
  incrementInterval = setInterval(() => {
    incrementPrice(isIncrement ? 10 : -10)
  }, 200)
}

// 长按结束
const stopIncrement = () => {
  clearInterval(incrementInterval)
}

// 开关切换服务状态
const switchChange = (e) => {
  serviceState.value = e.value
}

// 选择并上传图片
const chooseImage = () => {
  uni.chooseImage({
    count: 1,
    success: async (res) => {
      for (const filePath of res.tempFilePaths) {
        const uploadedUrl = await uploadImage(filePath)
        if (uploadedUrl) {
          uploadedImages.value.pop()
          uploadedImages.value.push(uploadedUrl)
        }
      }
    },
    fail: () => {
      uni.showToast({ title: '图片选择失败', icon: 'none' })
    },
  })
}

// 上传图片到服务器
const uploadImage = async (filePath) => {
  try {
    const uploadResult = await httpUploadFile('/file/upload', filePath)
    if (uploadResult && uploadResult.data && uploadResult.data.url) {
      return uploadResult.data.url
    } else {
      uni.showToast({ title: '图片上传失败', icon: 'none' })
      return ''
    }
  } catch (error) {
    uni.showToast({ title: '图片上传失败', icon: 'none' })
    return ''
  }
}

// 发布或更新服务的函数
const submitService = () => {
  const serviceData = {
    id: serviceId.value,
    serviceName: serviceName.value,
    type: serviceTypes.value[selectedServiceType.value],
    description: serviceDescription.value,
    price: servicePrice.value,
    serviceHost: serviceLocation.value,
    state: serviceState.value ? 1 : 0,
    url: uploadedImages.value[0],
    address: location.value.address,
    latitude: `${location.value.latitude}`,
    longitude: `${location.value.longitude}`,
  }
  console.log(serviceData)
  const endpoint = isEditMode.value ? '/personal-service/update' : '/personal-service/push'
  httpPost(endpoint, serviceData).then((response) => {
    if (response.code === 200) {
      uni.showToast({ title: isEditMode.value ? '更新成功' : '发布成功', icon: 'success' })
    } else {
      uni.showToast({ title: isEditMode.value ? '更新失败' : '发布失败', icon: 'none' })
    }
  })
}
</script>

<style scoped>
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

.border-t {
  border-top-width: 1px;
}

.border-gray-200 {
  border-color: #e5e7eb;
}
</style>
