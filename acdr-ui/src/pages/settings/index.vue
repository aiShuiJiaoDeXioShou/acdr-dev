<route lang="json5">
{
  style: {
    navigationBarTitleText: '设置页面',
  },
  needLogin: true,
}
</route>

<template>
  <view class="setting-root bg-[#F5F5F5] h-full p-5">
    <!-- 国际化设置 -->
    <view class="card mb-4 p-4 bg-white rounded-lg shadow">
      <view class="text-gray-800 text-lg mb-2">国际化设置</view>
      <wd-picker
        :columns="languageOptions"
        label="选择语言"
        v-model="selectedLanguage"
        @confirm="handleLanguageChange"
      />
    </view>

    <!-- 账户设置 -->
    <view class="card mb-4 p-4 bg-white rounded-lg shadow">
      <view class="text-gray-800 text-lg mb-2">账户设置</view>
      <view class="wd-cell" @click="goToBindPhone">
        <text class="text-gray-800">手机号绑定</text>
        <text class="text-gray-400 ml-auto">191****0915</text>
        <wd-icon name="right" size="16" class="ml-2"></wd-icon>
      </view>
      <view class="wd-cell" @click="goToRealNameAuth">
        <text class="text-gray-800">实名认证</text>
        <text class="text-gray-400 ml-auto">已实名</text>
        <wd-icon name="right" size="16" class="ml-2"></wd-icon>
      </view>
    </view>

    <!-- 退出登录按钮 -->
    <view class="card mb-4 p-4 bg-white rounded-lg shadow">
      <button class="logout-button" @click="logout">退出登录</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useConfigStore } from '../../store/config'
import { useUserStore } from '@/store'
import { httpGet } from '@/utils/http'

const configStore = useConfigStore()
const userStore = useUserStore()

const languages = configStore.languages
const selectedLanguage = ref<string>(languages['zh-Hans'])

const languageOptions = computed(() =>
  Object.entries(languages).map(([key, value]) => ({
    value: key,
    label: value,
  })),
)

const handleLanguageChange = ({ value }) => {
  configStore.changeLanguage(selectedLanguage.value)
}

const goToRealNameAuth = () => {
  uni.navigateTo({
    url: '/pages/permission/real-name-auth',
  })
}

const goToBindPhone = () => {
  uni.navigateTo({
    url: '/pages/permission/bind-phone',
  })
}

const logout = async () => {
  // 发送后台请求
  const logRes = await httpGet('/user/logout')
  if (logRes.code == 200) {
    uni.showToast({ title: '退出成功', icon: 'none' })
  } else {
    uni.showToast({ title: logRes.msg, icon: 'none' })
  }
  // 清除用户信息
  userStore.clearUserInfo()
  // 跳转到登录页面
  uni.reLaunch({
    url: '/pages/login/index',
  })
}
</script>

<style lang="scss" scoped>
.setting-root {
  @apply bg-[#F5F5F5] h-full p-5;
  height: 100vh;
}

.card {
  @apply mb-4 p-4 bg-white rounded-lg shadow;
}

.wd-cell {
  @apply flex items-center py-4 px-5 border-b border-gray-200;
}

.wd-cell:last-child {
  @apply border-b-0;
}

.text-gray-800 {
  @apply text-gray-800;
}

.text-gray-400 {
  @apply text-gray-400;
}

.logout-button {
  @apply w-full py-3 bg-red-500 text-white text-lg rounded;
}
</style>
