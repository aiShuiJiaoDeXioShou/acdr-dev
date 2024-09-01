<template>
  <view class="recommended-service-card" :style="{ height: height + 'px' }">
    <image :src="image" class="recommended-service-image" @load="updateHeight" />
    <view class="text-container">
      <text class="recommended-service-text">{{ title }}</text>
      <text class="recommended-service-description">{{ description }}</text>
      <br />
      <text class="recommended-service-price">{{ price }}</text>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref, defineProps, onMounted } from "vue";

const props = defineProps({
  image: String,
  title: String,
  description: String,
  price: String,
});

const minHeight = 300;
const height = ref(minHeight);

const updateHeight = (event: any) => {
  const { width, height: imgHeight } = event.detail;
  height.value = (imgHeight / width) * minHeight;
};
</script>

<style lang="scss" scoped>
.recommended-service-card {
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-bottom: 15px;
  overflow: hidden;
  background-color: #fff;
  border-radius: 10px;
}

.recommended-service-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.text-container {
  bottom: 0;
  width: 100%;
  min-height: 120px;
  padding: 10px;
  background: rgba(255, 255, 255, 0.8);
}

.recommended-service-text {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
}

.recommended-service-description {
  font-size: 11px;
  color: #888;
  text-align: center;
}

.recommended-service-price {
  font-size: 16px;
  color: #ff7043;
  text-align: center;
}
</style>
