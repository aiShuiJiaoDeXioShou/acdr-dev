<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationStyle: 'custom',
  },
}
</route>

<template>
  <view class="popup-content">
    <image class="popup-image" :src="imgUrl('@/static/certification/nursery.png')" mode="heightFix" />
    <view class="text-center text-lg mb-4">您还未认证，加入我们成为宠托师！</view>
    <view class="flex justify-around">
      <button class="cancel-button" @click="handleReturn">返回</button>
      <button class="join-button" @click="handleJoin">加入</button>
    </view>
  </view>
</template>

<script lang="js" setup>
import { imgUrl } from '@/utils/commUtils'

const emit = defineEmits(['handleReturn', 'handleJoin'])
const handleReturn = () => {
  // 调用父类方法
  emit('handleReturn')
}

const handleJoin = () => {
  // 调用父类方法
  emit('handleJoin')
}
</script>

<style lang="scss" scoped>
.popup-content {
  padding: 20px;
  text-align: center;
  background-color: white;
  border-radius: 10px;
}

.popup-image {
  height: 430px;
  margin: 0 auto 20px;
}

.cancel-button,
.join-button {
  width: 127px;
  height: 48px;
  border-radius: 24px;
}

.cancel-button {
  background-color: white;
  border: 1px solid #ccc;
  color: black;
}

.join-button {
  background-color: #fccb30;
  color: black;
}
</style>
