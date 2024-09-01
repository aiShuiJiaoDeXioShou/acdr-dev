<route lang="json5">
{
  style: {
    navigationBarTitleText: '选择服务',
    navigationBarBackgroundColor: '#ffff',
  },
}
</route>

<template>
  <view class="bg-[#F5F5F5] h-full p-4">
    <!-- 搜索框 -->
    <view class="flex items-center bg-white p-2 rounded-lg shadow mb-4">
      <view class="flex-1 flex items-center bg-gray-100 rounded-full px-4 py-2">
        <wd-icon name="search" size="40" class="text-gray-400"></wd-icon>
        <input
          class="ml-2 bg-transparent outline-none flex-1"
          type="text"
          v-model="searchTerm"
          placeholder="请输入服务内容"
          @input="onSearch"
        />
      </view>
    </view>

    <!-- 过滤栏 -->
    <view class="flex justify-around bg-white p-2 rounded-lg shadow mb-4">
      <view class="flex items-center" @click="sortByDistance">
        <text class="text-gray-600">离我最近</text>
      </view>
      <view class="flex items-center" @click="sortByPrice">
        <text class="text-gray-600">价格</text>
        <wd-icon :name="priceSortIcon" size="20" class="ml-2 text-gray-400"></wd-icon>
      </view>
      <view class="flex items-center" @click="sortByLatest">
        <text class="text-gray-600">新发布</text>
      </view>
      <view class="flex items-center">
        <text class="text-gray-600">区域</text>
        <wd-icon name="arrow-down" size="20" class="ml-2 text-gray-400"></wd-icon>
      </view>
    </view>

    <!-- 服务选择 -->
    <view class="mt-4">
      <text class="text-lg font-bold">选择服务</text>
      <view class="flex mt-2 space-x-4 overflow-x-auto">
        <view
          v-for="serviceType in serviceTypes"
          :key="serviceType.name"
          class="flex flex-col items-center"
          @click="filterByService(serviceType.label)"
        >
          <view
            :class="[
              'w-16 h-16 rounded-full flex items-center justify-center',
              selectedService === serviceType.name ? 'bg-[#ffc107]' : 'bg-gray-200',
            ]"
          >
            <wd-icon
              style="width: 36px; height: 36px"
              :name="imgUrl(serviceType.icon)"
              :size="48"
              :class="selectedService === serviceType.label ? 'text-white' : 'text-gray-500'"
            ></wd-icon>
          </view>
          <text :class="selectedService === serviceType.label ? 'text-[#ffc107]' : 'text-gray-500'">
            {{ serviceType.label }}
          </text>
        </view>
      </view>
    </view>

    <!-- 推荐的宠物托管师 -->
    <view v-if="petSitters.length > 0" class="mt-4">
      <text class="text-lg font-bold">选择宠托师</text>
      <view v-for="(petSitter, index) in petSitters" :key="index" class="mt-4">
        <PetSitterCard :petSitter="petSitter" />
      </view>
      <wd-loadmore
        :loading="loading"
        :finished="noMoreData"
        @load="loadMore"
        finished-text="没有更多数据了"
      ></wd-loadmore>
    </view>
    <view class="pt-[80px]" v-else>
      <EmptyState  type="search" message="暂无相关服务" />
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import PetSitterCard from '@/components/PetSitterCard.vue'
import { httpPost } from '@/utils/http'
import EmptyState from '@/components/EmptyState.vue'

const searchTerm = ref('')
const priceSortAsc = ref(true)
const petSitters = ref([])
const page = ref(1)
const loading = ref(false)
const noMoreData = ref(false)
const serviceTypes = ref([
  { name: 'interaction', label: '宠物互动', icon: '@/static/home/book.png' },
  { name: 'nanny', label: '宠物月嫂', icon: '@/static/home/pb.png' },
  { name: 'canteen', label: '宠物美容', icon: '@/static/home/yl.png' },
  { name: 'training', label: '社交教培', icon: '@/static/home/mr.png' },
  { name: 'cleaning', label: '宠物清洁', icon: '@/static/home/yl.png' },
])
const selectedService = ref(serviceTypes.value[0].label)

// 排序字段和顺序
const sortField = ref('')
const sortOrder = ref('')

// 获取宠托师数据
const fetchPetSitters = async () => {
  if (loading.value) return
  loading.value = true

  try {
    const response = await httpPost(
      '/personal-service/list',
      {
        current: page.value,
        size: 10,
        data: {
          serviceName: searchTerm.value,
          type: selectedService.value,
        },
      },
      {
        sortField: sortField.value,
        sortOrder: sortOrder.value,
      },
    )
    if (response.code === 200) {
      if (page.value === 1) {
        petSitters.value = response.records
      } else {
        petSitters.value = [...petSitters.value, ...response.records]
      }

      if (response.records.length < 10) {
        noMoreData.value = true
      }
    } else {
      console.error('获取宠托师数据失败', response.message)
    }
  } catch (error) {
    console.error('获取宠托师数据失败', error)
  } finally {
    loading.value = false
  }
}

const loadMore = async () => {
  if (!noMoreData.value) {
    page.value += 1
    fetchPetSitters()
  }
}

const sortByPrice = async () => {
  priceSortAsc.value = !priceSortAsc.value
  sortField.value = 'price'
  sortOrder.value = priceSortAsc.value ? 'asc' : 'desc'
  page.value = 1
  noMoreData.value = false
  await fetchPetSitters()
}

const sortByLatest = async () => {
  sortField.value = 'createTime'
  sortOrder.value = 'desc'
  page.value = 1
  noMoreData.value = false
  await fetchPetSitters()
}

const sortByDistance = () => {
  // 按距离排序的逻辑
}

const filterByService = async (serviceName) => {
  selectedService.value = serviceName
  page.value = 1
  noMoreData.value = false
  fetchPetSitters()
}

const onSearch = () => {
  page.value = 1
  noMoreData.value = false
  fetchPetSitters()
}

onLoad(() => {
  fetchPetSitters()
})
</script>

<style scoped>
page {
  background-color: #f5f5f5;
}

.bg-gray-100 {
  @apply bg-gray-100;
}

.bg-gray-200 {
  @apply bg-gray-200;
}

.text-gray-400 {
  @apply text-gray-400;
}

.text-gray-500 {
  @apply text-gray-500;
}

.bg-[#ffc107] {
  @apply bg-[#ffc107];
}

.text-[#ffc107] {
  @apply text-[#ffc107];
}

.rounded-full {
  @apply rounded-full;
}

.shadow {
  @apply shadow;
}

.space-x-4 {
  @apply space-x-4;
}
</style>
