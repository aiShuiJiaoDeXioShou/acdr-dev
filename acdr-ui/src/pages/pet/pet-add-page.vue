<route lang="json5">
{
  style: {
    navigationBarTitleText: '宠物档案信息',
  },
}
</route>

<template>
  <view class="p-5 bg-gray-100 h-screen">
    <!-- 上传头像区域 -->
    <view class="flex justify-center my-5">
      <view class="relative">
        <wd-img
          round
          :src="imageSrc"
          mode="aspectFill"
          class="w-24 h-24 rounded-full"
          @error="imageLoadError"
        ></wd-img>
        <view
          class="absolute bottom-0 right-0 bg-white rounded-full w-[24px] h-[24px] flex justify-center items-center"
        >
          <wd-icon name="add" color="#ffc107" @click="chooseImage"></wd-icon>
        </view>
      </view>
    </view>
    <view class="text-center text-sm text-gray-600">宠物头像</view>

    <!-- 表单区域 -->
    <view class="mt-5 bg-white p-5 rounded-lg shadow">
      <!-- 名字 -->
      <view class="mb-4">
        <text class="text-gray-800">宠物名字</text>
        <input placeholder="请输入名字" v-model="petName" />
      </view>

      <!-- 生日 -->
      <view class="mb-4">
        <text class="text-gray-800">宠物生日</text>
        <picker mode="date" :value="petBirthday" start="2000-01-01" @change="onBirthdayChange">
          <view class="w-full mt-2 p-2 border border-gray-300 rounded">
            {{ petBirthday || '请选择生日' }}
          </view>
        </picker>
      </view>

      <!-- 性别 -->
      <view class="mb-4">
        <text class="text-gray-800">宠物性别</text>
        <view class="flex mt-2">
          <button
            :class="[
              'flex-1 py-2 mx-1',
              gender === '男孩' ? 'bg-blue-500 text-white' : 'bg-gray-200',
            ]"
            @click="gender = '男孩'"
          >
            男孩
          </button>
          <button
            :class="[
              'flex-1 py-2 mx-1',
              gender === '女孩' ? 'bg-pink-500 text-white' : 'bg-gray-200',
            ]"
            @click="gender = '女孩'"
          >
            女孩
          </button>
        </view>
      </view>

      <!-- 品种 -->
      <view class="mb-4">
        <text class="text-gray-800">宠物品种</text>
        <wd-picker
          class="w-full mt-2 p-2 border border-gray-300 rounded"
          :columns="breedOptions"
          v-model="petBreed"
        />
      </view>

      <!-- 毛色 -->
      <view class="mb-4">
        <text class="text-gray-800">宠物毛色</text>
        <wd-picker
          class="w-full mt-2 p-2 border border-gray-300 rounded"
          :columns="colorOptions"
          v-model="petColor"
        />
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="mt-5">
      <button
        class="w-full py-4 bg-[#ffc107] text-white text-center rounded-full text-lg h-11 flex items-center justify-center shadow-lg"
        @click="createPetProfile"
      >
        {{ petId ? '更新电子身份证' : '创建电子身份证' }}
      </button>
    </view>

    <!-- 删除按钮 -->
    <button
      v-if="petId"
      class="mt-5 w-full py-4 bg-red-500 text-white rounded-full text-lg h-11 flex items-center justify-center shadow-lg"
      @click="deletePet"
    >
      删除
    </button>
  </view>
</template>

<script setup>
import { useUserStore } from '@/store'
import { baseUrl } from '@/utils/commUtils'
import { httpPost, httpGet } from '@/utils/http'
import { ref } from 'vue'

const petName = ref('')
const petBirthday = ref('')
const gender = ref('')
const petBreed = ref('')
const petColor = ref('')
const imageSrc = ref('https://via.placeholder.com/150')
const userId = ref('')
let petId = ref(null)

const imageLoadError = (errMsg) => {
  console.log('图片加载失败')
  imageSrc.value = 'https://via.placeholder.com/150'
}

const userStore = useUserStore()

const breedOptions = ref([
  { value: '其他', label: '其他' },
  { value: '阿比西尼亚猫', label: '阿比西尼亚猫' },
  { value: '安哥拉猫', label: '安哥拉猫' },
  { value: '埃及猫', label: '埃及猫' },
  { value: '奥西猫', label: '奥西猫' },
  { value: '巴厘猫', label: '巴厘猫' },
  { value: '波米拉猫', label: '波米拉猫' },
  { value: '伯曼猫', label: '伯曼猫' },
  { value: '布偶猫', label: '布偶猫' },
  { value: '波斯猫', label: '波斯猫' },
])

