import { CustomRequestOptions } from '@/interceptors/request'
import { useUserStore } from '@/store/user'

export const http = <T>(options: CustomRequestOptions) => {
  const userStore = useUserStore()
  // 1. 返回 Promise 对象
  return new Promise<IResData<T>>((resolve, reject) => {
    uni.request({
      ...options,
      url: options.url,
      dataType: 'json',
      // #ifndef MP-WEIXIN
      responseType: 'json',
      // #endif
      // 响应成功
      success(res) {
        // 状态码 2xx，参考 axios 的设计
        if (res.statusCode >= 200 && res.statusCode < 300) {
          // 2.1 提取核心数据 res.data
          resolve(res.data as IResData<T>)
        } else if (res.statusCode === 401) {
          // 401错误  -> 清理用户信息，跳转到登录页
          userStore.clearUserInfo()
          uni.navigateTo({ url: '/pages/login/index' })
          reject(res)
        } else {
          // 其他错误 -> 根据后端错误信息轻提示
          !options.hideErrorToast &&
            uni.showToast({
              icon: 'none',
              title: res.data['message'] || '请求错误',
            })
          reject(res)
        }
      },
      // 响应失败
      fail(err) {
        uni.showToast({
          icon: 'none',
          title: '网络错误，换个网络试试',
        })
        reject(err)
      },
    })
  })
}

/**
 * GET 请求
 * @param url 后台地址
 * @param query 请求query参数
 * @returns
 */
export const httpGet = <T>(url: string, query?: Record<string, any>) => {
  return http<T>({
    url,
    query,
    method: 'GET',
  })
}

/**
 * POST 请求
 * @param url 后台地址
 * @param formData 表单数据
 * @param query 请求query参数，post请求也支持query，很多微信接口都需要
 * @returns
 */
export const httpPostMultipart = <T>(
  url: string,
  formData: FormData,
  query?: Record<string, any>,
) => {
  return http<T>({
    url,
    query,
    data: formData,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

/**
 * POST 请求
 * @param url 后台地址
 * @param data 请求body参数
 * @param query 请求query参数，post请求也支持query，很多微信接口都需要
 * @returns
 */
export const httpPost = <T>(
  url: string,
  data?: Record<string, any>,
  query?: Record<string, any>,
) => {
  return http<T>({
    url,
    query,
    data,
    method: 'POST',
  })
}

/**
 * 文件上传请求
 * @param url 文件上传的后台地址
 * @param filePath 本地文件路径
 * @param name 文件表单字段名
 * @param formData 额外的表单数据
 * @returns Promise
 */
export const httpUploadFile = (
  url: string,
  filePath: string,
  name = 'file',
  formData: Record<string, any> = {},
) => {
  const userStore = useUserStore()
  return new Promise<IResData<any>>((resolve, reject) => {
    uni.uploadFile({
      url: url,
      filePath: filePath,
      name: name,
      formData: formData,
      header: {
        satoken: `Bearer ${userStore.token}`, // 添加认证头部
      },
      timeout: 10000,
      success(res) {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(JSON.parse(res.data) as IResData<any>)
        } else if (res.statusCode === 401) {
          userStore.clearUserInfo()
          uni.navigateTo({ url: '/pages/login/index' })
          reject(res)
        } else {
          uni.showToast({
            icon: 'none',
            title: '上传失败，' + (JSON.parse(res.data).message || '请稍后重试'),
          })
          reject(res)
        }
      },
      fail(err) {
        uni.showToast({
          icon: 'none',
          title: '网络错误，换个网络试试',
        })
        reject(err)
      },
    })
  })
}

/**
 * 上传多个文件
 * @param url 文件上传的后台地址
 * @param filePaths 本地文件路径数组
 * @param name 文件表单字段名
 * @param formData 额外的表单数据
 * @returns Promise
 */
export const httpUploadMultipleFiles = async (
  url: string,
  filePaths: string[],
  name = 'file',
  formData: Record<string, any> = {},
) => {
  const uploadResults = []
  for (const filePath of filePaths) {
    try {
      const result = await httpUploadFile(url, filePath, name, formData)
      if (result.code == 200) {
        uploadResults.push(result.data.url)
      }
    } catch (err) {
      // 可以根据需求处理上传失败的情况
      console.error('上传文件失败:', err)
    }
  }
  return uploadResults
}

http.uploadFile = httpUploadFile
http.uploadMultipleFiles = httpUploadMultipleFiles
http.get = httpGet
http.post = httpPost
