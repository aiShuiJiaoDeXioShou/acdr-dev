<script setup lang="js">
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'
import { notificationService } from './service/notificationService'

// 在应用启动时注册轮询服务
onLaunch(() => {
  console.log('App Launch');
  notificationService.startPollingUnreadMessages();
})

// 当应用进入前台时继续轮询服务
onShow(() => {
  console.log('App Show');
  notificationService.startPollingUnreadMessages();
})

// 当应用进入后台时停止轮询服务
onHide(() => {
  console.log('App Hide');
  notificationService.stopPollingUnreadMessages();
})
</script>

<style lang="scss">
/* stylelint-disable selector-type-no-unknown */
button::after {
  border: none;
}

swiper,
scroll-view {
  flex: 1;
  height: 100%;
  overflow: hidden;
}

image {
  width: 100%;
  height: 100%;
  vertical-align: middle;
}

// 单行省略，优先使用 unocss: text-ellipsis
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

// 两行省略
.ellipsis-2 {
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

// 三行省略
.ellipsis-3 {
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
