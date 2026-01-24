<template>
    <div class="carousel-index">
        <div class="carousel-container" :style="`background-color: ${color};`">
            <div
                class="carousel-transform"
                :style="transformStyle"
            >
                <div 
                    class="carousel-slide" 
                    :style="`width: ${slideWidth}%`" 
                    v-for="(item, index) in displayCarousels" 
                    :key="`${item.id}-${index}`"
                >
                    <a class="carousel-inner" :href="item.target" target="_blank">
                        <img :src="item.url" :alt="item.title">
                    </a>
                </div>
                <div class="shadow" :style="`background: linear-gradient(to top, ${color}, ${color}00);`"></div>
            </div>
            <div class="carousel-footer__left">
                <div class="title"><span>{{ title }}</span></div>
                <div class="page">
                    <div
                        class="point"
                        :class="index === currentIndex ? 'is-active' : ''"
                        v-for="index in carousels.length"
                        :key="index"
                        @click="changePoint(index)"
                    ></div>
                </div>
            </div>
            <div class="carousel-footer__right">
                <button type="button" @click="preSlide">
                    <i class="iconfont icon-zuojiantou"></i>
                </button>
                <button type="button" @click="nextSlide">
                    <i class="iconfont icon-youjiantou"></i>
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, watch, nextTick } from 'vue';
import { useCarouselStore, type CarouselItem } from '../../stores/useCarouselStore';

// 定义 props
interface Props {
  autoPlay?: boolean;
  interval?: number;
}

const props = withDefaults(defineProps<Props>(), {
  autoPlay: true,
  interval: 3500
});

// 使用 Pinia store
const carouselStore = useCarouselStore();

// 响应式数据
const displayCarousels = ref<CarouselItem[]>([]);
const currentIndex = ref<number>(2);
const isRoll = ref<number>(0); // 0: 不滚, 1: 上一张, 2: 下一张
const color = ref<string>('');
const title = ref<string>('');

// 定时器引用
let timer: any | null = null;

// 计算属性
const carousels = computed(() => carouselStore.carousels);

const slideWidth = computed(() => {
  return carousels.value.length > 0 ? (100 / carousels.value.length).toFixed(4) : '100';
});

const transformStyle = computed(() => {
  if (!carousels.value.length) return '';
  
  const totalSlides = carousels.value.length;
  const width = `${totalSlides}00%`;
  
  if (isRoll.value === 2) {
    const translateX = -((100 / totalSlides) * 2).toFixed(4);
    return `transform: translateX(${translateX}%); transition: transform 400ms ease 0s; width: ${width};`;
  } else if (isRoll.value === 1) {
    return `transform: translateX(0); transition: transform 400ms ease 0s; width: ${width};`;
  } else {
    const translateX = -(100 / totalSlides).toFixed(4);
    return `transform: translateX(${translateX}%); transition: transform 0ms ease 0s; width: ${width};`;
  }
});

// 方法
const getCarousels = async () => {
  const data = await carouselStore.fetchCarousels();
  displayCarousels.value = [...data];
  
  // 初始化颜色和标题
  if (data.length > 1) {
    color.value = data[1].color;
    title.value = data[1].title;
  }
};

const startTimer = () => {
  if (!props.autoPlay) return;
  
  clearTimer();
  timer = setTimeout(() => {
    nextSlide();
  }, props.interval);
};

const clearTimer = () => {
  if (timer) {
    clearTimeout(timer);
    timer = null;
  }
};

const nextSlide = () => {
  clearTimer();
  
  if (!carousels.value.length) return;
  
  isRoll.value = 2;
  
  if (currentIndex.value < carousels.value.length) {
    currentIndex.value++;
  } else {
    currentIndex.value = 1;
  }
  
  // 更新颜色和标题为第三张图
  const thirdIndex = (currentIndex.value + 1) % carousels.value.length || carousels.value.length;
  if (carousels.value[thirdIndex - 1]) {
    color.value = carousels.value[thirdIndex - 1].color;
    title.value = carousels.value[thirdIndex - 1].title;
  }
  
  setTimeout(() => {
    // 将第一张图移到数组末尾
    if (displayCarousels.value.length) {
      const firstItem = displayCarousels.value.shift();
      if (firstItem) {
        displayCarousels.value.push(firstItem);
      }
    }
    
    isRoll.value = 0;
    
    // 更新颜色和标题为第二张图
    if (carousels.value.length > 1 && displayCarousels.value[1]) {
      const originalIndex = carousels.value.findIndex(item => 
        item.id === displayCarousels.value[1].id
      );
      if (originalIndex !== -1) {
        color.value = carousels.value[originalIndex].color;
        title.value = carousels.value[originalIndex].title;
      }
    }
    
    startTimer();
  }, 500);
};