const colorOptions = ref([
  { value: '黑白色', label: '黑白色' },
  { value: '白色', label: '白色' },
  { value: '黑色', label: '黑色' },
  { value: '橘色', label: '橘色' },
  { value: '蓝色', label: '蓝色' },
  { value: '蓝白色', label: '蓝白色' },
  { value: '橘白色', label: '橘白色' },
  { value: '灰色', label: '灰色' },
  { value: '灰白色', label: '灰白色' },
  { value: '乳色', label: '乳色' },
  { value: '乳白色', label: '乳白色' },
  { value: '棕色', label: '棕色' },
  { value: '棕白色', label: '棕白色' },
  { value: '银色', label: '银色' },
  { value: '银白色', label: '银白色' },
  { value: '雪色', label: '雪色' },
  { value: '木炭色', label: '木炭色' },
  { value: '金渐层', label: '金渐层' },
  { value: '蓝金渐层', label: '蓝金渐层' },
  { value: '银渐层', label: '银渐层' },
  { value: '金点', label: '金点' },
  { value: '银点', label: '银点' },
  { value: '重点色', label: '重点色' },
  { value: '手套色', label: '手套色' },
  { value: '海豹色', label: '海豹色' },
  { value: '火焰色', label: '火焰色' },
  { value: '蓝双色', label: '蓝双色' },
  { value: '海豹双色', label: '海豹双色' },
  { value: '梵色', label: '梵色' },
  { value: '玳瑁色', label: '玳瑁色' },
  { value: '三花色', label: '三花色' },
  { value: '烟灰色', label: '烟灰色' },
  { value: '虎斑纹', label: '虎斑纹' },
  { value: '棕虎斑', label: '棕虎斑' },
  { value: '银虎斑', label: '银虎斑' },
  { value: '红虎斑', label: '红虎斑' },
  { value: '山猫纹', label: '山猫纹' },
  { value: '玫瑰纹', label: '玫瑰纹' },
  { value: '大理石纹', label: '大理石纹' },
  { value: '其他', label: '其他' },
  { value: '褐色', label: '褐色' },
  { value: '红色', label: '红色' },
  { value: '小鹿色', label: '小鹿色' },
  { value: '纯色', label: '纯色' },
  { value: '渐层色', label: '渐层色' },
  { value: '烟色', label: '烟色' },
  { value: '补丁', label: '补丁' },
  { value: '蓝虎斑', label: '蓝虎斑' },
  { value: '凯米尔虎斑', label: '凯米尔虎斑' },
  { value: '虎斑加白', label: '虎斑加白' },
  { value: '渐层加白', label: '渐层加白' },
  { value: '烟色加白', label: '烟色加白' },
  { value: '双色', label: '双色' },
  { value: '杂色', label: '杂色' },
  { value: '金吉拉银色', label: '金吉拉银色' },
  { value: '金吉拉金色', label: '金吉拉金色' },
  { value: '重点色加白', label: '重点色加白' },
  { value: '渐层重点色', label: '渐层重点色' },
  { value: '貂色', label: '貂色' },
  { value: '香槟色', label: '香槟色' },
  { value: '铂色', label: '铂色' },
  { value: '海豹山猫双色', label: '海豹山猫双色' },
  { value: '蓝山猫双色', label: '蓝山猫双色' },
  { value: '其他双色', label: '其他双色' },
  { value: '重点双色', label: '重点双色' },
  { value: '巧克力重点色', label: '巧克力重点色' },
  { value: '海豹重点色', label: '海豹重点色' },
  { value: '蓝重点色', label: '蓝重点色' },
  { value: '丁香重点色', label: '丁香重点色' },
  { value: '金色', label: '金色' },
  { value: '浅三花色', label: '浅三花色' },
  { value: '鱼骨纹色', label: '鱼骨纹色' },
  { value: '混合色', label: '混合色' },
  { value: '橙褐色', label: '橙褐色' },
  { value: '柏色', label: '柏色' },
  { value: '布伦海姆色', label: '布伦海姆色' },
  { value: '纯红色', label: '纯红色' },
  { value: '橙色', label: '橙色' },
  { value: '紫色', label: '紫色' },
  { value: '淡灰色', label: '淡灰色' },
  { value: '黄色', label: '黄色' },
  { value: '浅红色', label: '浅红色' },
  { value: '野猪色', label: '野猪色' },
  { value: '巧克力色', label: '巧克力色' },
  { value: '黑铁灰色', label: '黑铁灰色' },
  { value: '盐与胡椒色', label: '盐与胡椒色' },
  { value: '红褐色', label: '红褐色' },
  { value: '黑褐色', label: '黑褐色' },
  { value: '花斑色', label: '花斑色' },
  { value: '马斑色', label: '马斑色' },
  { value: '灰褐色', label: '灰褐色' },
  { value: '银灰色', label: '银灰色' },
  { value: '椒盐色', label: '椒盐色' },
  { value: '金黄色', label: '金黄色' },
  { value: '桃红色', label: '桃红色' },
  { value: '火烈鸟红色', label: '火烈鸟红色' },
  { value: '麦色', label: '麦色' },
  { value: '浅黄褐色', label: '浅黄褐色' },
  { value: '浅黄色', label: '浅黄色' },
  { value: '浅米色', label: '浅米色' },
  { value: '灰黄色', label: '灰黄色' },
  { value: '黄褐色', label: '黄褐色' },
  { value: '杏色', label: '杏色' },
  { value: '沙色', label: '沙色' },
  { value: '沙褐色', label: '沙褐色' },
  { value: '蓝灰色', label: '蓝灰色' },
  { value: '浅麦色', label: '浅麦色' },
  { value: '黑银色', label: '黑银色' },
  { value: '棕褐色', label: '棕褐色' },
  { value: '淡棕色', label: '淡棕色' },
  { value: '赤褐色', label: '赤褐色' },
  { value: '浅棕色', label: '浅棕色' },
  { value: '浅巧克力色', label: '浅巧克力色' },
  { value: '深棕色', label: '深棕色' },
  { value: '灰白色', label: '灰白色' },
  { value: '柠檬色', label: '柠檬色' },
  { value: '浅褐色', label: '浅褐色' },
  { value: '深红褐色', label: '深红褐色' },
  { value: '斑点色', label: '斑点色' },
  { value: '虎斑色', label: '虎斑色' },
  { value: '淡黄色', label: '淡黄色' },
  { value: '粉红色', label: '粉红色' },
  { value: '深灰色', label: '深灰色' },
  { value: '黑棕色', label: '黑棕色' },
  { value: '铁锈色', label: '铁锈色' },
  { value: '棕红色', label: '棕红色' },
  { value: '深红色', label: '深红色' },
  { value: '深褐色', label: '深褐色' },
  { value: '奶油色', label: '奶油色' },
  { value: '鹿皮色', label: '鹿皮色' },
  { value: '橙黄色', label: '橙黄色' },
])

