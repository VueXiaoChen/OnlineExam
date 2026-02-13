// stores/user.js
import { defineStore } from 'pinia'
import { ref,computed  } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useChannelStore } from '@/stores/useChannelStore'
import { useVideoStore } from '@/stores/useVideoStore'
import { useMessageStore } from '@/stores/useMessageStore'
import { useHeaderStore } from '@/stores/headerStore'
export const useUserStore = defineStore('user', () => {

  // 其他 store
  const channelStore = useChannelStore()
  const videoStore = useVideoStore()
  const messageStore = useMessageStore()
  const headerStore = useHeaderStore()
  // actions
  const initData = () => {
    isLogin.value = false
    user.value = {}
    messageStore.initMessageData()
    videoStore.initVideoData()
    channelStore.initChannelData()
  }
  
// 状态
const isLogin = ref<boolean>(false);
const user = ref<User | null>(null);
const token = ref<string>('');
const ws = ref<WebSocket | null>(null);
const favorites = ref<FavoriteItem[]>([]);
const likeComment = ref<number[]>([]);
const dislikeComment = ref<number[]>([]);
const isLoading = ref<boolean>(false);

// 登录
const login = async (username: string, password: string) => {
  try {
    isLoading.value = true;
    const response = await axios.post('/api/user/loading', {
      username,
      password,
    });
    if (response.data.code === 200) {
      const { token: userToken, userDTO: userData } = response.data.data;
      token.value = userToken;
      user.value = userData;
      headerStore.user.avatar_url=user.value.avatar_url
      headerStore.user.coin=user.value.coin
      headerStore.user.fansCount=user.value.fansCount
      headerStore.user.followsCount=user.value.followsCount
      headerStore.user.vip=user.value.vip
      headerStore.user.exp=user.value.exp
      headerStore.user.gender = user.value.gender
      headerStore.isLogin=true
      headerStore.user=userData
      //await messageStore.connectWebSocket()
      // 存储 token 到 localStorage
      // localStorage.setItem('token', userToken);
      // localStorage.setItem('user', JSON.stringify(userData));
      // localStorage.setItem('isLogin', headerStore.isLogin);
      
      return { success: true, message: response.data.message };
    } else {
      return { success: false, message: response.data.message };
    }
  } catch (error) {
    console.error('Login error:', error);
    return { success: false, message: '登录失败' };
  } finally {
    isLoading.value = false;
  }
};

// 注册
const register = async (username: string, password: string, confirmedPassword: string) => {
  try {
    const response = await axios.post('/api/user/account/register', {
      username,
      password,
      confirmedPassword,
    });
    
    if (response.data.code === 200) {
      return { success: true, message: response.data.message };
    } else {
      return { success: false, message: response.data.message };
    }
  } catch (error) {
    console.error('Register error:', error);
    return { success: false, message: '注册失败' };
  }
};

// 连接 WebSocket
const connectWebSocket = async () => {
  try {
    // 这里根据你的 WebSocket 配置进行调整
    const wsUrl = `ws://${window.location.host}/ws`;
    const socket = new WebSocket(wsUrl);
    
    socket.onopen = () => {
      console.log('WebSocket connected');
      if (token.value) {
        const connection = JSON.stringify({
          code: 100,
          content: `Bearer ${token.value}`,
        });
        socket.send(connection);
      }
    };
    
    socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
    
    ws.value = socket;
  } catch (error) {
    console.error('Connect WebSocket error:', error);
  }
};

// 获取收藏夹
const getFavorites = async () => {
  try {
    if (!user.value) return;
    
    const response = await axios.get('/api/favorite/get-all/user', {
      params: { uid: user.value.uid },
      headers: { Authorization: `Bearer ${token.value}` },
    });
    
    if (response.data.data) {
      const defaultFav = response.data.data.find((item: FavoriteItem) => item.type === 1);
      const list = response.data.data.filter((item: FavoriteItem) => item.type !== 1);
      if (defaultFav) list.unshift(defaultFav);
      favorites.value = list;
    }
  } catch (error) {
    console.error('Get favorites error:', error);
  }
};

// 获取用户赞踩的评论集合
const getLikeAndDisLikeComment = async () => {
  try {
    if (!user.value) return;
    
    const response = await axios.get('/api/comment/get-like-and-dislike', {
      params: { uid: user.value.uid },
      headers: { Authorization: `Bearer ${token.value}` },
    });
    
    if (response.data.data) {
      likeComment.value = response.data.data.userLike || [];
      dislikeComment.value = response.data.data.userDislike || [];
    }
  } catch (error) {
    console.error('Get like/dislike comments error:', error);
  }
};

// 获取未读消息
const getMsgUnread = async () => {
  // 根据你的实际 API 实现
  // 这里假设有一个获取未读消息的接口
  try {
    const response = await axios.get('/api/message/unread', {
      headers: { Authorization: `Bearer ${token.value}` },
    });
    
    // 处理未读消息数据
    // ...
  } catch (error) {
    console.error('Get unread messages error:', error);
  }
};

// 登出
const logout = () => {
  isLogin.value = false;
  user.value = null;
  token.value = '';
  favorites.value = [];
  likeComment.value = [];
  dislikeComment.value = [];
  
  // 关闭 WebSocket
  if (ws.value) {
    ws.value.close();
    ws.value = null;
  }
  
  // 清除 localStorage
  localStorage.removeItem('teri_token');
};

return {
  // 状态
  isLogin,
  user,
  token,
  ws,
  favorites,
  likeComment,
  dislikeComment,
  isLoading,
  
  // 方法
  login,
  register,
  connectWebSocket,
  getFavorites,
  getLikeAndDisLikeComment,
  getMsgUnread,
  logout,
};
},{
  persist:{
    key: 'user_stores',
    storage: localStorage,
    paths: ['user','token'] // 只持久化这些状态
  }
});