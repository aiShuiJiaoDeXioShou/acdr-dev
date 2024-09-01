<template>
  <view>
    <image v-show="loaded" :src="imageSrc" :mode="mode" @load="handleLoad" @error="handleError" />
    <view v-show="!loaded">
      <image :src="placeholder" />
      <view></view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref, onMounted, defineProps } from 'vue'
import imageCache from '../utils/imageCache'

const props = defineProps({
  src: {
    type: String,
    required: true,
  },
  mode: {
    type: String,
    default: 'aspectFill',
  },
  placeholder: {
    type: String,
    default: '/static/images/placeholder.png',
  },
  cacheEnabled: {
    type: Boolean,
    default: false,
  },
})

const loaded = ref(false)
const error = ref(false)
const imageSrc = ref('')

const initImage = async () => {
  if (props.cacheEnabled) {
    try {
      const cachedPath = await imageCache.getCachedImage(props.src)
      imageSrc.value = cachedPath
    } catch (err) {
      console.error('图片加载失败:', err)
      imageSrc.value = props.src
    }
  } else {
    imageSrc.value = props.src
  }
}

const handleLoad = () => {
  loaded.value = true
}

const handleError = () => {
  error.value = true
  loaded.value = true
  imageSrc.value = props.placeholder
}

onMounted(() => {
  initImage()
})
</script>
