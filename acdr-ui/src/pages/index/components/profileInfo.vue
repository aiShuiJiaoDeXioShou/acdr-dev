<template>
  <view class="profile-info-container">
    <view class="profile-info">
      <image :src='imgUrl("@/static/icons/cat.png")' class="profile-icon" />
      <view class="profile-details">
        <view class="profile-header">
          <text class="profile-name">{{ profileName }}</text>
          <image :src="imgUrl(genderIcon)" class="gender-icon" />
        </view>
        <view class="profile-tags">
          <text v-for="(tag, index) in profileTags" :key="index" class="profile-tag">
            {{ tag }}
          </text>
        </view>
      </view>
    </view>
    <view class="profile-arrow">
      <image :src="imgUrl('@/static/icons/more.png')" class="arrow-icon" />
    </view>
  </view>
</template>

<script lang="ts" setup>
import { imgUrl } from '@/utils/commUtils';
import { defineProps } from 'vue'

const props = defineProps<{
  profileIcon: string
  profileName: string
  gender: number
  profileTags: string[]
}>()

const genderIcon = computed(
  () =>
    props.gender === 1
      ? '/static/icons/arrow-icon.png' // 使用绝对路径
      : '/static/icons/female-icon.png', // 使用绝对路径
)
</script>

<style scoped>
.profile-info-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.profile-info {
  display: flex;
  align-items: center;
}

.profile-icon {
  width: 50px;
  height: 50px;
  border-radius: 25px;
}

.profile-details {
  margin-left: 10px;
}

.profile-header {
  display: flex;
  align-items: center;
}

.profile-name {
  font-size: 18px;
  font-weight: bold;
}

.gender-icon {
  width: 16px;
  height: 16px;
  margin-left: 5px;
}

.profile-tags {
  display: flex;
  margin-top: 5px;
}

.profile-tag {
  padding: 2px 5px;
  margin-right: 5px;
  font-size: 12px;
  color: #000000;
  background-color: #fee7da;
  border-radius: 5px;
}

.profile-arrow {
  display: flex;
  align-items: center;
}

.arrow-icon {
  width: 20px;
  height: 20px;
  /* tint-color: #ff7043; */
}
</style>
