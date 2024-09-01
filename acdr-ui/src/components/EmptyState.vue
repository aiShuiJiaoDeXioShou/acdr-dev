<template>
  <view class="empty-state">
    <image v-if="this.type == 'map'" class="img" src="@/static/null/mapnull.png" mode="widthFix" />
    <image
      v-else-if="this.type == 'search'"
      class="img"
      src="@/static/null/searchnull.png"
      mode="widthFix"
    />
    <image
      v-else-if="this.type == 'message'"
      class="img"
      src="@/static/null/messagenull.png"
      mode="widthFix"
    />
    <image v-else class="img" src="@/static/null/searchnull.png" mode="widthFix" />
    <view class="txt">{{ message }}</view>
  </view>
</template>

<script>
export default {
  name: 'EmptyState',
  props: {
    type: {
      type: String,
      default: 'default',
    },
    message: {
      type: String,
      default: '空空如也！',
    },
  },
}
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.empty-state .img {
  width: 100%;
  width: 250px;
  height: auto;
  margin: 20px 0;
}

.empty-state .txt {
  font-size: 16px; // Adjust according to your design
  color: #666;
  text-align: center;
}
</style>
