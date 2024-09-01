<route lang="json5">
{
  style: {
    navigationBarTitleText: "消息列表",
  },
}
</route>
<template>
  <view v-if="messages.length > 0" class="message-list">
    <view v-for="message in messages" :key="message.id" class="message-card">
      <view class="message-header">
        <text class="message-title">{{ message.title }}</text>
        <text class="message-date">{{ message.createTime }}</text>
      </view>
      <view class="message-content">
        {{ truncateString(message.content) }}
      </view>
    </view>
  </view>
  <view v-else>
    <EmptyState />
  </view>
</template>

<script lang="js" setup>
import { truncateString } from "@/utils/strings";
import EmptyState from "@/components/EmptyState.vue";
import { httpGet, httpPost } from "@/utils/http";
import { toast } from "@/utils/commUtils";
import { useMessageStore } from "@/store/message";


const messages = ref([])
const useMessage = useMessageStore()

const messageList = async () => {
  try {
    const res = await httpGet("/notifications/list");
    if(res.code == 200) {
      messages.value = res.data
    } else {
      toast(res.message)
    }
  } catch(e) {
    toast(e.data?.message || "消息请求失败")
  }
};

// 把所有未读的消息标记为已读
const markAllAsRead = async () => {
  try {
    let messageIdList = useMessage.messages.unreadMessages.map(item => item.id)
    const res = await httpPost("/notifications/readList",messageIdList);
    if(res.code == 200) {
      // toast("消息阅读成功")
    } else {
      toast(res.message)
    }
  } catch(e) {
    toast(e.data?.message || "消息阅读失败")
  }
};

onLoad(async ()=> {
  await messageList()
  await markAllAsRead()
})
</script>

<style lang="scss" scoped>
.message-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px;
  margin: 20px;
}

.message-card {
  position: relative; /* 为了定位通知数字 */
  padding: 15px;
  background-color: #fff; /* 白色背景 */ /* 圆角边框 */
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.message-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 5px;
}

.message-title {
  font-weight: bold;
  color: #333; /* 标题颜色加深 */
}

.message-date {
  font-size: 0.8rem;
  color: #999; /* 日期使用更淡的颜色 */
}

.message-notification {
  position: absolute;
  top: 10px;
  right: 10px; /* 通知数字定位在右上角 */
  padding: 2px 6px;
  font-size: 0.75rem;
  color: white;
  background-color: red;
  border-radius: 50%;
}

.message-content {
  color: #666; /* 内容文字颜色 */
}
</style>
