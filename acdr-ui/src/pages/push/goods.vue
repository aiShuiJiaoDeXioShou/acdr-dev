<template>
    <view class="p-4">
      <view class="flex justify-between items-center py-4 border-b border-gray-200">
        <text class="text-2xl">×</text>
        <text class="text-xl font-bold">发闲置</text>
        <button class="btn bg-yellow-500 text-black">发布</button>
      </view>
      <view class="card mt-4">
        <textarea class="input h-24" placeholder="描述一下宝贝的品牌型号、货品来源..."></textarea>
        <view class="flex items-center justify-between mb-4">
          <view class="w-24 h-24 border-dashed border-gray-200 border-2 flex-center rounded-lg">
            <text class="text-gray-400">+ 添加优质首图更吸引人~</text>
          </view>
        </view>
        <view class="flex justify-between mb-4">
          <text class="text-gray-500">广州 增城区</text>
          <text class="text-gray-500">描述范例</text>
          <text class="text-gray-500">搜索发布</text>
        </view>
        <view class="flex flex-col gap-4">
          <view class="flex justify-between items-center">
            <text class="label">价格</text>
            <text class="text-red">¥ 0.00</text>
          </view>
          <view class="flex justify-between items-center">
            <text class="label">发货方式</text>
            <text class="text-gray-500">邮寄（运费未填）</text>
          </view>
        </view>
      </view>
    </view>
  </template>
  
  <script>
  export default {
    name: 'Index'
  }
  </script>
  
  <style>
  /* 如果需要额外的样式，可以在这里添加 */
  </style>
  