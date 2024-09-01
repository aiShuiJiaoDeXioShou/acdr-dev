<route lang="json5">
{
  style: {
    navigationBarTitleText: "互动空间",
    navigationStyle: "custom",
  },
}
</route>

<template>
  <view class="space-root">
    <view class="index-bg"></view>
    <image class="rootbg" :src="imgUrl('@/static/space/bg.png')" mode="widthFix"></image>
    <view class="ai-space" @click="toPath('/pages/ai/ai-interactive-space')">
      <image
        :src="imgUrl('@/static/space/ai-space-button.png')"
        class="ai-space-button"
      ></image>
      <view class="ai-space-text">
        您已经互动了
        <text class="orange">10次,</text>
        还有
        <text class="orange">2次~</text>
        <wd-icon class="arrow" name="arrow-right"></wd-icon>
      </view>
    </view>
    <view class="actions">
      <image
        @tap="actions"
        :src="imgUrl('@/static/space/doctor.png')"
        class="action_doctor"
        mode="widthFix"
      ></image>
      <image
        @tap="actions"
        :src="imgUrl('@/static/space/talent.png')"
        class="action_talent"
        mode="widthFix"
      ></image>
    </view>
    <view class="calendar">
      <view class="calendar-number">
        <text class="calendar-moth">7月</text>
        <text class="calendar-day">17</text>
        <text class="calendar-description">今日任务</text>
        <image
          class="calendar-bg"
          :src="imgUrl('@/static/space/calendar-number.png')"
        ></image>
      </view>
      <image class="c-bg" :src="imgUrl('@/static/space/calendar.png')"></image>
    </view>
    <view class="tabs-container">
      <view class="tabs">
        <view
          v-for="(tab, index) in tabs"
          :key="index"
          :class="['tab', { active: activeTab === index }]"
          @tap="selectTab(index)"
        >
          {{ tab }}
          <view v-if="activeTab === index" class="underline"></view>
        </view>
      </view>
      <view class="camera-icon" @click="pushTo">
        <wd-icon @click="camera" class="camera" name="camera"></wd-icon>
      </view>
    </view>
    <view class="posts-container">
      <scroll-view scroll-x="true">
        <view class="scroll-container" v-if="focusUser.length > 0">
          <view class="user-item" v-for="(user, index) in focusUser" :key="index">
            <wd-img :round="true" :src="imgUrl(user.avatar)" class="avatar"></wd-img>
            <text class="name">{{ user.name }}</text>
          </view>
        </view>
        <view v-else>
          <view class="flex items-center justify-center h-[40px] color-gray"
            >暂无内容...</view
          >
        </view>
      </scroll-view>
      <scroll-view scroll-y="true">
        <view v-for="post in postsList[activeTab == 0 ? 'posts' : 'followedPosts']">
          <UserPost :post="post" />
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import UserPost from './components/UserPost.vue'
import { httpGet } from '@/utils/http'
import { imgUrl, toast } from '@/utils/commUtils'

const tabs = ref(['推荐', '关注'])
const activeTab = ref(0)
const focusUser = ref([])
const postsList = ref(null)

// 测试数据
// focusUser = ref([
//   { name: '曲三岁', avatar: '/static/my/pet-auatar.jpg' },
//   { name: '王小样', avatar: '/static/my/pet-auatar-2.jpg' },
//   { name: '碎碎念', avatar: '/static/my/pet-auatar.jpg' },
//   { name: '小雨离线', avatar: '/static/my/pet-auatar-2.jpg' },
//   { name: '王小样', avatar: '/static/my/pet-auatar.jpg' },
//   { name: '碎碎念', avatar: '/static/my/pet-auatar-2.jpg' },
// ])

const user = ref({
  name: '王小样',
  avatar: '/static/my/pet-auatar.jpg',
  location: '不拉多尔-泡芙',
})

const post = ref({
  time: '1分钟前',
  images: [
    '/static/my/pet-auatar.jpg',
    '/static/my/pet-auatar-2.jpg',
    '/static/my/pet-auatar.jpg',
    '/static/my/pet-auatar-2.jpg',
    '/static/my/pet-auatar.jpg',
  ],
  content: '狗狗是人类的最好的朋友，它们忠诚可靠，守护着他们的主人不离不弃。甚至，比生命更为重要。',
  likes: 289,
  comments: 36,
  shares: 14,
})