const preSlide = () => {
  clearTimer();
  
  if (!carousels.value.length) return;
  
  isRoll.value = 1;
  
  if (currentIndex.value > 1) {
    currentIndex.value--;
  } else {
    currentIndex.value = carousels.value.length;
  }
  
  // 更新颜色和标题为第一张图
  if (carousels.value[0]) {
    color.value = carousels.value[0].color;
    title.value = carousels.value[0].title;
  }
  
  setTimeout(() => {
    // 将最后一张图移到数组开头
    if (displayCarousels.value.length) {
      const lastItem = displayCarousels.value.pop();
      if (lastItem) {
        displayCarousels.value.unshift(lastItem);
      }
    }
    
    isRoll.value = 0;
    
    // 更新颜色和标题为第二张图
    if (carousels.value.length > 1 && displayCarousels.value[1]) {
      const originalIndex = carousels.value.findIndex(item => 
        item.id === displayCarousels.value[1].id
      );
      if (originalIndex !== -1) {
        color.value = carousels.value[originalIndex].color;
        title.value = carousels.value[originalIndex].title;
      }
    }
    
    startTimer();
  }, 400);
};

const changePoint = (index: number) => {
  clearTimer();
  
  if (!carousels.value.length) return;
  
  currentIndex.value = index;
  
  // 从 store 中获取原始数据副本
  let list = [...carouselStore.carousels];
  
  if (index === 1) {
    // 将最后一项移到开头
    const lastItem = list.pop();
    if (lastItem) {
      list.unshift(lastItem);
    }
  } else {
    // 移动数组项
    for (let i = 0; i < index - 2; i++) {
      const firstItem = list.shift();
      if (firstItem) {
        list.push(firstItem);
      }
    }
  }
  
  displayCarousels.value = list;
  
  // 更新颜色和标题
  if (list.length > 1 && list[1]) {
    const originalIndex = carouselStore.carousels.findIndex(item => 
      item.id === list[1].id
    );
    if (originalIndex !== -1) {
      color.value = carouselStore.carousels[originalIndex].color;
      title.value = carouselStore.carousels[originalIndex].title;
    }
  }
  
  startTimer();
};

// 监听 carousels 变化
watch(
  () => carousels.value,
  (newCarousels) => {
    if (newCarousels.length) {
      displayCarousels.value = [...newCarousels];
      if (newCarousels.length > 1) {
        color.value = newCarousels[1].color;
        title.value = newCarousels[1].title;
      }
    }
  },
  { immediate: true }
);

// 生命周期钩子
onMounted(async () => {
  if (!carousels.value.length) {
    await getCarousels();
  }
  startTimer();
});

onBeforeUnmount(() => {
  clearTimer();
});
</script>

<style scoped>
.carousel-index {
    height: 100%;
}

.carousel-container {
    overflow: hidden;
    height: 100%;
    width: 100%;
}

.carousel-transform {
    overflow: hidden;
    display: flex;
    align-items: center;
}

.carousel-inner {
    height: 100%;
    width: 100%;
    position: relative;
    display: inline-block;
    line-height: 1;
    vertical-align: middle;
    background-color: var(--graph_bg_regular);
    cursor: pointer;
}

.carousel-inner img {
    display: block;
    width: 100%;
    height: 100%;
    object-fit: inherit;
}

.shadow {
    position: absolute;
    width: 100%;
    height: 30%;
    bottom: 0;
    pointer-events: none;   /* 禁止蒙版元素捕获鼠标事件 */
}

.carousel-footer__left {
    position: absolute;
    bottom: 20px;
    left: 15px;
}

.title span{
    display: block;
    font-weight: 400;
    line-height: 25px;
    color: #fff;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 10px;
}

@media (max-width: 1700.9px) {
    .title span{
        font-size: 18px;
    }
}

@media (min-width: 1701px) {
    .title span{
        font-size: 20px;
    }
}

.page {
    display: flex;
    align-items: center;
    margin-left: -1.5px;
    font-size: 0;
    transform: translateZ(0);
}

.point {
    position: relative;
    display: inline-block;
    width: 8px;
    height: 8px;
    margin: 4px;
    border-radius: 50%;
    background-color: rgba(255,255,255,.4);
    overflow: hidden;
    cursor: pointer;
}

.is-active {
    width: 14px;
    height: 14px;
    margin: 1px;
    background-color: #fff;
}

.carousel-footer__right {
    position: absolute;
    bottom: 40px;
    right: 15px;
}

.carousel-footer__right button {
    display: -webkit-flex;
    display: flex;
    align-items: center;
    justify-content: center;
    display: -webkit-inline-flex;
    display: inline-flex;
    width: 28px;
    height: 28px;
    border-radius: 8px;
    margin-right: 12px;
    background-color: rgba(255,255,255,.1);
    cursor: pointer;
}

.carousel-footer__right button:hover {
    background-color: rgba(255,255,255,.2);
}

.carousel-footer__right .iconfont {
    color: #fff;
}
</style>