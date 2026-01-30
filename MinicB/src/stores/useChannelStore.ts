// stores/channel.js
import { defineStore } from 'pinia'
import { ref,computed  } from 'vue'

// 定义子分类接口
interface SubCategory {
  scId: string;
  scName: string;
  descr?: string;
}

// 定义主分类接口
interface MainCategory {
  mcId: string;
  mcName: string;
  scList: SubCategory[];
}


export const useChannelStore = defineStore('channel', () => {
  // 状态
  const channels = ref<MainCategory[]>([]);
  const isLoading = ref<boolean>(false);
  const error = ref<string | null>(null);

  // 获取频道数据
  const fetchChannels = async () => {
    try {
      isLoading.value = true;
      error.value = null;
      
      // 这里根据你的实际 API 进行修改
      // const response = await fetch('/api/channels');
      // const data = await response.json();
      // channels.value = data;
      
      // 临时示例数据
      channels.value = [
        {
          mcId: 'anime',
          mcName: '番剧',
          scList: [
            { scId: 'finish', scName: '完结动画', descr: '已完结的动画作品' },
            { scId: 'serial', scName: '连载动画', descr: '正在连载的动画作品' },
            { scId: 'guochuang', scName: '国创', descr: '国产动画作品' },
            { scId: 'movie', scName: '动画电影', descr: '动画电影作品' },
          ]
        },
        {
          mcId: 'live',
          mcName: '直播',
          scList: [
            { scId: 'game', scName: '游戏直播', descr: '各种游戏直播' },
            { scId: 'entertainment', scName: '娱乐直播', descr: '娱乐互动直播' },
            { scId: 'education', scName: '学习直播', descr: '知识分享直播' },
          ]
        },
        // ... 更多分类
      ];
      
      return channels.value;
    } catch (err) {
      error.value = '获取频道数据失败';
      console.error('Fetch channels error:', err);
      return [];
    } finally {
      isLoading.value = false;
    }
  };

  // 根据主分类 ID 获取子分类
  const getSubCategories = (mcId: string): SubCategory[] => {
    const mainCategory = channels.value.find(item => item.mcId === mcId);
    return mainCategory?.scList || [];
  };

  // 根据分类 ID 获取分类名称
  const getCategoryName = (mcId: string, scId?: string): string => {
    const mainCategory = channels.value.find(item => item.mcId === mcId);
    if (!mainCategory) return '';
    
    if (!scId) return mainCategory.mcName;
    
    const subCategory = mainCategory.scList.find(item => item.scId === scId);
    return subCategory?.scName || '';
  };

  return {
    // 状态
    channels,
    isLoading,
    error,
    
    // 方法
    fetchChannels,
    getSubCategories,
    getCategoryName,
  };
});