// 获取关注博主的数据
const focus = async () => {
  try {
    const res = await httpGet('/posts/followed-users')
    if (res.code == 200) {
      focusUser.value = res.data
    } else {
      toast(res.message || '数据获取失败')
    }
  } catch (error) {
    toast(error.data.message || '获取关注博主数据失败')
  }
}

// 获取帖子数据
const getPosts = async () => {
  try {
    const res = await httpGet('/posts/list')
    if (res.code == 200) {
      postsList.value = res.data
    } else {
      toast(res.message || '数据获取失败')
    }
  } catch (error) {
    toast(error.data.message || '获取关注博主数据失败')
  }
}

const selectTab = (index) => {
  activeTab.value = index
  console.log(index)
}

const actions = () => {
  console.log('actions')
}

const camera = () => {
  console.log('camera')
}

const pushTo = () => {
  uni.navigateTo({
    url: '/pages/push/share',
  })
}

const toPath = (path) => {
  uni.navigateTo({
    url: path,
  })
}

onLoad(async ()=> {
  await focus()
  await getPosts()
})
</script>

<style lang="scss" scoped>
.main-title-color {
  color: #d14328;
}

.space-root {
  width: 100%;
  height: 100vh;

  .index-bg {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
    height: 100%;
    background-color: #f5f5f5;
  }

  .rootbg {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
  }

  .ai-space {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    padding-top: 350rpx;

    .ai-space-button {
      width: 100%;
      height: 120rpx;
      background-repeat: no-repeat;
      background-size: contain;
    }

    .orange {
      color: #ff9775;
    }

    .ai-space-text {
      position: absolute;
      top: 85%;
      left: 60%;
      width: 100%;
      text-align: center;
      transform: translate(-50%, -50%);

      .arrow {
        position: absolute;
        top: 50%;
        right: 15%;
        color: #9d9d9d;
        transform: translateY(-50%);
      }
    }
  }

  .actions {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 18%;

    .action_doctor {
      width: 100%;
    }

    .action_talent {
      width: 100%;
    }
  }

  .calendar {
    position: relative;
    width: 100%;
    height: 26%;

    .c-bg {
      position: absolute;
      top: -3%;
      z-index: -1;
      width: 100%;
      height: 100%;
    }

    .calendar-number {
      width: 131rpx;
      height: 159rpx;

      .calendar-bg {
        position: absolute;
        top: 50rpx;
        left: 50rpx;
        // z-index: 9;
        width: 90px;
        height: 110px;
      }
    }

    .calendar-moth {
      position: absolute;
      top: 31px;
      left: 58px;
      z-index: 1;
      font-weight: 600;
    }

    .calendar-day {
      position: absolute;
      top: 78px;
      left: 63px;
      z-index: 1;
      font-weight: 600;
    }

    .calendar-description {
      position: absolute;
      top: 109px;
      bottom: 0;
      left: -134px;
      z-index: 1;
      width: 100%;
      font-size: 15px;
      color: #808080;
      text-align: center;
    }
  }

  .tabs-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 20px;
    background-color: #fff;
    border-bottom: 1px solid #eee;

    .tabs {
      display: flex;
      align-items: center;
    }

    .tab {
      position: relative;
      padding: 10px 20px;
      font-size: 16px;
      color: #666;
      cursor: pointer;

      &.active {
        font-weight: bold;
        color: #000;
      }
    }

    .underline {
      position: absolute;
      bottom: 0;
      left: 50%;
      width: 20px;
      height: 2px;
      background-color: #fcd038;
      border-radius: 1px;
      transform: translateX(-50%);
    }

    .camera-icon {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .posts-container {
    padding-bottom: 100px;
  }

  .scroll-container {
    display: flex;
    padding: 10px;
    padding-left: 20px;
    white-space: nowrap;
    background-color: #fff;
  }

  .user-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 20px;
  }

  .avatar {
    width: 60px;
    height: 60px;
    border: 2px solid #ffd700;
    border-radius: 50%;
  }

  .name {
    margin-top: 8px;
    font-size: 14px;
    color: #333;
    text-align: center;
  }
}
</style>
