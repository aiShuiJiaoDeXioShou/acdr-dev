<route lang="json5">
{
  style: {
    navigationBarTitleText: "我的页面",
    navigationStyle: "custom",
  },
  needLogin: true,
}
</route>

<template>
  <view class="my-page-container">
    <view
      class="background"
      :style="{ backgroundImage: `url(${imgUrl('@/static/my/my-bg.png')})` }"
    ></view>
    <image :src="imgUrl('@/static/my/cat-dog.png')" class="cat-dog"></image>
    <view class="bg"></view>
    <view class="my-avatar">
      <wd-img
        :width="100"
        :height="100"
        round
        mode="aspectFill"
        :src="
          userInfo.avatar == ''
            ? imgUrl('@/static/my/avatar.jpg')
            : baseUrl + userInfo.avatar
        "
      ></wd-img>
      <view class="info">
        <view class="name">{{ userInfo.nickname }}</view>
        <view class="description">爱猫猫，爱狗狗</view>
      </view>
    </view>

    <view class="stats-container card">
      <view class="stat-item" v-for="(item, index) in stats" :key="index">
        <view class="stat-number">{{ item.number }}</view>
        <view class="stat-label">{{ item.label }}</view>
      </view>
    </view>

    <view class="services-container card">
      <view
        class="service-item"
        v-for="(service, index) in services"
        @click="toPath(service.path)"
        :key="index"
      >
        <view class="service-box">
          <wd-img
            v-if="service.label == '我的服务'"
            :width="42"
            :height="40"
            :src="imgUrl(service.icon)"
            class="service-icon"
          />
          <wd-img
            v-else
            :width="38"
            :height="40"
            :src="imgUrl(service.icon)"
            class="service-icon"
          />
        </view>
        <view class="service-label">{{ service.label }}</view>
      </view>
    </view>

    <!-- 判断该用户是否为宠托师 -->
    <view v-if="userInfo.isPetNursery" class="services-container card">
      <button class="w-full bg-[#ffc107] mt-20" @click="toPath('/pages/order/take')">
        接单页面
      </button>
    </view>

    <view class="pets-container card">
      <view class="pets-title">我的宠物</view>
      <view class="pets-list scroll-x overflow-x-auto" scroll-x>
        <view
          class="pet-item"
          v-for="(pet, index) in pets"
          @click="editPet(pet)"
          :key="index"
        >
          <wd-img :width="60" :height="60" round :src="pet.icon" class="pet-avatar" />
          <view class="pet-label">{{ pet.name }}</view>
        </view>
        <view class="pet-item add-pet" @click="toPath('/pages/pet/pet-add-page')">
          <w-avatar :size="80" class="pet-avatar add-avatar">
            <view class="add-icon">+</view>
          </w-avatar>
          <view class="pet-label">添加</view>
        </view>
      </view>
    </view>

    <view class="settings-container card">
      <view class="list">
        <view
          class="list-item"
          v-for="(item, index) in items"
          @click="toPath(item.to)"
          :key="index"
        >
          <view class="icon">
            <image :src="imgUrl(item.icon)" class="item-icon" />
          </view>
          <view class="label">{{ item.label }}</view>
          <view class="arrow">
            <wd-icon name="arrow-right"></wd-icon>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/store/user";
import { baseUrl, imgUrl } from "@/utils/commUtils";
import { httpGet } from "@/utils/http"; // 假设你已经封装好了这个方法

const userStore = useUserStore();

const userInfo = userStore.userInfo;

const stats = [
  { number: 0, label: "粉丝" },
  { number: 0, label: "关注" },
  { number: 0, label: "收藏" },
  { number: 0, label: "获赞" },
];

const services = [
  { icon: "@/static/my/order.png", label: "我的订单", path: "/pages/order/index" },
  { icon: "@/static/my/pet.png", label: "我的服务", path: "/pages/service/my-service" },
  { icon: "@/static/my/wash.png", label: "我的评价", path: "/pages/order/index" },
  { icon: "@/static/my/service.png", label: "售后服务", path: "/pages/order/index" },
];

const pets = ref([]);

onShow(() => {
  fetchPets();
});

const fetchPets = async () => {
  try {
    const response = await httpGet("/petInfo/select", { userId: userStore.userInfo.id });
    pets.value = response.records.map((pet) => ({
      ...pet,
      icon: baseUrl + pet.profileUrl,
    }));
  } catch (error) {
    uni.showToast({ title: "网络错误，请重试", icon: "error" });
  }
};

