<template>
  <view class="cell-container">
    <view class="cell-icon">
      <image :src="icon" class="icon" />
    </view>
    <view class="cell-content">
      <view class="cell-label">{{ label }}</view>
      <view class="cell-arrow">
        <text>></text>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'

const props = defineProps({
  icon: {
    type: String,
    required: true,
  },
  label: {
    type: String,
    required: true,
  },
})
</script>

<style scoped lang="scss">
.cell-container {
  display: flex;
  align-items: center;
  padding: 15px 10px;
  background-color: #fff;
  border-bottom: 1px solid #eee;

  &:last-child {
    border-bottom: none;
  }

  .cell-icon {
    margin-right: 10px;
  }

  .icon {
    width: 20px;
    height: 20px;
  }

  .cell-content {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: space-between;
  }

  .cell-label {
    font-size: 16px;
  }

  .cell-arrow {
    font-size: 16px;
    color: #b2b2b2;
  }
}
</style>
