<route lang="json5" type="page">
{
  layout: "default",
  style: {
    navigationBarTitleText: "地图详细页面",
  },
}
</route>

<template>
  <view class="root">
    <Map />
  </view>
</template>

<script lang="js" setup>
import Map from '@/components/Map.vue';
</script>

<style lang="scss" scoped></style>
