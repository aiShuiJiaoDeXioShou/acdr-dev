import { createSSRApp } from 'vue'
import App from './App.vue'
import store from './store' // 这里导入上面定义的 store
import { routeInterceptor, requestInterceptor, prototypeInterceptor } from './interceptors'
import 'virtual:uno.css'
import '@/style/index.scss'
import '@/style/index.css'

export function createApp() {
  const app = createSSRApp(App)

  // 使用 Pinia 存储
  app.use(store)

  // 其他插件
  app.use(routeInterceptor)
  app.use(requestInterceptor)
  app.use(prototypeInterceptor)

  return {
    app,
  }
}
