<route lang="json5">
{
  style: {
    navigationBarTitleText: '客服',
  },
}
</route>
<template>
  <view class="chat-container">
    <view
      v-for="message in messages"
      :key="message.id"
      class="message"
      :class="{
        mine: message.isMine,
        customer: message.type === 'customer',
        support: message.type === 'support',
      }"
    >
      <view class="message-avatar">
        <image class="avatar" :src="message.avatar" />
      </view>
      <view class="message-body">
        <view class="message-content">
          {{ message.text }}
        </view>
        <view class="message-time">
          {{ message.time }}
        </view>
      </view>
    </view>
    <view class="input-area">
      <input class="input-text" v-model="newMessage" type="text" placeholder="输入消息..." />
      <button @click="sendMessage">发送</button>
    </view>
  </view>
</template>
<script setup>
import { ref } from 'vue'

const messages = ref([
  {
    id: 1,
    text: '今天天气不错',
    time: '15:24',
    isMine: false,
    type: 'support',
    avatar: '/acdr/src/static/my/avatar.jpg',
  },
  {
    id: 2,
    text: '是啊，阳光明媚',
    time: '15:25',
    isMine: true,
    type: 'customer',
    avatar: '/acdr/src/static/my/avatar.jpg',
  },
])

const newMessage = ref('')

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    messages.value.push({
      id: messages.value.length + 1,
      text: newMessage.value,
      time: new Date().toLocaleTimeString().slice(0, 5),
      isMine: true, // Assume the new message is always from the customer
      type: 'customer',
      avatar: '/acdr/static/customer/avatar.png',
    })
    newMessage.value = '' // Clear input after sending
  }
}
</script>
<style lang="scss" scoped>
.chat-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  padding-top: 10px;

  .message {
    display: flex;
    margin-bottom: 10px;
  }

  .message.support {
    flex-direction: row;
  }

  .message.customer {
    flex-direction: row-reverse;
  }

  .message-avatar {
    margin: 0 10px;
    border-radius: 50%;

    .avatar {
      width: 40px;
      height: 40px;
    }
  }

  .message-body {
    display: flex;
    flex-direction: column;
    gap: 10px;
    align-items: flex-end; /* 使所有子元素在容器的右侧对齐 */
    width: 50%;
  }

  .mine .message-content {
    background-color: #fcd038;
  }

  .support .message-content {
    align-self: flex-start;
    background-color: #f1f0f0;
  }

  .customer .message-content {
    align-self: flex-end;
    background-color: #fcd038;
  }

  .message-content {
    max-width: 70%;
    padding: 8px 10px;
    border-radius: 10px;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
  }

  .message-time {
    align-self: flex-end;
    font-size: 0.75rem;
    color: #666;
  }

  .input-area {
    position: absolute;
    bottom: 300rpx;
    display: flex;
    flex-direction: column;
    width: 100%;
    padding: 10px;
    border-top: 1px solid #ccc;

    .input-text {
      flex-grow: 1;
      padding: 10px;
      border: none;
      border-radius: 20px;
    }
  }

  .input-area button {
    position: absolute;
    right: 20rpx;
    bottom: -200rpx;
    width: 240rpx;
    height: 80rpx;
    color: white;
    cursor: pointer;
    background-color: #fcd038;
    border: none;
    border-radius: 40rpx;
  }
}
</style>
