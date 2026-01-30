<template>
    <div class="search-video">
      <div class="search-page i_wrapper">
        <div class="container">
          <!-- 视频卡片列表 -->
          <div class="video-card" v-for="(item, index) in videoList" :key="item.video.vid || index">
            <!-- 实体内容 -->
            <div class="video-card__wrap">
              <!-- 视频封面和链接 -->
              <a :href="`/video/${item.video.vid}`" target="_blank" @click.prevent="goToVideo(item.video.vid)">
                <div class="video-card__image">
                  <div class="video-card__image--wrap">
                    <picture class="video-card__cover">
                      <img :src="item.video.coverUrl" :alt="item.video.title" loading="lazy">
                    </picture>
                  </div>
                  <div class="video-card__mask">
                    <div class="video-card__stats">
                      <div class="video-card__stats--left">
                        <span class="video-card__stats--item">
                          <i class="iconfont icon-bofangshu"></i>
                          <span class="video-card__stats--text">
                            {{ handleNum(item.stats.play) }}
                          </span>
                        </span>
                        <span class="video-card__stats--item">
                          <i class="iconfont icon-danmushu"></i>
                          <span class="video-card__stats--text">
                            {{ handleNum(item.stats.danmu) }}
                          </span>
                        </span>
                      </div>
                      <div class="video-card__stats__duration">
                        {{ handleDuration(item.video.duration) }}
                      </div>
                    </div>
                  </div>
                </div>
              </a>
              
              <!-- 视频信息 -->
              <div class="video-card__info">
                <div class="video-card__info--right">
                  <h3 class="video-card__info--tit">
                    <a 
                      :href="`/video/${item.video.vid}`" 
                      target="_blank" 
                      :title="item.video.title" 
                      @click.prevent="goToVideo(item.video.vid)"
                      v-html="highlightKeyword(item.video.title)"
                    ></a>
                  </h3>
                  <div class="video-card__info--bottom">
                    <!-- 关注状态（如果功能已实现） -->
                    <div 
                      v-if="isFollowing(item.user.uid)" 
                      class="video-card__info--icon-text"
                    >
                      已关注
                    </div>
                    <a 
                      class="video-card__info--owner" 
                      :href="`/space/${item.user.uid}`" 
                      target="_blank"
                      @click.prevent="goToUserSpace(item.user.uid)"
                    >
                      <i class="iconfont icon-uper"></i>
                      <span class="video-card__info--author">{{ item.user.nickname }}</span>
                      <span class="video-card__info--date">
                        · {{ handleDate(item.video.uploadDate) }}
                      </span>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 骨架屏加载状态 -->
          <div v-if="loading" class="skeleton-container">
            <div class="video-card" v-for="index in skeletonCount" :key="`skeleton-${index}`">
              <div class="video-card__skeleton loading_animation">
                <div class="video-card__skeleton--cover"></div>
                <div class="video-card__skeleton--info">
                  <div class="video-card__skeleton--right">
                    <p class="video-card__skeleton--text"></p>
                    <p class="video-card__skeleton--text short"></p>
                    <p class="video-card__skeleton--light"></p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div v-if="!loading && videoList.length === 0" class="empty-state">
            <div class="empty-content">
              <i class="iconfont icon-wushuju"></i>
              <p>未找到相关视频</p>
              <p class="empty-tip">尝试使用其他关键词搜索</p>
            </div>
          </div>
        </div>
        
        <!-- 分页组件 -->
        <div class="search-bottom flex_center" v-if="total > pageSize && !loading">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :pager-count="7"
            :current-page="page"
            @current-change="pageChange"
            hide-on-single-page
          />
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, watch, onMounted, nextTick } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { handleTime, handleNum, handleDate, highlightKeyword } from '@/utils/utils.js'
  
  // 导入 Pinia stores
  import { useSearchStore } from '@/stores/usesearchStore'
  import { useUserStore } from '@/stores/useUserStore'
  import { useVideoStore } from '@/stores/useVideoStore'
