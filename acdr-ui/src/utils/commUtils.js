
// 获取服务baseUrl
export const baseUrl = import.meta.env.VITE_SERVER_BASEURL
export const uploadUrl = import.meta.env.VITE_SERVER_BASEURL
export const ossUrl = import.meta.env.VITE_OSS_BASEURL

export const toast = (message) => {
  uni.showToast({ title: message, icon: 'none' })
}

// 解析图片
export const imgUrl = (img, isOss) => {
  const placeholder = 'https://via.placeholder.com/150' // 占位符图片的URL
  let isLocation = img.startsWith('@/')
  if(isLocation) return img.replace('@', ossUrl)
  if(isOss || isLocation) ossUrl + img || placeholder

  if (!img) return placeholder

  // 检查图片是否为网络图片（判断是否以 http:// 或 https:// 开头）
  const isNetworkImage = img.startsWith('http://') || img.startsWith('https://')

  return isNetworkImage ? img : `${uploadUrl + img}`
}


// 使用异步的询问对话框
export const showModalAsync = (options) => {
  return new Promise((resolve, reject) => {
    uni.showModal({
      ...options,
      success: (res) => {
        if (res.confirm) {
          resolve(res);
        } else if (res.cancel) {
          resolve(res);
        }
      },
      fail: (err) => {
        reject(err);
      },
    });
  });
};


// 扫码逻辑
export const scanCodeAsync = () => {
  return new Promise((resolve, reject) => {
    uni.scanCode({
      onlyFromCamera: true,  // 是否只能从相机扫码
      success: (res) => {
        resolve(res);  // 成功时返回扫码结果
      },
      fail: (err) => {
        reject(err);  // 失败时返回错误信息
      }
    });
  });
};

// 路径导航
export const toPath = (path) => {
  uni.navigateTo({ url: path })
}
