<template>
  <view class="loading-overlay" v-if="modelValue">
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
import { defineProps } from 'vue'

const props = defineProps({
  modelValue: Boolean,
})

const colors = ['#FF6347', '#4682B4', '#32CD32', '#FFD700', '#FF69B4', '#00FA9A']
</script>

<style scoped>
.loading-overlay {
  display: flex;
  align-items: center;
  justify-content: center;
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
