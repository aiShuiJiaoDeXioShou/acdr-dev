<template>
  <view class="recommended-services">
    <view class="masonry">
      <view v-for="(item, index) in services" @click="toDetail(item)" :key="index">
        <RecommendedServiceCard
          :image="imgUrl(item.image)"
          :title="item.title"
          :description="item.description"
          :price="item.price"
        />
      </view>
    </view>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import RecommendedServiceCard from '@/components/RecommendedServiceCard.vue'
import { imgUrl } from '@/utils/commUtils'

const toDetail = (item) => {
  uni.navigateTo({
    url: '/pages/service/detail?id=1823840859376168962',
  })
}

const services = ref([
  {
    image: '@/static/home/recommend1.png',
    title: '超可爱宠物清洗服务',
    description: '寄养/美容/清洗 已售1000+',
    price: '¥48.8',
  },
  {
    image: '@/static/home/recommend3.png',
    title: '超可爱宠物清洗服务',
    description: '寄养/美容/清洗 已售1000+',
    price: '¥48.8',
  },
  {
    image: '@/static/home/recommend2.png',
    title: '超可爱宠物清洗服务',
    description: '寄养/美容/清洗 已售1000+',
    price: '¥48.8',
  },
  {
    image: '@/static/home/recommend4.png',
    title: '超可爱宠物清洗服务',
    description: '寄养/美容/清洗 已售1000+',
    price: '¥48.8',
  },
])
</script>

<style scoped>
.recommended-services {
  width: 100%;
  padding: 10px;
}

.masonry {
  gap: 10px;
  column-gap: 15px;
  width: 100%;
  column-count: 2;
}
</style>
