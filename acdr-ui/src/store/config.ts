import { defineStore } from 'pinia'
import { ref } from 'vue'
import i18n from '@/locale'

// 定义主题颜色
const themeColors = {
  primary: '#3498db',
  secondary: '#2ecc71',
  success: '#28a745',
  danger: '#dc3545',
  warning: '#ffc107',
  info: '#17a2b8',
  light: '#f8f9fa',
  dark: '#343a40',
}

// 定义支持的语言
const languages = {
  en: 'English',
  'zh-Hans': '中文',
  es: 'Español',
  fr: 'Français',
  de: 'Deutsch',
}

const getDefaultTheme = () => uni.getStorageSync('theme') || themeColors.primary
const getDefaultLanguage = () => uni.getStorageSync('language') || i18n.global.locale.value

export const useConfigStore = defineStore('config', () => {
  const currentTheme = ref(getDefaultTheme())
  const currentLanguage = ref(getDefaultLanguage())

  const changeTheme = (color: string) => {
    if (themeColors[color]) {
      currentTheme.value = themeColors[color]
      uni.setStorageSync('theme', themeColors[color]) // 保存主题到本地存储
    }
  }

  const changeLanguage = (lang: string) => {
    if (languages[lang]) {
      currentLanguage.value = lang
      i18n.global.locale = lang
      uni.setStorageSync('language', lang) // 保存语言设置到本地存储
    }
  }

  return {
    themeColors,
    currentTheme,
    languages,
    currentLanguage,
    changeTheme,
    changeLanguage,
  }
})

export default i18n