onLoad(async (options) => {
  if (options && options.id) {
    petId.value = options.id
    await fetchPetData(petId.value)
  }
})

const fetchPetData = async (id) => {
  const response = await httpGet(`/petInfo/find_by_id/${id}`)
  if (response.code === 200) {
    const petData = response.data
    petName.value = petData.name || ''
    petBirthday.value = petData.age || ''
    gender.value = petData.sex === 1 ? '男孩' : '女孩'
    petBreed.value = petData.breed || ''
    petColor.value = petData.hairColor || ''
    imageSrc.value = baseUrl + petData.profileUrl || 'https://via.placeholder.com/150'
    userId.value = petData.userId || '-1'
  } else {
    uni.showToast({ title: '获取宠物信息失败', icon: '' })
  }
}

const chooseImage = () => {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      imageSrc.value = res.tempFilePaths[0]
    },
  })
}

const onBirthdayChange = (e) => {
  petBirthday.value = e.detail.value
}

const createPetProfile = () => {
  const formData = {
    id: petId.value || undefined,
    name: petName.value,
    age: petBirthday.value + ' 00:00:00',
    sex: gender.value === '男孩' ? 1 : 0,
    breed: petBreed.value,
    hairColor: petColor.value,
  }
  if (petId.value) {
    formData['userId'] = userId.value
  }

  const url = petId.value ? '/petInfo/update' : '/petInfo/addPet'
  const isNetworkUrl = imageSrc.value.startsWith('http')

  uni.uploadFile({
    url: url,
    filePath: imageSrc.value,
    name: 'file',
    formData: formData,
    header: {
      satoken: `Bearer ${userStore.token}`, // 添加认证头部
    },
    success: (uploadRes) => {
      const data = JSON.parse(uploadRes.data)
      if (data.code === 200) {
        uni.showToast({ title: petId.value ? '更新成功' : '创建成功', icon: 'success' })
        resetForm()
        uni.navigateBack()
      } else {
        uni.showToast({ title: data.message, icon: 'none' })
      }
    },
    fail: (e) => {
      console.log(e)
      uni.showToast({ title: '上传失败', icon: 'none' })
    },
  })
}

const calculateAge = (birthday) => {
  const birthDate = new Date(birthday)
  const currentDate = new Date()
  let age = currentDate.getFullYear() - birthDate.getFullYear()
  const monthDiff = currentDate.getMonth() - birthDate.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && currentDate.getDate() < birthDate.getDate())) {
    age--
  }
  return age
}

const deletePet = () => {
  uni.showModal({
    title: '确认删除',
    content: '你确定要删除这个宠物档案吗？',
    success: (res) => {
      if (res.confirm) {
        httpPost('/petInfo/delete', { id: petId.value }).then(() => {
          uni.showToast({ title: '删除成功', icon: 'success' })
          uni.navigateBack()
        })
      }
    },
  })
}

const resetForm = () => {
  petName.value = ''
  petBirthday.value = ''
  gender.value = ''
  petBreed.value = ''
  petColor.value = ''
  imageSrc.value = 'https://via.placeholder.com/150'
}
</script>

<style scoped>
/* 使用 UnoCSS 定义样式 */
button {
  @apply px-2 py-1 text-sm;
}
button.primary {
  @apply px-2 py-1 bg-[#ffc107] text-white text-sm;
}
</style>
