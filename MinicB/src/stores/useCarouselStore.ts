import { defineStore } from 'pinia';
import { ref,computed  } from 'vue';

// 定义轮播图项接口
interface CarouselItem {
  id: number;
  url: string;
  target: string;
  color: string;
  title: string;
  index?: number;
}

export const useCarouselStore = defineStore('carousel', () => {
  // 状态
  const carousels = ref<CarouselItem[]>([]);
  const isLoading = ref<boolean>(false);
  
  // Action 方法
  const updateCarousels = (newCarousels: CarouselItem[]) => {
    carousels.value = newCarousels;
  };
  
  const fetchCarousels = async () => {
    try {
      isLoading.value = true;
      // 这里可以根据需要改为 API 请求
      const response = await import('@/assets/json/carousel.json');
      carousels.value = response.default;
      return response.default;
    } catch (error) {
      console.error('Failed to load carousels:', error);
      return [];
    } finally {
      isLoading.value = false;
    }
  };
  
  return {
    // 状态
    carousels,
    isLoading,
    
    // Action
    updateCarousels,
    fetchCarousels
  };
});

// 导出类型
export type { CarouselItem };