<route lang="json5">
{
  style: {
    navigationBarTitleText: '新增地址',
  },
}
</route>
<template>
  <view class="address-management">
    <view v-for="(address, index) in addresses" :key="index" class="address-cell">
      <view class="address-info">
        <view class="address-label">
          <view class="tag">{{ address.type }}</view>
          <view class="address-detail">
            {{ address.name }} {{ address.phone }} -
            {{ `${address['province']}-${address['city']}-${address['district']}` }}
          </view>
        </view>
      </view>
      <view class="address-actions">
        <wd-icon name="edit" size="20px" @click="editAddress(index)" />
        <wd-icon name="delete" size="20px" @click="deleteAddress(index)" />
      </view>
    </view>
    <wd-status-tip v-if="addresses.length == 0" image="content" tip="暂无内容" />
    <!-- 新增地址按钮 -->
    <button class="add-address-button" @click="addAddress">新增地址</button>

    <!-- 弹窗组件 -->
    <wd-popup
      v-model="showPopup"
      position="bottom"
      custom-style="height: 60%; width: 100%;"
      @close="handleClose"
    >
      <AddAddress :initialAddress="currentAddress" @save-address="saveAddress" />
    </wd-popup>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AddAddress from './components/AddAddress.vue'
import { httpGet, httpPost } from '@/utils/http'

// 地址列表数据
const addresses = ref([])

// 当前操作的地址数据
const currentAddress = ref({
  id: null,
  name: '',
  phone: '',
  address: '',
})

// 控制弹窗的显示
const showPopup = ref(false)

// 获取地址列表
const fetchAddresses = async () => {
  const res = await httpPost('/china-address/page')
  addresses.value = res.records
}

// 页面加载时获取地址列表
onMounted(() => {
  fetchAddresses()
})

// 新增地址
const addAddress = () => {
  currentAddress.value = { id: null, name: '', phone: '', address: '' }
  showPopup.value = true
}

// 编辑地址
const editAddress = (index) => {
  const address = addresses.value[index]
  addresses.value[index].address =
    `${address['province']}-${address['city']}-${address['district']}`
  currentAddress.value = { ...addresses.value[index] }
  showPopup.value = true
}

// 保存地址（新增或修改）
const saveAddress = async (address) => {
  let res
  if (address.address != '') {
    const addressList = address.address.split('-')
    address['province'] = addressList[0]
    address['city'] = addressList[1]
    address['district'] = addressList[2]
  }
  if (address.id) {
    // 修改地址
    res = await httpPost('/china-address/modify', address)
  } else {
    // 新增地址
    res = await httpPost('/china-address/add', address)
  }
  if (res.code == 200) {
    uni.showToast({ title: '操作成功', icon: '' })
    fetchAddresses() // 刷新地址列表
    showPopup.value = false
  }
}

// 删除地址
const deleteAddress = async (index) => {
  const addressId = addresses.value[index].id
  uni.showModal({
    title: '确认删除',
    content: '你确定要删除这个地址吗？',
    success: async (res) => {
      if (res.confirm) {
        const deleteRes = await httpGet(`/china-address/remove/${addressId}`)
        if (deleteRes.code === 200) {
          uni.showToast({ title: '删除成功', icon: 'success' })
          fetchAddresses() // 刷新地址列表
        }
      }
    },
  })
}
</script>

<style scoped>
.address-management {
  padding: 20px;
}

.address-cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.address-info {
  display: flex;
  flex: 1;
  align-items: center;
}

.address-label {
  display: flex;
  align-items: center;
}

.tag {
  padding: 2px 5px;
  margin-right: 10px;
  color: white;
  background-color: #f5a623;
  border-radius: 3px;
}

.address-detail {
  flex-grow: 1;
}

.address-actions {
  display: flex;
  gap: 10px; /* Ensure space between icons */
  align-items: center;
}

.wd-icon {
  color: #333; /* Adjust color as needed */
  cursor: pointer;
}

.add-address-button {
  position: absolute;
  bottom: 30px;
  left: 50%;
  width: 90%;
  min-height: 50px;
  cursor: pointer;
  background-color: #fcd038 !important;
  border: none;
  transform: translateX(-50%);
}

.wd-popup {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
