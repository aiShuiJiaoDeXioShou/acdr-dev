<template>
  <view class="add-address">
    <wd-form ref="addressForm">
      <wd-input label="姓名" v-model="address.name" required />
      <wd-input label="电话" v-model="address.phone" required type="tel" />

      <!-- 使用 WotDesign 的选择器 -->
      <wd-picker
        :columns="typeOptions"
        label="地址类型"
        v-model="address.type"
        @confirm="handleTypeChange"
      />

      <!-- 如果选择了自定义类型，显示输入框 -->
      <wd-input v-if="isCustomType" label="自定义类型" v-model="address.customType" required />

      <wd-col-picker
        label="选择地址"
        v-model="value"
        :columns="area"
        :column-change="columnChange"
        :display-format="displayFormat"
        @confirm="handleConfirm"
      ></wd-col-picker>
      <view
        style="width: 100%; padding-left: 15px; color: #bebebe"
        v-if="address.address.length && address.address && !value.length"
      >
        当前地址：{{ address.address }}
      </view>
      <wd-input label="详细地址" v-model="address.detailAddress" required />
      <button class="add-address-button" type="primary" @click="submitAddress">保存</button>
    </wd-form>
  </view>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useColPickerData } from '@/hooks/useColPickerData'

const { colPickerData, findChildrenByCode } = useColPickerData()

const value = ref([])
const props = defineProps({
  initialAddress: {
    type: Object,
    default: () => ({
      name: '',
      phone: '',
      address: '',
      detailAddress: '',
      type: '',
      customType: '',
    }),
  },
})

const initArea = ref([])
const area = ref([
  colPickerData.map((item) => {
    return {
      value: item.value,
      label: item.text,
    }
  }),
])

const typeOptions = ref(['家', '公司', '自定义'])

const isCustomType = ref(false)

const columnChange = ({ selectedItem, resolve, finish }) => {
  const areaData = findChildrenByCode(colPickerData, selectedItem.value)
  if (areaData && areaData.length) {
    resolve(areaData.map((item) => ({ value: item.value, label: item.text })))
  } else {
    finish()
  }
}

const displayFormat = (selectedItems) => {
  return selectedItems.map((v) => v.label).join('-')
}

function handleConfirm({ value, selectedItems }) {
  address.value.address = selectedItems.map((v) => v.label).join('-')
  console.log(address.value.address)
}

function handleTypeChange({ value }) {
  isCustomType.value = value === '自定义'
  if (!isCustomType.value) {
    address.value.customType = ''
  }
}

const emit = defineEmits(['save-address'])
const address = ref({ ...props.initialAddress })

const initAddress = () => {
  console.log('ddd', area.value)
  const initAddress = props.initialAddress.address.split('-')
  const pcode = area.value[0].filter((area) => area.label == initAddress[0])
  const areaData = findChildrenByCode(colPickerData, pcode[0].value)
  const ccode = areaData.filter((area) => area.text == initAddress[1])
  const areaDataTwo = findChildrenByCode(colPickerData, ccode[0].value)
  const dcode = areaDataTwo.filter((area) => area.text == initAddress[2])
  value.value = [pcode[0].value, ccode[0].value, dcode[0].value]
}

watch(
  () => props.initialAddress,
  (newVal) => {
    address.value = { ...newVal }
    isCustomType.value = newVal.type === '自定义'
  },
)

const submitAddress = () => {
  if (
    address.value.name &&
    address.value.phone &&
    address.value.detailAddress &&
    (address.value.type !== '自定义' || address.value.customType)
  ) {
    emit('save-address', address.value)
    resetForm()
  } else {
    uni.showToast({ title: '请填写完整的地址信息', icon: 'error' })
  }
}

const resetForm = () => {
  address.value = { name: '', phone: '', address: '', detailAddress: '', type: '', customType: '' }
  value.value = []
  isCustomType.value = false
}
</script>

<style scoped>
.add-address {
  padding: 20px;
}
.add-address-button {
  width: 100%;
  font-size: 18px !important;
  background-color: #fcd038 !important;
}

.picker {
  padding: 10px;
  margin-top: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>
