<template>
  <view class="container" :class="themeClass">
    <image
      v-show="loaded"
      :src="imageSrc"
      :mode="mode"
      :style="imageStyle"
      @load="handleLoad"
      @error="handleError"
    />
    <view v-show="!loaded" class="placeholder" :style="placeholderStyle">
      <image :src="placeholder" class="placeholder-image" />
      <view class="shimmer-container">
        <view class="shimmer"></view>
      </view>
    </view>
  </view>
</template>
<script lang="ts" setup>
import { ref, computed, onMounted, defineProps } from 'vue'
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
  width: {
    type: Number,
    default: null,
  },
  height: {
    type: Number,
    default: null,
  },
  pwidth: {
    type: Number,
    default: null,
  },
  pheight: {
    type: Number,
    default: null,
  },
  placeholder: {
    type: String,
    default: '/static/images/placeholder.png',
  },
  cacheEnabled: {
    type: Boolean,
    default: false,
  },
  theme: {
    type: String,
    default: 'light',
    validator: (value) => ['light', 'dark'].includes(value),
  },
  borderRadius: {
    type: Number,
    default: 0,
  },
})

const loaded = ref(false)
const error = ref(false)
const imageSrc = ref('')

const themeClass = computed(() => {
  return props.theme === 'dark' ? 'dark-theme' : 'light-theme'
})

const imageStyle = computed(() => {
  let style = {}
  if (props.width) {
    style.width = `${props.width}rpx`
  }
  if (props.height) {
    style.height = `${props.height}rpx`
  }
  style.borderRadius = `${props.borderRadius}rpx`
  return style
})

const placeholderStyle = computed(() => {
  let style = {}
  if (props.pwidth) {
    style.width = `${props.pwidth}rpx`
  } else if (props.width) {
    style.width = `${props.width}rpx`
  }
  if (props.pheight) {
    style.height = `${props.pheight}rpx`
  } else if (props.height) {
    style.height = `${props.height}rpx`
  }
  style.borderRadius = props.borderRadius
  return style
})

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
<style scoped>
.container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: inherit;
}

.light-theme .placeholder {
  background-color: #f5f5f5;
}

.dark-theme .placeholder {
  background-color: #333;
}

.placeholder-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: inherit;
}

.shimmer-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none; /* Ensure it does not block events */
  opacity: 0.4; /* Adjust the opacity to make sure the shimmer is visible but not obstructive */
}

.shimmer {
  width: 100%;
  height: 100%;
  background: linear-gradient(
    100deg,
    rgba(255, 255, 255, 0) 40%,
    rgba(255, 255, 255, 0.5) 50%,
    rgba(255, 255, 255, 0) 60%
  );
  background-position: 100% 50%;
  background-position-x: 180%;
  background-size: 200% 100%;
  border-radius: inherit;
  animation: shimmer 1.5s ease-in-out infinite;
}

.light-theme .shimmer {
  background-color: #edeeef;
}

.dark-theme .shimmer {
  background-color: #444;
}

@keyframes shimmer {
  to {
    background-position-x: -20%;
  }
}
</style>
