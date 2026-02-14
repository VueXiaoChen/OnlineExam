<template>
    <div class="comment">
      <div class="teriteri-comment browser-pc">
        <div class="comment-container">
          <!-- 评论头部 -->
          <div class="reply-header">
            <div class="reply-navigation">
              <ul class="nav-bar">
                <li class="nav-title">
                  <span class="nav-title-text">评论</span>
                  <span class="total-reply">{{ count }}</span>
                </li>
                <li class="nav-sort">
                  <div 
                    class="sort-item" 
                    :class="{ 'active': type === 1 }" 
                    @click="changeCommentSort(1)"
                  >
                    最热
                  </div>
                  <div class="part-symbol"></div>
                  <div 
                    class="sort-item" 
                    :class="{ 'active': type === 2 }" 
                    @click="changeCommentSort(2)"
                  >
                    最新
                  </div>
                </li>
              </ul>
            </div>
          </div>
          
          <!-- 评论区 -->
          <div class="reply-wrap">
            <!-- 主回复框 -->
            <div class="main-reply-box">
              <div class="reply-box">
                <ReplyTextarea 
                  :comment-info="commentInfo" 
                  ref="rootReplyRef" 
                  @add-comment="addComment" 
                  :is-wide-window="isWideWindow" 
                />
              </div>
            </div>
            
            <!-- 评论树组件 -->
            <CommentTree 
              ref="commentTreeRef" 
              :type="type" 
              :up-uid="uid" 
              :is-wide-window="isWideWindow" 
            />
            
            <!-- 固定回复框（滚动时显示） -->
            <div 
              class="fixed-reply-box" 
              :class="isHideReplyBox ? 'reply-box-hide' : 'reply-box-show'"
              :style="fixedReplyBoxStyle"
            >
              <i class="reply-box-shadow"></i>
              <div class="reply-box fixed-box">
                <ReplyTextarea 
                  ref="fixReplyBoxRef" 
                  :comment-info="commentInfo" 
                  @add-comment="addComment" 
                  :is-wide-window="isWideWindow" 
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, onUnmounted, watch, nextTick, computed } from 'vue'
  import { useRoute } from 'vue-router'
  
  // 组件
  import CommentTree from './CommentTree.vue'
  import ReplyTextarea from './ReplyTextarea.vue'
  
  // Pinia store
  import { useUserStore } from '../../stores/useUserStore'
  
  // 定义 props
  const props = defineProps({
    uid: {
      type: Number,
      default: 0
    },
    count: {
      type: Number,
      default: 0
    }
  })

  
  // 定义 emits
  const emit = defineEmits(['comment-added'])
  
  // 路由和 store
  const route = useRoute()
  const userStore = useUserStore()
  // 响应式数据
  const type = ref(0) // 评论排序方式 1最热 2最新
  const isWideWindow = ref(false) // 是否是宽屏
  const left = ref(0) // 固钉评论框左偏移量
  const width = ref(0) // 固钉评论框宽度
  const isHideReplyBox = ref(true) // 是否隐藏回复框
  const replyBoxDisplay = ref('none') // 回复框显示状态
  
  // refs
  const rootReplyRef = ref(null)
  const commentTreeRef = ref()
  const fixReplyBoxRef = ref(null)
  
  // 评论信息
  const commentInfo = computed(() => ({
    root_id: 0,
    parent_id: 0,
    to_user_id: props.uid,
    vid: route.params.vid
  }))
  
  // 计算属性：固定回复框样式
  const fixedReplyBoxStyle = computed(() => {
    return {
      '--left': `${left.value}px`,
      '--width': `${width.value}px`,
      display: replyBoxDisplay.value
    }
  })
  
  // 添加评论
  const addComment = (comment) => {
    if (commentTreeRef.value) {
      commentTreeRef.value.addComment(comment.data.data)
    }
    //emit('comment-added', comment)
  }
  
  // 改变评论排序方式
  const changeCommentSort = (sortType) => {
    if (type.value === sortType) return
    
    type.value = sortType
    
    if (commentTreeRef.value) {
      commentTreeRef.value.clearCommentList()
      
      nextTick(() => {
        commentTreeRef.value.getCommentTree()
      })
    }
  }
  
  // 点击空白处关闭回复框
  const handleOutSideClick = (event) => {
    // 获取所有可能触发回复框的元素
    const selectors = [
      '.textarea-wrap',
      '.reply-box-emoji',
      '.at-btn',
      '.reply-box-send',
      '.reply-btn',
      '.sub-reply-btn',
      '.emoji-box'
    ]
    
    const elements = []
    selectors.forEach(selector => {
      const els = document.querySelectorAll(selector)
      elements.push(...els)
    })
    
    // 检查点击是否在这些元素内
    const isClickInside = elements.some(element => 
      element && element.contains(event.target)
    )
    
    if (isClickInside) return
    
    // 取消所有回复框的焦点
    nextTick(() => {
      if (rootReplyRef.value) {
        rootReplyRef.value.cancelFocus?.()
      }
      
      if (commentTreeRef.value) {
        commentTreeRef.value.cancelFocus?.()
      }
      
      if (fixReplyBoxRef.value) {
        fixReplyBoxRef.value.cancelFocus?.()
      }
    })
  }
  
  // 调整窗口大小
  const resize = () => {
    isWideWindow.value = window.innerWidth >= 1620
    
    const replyWrap = document.querySelector('.reply-wrap')
    if (replyWrap) {
      width.value = replyWrap.clientWidth
      left.value = replyWrap.getBoundingClientRect().left
    }
  }
  
  // 处理滚动，显示/隐藏固定回复框
  const handleScroll = () => {
    const inputElement = document.querySelector('.main-reply-box')
    if (!inputElement) return
    
    const offsetBottom = inputElement.getBoundingClientRect().bottom
    
    if (offsetBottom < 0 && isHideReplyBox.value) {
      // 显示固定回复框
      replyBoxDisplay.value = ''
      isHideReplyBox.value = false
    } else if (offsetBottom >= 0 && !isHideReplyBox.value) {
      // 隐藏固定回复框
      isHideReplyBox.value = true
      setTimeout(() => {
        replyBoxDisplay.value = 'none'
      }, 300)
    }
  }
  
  // 初始化
  const init = () => {
    nextTick(() => {
      resize()
      handleScroll()
    })
  }
  
  // 生命周期
  onMounted(() => {
    init()
    window.addEventListener('click', handleOutSideClick)
    window.addEventListener('resize', resize)
    window.addEventListener('scroll', handleScroll)
  })
  
  onUnmounted(() => {
    window.removeEventListener('click', handleOutSideClick)
    window.removeEventListener('resize', resize)
    window.removeEventListener('scroll', handleScroll)
  })
  
  // 监听 props 变化
  watch(() => props.uid, (newUid) => {
    // 更新评论信息中的 to_user_id
    // 注意：由于 commentInfo 是计算属性，它会自动更新
  })
  
  // 监听路由变化
  watch(() => route.params.vid, (newVid) => {
    // 更新评论信息中的 vid
    // 注意：由于 commentInfo 是计算属性，它会自动更新
  })
  </script>
  

