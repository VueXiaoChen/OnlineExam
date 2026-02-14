// @/network/request.js
import axios from 'axios';
import { ElMessage } from 'element-plus';

// 导入 Pinia stores
let userStore = null;
let messageStore = null;
let appStore = null;
let channelStore = null;
let videodeoStore = null;

// 初始化 stores（在 main.js 中调用）
export function initRequestStores(stores) {
  userStore = stores.userStore;
  messageStore = stores.messageStore;
  appStore = stores.appStore;
  channelStore=stores.channelStore;
  videodeoStore=stores.videodeoStore
}

// 处理未登录错误的函数
function handleNotLoginError() {
  if (userStore) {
    userStore.initData();
  }
  
  // 关闭websocket
  if (messageStore && messageStore.ws) {
    messageStore.ws.close();
    messageStore.setWebSocket(null);
  }
  
  // 清除本地token缓存
  localStorage.removeItem("token");
  ElMessage.error("请登录后查看");
  
  if (appStore) {
    appStore.setLoading(false);
  }
}

// 处理其他错误的函数
function handleOtherError() {
  ElMessage.error("特丽丽被玩坏了(¯﹃¯)");
  
  if (appStore) {
    appStore.setLoading(false);
  }
}

/** 这两个封装方法适合有返回值的请求 **/

// get请求
export function get(url, config) {
  // 请求超过30秒则判定为超时
  const instance = axios.create({
    baseURL: '/api',
    timeout: 30000,
    withCredentials: true,
  });

  // axios拦截器
  // 请求拦截
  instance.interceptors.request.use(
    (config) => {
      return config;
    },
    (err) => {
      console.log(err);
    },
  );

  // 响应拦截
  instance.interceptors.response.use(
    (config) => {
      const code = config.data.code;
      if (code && code !== 200)
        ElMessage.error(config.data.message || '未知错误, 请打开控制台查看');
      return config;
    },
    (err) => {
      console.log(err);
      if (err.response && err.response.headers && err.response.headers.message === 'not login') {
        handleNotLoginError();
      } else {
        handleOtherError();
      }
    },
  );

  instance.defaults.withCredentials = true;

  if (config) {
    if (config.params) {
      if (config.headers) {
        return instance.get(url, {params: config.params, headers: config.headers}); // 有参数和请求头
      }
      return instance.get(url, {params: config.params}); // 有参数没请求头
    }
    if (config.headers) {
      return instance.get(url, {headers: config.headers}); // 没参数有请求头
    }
  } else {
    return instance.get(url); // 没参数和请求头
  }
}

// post请求
export function post(url, data, headers) {
  // 请求超过30秒则判定为超时
  const instance = axios.create({
    baseURL: '/api',
    timeout: 30000,
    withCredentials: true,
  });

  // axios拦截器
  // 请求拦截
  instance.interceptors.request.use(
    (config) => {
      return config;
    },
    (err) => {
      console.log(err);
    },
  );

  // 响应拦截
  instance.interceptors.response.use(
    (config) => {
      const code = config.data.code;
      if (code && code !== 200)
        ElMessage.error(config.data.message || '未知错误, 请打开控制台查看');
      return config;
    },
    (err) => {
      console.log(err);
      if (err.response && err.response.headers && err.response.headers.message == 'not login') {
        handleNotLoginError();
      } else {
        handleOtherError();
      }
    },
  );

  instance.defaults.withCredentials = true;

  // 如果 data 是 Content-Type: application/json ，后端要用 @RequestBody 接收
  if (headers) {
    return instance.post(url, data, headers); // 有请求头
  }
  return instance.post(url, data);  // 没请求头
}