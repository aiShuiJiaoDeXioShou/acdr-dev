<template>
  <scroll-view
    class="refresh-list"
    scroll-y
    :lower-threshold="50"
    @scrolltolower="loadMore"
    @scrolltoupper="refreshData"
    :style="{ height: listHeight }"
  >
    <slot :data="data" />
    <view v-if="loading" class="text-center text-gray-500 mt-4">加载中...</view>
    <view v-if="noMoreData" class="text-center text-gray-500 mt-4">没有更多数据了</view>
  </scroll-view>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  fetchData: {
    type: Function,
    required: true,
  },
  pageSize: {
    type: Number,
    default: 10,
  },
  listHeight: {
    type: String,
    default: '100vh',
  },
})

const data = ref([])
const page = ref(1)
const loading = ref(false)
const noMoreData = ref(false)

const refreshData = async () => {
  if (loading.value) return
  loading.value = true
  page.value = 1
  noMoreData.value = false
  data.value = []

  try {
    const response = await props.fetchData(page.value, props.pageSize)
    if (response.length < props.pageSize) {
      noMoreData.value = true
    }
    data.value = response
  } catch (error) {
    console.error('数据刷新失败', error)
  }
  loading.value = false
}

const loadMore = async () => {
  if (loading.value || noMoreData.value) return
  loading.value = true
  page.value += 1

  try {
    const response = await props.fetchData(page.value, props.pageSize)
    if (response.length < props.pageSize) {
      noMoreData.value = true
    }
    data.value = [...data.value, ...response]
  } catch (error) {
    console.error('加载更多数据失败', error)
  }
  loading.value = false
}

const onLoad = () => {
  refreshData()
}

onLoad()
</script>

<style scoped>
.refresh-list {
  overflow-y: auto;
}

.text-center {
  text-align: center;
}
</style>