<style scoped>
.comment {
    margin-top: 24px;
    z-index: 0;
    position: relative;
}

.teriteri-comment .browser-pc {
    background-color: #FFFFFF;
}

.comment-container {
    animation-name: enterAnimation-commentContainer;
    animation-duration: 1s;
    animation-fill-mode: forwards;
}



.reply-navigation {
    margin-bottom: 22px;
}

.nav-bar {
    display: flex;
    align-items: center;
    list-style: none;
    margin: 0;
    padding: 0;
}

.nav-title {
    display: flex;
    align-items: center;
    justify-content: center;
}

.nav-title .nav-title-text {
    color: black;
    font-size: 20px;
    font-weight: 500;
}

.total-reply {
    margin: 0 36px 0 6px;
    font-weight: 400;
    color: #9499A0;

}

@media screen and (max-width: 1681px) {
    .total-reply {
        font-size: 13px;
    }
}

@media screen and (min-width: 1681px) {
    .total-reply {
        font-size: 14px;
    }
}

.nav-sort {
    display: flex;
    align-items: center;
}

.sort-item {
    color: var(--text3);
    cursor: pointer;
}

.sort-item:hover {
    color: var(--brand_pink);
}

.sort-item.active {
    color: var(--text1) !important;
}

.part-symbol {
    height: 11px;
    margin: 0 12px;
    border-left: solid 1px;
}

.reply-wrap {
    position: relative;
}

.reply-box {
    display: flex;
    flex-direction: column;
}

.fixed-reply-box {
    position: fixed;
    bottom: 0;
    left: var(--left);
    z-index: 10;
    width: var(--width);
}

.reply-box-hide {
    animation: fade-out-bottom 0.3s ease-out forwards;
    transform-origin: bottom;
}

.reply-box-show {
    animation: fade-in-bottom 0.3s ease-out forwards;
    transform-origin: bottom;
}

@keyframes fade-in-bottom {
    0% {
        opacity: 0;
        transform: translateY(5px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes fade-out-bottom {
    0% {
        opacity: 1;
        transform: translateY(0);
    }

    100% {
        opacity: 0;
        transform: translateY(5x);
    }
}

.reply-box-shadow {
    position: absolute;
    top: -10px;
    z-index: 1;
    width: 100%;
    height: 36px;
    border-radius: 50%;
    background-color: #00000014;
    filter: blur(10px);
}

.reply-box.fixed-box {
    position: relative;
    z-index: 2;
    padding: 15px 0;
    border-top: 0.5px solid #E3E5E7;
    background-color: #FFFFFF;
}

.reply-box {
    display: flex;
    flex-direction: column;
}
</style>