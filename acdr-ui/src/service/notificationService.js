import { useUserStore } from '@/store/user'
import { useMessageStore } from '@/store/message'
import { httpGet, httpPost } from '@/utils/http'

const POLLING_INTERVAL = 60000 // 6秒轮询一次
let pollingTimer = null

const startPollingUnreadMessages = () => {
  try {
    const userStore = useUserStore()
    const messageStore = useMessageStore()

    // 检查用户是否已登录
    if (!userStore.userInfo.token) {
      console.log('用户未登录，停止轮询请求')
      return
    }

    // 防止多次调用导致重复轮询
    if (pollingTimer) return

    pollingTimer = setInterval(() => {
      if (!userStore.userInfo.token) {
        console.log('用户未登录，停止轮询请求')
        stopPollingUnreadMessages()
        return
      }

      httpGet('/notifications')
        .then((response) => {
          if (response.code === 200 && response.data.length > 0) {
            console.log('未读消息:', response.data)
            // 将获取到的未读消息存储到 message store 中
            messageStore.setUnreadMessages(response.data)
          }
        })
        .catch((error) => {
          console.error('获取未读消息失败:', error)
        })
    }, POLLING_INTERVAL)
  } catch (error) {
    console.error('Pinia 未初始化或其他错误:', error)
  }
}

const stopPollingUnreadMessages = () => {
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
  }
}

const markMessageAsRead = (messageId) => {
  try {
    const userStore = useUserStore()
    const messageStore = useMessageStore()

    // 检查用户是否已登录
    if (!userStore.userInfo.token) {
      console.log('用户未登录，无法标记消息为已读')
      return Promise.reject('用户未登录')
    }

    return httpPost('/notifications', {}, { messageId })
      .then((response) => {
        if (response.code === 200) {
          console.log('消息已读:', messageId)
          // 在成功标记消息为已读后，将消息从未读列表移到已读列表中
          messageStore.markMessageAsRead(messageId)
        }
      })
      .catch((error) => {
        console.error('标记消息为已读失败:', error)
      })
  } catch (error) {
    console.error('Pinia 未初始化或其他错误:', error)
  }
}

// 将服务导出，供全局使用
export const notificationService = {
  startPollingUnreadMessages,
  stopPollingUnreadMessages,
  markMessageAsRead,
}
