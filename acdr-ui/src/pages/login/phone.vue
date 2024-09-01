<route lang="json5">
{
  style: {
    navigationBarTitleText: '手机号登录',
    navigationStyle: 'custom',
  },
}
</route>

<template>
  <TopBar/>
  <view class="login-phone">
    <image class="background" :src="imgUrl('@/static/login/login.png')" mode="aspectFill"></image>
    <view class="content">
      <view class="input-container">
        <view class="">
          <input class="input" type="text" placeholder="请输入手机号" v-model="phoneNumber" />
        </view>
        <view class="verification-container mt-[20px]">
          <input
            class="input verification-input"
            type="text"
            placeholder="请输入验证码"
            v-model="code"
          />
          <button class="get-code-button" @click="sendCode" :disabled="countdown > 0">
            {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
          </button>
        </view>
      </view>
      <button class="login-button" @click="login">立即登录</button>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { httpGet, httpPost } from '@/utils/http'
import { useUserStore } from '@/store/user'
import TopBar from '@/components/TopBar.vue'
import { imgUrl } from '@/utils/commUtils'

const userStore = useUserStore()

const phoneNumber = ref('')
const code = ref('')
const countdown = ref(0)
let timer = null

const sendCode = async () => {
  if (countdown.value === 0) {
    if (!phoneNumber.value) {
      uni.showToast({
        title: '请输入手机号',
        icon: 'none',
      })
      return
    }
    console.log('发送验证码到', phoneNumber.value)
    const res = await httpGet('/public/getCode', { phone: phoneNumber.value })
    console.log(res)
    countdown.value = 60
    if (res.code == 200) {
      uni.showToast({
        title: res.message,
        icon: 'none',
      })
    } else {
      uni.showToast({
        title: res.message,
        icon: 'none',
      })
    }
    timer = setInterval(() => {
      countdown.value -= 1
      if (countdown.value === 0) {
        clearInterval(timer)
      }
    }, 1000)
  }
}

const login = async () => {
  if (!phoneNumber.value) {
    uni.showToast({
      title: '请输入手机号',
      icon: 'none',
    })
    return
  }
  if (!code.value) {
    uni.showToast({
      title: '请输入验证码',
      icon: 'none',
    })
    return
  }
  const res = await httpPost('/public/login', {}, { phone: phoneNumber.value, code: code.value })
  if (res.code == 200) {
    uni.showToast({
      title: res.message,
      icon: 'none',
    })
    userStore.setUserInfo({ token: res.data.userToken, shopLoginUser: res.data.shopLoginUser })
    const userRes = await httpGet('/user/userinfo')
    if (userRes.code == 200) {
      userStore.setUserInfo(userRes.data)
      uni.switchTab({
        url: '/pages/index/index',
      })
    } else {
      uni.showToast({
        title: userRes.message,
        icon: 'none',
      })
    }
  } else {
    uni.showToast({
      title: res.message,
      icon: 'none',
    })
  }
}

onMounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style lang="scss" scoped>
.login-phone {
  @apply relative w-full h-screen bg-[#fff7e9];
}

.background {
  @apply w-full h-full;
}

.content {
  @apply absolute top-[50%] left-1/2 transform -translate-x-1/2 w-[90%] bg-white rounded-lg p-6 shadow-lg;
}

.logo {
  @apply w-full h-[120px] mb-6;
}

.input-container {
  @apply flex flex-col space-y-4;
}

.input {
  @apply w-full text-lg border border-gray-300 rounded-lg;
}

.verification-container {
  @apply flex items-center space-x-4;
}

.verification-input {
  @apply flex-1;
}

.get-code-button {
  @apply w-[120px] p-2 bg-[#FCCB30] text-black text-sm rounded-lg;
}

.login-button {
  @apply w-full mt-6 p-3 bg-[#FCCB30] text-black text-lg rounded-lg;
}
</style>