const editPet = (pet) => {
  uni.navigateTo({
    url: `/pages/pet/pet-add-page?id=${pet.id}&name=${pet.name}&icon=${pet.icon}&breed=${pet.breed}&color=${pet.color}&birthday=${pet.birthday}&gender=${pet.gender}&userId=${pet.userId}`,
  });
};

const items = [
  { icon: "@/static/my/send.png", label: "我的发布", to: "/pages/publish/index" },
  {
    icon: "@/static/my/handshake.png",
    label: "帮助中心",
    to: "/pages/others/official-account",
  },
  {
    icon: "@/static/my/feedback.png",
    label: "建议反馈",
    to: "/pages/others/official-account",
  },
  {
    icon: "@/static/my/badge.png",
    label: "宠托师认证",
    to: "/pages/certification/index",
  },
  { icon: "@/static/my/address.png", label: "我的地址", to: "/pages/address/index" },
  { icon: "@/static/my/settings.png", label: "设置", to: "/pages/settings/index" },
  { icon: "@/static/my/address.png", label: "地址", to: "/pages/map/index" },
];

const toPath = (path) => {
  uni.navigateTo({
    url: path,
  });
};
</script>

<style lang="scss" scoped>
.my-page-container {
  position: relative;
  width: 100%;
  overflow: scroll;

  .bg {
    position: absolute;
    top: 250px;
    z-index: -1;
    width: 100%;
    height: 100%;
    background-color: #fbfbfb;
  }

  .card {
    margin: 10rpx;
    border-radius: 10px;
  }

  .background {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
    height: 30%;
    background-repeat: no-repeat;
    background-size: cover;
  }

  .cat-dog {
    position: absolute;
    top: 140px;
    right: 0;
    width: 120px;
    height: 127px;
  }

  .my-avatar {
    display: grid;
    grid-template-columns: auto 1fr;
    grid-gap: 10px;
    align-items: center;
    padding-top: 130px;
    padding-left: 30px;

    .info {
      display: flex;
      flex-direction: column;
    }

    .name {
      font-size: 20px;
      font-weight: bold;
    }

    .description {
      font-size: 16px;
      color: #666;
    }
  }

  .stats-container {
    display: flex;
    justify-content: space-around;
    padding: 20px 0;
    background-color: #fff;

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .stat-number {
      font-size: 20px;
      font-weight: 500;
    }

    .stat-label {
      font-size: 14px;
      color: #666;
    }
  }

  .services-container {
    display: flex;
    justify-content: space-around;
    padding: 20px 0;
    background-color: #fff;

    .service-item {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .service-box {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      width: 105rpx;
      height: 100rpx;
      padding: 8px;
      cursor: pointer;
      background-color: #fcd038;
      border-radius: 50%;
    }

    .service-icon {
      width: 100%;
      object-fit: cover;
      border-radius: inherit;
    }

    .service-label {
      padding-top: 8px;
      font-size: 14px;
      color: #666;
    }
  }

  .pets-container {
    padding: 20px;
    background-color: #fff;

    .pets-title {
      margin-bottom: 10px;
      font-size: 18px;
      font-weight: bold;
    }

    .pets-list {
      display: flex;
      gap: 30rpx;
    }

    .pet-item {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .pet-avatar {
      border: 2px solid #fcd038;
      border-radius: 50%;
    }

    .add-avatar {
      width: 102rpx;
      height: 102rpx;
      background-color: #fff;
      border: 2px dashed #fcd038;
    }

    .add-icon {
      font-size: 36px;
      line-height: 45px;
      color: #fcd038;
      text-align: center;
    }

    .pet-label {
      font-size: 14px;
      color: #666;
    }
  }

  .settings-container {
    background-color: #f5f5f5;

    .list {
      margin-bottom: 10px;
      overflow: hidden;
      background-color: #fff;
      border-radius: 10px;
    }

    .list-item {
      display: flex;
      align-items: center;
      padding: 15px 10px;
      border-bottom: 1px solid #eee;
    }

    .list-item:last-child {
      border-bottom: none;
    }

    .icon {
      margin-right: 10px;
    }

    .item-icon {
      width: 20px;
      height: 20px;
    }

    .label {
      flex: 1;
      font-size: 16px;
    }

    .arrow {
      font-size: 16px;
      color: #b2b2b2;
    }
  }
}
</style>
