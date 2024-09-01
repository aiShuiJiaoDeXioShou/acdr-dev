<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '登录',
    navigationStyle: 'custom',
  },
}
</route>

<template>
  <TopBar/>
  <view class="container">
    <image class="background" :src="imgUrl('@/static/login/login.png')" mode="aspectFill"></image>
    <view class="content">
      <button class="login-button" @click="login">微信快捷登录</button>
      <button class="login-button" @click="phoneLogin">手机号快捷登录</button>
      <view class="agreement">
        <wd-checkbox checked-color="#C09200" class="checkbox" v-model="agreed" @change="handleChange"></wd-checkbox>
        <text>我已认真阅读并同意</text>
        <navigator url="/pages/others/agreement" class="agreement-link">《服务条款》</navigator>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import PLATFORM from '@/utils/platform'
import { httpGet, httpPost } from '@/utils/http'
import { useUserStore } from '@/store/user'
import TopBar from '@/components/TopBar.vue'
import { imgUrl } from '@/utils/commUtils'

const userStore = useUserStore()
const agreed = ref(false)

const phoneLogin = () => {
  if (!agreed.value) {
    uni.showModal({
      title: '提示',
      content: '请先阅读并同意服务条款',
      showCancel: false,
    })
    return
  }
  uni.navigateTo({ url: '/pages/login/phone' })
}

const handleChange = () => {}

// #ifdef MP-WEIXIN
async function login() {
  try {
    const wxres = await uni.login({ provider: 'weixin' })
    console.log(wxres)
    const res = await httpGet('/public/wxLogin', { code: wxres.code })
    if (res.code == 200) {
      // 登录成功！
      userStore.setUserInfo({ token: res.message })
      userStore.setUserInfo(res.data)
      // 跳转到首页
      uni.switchTab({
        url: '/pages/index/index',
      })
    }
  } catch (e) {
    uni.showToast({
      title: '获取登录操作失败！',
      icon: 'none',
    })
  }
}
// #endif

// #ifdef H5
function login() {
  if (!agreed.value) {
    uni.showModal({
      title: '提示',
      content: '请先阅读并同意服务条款',
      showCancel: false,
    })
    return
  }

  if (PLATFORM.isApp) {
    // 检查微信是否安装
    uni.getProvider({
      service: 'oauth',
      success: (res) => {
        if (res.provider.indexOf('weixin') !== -1) {
          wxlogin()
        } else {
          uni.showModal({
            title: '提示',
            content: '请先安装微信',
            showCancel: false,
          })
        }
      },
    })
  } else if (PLATFORM.isMp) {
    wxlogin()
  } else {
    h5Wxlogin()
  }
}

const h5Wxlogin = () => {
  const appId = 'wx9c4d903994e8b49f' // 替换为你的AppID
  const redirectUri = encodeURIComponent(window.location.origin + '/pages/login/wx-login-callback') // 设置你的回调地址
  const state = 'random_state' // 你可以设置任意状态值
  const scope = 'snsapi_userinfo'
  const wechatAuthUrl = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${redirectUri}&response_type=code&scope=${scope}&state=${state}#wechat_redirect`

  // 跳转到微信授权页面
  window.location.href = wechatAuthUrl
}

const wxlogin = () => {
  // 调用微信登录
  uni.login({
    provider: 'weixin',
    success: async (loginRes) => {
      console.log('登录成功', loginRes)
      // 发送code到后台换取openid和session_key
      const res = await httpPost('/wxUserInfo/login', {}, { code: loginRes.code })
      console.log(res)
    },
    fail: (err) => {
      console.error('微信登录失败：', err)
    },
  })
}

// 处理微信授权回调
const handleWxLoginCallback = async () => {
  const urlParams = new URLSearchParams(window.location.search)
  const code = urlParams.get('code')
  const state = urlParams.get('state')

  if (code) {
    try {
      const res = await httpPost('/wxUserInfo/login', {}, { code })
      console.log('登录成功，用户信息：', res.data)
      // 在这里可以保存用户信息到本地或者进行页面跳转
    } catch (err) {
      console.error('服务器请求失败：', err)
    }
  } else {
    console.error('未能获取到微信登录的code参数')
  }
}

// 检查是否是微信授权回调
if (window.location.pathname.endsWith('/pages/login/wx-login-callback')) {
  handleWxLoginCallback()
}
// #endif
</script>

<style scoped>
.container {
  @apply relative w-full h-screen bg-gray-200;
}
.background {
  @apply w-full h-full;
}
.content {
  @apply absolute top-3/5 left-1/2 transform -translate-x-1/2 -translate-y-1/2;
  width: 70%;
}

.welcome {
  @apply text-center text-lg font-bold mb-4;
}
.login-button {
  height: 48px;
  line-height: 28px;
  @apply w-full py-2 bg-[#FCCB30] text-black text-[18px] rounded mb-4;
  border-radius: 24px;
}
.agreement {
  @apply flex items-center justify-center text-gray-500;
}
.checkbox {
  @apply mr-2;
}
.agreement-link {
  @apply text-blue-500 underline ml-1;
}
</style>
