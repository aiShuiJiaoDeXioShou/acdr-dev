import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 初始化状态
const initState = {
  unreadMessages: [], // 未读消息列表
  readMessages: [], // 已读消息列表
}

export const useMessageStore = defineStore('message', () => {
  const messages = ref({ ...initState })

  // 设置未读消息
  const setUnreadMessages = (newMessages) => {
    messages.value.unreadMessages = newMessages;
  }

  // 标记消息为已读
  const markMessageAsRead = (messageId) => {
    const index = messages.value.unreadMessages.findIndex(msg => msg.id === messageId)
    if (index !== -1) {
      const readMessage = messages.value.unreadMessages.splice(index, 1)[0]
      messages.value.readMessages.push(readMessage)
    }
  }

  // 获取未读消息数量
  const unreadCount = computed(() => messages.value.unreadMessages.length)

  // 清空所有消息（用于重置或清理）
  const clearMessages = () => {
    messages.value = { ...initState }
  }

  return {
    messages,
    setUnreadMessages,
    markMessageAsRead,
    unreadCount,
    clearMessages,
  }
})
