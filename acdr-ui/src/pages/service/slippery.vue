<route lang="json5">
{
    style: {
    navigationBarTitleText: '宠物培溜',
    },
}
</route>
<template>
    <view class="container">
        <view class="header">
            <view class="logo-section">
                <image :src="imgUrl('@/static/logo.svg')" class="logo" />
                <view class="app-name">宠物派</view>
            </view>
            <view class="rating-section">
                <text class="rating">5.0</text>
                <text class="rating-icon">★</text>
            </view>
        </view>
        <view class="hero">
            <image :src="imgUrl('@/static/hero.png')" class="hero-image" />
        </view>
        <view class="services">
            <view class="service-item">
                <image src="/static/cat.png" class="service-icon" />
                <text class="service-text">上门喂猫</text>
                <button class="service-button">预订</button>
            </view>
            <view class="service-item">
                <image src="/static/dog.png" class="service-icon" />
                <text class="service-text">上门遛狗</text>
                <button class="service-button">预订</button>
            </view>
        </view>
        <view class="statistics">
            <view class="statistic-item">
                <image src="/static/statistics-cat.png" class="statistic-icon" />
                <text class="statistic-text">已累计服务宠物</text>
                <text class="statistic-number">19,5602</text>
                <text class="statistic-date">截至2024.06</text>
            </view>
        </view>
        <view class="features">
            <view class="feature-item">
                <image src="/static/feature-subscribe.png" class="feature-icon" />
                <text class="feature-text">关注公众号</text>
            </view>
            <view class="feature-item">
                <image src="/static/feature-card.png" class="feature-icon" />
                <text class="feature-text">年卡优惠</text>
            </view>
            <view class="feature-item">
                <image src="/static/feature-rewards.png" class="feature-icon" />
                <text class="feature-text">小红书征集</text>
            </view>
        </view>
        <view class="footer">
            <navigator url="/pages/home/home" class="footer-item">首页</navigator>
            <navigator url="/pages/order/order" class="footer-item">订单</navigator>
            <navigator url="/pages/profile/profile" class="footer-item">我的</navigator>
        </view>
    </view>
</template>

<script setup>
import { imgUrl } from '@/utils/commUtils';

</script>

<style scoped>
.container {
    @apply flex flex-col items-center bg-yellow-100 min-h-screen;
}

.header {
    @apply flex justify-between items-center w-full px-4 py-2 bg-yellow-300;
}

.logo-section {
    @apply flex items-center;
}

.logo {
    @apply w-10 h-10;
}

.app-name {
    @apply ml-2 text-xl font-bold;
}

.rating-section {
    @apply flex items-center;
}

.rating {
    @apply text-lg font-bold;
}

.rating-icon {
    @apply ml-1 text-lg;
}

.hero {
    @apply mt-4;
}

.hero-image {
    @apply w-40 h-40;
}

.services {
    @apply flex justify-around w-full mt-4 bg-white rounded-lg shadow p-4;
}

.service-item {
    @apply flex flex-col items-center;
}

.service-icon {
    @apply w-16 h-16;
}

.service-text {
    @apply mt-2 text-lg font-bold;
}

.service-button {
    @apply mt-2 bg-pink-500 text-white py-1 px-3 rounded-full;
}

.statistics {
    @apply mt-6 bg-white rounded-lg shadow p-4 w-11/12;
}

.statistic-item {
    @apply flex flex-col items-center;
}

.statistic-icon {
    @apply w-16 h-16;
}

.statistic-text {
    @apply mt-2 text-lg;
}

.statistic-number {
    @apply mt-2 text-3xl font-bold text-red-500;
}

.statistic-date {
    @apply mt-1 text-sm;
}

.features {
    @apply mt-6 flex justify-around w-full;
}

.feature-item {
    @apply flex flex-col items-center;
}

.feature-icon {
    @apply w-12 h-12;
}

.feature-text {
    @apply mt-2 text-center;
}

.footer {
    @apply mt-auto w-full bg-gray-100 flex justify-around py-2;
}

.footer-item {
    @apply text-center text-gray-700;
}
</style>
