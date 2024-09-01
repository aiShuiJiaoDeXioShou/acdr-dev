import { httpPost } from "@/utils/http"

export const pay = async (order) => {
  // 调用支付接口
  // #ifdef MP-WEIXIN
  // 调用接口创建订单
  try {
    const createOrder = await httpPost('/order/create', order)
    if (createOrder.code === 200) {
      const orderData = createOrder.data
      // 请求微信支付
      const wxres = await requestPayment(orderData.payData)
      if (wxres.errMsg === 'requestPayment:ok') {
        // 支付成功
        uni.showToast({ title: '支付成功', icon: 'success' })
      } else {
        // 支付失败
        uni.showToast({ title: '支付失败', icon: 'none' })
      }
    }
  } catch (err) {
    uni.showToast({ title: '创建订单失败', icon: 'none' })
  }
  // #endif
  // #ifdef H5
  // uni.showModal({
  //   title: '提示',
  //   content: 'h5不支持支付功能',
  //   showCancel: false,
  //   success: function (res) {},
  // })
  // h5 模拟支付功能
  try {
    const createOrder = await httpPost('/order/create', order)
    if (createOrder.code === 200) {
      const orderData = createOrder.data
      const payRes = await httpPost("/order/pay/" + orderData.orderData.id)
      if (!payRes.code === 200) {
        uni.showToast({ title: '支付失败: ' + payRes.message, icon: 'none' })
        return false
      }
      uni.showToast({ title: '支付成功', icon: 'success' })
      return true
    }
    toast(createOrder.message)
  } catch (err) {
    uni.showToast({ title: '创建订单失败', icon: 'none' })
  }
  return false
  // #endif
}

export const payOrder = async (orderId)=> {
  const payRes = await httpPost("/order/pay/" + orderId, {})
  return payRes
}

// 将微信小程序支付请求接口封装
function requestPayment(paymentData) {
  return new Promise((resolve, reject) => {
    uni.requestPayment({
      provider: 'wxpay', // 微信支付
      timeStamp: paymentData.timeStamp,
      nonceStr: paymentData.nonceStr,
      package: paymentData.package,
      signType: paymentData.signType,
      paySign: paymentData.paySign,
      success: function (res) {
        resolve(res)
      },
      fail: function (err) {
        reject(err)
      },
    })
  })
}
