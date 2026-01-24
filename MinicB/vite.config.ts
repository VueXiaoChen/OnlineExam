import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from "path";
// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: [ // 配置 @ 指代 src
      {
        find: "@",
        replacement: resolve(__dirname, "./src"),
      },
    ],
  },
  css:{
    preprocessorMaxWorkers: true,
    preprocessorOptions: {
      scss: {
        additionalData: '@use "@/styles/style.scss" as *; '
      }
    }
  },
  server: {
    open: true, // 项目建成自动打开窗口
    proxy: {
      "/api": {
        target: "http://127.0.0.1:8080",  // 连接本地后端地址
        changeOrigin: true, // 是否改变源地址，设置为 true 可以通过更改请求头中的 host 和 origin 属性来更改请求的源地址
        ws: true, // 表示开启 WebSocket 代理。如果后端服务使用了 WebSocket，那么这个选项需要设置为 true
        cors: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
    },
    client: {
      overlay: false, // 关闭 Uncaught error 的全屏提示
    },
  },
})
