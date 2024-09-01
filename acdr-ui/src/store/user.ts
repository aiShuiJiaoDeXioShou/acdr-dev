import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const initState = {
  nickname: '',
  avatar: '',
  id: null,
  name: '',
  phone: '',
  email: '',
  typeId: '',
  createTime: '',
  updateTime: '',
  token: '',
  sex: '',
  openid: '', // 微信openid，如果不为空就证明用户是用微信登录的
  isRealName: false,
  isPetNursery: false,
  shopLoginUser: null,
}

export const useUserStore = defineStore(
  'user',
  () => {
    const userInfo = ref({ ...initState })

    const setUserInfo = (val) => {
      userInfo.value = {
        ...userInfo.value,
        ...val,
      }
    }

    const clearUserInfo = () => {
      userInfo.value = { ...initState }
    }

    // 一般没有reset需求，不需要的可以删除
    const reset = () => {
      userInfo.value = { ...initState }
    }

    const isLogined = computed(() => userInfo.value.token != '')

    return {
      userInfo,
      setUserInfo,
      clearUserInfo,
      isLogined,
      reset,
    }
  },
  {
    persist: true,
  },
)
