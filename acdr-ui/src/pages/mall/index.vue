<route lang="json5" type="page">
{
  layout: 'default',
  style: {
    navigationBarTitleText: '宠物商城',
  },
}
</route>

<template>
  <view class="mall-root">
    <!-- 当 webviewSrc 有值时才渲染 WebView -->
    <web-view v-if="webviewSrc" id="mall-webview" :src="webviewSrc"></web-view>
    <view v-else class="loading">
      <text>加载中...</text>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import { toast } from '@/utils/commUtils'
import { httpPost } from '@/utils/http'
import { useUserStore } from '@/store'

const webviewSrc = ref('')
const shopUserData = ref({})

const userStore = useUserStore()
const shopUserInfo = async () => {
  if (userStore.userInfo.shopLoginUser) {
    shopUserData.value = userStore.userInfo.shopLoginUser
    webviewSrc.value = `http://localhost:3000/?token=${shopUserData.value.accessToken}&refresh-token=${shopUserData.value.refreshToken}`
    return
  }
  try {
    const shopUser = await httpPost('/shopLogin')
    if (shopUser && shopUser.code === 200) {
      // 成功获取用户信息后，设置 WebView 的 src
      webviewSrc.value = `http://localhost:3000/?token=${shopUserData.value.accessToken}&refresh-token=${shopUserData.value.refreshToken}`
      shopUserData.value = shopUser.data
      userStore.setUserInfo({ shopLoginUser: shopUserData.value })
      onWebViewLoad()
    } else {
      // 获取用户信息失败，显示提示信息
      toast(shopUser.message || '获取商城用户信息失败, 无法跳转到商城, 请联系管理员！')
    }
  } catch (e) {
    // 捕获并显示错误信息
    toast(e.data.message || '发生错误，请稍后重试！')
  }
}

// 在组件挂载时调用 shopUserInfo 方法
onLoad(async () => {
  await shopUserInfo()
})
</script>

<style lang="scss" scoped>
.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}
</style>
