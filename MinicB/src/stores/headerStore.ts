import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// 定义用户接口
interface User {
  uid: number | null;
  nickname: string;
  avatar_url: string;
  vip: number; // 0: 非会员, 1: 月度, 2: 季度, 3: 年度
  exp: number;
  gender: number; // 0: 女, 1: 男
  coin: number;
  followsCount: number;
  fansCount: number;
}

// 定义热搜项接口
interface TrendingItem {
  id: number;
  content: string;
  type: number; // 1: new, 2: hot
  rank: number;
}

// 定义 store 状态接口
interface HeaderState {
  isLogin: boolean;
  user: User;
  msgUnread: number[]; // [回复, @我, 点赞, 系统, 私信]
  trendings: TrendingItem[];
  openLogin: boolean;
}

export const useHeaderStore = defineStore('header', () => {
  // 状态
  const isLogin = ref<boolean>(false);
  const user = ref<User>({
    uid: null,
    nickname: '',
    avatar_url: '',
    vip: 0,
    exp: 0,
    gender: 0,
    coin: 0,
    followsCount: 0,
    fansCount: 0
  });
  const msgUnread = ref<number[]>([0, 0, 0, 0, 0]);
  const trendings = ref<TrendingItem[]>([]);
  const openLogin = ref<boolean>(false);

  // Getter 计算属性
  const totalMsgUnread = computed(() => {
    return msgUnread.value.reduce((sum, count) => sum + count, 0);
  });

  // Action 方法
  const setUser = (userData: Partial<User>) => {
    user.value = { ...user.value, ...userData };
    isLogin.value = true;
  };

  const clearUser = () => {
    isLogin.value = false;
    user.value = {
      uid: null,
      nickname: '',
      avatar_url: '',
      vip: 0,
      exp: 0,
      gender: 0,
      coin: 0,
      followsCount: 0,
      fansCount: 0
    };
  };

  const setTrendings = (newTrendings: TrendingItem[]) => {
    trendings.value = newTrendings;
  };

  const setMsgUnread = (newMsgUnread: number[]) => {
    msgUnread.value = newMsgUnread;
  };

  const setOpenLogin = (status: boolean) => {
    openLogin.value = status;
  };

  const logout = () => {
    clearUser();
    // 这里可以添加清除token等操作
    localStorage.removeItem('token');
  };

  return {
    // 状态
    isLogin,
    user,
    msgUnread,
    trendings,
    openLogin,
    
    // Getter
    totalMsgUnread,
    
    // Action
    setUser,
    clearUser,
    setTrendings,
    setMsgUnread,
    setOpenLogin,
    logout
  };
},{
  persist:{
    key: 'head_stores',
    storage: localStorage,
    paths: ['isLogin','user'] // 只持久化这些状态
  }
});

// 导出类型
export type { User, TrendingItem, HeaderState };