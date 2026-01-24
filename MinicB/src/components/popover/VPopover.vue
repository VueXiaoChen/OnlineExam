<template>
    <div @mouseleave="handleMouseLeave" style="position: relative;">
      <div @mouseenter="handleMouseEnter" @click="handleClick" style="position: relative;" ref="vPopRef">
        <slot name="reference"></slot>
      </div>
      <div class="v-popover" :class="'to-' + placement" :style="popStyle">
        <div
          class="v-popover-content"
          ref="vPopCon"
          :class="isPopoverShow ? `popShow-${placement}` : `popHide-${placement}`"
          :style="{ display: popoverDisplay }"
        >
          <slot name="content"></slot>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
  
  // 定义 Props 接口
  interface VPopoverProps {
    // 显示方向
    placement?: string;
    // 触发方式 目前支持悬停hover、点击click
    trigger?: 'hover' | 'click';
    // 自定义样式
    popStyle?: string;
  }
  
  // 定义 Props
  const props = withDefaults(defineProps<VPopoverProps>(), {
    placement: 'bottom',
    trigger: 'hover',
    popStyle: ''
  });
  
  // 定义模板引用
  const vPopRef = ref<HTMLElement | null>(null);
  const vPopCon = ref<HTMLElement | null>(null);
  
  // 响应式数据
  const popoverDisplay = ref<string>('none');
  const isPopoverShow = ref<boolean>(false);
  
  // 节流计时器
  let inTimer: any;
  // let outTimer: NodeJS.Timeout;
  
  // 显示弹窗
  const show = () => {
    popoverDisplay.value = '';
    isPopoverShow.value = true;
  };
  
  // 隐藏弹窗
  const hide = () => {
    isPopoverShow.value = false;
    setTimeout(() => {
      popoverDisplay.value = 'none';
    }, 300);
  };
  
  // 鼠标进入处理
  const handleMouseEnter = () => {
    if (props.trigger === 'hover') {
      inTimer = setTimeout(() => {
        show();
      }, 100);
    }
  };
  
  // 鼠标离开处理
  const handleMouseLeave = () => {
    if (props.trigger === 'hover') {
      clearTimeout(inTimer);
      hide();
    }
  };
  
  // 点击处理
  const handleClick = () => {
    if (props.trigger === 'click') {
      if (isPopoverShow.value) {
        hide();
      } else {
        show();
      }
    }
  };
  
  // 点击空白处关闭气泡
  const handleOutsideClick = (event: MouseEvent) => {
    const vPopRefElement = vPopRef.value;
    const vPopConElement = vPopCon.value;
    
    if (
      vPopRefElement && 
      !vPopRefElement.contains(event.target as Node) && 
      vPopConElement && 
      !vPopConElement.contains(event.target as Node)
    ) {
      hide();
    }
  };
  
  // 监听 trigger 变化，重新绑定/解绑事件
  watch(
    () => props.trigger,
    (newTrigger, oldTrigger) => {
      if (oldTrigger === 'click') {
        window.removeEventListener('click', handleOutsideClick);
      }
      if (newTrigger === 'click') {
        window.addEventListener('click', handleOutsideClick);
      }
    },
    { immediate: true }
  );
  
  onMounted(() => {
    // 如果初始 trigger 是 click，则添加事件监听
    if (props.trigger === 'click') {
      window.addEventListener('click', handleOutsideClick);
    }
  });
  
  onBeforeUnmount(() => {
    // 清理定时器
    if (inTimer) {
      clearTimeout(inTimer);
    }
    
    // 移除事件监听
    if (props.trigger === 'click') {
      window.removeEventListener('click', handleOutsideClick);
    }
  });
  
  // 暴露方法给父组件
  defineExpose({
    show,
    hide
  });
  </script>
  

<style scoped>
.v-popover {
    position: absolute;
    transition: .3s;
    z-index: 1;
}

.v-popover-content {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 0 30px rgba(0,0,0,.1);
    border: 1px solid var(--line_regular);
}

.to-bottom {
    top: 100%;
    left: 50%;
    padding-top: 5px;
}

.to-right {
    top: 50%;
    left: 100%;
    padding-left: 5px;
}

.to-top {
    bottom: 100%;
    left: 50%;
    padding-bottom: 5px;
}

.to-left {
    top: 50%;
    right: 100%;
    padding-right: 5px;
}

.to-top, .to-bottom {
    transform: translate3d(-50%,0,0);   /* 水平左移半个元素身位，使其水平与父元素居中 */
}

.to-left, .to-right {
    transform: translate3d(0,-50%,0);   /* 垂直上移半个元素身位，使其垂直与父元素居中 */
}

.popHide-bottom {
    animation: fade-out-bottom 0.2s ease-out forwards;
    transform-origin: top; /* 设置动画的旋转点为顶部 */
}

.popShow-bottom {
    animation: fade-in-bottom 0.2s ease-out forwards;
    transform-origin: top;
}

/* 淡入动画 */
@keyframes fade-in-bottom {
    0% {
        opacity: 0; /* 初始状态透明 */
        transform: translateY(-5px); /* 向上平移 10px，将元素隐藏在顶部 */
    }
    100% {
        opacity: 1; /* 最终状态不透明 */
        transform: translateY(0); /* 平移恢复到原始位置 */
    }
}

/* 淡出动画 */
@keyframes fade-out-bottom {
    0% {
        opacity: 1; /* 初始状态不透明 */
        transform: translateY(0);   /* 原始位置 */
    }
    100% {
        opacity: 0; /* 最终状态透明 */
        transform: translateY(-5px); /* 向上平移 10px，将元素隐藏在顶部 */
    }
}

.popHide-right {
    animation: fade-out-right 0.2s ease-out forwards;
    transform-origin: left;
}

.popShow-right {
    animation: fade-in-right 0.2s ease-out forwards;
    transform-origin: left;
}

/* 淡入动画 */
@keyframes fade-in-right {
    0% {
        opacity: 0;
        transform: translateX(-5px);
    }
    100% {
        opacity: 1;
        transform: translateX(0);
    }
}

/* 淡出动画 */
@keyframes fade-out-right {
    0% {
        opacity: 1;
        transform: translateX(0);
    }
    100% {
        opacity: 0;
        transform: translateX(-5px);
    }
}

.popHide-top {
    animation: fade-out-top 0.2s ease-out forwards;
    transform-origin: bottom;
}

.popShow-top {
    animation: fade-in-top 0.2s ease-out forwards;
    transform-origin: bottom;
}

/* 淡入动画 */
@keyframes fade-in-top {
    0% {
        opacity: 0;
        transform: translateY(5px);
    }
    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 淡出动画 */
@keyframes fade-out-top {
    0% {
        opacity: 1;
        transform: translateY(0);
    }
    100% {
        opacity: 0;
        transform: translateY(5px);
    }
}

.popHide-left {
    animation: fade-out-left 0.2s ease-out forwards;
    transform-origin: right;
}

.popShow-left {
    animation: fade-in-left 0.2s ease-out forwards;
    transform-origin: right;
}

/* 淡入动画 */
@keyframes fade-in-left {
    0% {
        opacity: 0;
        transform: translateX(5px);
    }
    100% {
        opacity: 1;
        transform: translateX(0);
    }
}

/* 淡出动画 */
@keyframes fade-out-left {
    0% {
        opacity: 1;
        transform: translateX(0);
    }
    100% {
        opacity: 0;
        transform: translateX(5px);
    }
}
</style>