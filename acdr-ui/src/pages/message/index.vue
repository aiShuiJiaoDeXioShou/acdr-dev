<route lang="json5">
{
  style: {
    navigationBarTitleText: "消息",
  },
}
</route>
<template>
  <view class="container">
    <view
      @click="toPath(message.path)"
      v-for="(message, index) in messages"
      :key="index"
      class="message-item"
    >
      <view class="icon-wrapper" :class="message.iconWrapperClass">
        <image :src="imgUrl(message.icon)" class="icon"></image>
      </view>
      <view class="content">
        <view class="title">{{ message.title }}</view>
        <view class="description">{{ message.description }}</view>
      </view>
      <view v-if="message.badge" class="badge-wrapper">
        <view class="badge">{{ message.badge }}</view>
      </view>
    </view>
  </view>
</template>

<script lang="js" setup>
import { useMessageStore } from '@/store/message';
import { imgUrl } from '@/utils/commUtils';

const useMessage = useMessageStore()

const messages = ref([
  {
    title: '订阅消息',
    description: '微信消息订阅，避免错过重要通知',
    icon: '@/static/message/subscribed.png',
    iconWrapperClass: '',
    badge: useMessage.messages.unreadMessages.length,
    path: '/pages/message/message-list',
  },
  {
    title: '在线客服',
    description: '工作时间：10:00-18:00',
    icon: '@/static/message/maotou_kefu.png',
    iconWrapperClass: 'icon-wrapper-2',
    badge: null,
    path: '/pages/message/chat',
  },
])




const toPath = (path) => {
  uni.navigateTo({
    url: path,
  })
}
</script>

<style scoped>
.container {
  height: 100vh;
  padding: 16px;
  background-color: #f8f8f8;
}

.message-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 12px;
  margin-bottom: 10px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.icon-wrapper {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  margin-right: 12px;
  background-color: #f5d143;
  border-radius: 50%;
}

.icon-wrapper-2 {
  background-color: #ffb7b9;
}

.icon {
  width: 24px;
  height: 24px;
}

.content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  gap: 10px;
}

.title {
  font-size: 16px;
  color: #333;
}

.description {
  font-size: 14px;
  color: #999;
}

.badge-wrapper {
  flex-shrink: 0;
  margin-top: -30px;
  margin-left: auto;
}

.badge {
  padding: 4px 8px;
  font-size: 12px;
  color: #ffffff;
  background-color: #f44336;
  border-radius: 50%;
}
</style>
