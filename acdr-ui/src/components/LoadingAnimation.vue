<template>
  <view
    class="loading-overlay"
    :catchtouchmove="modelValue"
    @click.stop="stopEvent"
    v-if="modelValue"
  >
    <view class="loading-animation">
      <view
        v-for="color in colors"
        :key="color"
        :style="{ backgroundColor: color }"
        class="ball"
      ></view>
    </view>
  </view>
</template>

<script setup>
import { defineProps, watchEffect } from 'vue'

const props = defineProps({
  modelValue: Boolean,
})

const stopEvent = (event) => {
  event.preventDefault() // 阻止默认行为
  event.stopPropagation() // 阻止事件冒泡
}

const preventTouchMove = (event) => {
  event.preventDefault()
}

// H5和APP端清除点击事件
// #ifdef H5 || APP-PLUS
watchEffect(() => {
  if (props.modelValue) {
    document.addEventListener('touchmove', preventTouchMove, { passive: false })
  } else {
    document.removeEventListener('touchmove', preventTouchMove)
  }
})
// #endif

const colors = ['#FF6347', '#4682B4', '#32CD32', '#FFD700', '#FF69B4', '#00FA9A']
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: auto; /* 添加这行代码 */
  background: rgba(0, 0, 0, 0.5);
}

.loading-animation {
  display: flex;
  gap: 5px;
  justify-content: space-around;
  width: 100px;
}

.ball {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  animation: bounce 1.2s infinite ease-in-out;
}

.ball:nth-child(1) {
  animation-delay: -0.32s;
}
.ball:nth-child(2) {
  animation-delay: -0.16s;
}
.ball:nth-child(3) {
  animation-delay: -0.13s;
}
.ball:nth-child(4) {
  animation-delay: -0.07s;
}
.ball:nth-child(5) {
  animation-delay: -0.04s;
}
.ball:nth-child(6) {
  animation-delay: 0s;
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}
</style>
