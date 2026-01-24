import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { initRequestStores } from '@/network/request'
// 全局样式表
import "./assets/css/base.css"
// 导入 Pinia stores
import { useAppStore } from '@/stores/useAppStore'
import { useChannelStore } from '@/stores/useChannelStore'
import { useMessageStore } from '@/stores/useMessageStore'
import { useUserStore} from '@/stores/useUserStore'
import { useVideoStore} from '@/stores/useVideoStore'
const app = createApp(App);
const pinia = createPinia()

pinia.use(piniaPluginPersistedstate);
// 创建 Pinia store 实例
// const appStore = useAppStore(pinia)
// const userStore = useUserStore(pinia)
// const messageStore = useMessageStore(pinia)
// const channelStore = useChannelStore(pinia)
// const videodeoStore = useVideoStore(pinia)

// // 初始化请求封装中的 Pinia stores
// initRequestStores({
//   userStore,
//   messageStore,
//   appStore,
//   channelStore,
//   videodeoStore,
// })

app.use(router).use(ElementPlus).use(pinia).mount('#app');


