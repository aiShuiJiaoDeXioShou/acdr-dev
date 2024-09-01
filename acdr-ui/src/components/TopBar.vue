<template>
  <view class="absolute top-[25px] left-[5px] z-999">
    <wd-icon size="30px" name="arrow-left" @click="toBack"></wd-icon>
  </view>
</template>

<script lang="js" setup>
const toBack = () => {
  uni.navigateBack()
}
</script>

<style lang="scss" scoped></style>