import axios from 'axios'
  
  // 导入自定义请求方法
  
  // 定义 props
  const props = defineProps({
    keyword: {
      type: String,
      default: ''
    }
  })
  
  // 路由和 store
  const route = useRoute()
  const router = useRouter()
  const searchStore = useSearchStore()
  const userStore = useUserStore()
  const videoStore = useVideoStore()
  
  // 响应式数据
  const page = ref(1)
  const pageSize = ref(30)
  const videoList = ref([])
  const loading = ref(false)
  const total = ref(0)
  const skeletonCount = ref(12) // 骨架屏数量
  const followingUsers = ref(new Set()) // 已关注的用户ID集合
  
  // 计算属性
  // 从 store 获取视频匹配总数
  const totalFromStore = computed(() => {
    return searchStore.getMatchingCount?.[0] || 0
  })
  
  // 检查是否已关注某个用户
  const isFollowing = (uid) => {
    return followingUsers.value.has(uid)
  }
  
  // 搜索视频
  const searchVideos = async () => {
    if (!props.keyword?.trim()) {
      videoList.value = []
      return
    }
    
    loading.value = true
    videoList.value = []
    
    try {
      // 编码关键词
      const encodedKeyword = encodeURIComponent(props.keyword.trim())
      
      const res = await axios.get('/search/video/only-pass', {
        params: {
          keyword: encodedKeyword,
          page: page.value,
          pageSize: pageSize.value
        }
      })
      
      if (res.data?.data) {
        videoList.value = res.data.data
        total.value = res.data.total || 0
        
        // 如果有总数，更新 store
        if (res.data.total !== undefined) {
          searchStore.updateMatchingCount(0, res.data.total)
        }
        
        // 如果是第一页，获取已关注状态
        if (page.value === 1 && userStore.isLoading && videoList.value.length > 0) {
          getFollowingStatus()
        }
      }
    } catch (error) {
      console.error('搜索视频失败:', error)
      ElMessage.error('搜索失败')
    } finally {
      loading.value = false
    }
  }
  
  // 获取已关注状态
  const getFollowingStatus = async () => {
    if (!userStore.isLoading) return
    
    try {
      // 获取视频作者ID列表
      const userIds = [...new Set(videoList.value.map(item => item.user.uid))]
      if (userIds.length === 0) return
      
      // 调用API获取关注状态
      const res = await axios.get('/user/following-status', {
        params: {
          userIds: userIds.join(',')
        },
        headers: { 
          Authorization: 'Bearer ' + localStorage.getItem('teri_token') 
        }
      })
      
      if (res.data?.data) {
        // 更新已关注用户集合
        followingUsers.value = new Set(res.data.data)
      }
    } catch (error) {
      console.error('获取关注状态失败:', error)
    }
  }
  
  // 关注用户
  const followUser = async (uid) => {
    if (!userStore.isLoading) {
      userStore.isLoading=true
      return
    }
    
    try {
      const res = await axios.post('/user/follow', {
        targetUid: uid
      }, {
        headers: { 
          Authorization: 'Bearer ' + localStorage.getItem('teri_token') 
        }
      })
      
      if (res.data?.success) {
        // 添加到已关注集合
        followingUsers.value.add(uid)
        ElMessage.success('关注成功')
      }
    } catch (error) {
      console.error('关注用户失败:', error)
      ElMessage.error('关注失败')
    }
  }
  
  // 取消关注
  const unfollowUser = async (uid) => {
    try {
      const res = await axios.post('/user/unfollow', {
        targetUid: uid
      }, {
        headers: { 
          Authorization: 'Bearer ' + localStorage.getItem('teri_token') 
        }
      })
      
      if (res.data?.success) {
        // 从已关注集合移除
        followingUsers.value.delete(uid)
        ElMessage.success('已取消关注')
      }
    } catch (error) {
      console.error('取消关注失败:', error)
      ElMessage.error('取消关注失败')
    }
  }
  
  // 跳转到视频页面
  const goToVideo = (vid) => {
    // 在新标签页打开
    window.open(`/video/${vid}`, '_blank')
    
    // 或者在当前页面跳转
    // router.push(`/video/${vid}`)
  }
  
  // 跳转到用户空间
  const goToUserSpace = (uid) => {
    window.open(`/space/${uid}`, '_blank')
  }
  
  // 分页变化
  const pageChange = (newPage) => {
    page.value = newPage
    searchVideos()
    
    // 滚动到顶部
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
  
  // 处理播放时长
  const handleDuration = (time) => handleTime(time)
  
  // 处理数字格式化
  const handleNumFn = (number) => handleNum(number)
  
  // 处理日期格式化
  const handleDateFn = (date) => handleDate(date)
  
  // 高亮关键词
  const highlightKeywordFn = (text) => {
    if (!props.keyword) return text
    return highlightKeyword(props.keyword, text)
  }
  
  // 监听关键词变化
  watch(() => props.keyword, (newKeyword) => {
    if (newKeyword?.trim()) {
      page.value = 1
      searchVideos()
    } else {
      videoList.value = []
    }
  }, { immediate: true })
  
  // 监听登录状态变化
  watch(() => userStore.isLoading, (isLogin) => {
    if (isLogin && videoList.value.length > 0) {
      getFollowingStatus()
    } else if (!isLogin) {
      followingUsers.value.clear()
    }
  })
  
  // 监听页面变化，重新获取关注状态
  watch(() => page.value, () => {
    if (userStore.isLoading) {
      getFollowingStatus()
    }
  })
  
  // 生命周期
  onMounted(() => {
    // 初始化时，如果有关键词就搜索
    if (props.keyword?.trim()) {
      searchVideos()
    }
  })
  </script>

<style scoped>
.search-page {
    padding-bottom: 30px !important;
    margin-top: 30px !important;
    position: relative;
}

.container {
    grid-gap: 20px;
    display: grid;
    position: relative;
    width: 100%;
}

@media (max-width: 1399.9px) {
    .container {
        grid-column: span 4;
        grid-template-columns: repeat(4,1fr);
    }
}

@media (min-width: 1400px) {
    .container {
        grid-column: span 5;
        grid-template-columns: repeat(5,1fr);
    }
}

@media (min-width: 1700px) {
    .container {
        grid-column: span 6;
        grid-template-columns: repeat(6,1fr);
    }
}

@media (min-width: 2200px) {
    .container {
        grid-column: span 7;
        grid-template-columns: repeat(7,1fr);
    }
}

.search-bottom {
    margin: 50px 0 20px;
}
</style>