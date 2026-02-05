<template>
    <div class="videoDetail">
      <HeaderBar :is-fix-header-bar="true"></HeaderBar>
      <div class="video-container">
        <!-- 左侧内容区域 -->
        <div class="left-container" :style="`width: ${playerSize.width}px;`">
          <!-- 标题区域 -->
          <div class="video-info-container">
            <h1 :title="video.title" class="video-title">{{ video.title }}</h1>
            <div class="video-info-detail">
              <div class="video-info-detail-list">
                <!-- 排名信息 -->
                <a href="/popular/rank/all" target="_blank" class="honor item honor-rank" v-if="video.top">
                  <i class="iconfont icon-paihang"></i>
                  <span class="honor-text">全站排行榜最高第1名</span>
                  <i class="iconfont icon-youjiantou"></i>
                </a>
                <span class="view item">
                  <i class="iconfont icon-bofangshu"></i>
                  &nbsp;{{ handleNum(view) }}&nbsp;
                </span>
                <span class="danmu item">
                  <i class="iconfont icon-danmushu"></i>
                  &nbsp;{{ handleNum(danmuList.length) }}&nbsp;
                </span>
                <span class="date item">
                  {{  formatDate(video.uploadDate) }}
                </span>
                <span class="copyright item" v-if="video.type === 1 && video.auth === 1">
                  <i class="iconfont icon-jinzhi"></i>
                  未经作者授权，禁止转载
                </span>
              </div>
            </div>
          </div>
          
          <!-- 播放器组件 -->
          <PlayerWrap 
            :video-url="video.videoUrl" 
            :title="video.title" 
            :duration="video.duration" 
            :user="user"
            :population="population" 
            v-model:jump-time-point="jumpTimePoint" 
            v-model:autonext="autonext"
            @resize="updatePlayerSize" 
            @send-dm="sendDanmu" 
            @next="next"
          />
          
          <!-- 三连工具栏 -->
          <div class="video-toolbar-container">
            <div class="video-toolbar-left">
              <div class="toolbar-left-item-wrap">
                <div 
                  class="video-toolbar-left-item"
                  :class="{ 'on': attitudeToVideo.love }"
                  @click="loveOrNot(true, !attitudeToVideo.love)"
                >
                  <i class="iconfont icon-dianzan"></i>
                  <span class="video-toolbar-item-text">{{ handleNum(good) }}</span>
                  <div class="dianzan-gif" :class="isGifShow ? 'gif-show' : 'gif-hide'">
                    <img src="@/assets/img/dianzan.gif" alt="" v-if="gifDisplay">
                  </div>
                </div>
              </div>
              <!-- 其他按钮... -->
            </div>
          </div>
          
          <!-- 简介、标签、评论区 -->
          <div class="left-container-under-player">
            <!-- 简介 -->
            <div class="video-desc-container" v-if="video.descr">
              <div class="basic-desc-info" :style="showAllDesc ? 'height: auto;' : 'height: 84px;'">
                <span class="desc-info-text" v-html="handleLinkify(video.descr)"></span>
              </div>
              <div class="toggle-btn" v-if="descTooLong">
                <span class="toggle-btn-text" @click="showAllDesc = !showAllDesc">
                  {{ showAllDesc ? '收起' : '展开更多' }}
                </span>
              </div>
            </div>
            
            <!-- 标签 -->
            <div class="video-tag-container">
              <div class="tag-container" v-if="category.mcName">
                <a :href="`/v/${category.mcId}`" target="_blank" class="tag-link">{{ category.mcName }}</a>
              </div>
              <div class="tag-container" v-if="category.scName">
                <a :href="`/v/${category.mcId}/${category.scId}`" target="_blank" class="tag-link">
                  {{ category.scName }}
                </a>
              </div>
              <div class="tag-container" v-for="(item, index) in tags" :key="index">
                <a :href="`/search/video?keyword=${item}`" target="_blank" class="tag-link">{{ item }}</a>
              </div>
            </div>
            
            <!-- 评论组件 -->
            <CommentVue :uid="user.uid" :count="comment" />
          </div>
        </div>
        
        <!-- 右侧边栏 -->
        <div class="right-container">
          <div class="right-container-inner">
            <!-- UP主信息 -->
            <div class="up-panel-container">
              <div class="up-info-container">
                <div class="up-info--left">
                  <div class="up-avatar-wrap">
                    <VPopover pop-style="z-index: 2000; cursor: default; padding-top: 30px;">
                      <template #reference>
                        <a :href="`/space/${user.uid}`" target="_blank" class="up-avatar">
                          <VAvatar :img="user.avatar_url" :size="48" :auth="user.auth" />
                        </a>
                      </template>
                      <template #content>
                        <UserCard :user="user" />
                      </template>
                    </VPopover>
                  </div>
                </div>
                <div class="up-info--right">
                  <div class="up-info__detail">
                    <div class="up-detail-top">
                      <a :href="`/space/${user.uid}`" target="_blank" class="up-name" :class="user.vip !== 0 ? 'vip-name' : ''">
                        {{ user.nickname }}
                      </a>
                      <a class="send-msg" @click="createChat">
                        <i class="iconfont icon-xinfeng1"></i>
                        发消息
                      </a>
                    </div>
                    <div class="up-description" :title="user.description">{{ user.description }}</div>
                  </div>
                  <div class="up-info__btn-panel">
                    <!-- 关注按钮 -->
                    <div class="default-btn follow-btn not-follow" v-if="!isFollowing" @click="followUser">
                      <i class="iconfont icon-jia"></i>
                      关注 {{ handleNum(user.fansCount) }}
                    </div>
                    <VPopover v-else pop-style="padding-top: 10px;">
                      <template #reference>
                        <div class="default-btn follow-btn following">
                          <i class="iconfont icon-caidan"></i>
                          已关注 {{ handleNum(user.fansCount) }}
                        </div>
                      </template>
                      <template #content>
                        <div class="following-dropdown">
                          <div class="dropdown-item" @click="unfollowUser">
                            <span>取消关注</span>
                          </div>
                        </div>
                      </template>
                    </VPopover>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 弹幕组件 -->
            <DanmuBox 
              :box-height="playerSize.height" 
              :author-id="user.uid"
              @jump="(time) => jumpTimePoint = time"
            />
            
            <!-- 相关视频列表 -->
            <div class="recommend-list">
              <div class="next-play">
                <p class="rec-title">
                  接下来播放
                  <span class="next-button" @click="autonext = !autonext">
                    <span class="txt">自动连播</span>
                    <span class="switch-button" :class="{ 'on': autonext }"></span>
                  </span>
                </p>
                
                <!-- 下一个视频卡片 -->
                <div class="video-page-card-small" v-if="recommendVideos[0]">
                  <div class="card-box">
                    <div class="pic-box">
                      <div class="pic" @click="changeVideo(recommendVideos[0].video.vid)">
                        <img :src="recommendVideos[0].video.coverUrl" alt="">
                        <span class="duration">
                          {{ handleDuration(recommendVideos[0].video.duration) }}
                        </span>
                      </div>
                    </div>
                    <div class="info">
                      <p class="title" @click="changeVideo(recommendVideos[0].video.vid)">
                        {{ recommendVideos[0].video.title }}
                      </p>
                      <a :href="`/space/${recommendVideos[0].user.uid}`" target="_blank" class="upname">
                        <!-- UP主图标 -->
                        <span class="name">{{ recommendVideos[0].user.nickname }}</span>
                      </a>
                      <div class="playinfo">
                        <!-- 播放统计 -->
                        {{ handleNum(recommendVideos[0].stats.play) }} 播放
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 分隔线 -->
                <div class="split-line"></div>
              </div>
              
              <!-- 其他推荐视频 -->
              <div class="rec-list" v-if="recommendVideos.length > 1">
                <div 
                  class="video-page-card-small" 
                  v-for="(item, index) in recommendVideos.slice(1)" 
                  :key="index"
                >
                  <!-- 视频卡片内容 -->
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 收藏对话框 -->
      <!-- <el-dialog v-model="collectVisible" :close-on-click-modal="false" destroy-on-close align-center>
        <AddToFavorite 
          :last-selected="collectedFids" 
          :vid="video.vid || 0" 
          @collected="updateCollect"
        />
      </el-dialog> -->
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { handleTime, handleNum, handleDate, linkify,formatDate } from '../../utils/utils.js'
  
  // Pinia stores
  import { useVideoStore } from '../../stores/useVideoStore'
  import { useUserStore } from '../../stores/useUserStore'
  
  // 组件
  import CommentVue from '../../components/comment/CommentVue.vue'
  import HeaderBar from '../../components/headerBar/HeaderBar.vue'
  //import PlayerWrap from '@/components/player/PlayerWrapper.vue'
  import VPopover from '../../components/popover/VPopover.vue'
  import VAvatar from '../../components/avatar/VAvatar.vue'
  import UserCard from '../../components/UserCard/UserCard.vue'
  import axios from 'axios'
  import { get } from '@/network/request'
  //import DanmuBox from '@/components/danmu/DanmuBox.vue'
  //import AddToFavorite from '@/components/favorite/AddToFavorite.vue'
  
  // 路由和store
  const route = useRoute()
  const router = useRouter()
  const videoStore = useVideoStore()
  const userStore = useUserStore()
  
  // 响应式数据
  const playerSize = reactive({ width: 704, height: 442 })
  const video:any = ref({})
  const view:any = ref(0)
  const danmu:any = ref(0)
  const good:any = ref(0)
  const coin:any = ref(0)
  const collect:any = ref(0)
  const share:any = ref(0)
  const comment:any = ref(0)
  const population:any = ref(0)
  const user:any = ref({})
  const category:any = ref({})
  const tags:any = ref([])
  const showAllDesc:any = ref(true)
  const descTooLong:any = ref(false)
  const jumpTimePoint:any = ref(-1)
  const autonext:any = ref(false)
  const recommendVideos:any = ref([])
  const vids:any= ref([])
  const isGifShow:any = ref(false)
  const gifDisplay:any = ref(false)
  const collectVisible:any = ref(false)
  const collectedFids:any = ref(new Set())
  const isMounted:any = ref(false)
  const loveLoading:any = ref(false)
  const isFollowing:any = ref(false)
  const socket:any = ref(null)
  
  // 计算属性
  const danmuList = computed(() => videoStore.getDanmuList)
  const attitudeToVideo = computed(() => videoStore.getAttitudeToVideo)
  
  // 工具函数
  const handleDuration = (time) => handleTime(time)
  const handleNumFn = (number) => handleNum(number)
  const handleDateFn = (date) => handleDate(date)
  const handleLinkify = (text) => linkify(text)
  
  // 获取视频详情
  const getVideoDetail = async () => {
    try {
      const res = await axios.get('/api/video/getone/'+ route.params.vid)
      if (res.data.code === 404) {
        router.push('/notfound')
        return false
      }
      if (res.data.data) {  
        const data = res.data.data
        video.value = data.video
        user.value = data.user
        category.value = data.category
        tags.value = data.video.tags?.split('\r\n').filter(tag => tag.trim() !== '') || []
        view.value = data.stats.play
        danmu.value = data.stats.danmu
        good.value = data.stats.good
        coin.value = data.stats.coin
        collect.value = data.stats.collect
        share.value = data.stats.share
        comment.value = data.stats.comment
        
        // 检查简介是否过长
        checkDescTooLong()
        
        // 如果已登录，获取收藏信息
        // if (userStore.getIsLogin) {
        //   getCollectedFids()
        // }
        
        return true
      }
    } catch (error) {
      console.error('获取视频详情失败:', error)
      ElMessage.error('获取视频详情失败')
      return false
    }
  }
  
  // 获取推荐视频
  const getRecommendVideos = async () => {
    try {
      recommendVideos.value = []
      vids.value = [Number(route.params.vid)]
      
      // 第一次请求
      const res1 = await axios.get('/video/cumulative/visitor', {
        params: { vids: vids.value.join(',') }
      })
      
      if (res1.data.data) {
        recommendVideos.value.push(...res1.data.data.videos)
        vids.value.push(...res1.data.data.vids)
        
        // 第二次请求
        const res2 = await axios.get('/video/cumulative/visitor', {
          params: { vids: vids.value.join(',') }
        })
        
        if (res2.data.data) {
          recommendVideos.value.push(...res2.data.data.videos)
          vids.value.push(...res2.data.data.vids)
        }
      }
    } catch (error) {
      console.error('获取推荐视频失败:', error)
    }
  }
  
  //获取弹幕列表
  const getDanmuList = async () => {
    try {
      const res = await axios.get(`/danmu-list/${route.params.vid}`)
      if (res.data.data) {
        videoStore.updateDanmuList(res.data.data)
      }
    } catch (error) {
      console.error('获取弹幕列表失败:', error)
    }
  }
  
  // 初始化WebSocket
  const initWebsocket = () => {
    const wsBaseUrl = import.meta.env.VITE_APP_WS_DANMU_URL
    const socketUrl = `${wsBaseUrl}/ws/danmu/${route.params.vid}`
    
    if (socket.value) {
      socket.value.close()
      socket.value = null
    }
    
    socket.value = new WebSocket(socketUrl)
    
    socket.value.addEventListener('close', handleWsClose)
    socket.value.addEventListener('message', handleWsMessage)
    socket.value.addEventListener('error', handleWsError)
  }
  
  // 关闭WebSocket
  const closeWebSocket = () => {
    if (socket.value) {
      socket.value.close()
      socket.value = null
    }
  }
  
  // 点赞/取消点赞
  const loveOrNot = async (isLove, isSet) => {
    if (loveLoading.value) return
    
    // 检查登录状态
    if (!userStore.isLoading) {
      userStore.isLoading=true
      return
    }
    
    if (!video.value.vid) {
      ElMessage.error('视频不存在')
      return
    }
    
    loveLoading.value = true
    
    try {
      const formData = new FormData()
      formData.append('vid', video.value.vid)
      formData.append('isLove', isLove)
      formData.append('isSet', isSet)
      
      const res = await axios.post('/video/love-or-not', formData, {
        headers: { Authorization: 'Bearer ' + localStorage.getItem('teri_token') }
      })
      
      if (res.data.data) {
        const data = res.data.data
        const atv = {
          love: data.love === 1,
          unlove: data.unlove === 1,
          coin: data.coin,
          collect: data.collect === 1
        }
        
        videoStore.updateAttitudeToVideo(atv)
        
        // 更新点赞数
        if (isLove && isSet) {
          good.value++
          showGifAnimation()
        } else if (isLove || (!isLove && isSet && attitudeToVideo.value.love)) {
          good.value = Math.max(0, good.value - 1)
        }
      }
    } catch (error) {
      console.error('点赞操作失败:', error)
      ElMessage.error('操作失败')
    } finally {
      loveLoading.value = false
    }
  }
  
  // 获取收藏信息
  const getCollectedFids = async () => {
    try {
      const res = await axios.get('/video/collected-fids', {
        params: { vid: Number(video.value.vid) },
        headers: { Authorization: 'Bearer ' + localStorage.getItem('teri_token') }
      })
      
      if (res.data?.data) {
        collectedFids.value = new Set(res.data.data)
      }
    } catch (error) {
      console.error('获取收藏信息失败:', error)
    }
  }
  
  // 检查简介是否过长
  const checkDescTooLong = () => {
    nextTick(() => {
      const desc = document.querySelector('.basic-desc-info')
      if (desc && desc.clientHeight > 84) {
        descTooLong.value = true
        showAllDesc.value = false
      }
    })
  }
  
  // 创建聊天
  const createChat = () => {
    if (!userStore.isLoading) {
      userStore.isLoading=true
      return
    }
    window.open(`/message/whisper/${user.value.uid}`, '_blank')
  }
  
  // 更新播放器尺寸
  const updatePlayerSize = (size) => {
    playerSize.width = size.width
    playerSize.height = size.height
  }
  
  // WebSocket事件处理
  const handleWsClose = () => {
    setTimeout(() => {
      if (!socket.value) {
        initWebsocket()
      }
    }, 2000)
  }
  
  const handleWsMessage = (e) => {
    if (e.data === '登录已过期') {
      ElMessage.error(e.data)
    } else if (e.data.startsWith('当前观看人数')) {
      const numberPart = e.data.substring(6).trim()
      population.value = parseInt(numberPart, 10)
    } else {
      try {
        const dm = JSON.parse(e.data)
        videoStore.addDanmu(dm)
      } catch (error) {
        console.error('解析弹幕失败:', error)
      }
    }
  }
  
  const handleWsError = (e) => {
    console.error('WebSocket错误:', e)
  }
  
  // 发送弹幕
  const sendDanmu = (dm) => {
    if (!userStore.isLoading) {
      userStore.isLoading=true
      return
    }
    
    const dmJson = JSON.stringify({
      token: 'Bearer ' + localStorage.getItem('token'),
      data: dm
    })
    
    if (socket.value && socket.value.readyState === WebSocket.OPEN) {
      socket.value.send(dmJson)
    }
  }
  
  // 切换视频
  const changeVideo = async (vid) => {
    await router.push(`/video/${vid}`)
    initWebsocket()
    
    if (await getVideoDetail()) {
      await getDanmuList()
      await getRecommendVideos()
    }
  }
  
  // 自动连播
  const next = () => {
    if (recommendVideos.value[0]) {
      changeVideo(recommendVideos.value[0].video.vid)
    }
  }
  
  // 点赞动画
  const showGifAnimation = () => {
    gifDisplay.value = true
    isGifShow.value = true
    
    setTimeout(() => {
      isGifShow.value = false
      setTimeout(() => {
        gifDisplay.value = false
      }, 300)
    }, 3000)
  }
  
  // 打开收藏对话框
  const openCollectDialog = () => {
    if (!userStore.isLoading) {
      userStore.isLoading=true
      return
    }
    
    if (!video.value.vid) {
      ElMessage.error('视频不存在')
      return
    }
    
    collectVisible.value = true
  }
  
  // 更新收藏
  const updateCollect = (info) => {
    collectedFids.value = info.fids
    collect.value += info.num
    collectVisible.value = false
  }
  
  // 关注用户
  const followUser = async () => {
    if (!userStore.isLoading) {
      userStore.isLoading=true
      return
    }
    
    try {
      // 调用关注API
      // await $post('/user/follow', { uid: user.value.uid })
      isFollowing.value = true
      ElMessage.success('关注成功')
    } catch (error) {
      console.error('关注失败:', error)
      ElMessage.error('关注失败')
    }
  }
  
  // 取消关注
  const unfollowUser = async () => {
    try {
      // 调用取消关注API
      // await $post('/user/unfollow', { uid: user.value.uid })
      isFollowing.value = false
      ElMessage.success('已取消关注')
    } catch (error) {
      console.error('取消关注失败:', error)
      ElMessage.error('取消关注失败')
    }
  }
  
  // 滚动处理
  const handleScroll = () => {
    const windowHeight = window.innerHeight
    const leftPart = document.querySelector('.left-container')
    const rightPart = document.querySelector('.right-container-inner')
    
    if (leftPart) {
      leftPart.style.top = leftPart.clientHeight <= windowHeight - 64 ? '64px' : `-${leftPart.clientHeight - windowHeight}px`
    }
    
    if (rightPart) {
      rightPart.style.top = rightPart.clientHeight <= windowHeight - 64 ? '64px' : `-${rightPart.clientHeight - windowHeight}px`
    }
  }
  
  // 暂未开放提示
  const noPage = () => {
    ElMessage.warning('该功能暂未开放')
  }
  
  // 生命周期
  onMounted(async () => {
    // 读取自动连播设置
    const playerSetting = localStorage.getItem('playerSetting')
    if (playerSetting) {
      try {
        const setting = JSON.parse(playerSetting)
        autonext.value = setting.autonext
      } catch (error) {
        console.error('解析播放器设置失败:', error)
      }
    }
    
    // 初始化数据
    initWebsocket()
    if (await getVideoDetail()) {
      await getDanmuList()
      await getRecommendVideos()
    }
    
    // 添加事件监听
    window.addEventListener('scroll', handleScroll)
    window.addEventListener('beforeunload', closeWebSocket)
    
    // 标记已挂载
    setTimeout(() => {
      isMounted.value = true
    }, 3000)
  })
  
  onUnmounted(() => {
    //closeWebSocket()
    window.removeEventListener('scroll', handleScroll)
    window.removeEventListener('beforeunload', closeWebSocket)
  })
  
  // 监听路由变化
  watch(() => route.path, () => {
    collectVisible.value = false
  })
  
  // 监听登录状态变化
  watch(() => userStore.isLoading, (curr) => {
    if (isMounted.value && curr) {
      getCollectedFids()
    } else if (!curr) {
      collectedFids.value = new Set()
    }
  })
  </script>

<style scoped>
.video-container {
    width: auto;
    padding: 64px 10px 0px;
    max-width: 2540px;
    min-width: 1080px;
    margin: 0 auto;
    display: flex;
    justify-content: center;
    box-sizing: content-box;
    position: relative;
}

.left-container {
    position: sticky;
    height: fit-content;
}

.video-info-container {
    height: 104px;
    box-sizing: border-box;
    padding-top: 22px;
}

.video-title {
    font-size: 20px;
    font-weight: 500;
    -webkit-font-smoothing: antialiased;
    color: var(--text1);
    line-height: 28px;
    margin-bottom: 6px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.video-info-detail {
    font-size: 13px;
    color: var(--text3);
    display: flex;
    align-items: center;
    height: 24px;
    line-height: 18px;
    position: relative;
    overflow: hidden;
}

.video-info-detail-list {
    display: flex;
    align-items: center;
    overflow: hidden;
    box-sizing: border-box;
}

.video-info-detail-list .item {
    flex-shrink: 0;
    margin-right: 12px;
    overflow: hidden;
}

.video-info-detail-list .item:last-child {
    margin-right: 0;
}

.view,
.danmu,
.copyright {
    display: inline-flex;
    align-items: center;
}

.icon-bofangshu .icon-danmushu {
    font-size: 18px;
}

.honor {
    display: inline-flex;
    align-items: center;
    font-size: 13px;
    height: 24px;
    border-radius: 2px;
    padding: 0px 6px;
}

.honor.honor-rank {
    color: var(--brand_pink);
    background-color: rgba(255, 102, 153, 0.1);
}

.honor .icon-paihang {
    font-size: 14px;
    margin: 0 5px 0 3px;
}

.honor .icon-youjiantou {
    font-size: 14px;
}

.date {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    line-height: 24px;
    font-size: 13px;
    height: 100%;
    display: inline-block;
    vertical-align: middle;
}

.icon-jinzhi {
    font-size: 12px;
    margin-right: 4px;
    color: var(--stress_red);
}

.video-toolbar-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 16px;
    padding-bottom: 12px;
    line-height: 28px;
    border-bottom: 1px solid var(--line_regular);
}

.video-toolbar-left {
    position: relative;
    display: flex;
    align-items: center;
    -webkit-user-select: none;
    user-select: none;
}

.toolbar-left-item-wrap {
    position: relative;
    margin-right: 8px;
}

.video-toolbar-left-item {
    position: relative;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-align: center;
    align-items: center;
    width: 92px;
    white-space: nowrap;
    transition: all .3s;
    font-size: 13px;
    color: var(--text2);
    font-weight: 500;
    cursor: pointer;
}

.video-toolbar-left-item.on,
.video-toolbar-left-item:hover {
    color: var(--brand_pink);
}

.video-toolbar-left-item .iconfont {
    margin-right: 8px;
    font-size: 26px;
}

.video-toolbar-left-item .icon-diancai {
    transform: translateY(2px);
}

.video-toolbar-item-text {
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-word;
    white-space: nowrap;
}

.dianzan-gif {
    position: absolute;
    top: -50px;
    left: -10px;
    height: 40px;

}

.dianzan-gif img {
    height: 100%;
}

.gif-hide {
    animation: disappear 0.2s ease-out forwards;
    transform-origin: bottom;
}

.gif-show {
    animation: appear 0.2s ease-out forwards;
    transform-origin: bottom;
}

@keyframes appear {
    0% {
        opacity: 0;
        transform: translateY(5px) scale(0);
    }

    100% {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

@keyframes disappear {
    0% {
        opacity: 1;
        transform: translateY(0) scale(1);
    }

    100% {
        opacity: 0;
        transform: translateY(5px) scale(0);
    }
}

.video-toolbar-right {
    display: flex;
    align-items: center;
    -webkit-user-select: none;
    user-select: none;
}

.video-toolbar-right-item {
    display: -ms-inline-flexbox;
    display: inline-flex;
    -ms-flex-align: center;
    align-items: center;
    font-size: 13px;
    color: var(--text2);
    transition: all .3s;
    cursor: pointer;
}

.video-toolbar-right-item:hover {
    color: var(--brand_pink);
}

.video-tool-more {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
}

.icon-gengduo {
    font-size: 18px;
}

.video-tool-more-dropdown {
    padding: 12px 0px;
    cursor: auto;
}

.dropdown-item {
    position: relative;
    display: flex;
    align-items: center;
    height: 40px;
    width: 120px;
    padding: 0 20px;
    color: var(--text1);
    cursor: pointer;
}

.dropdown-item:hover {
    background-color: var(--Ga1);
}

.icon-jubao1 {
    margin-right: 10px;
}

.video-desc-container {
    margin: 16px 0;
}

.basic-desc-info {
    white-space: pre-line;
    letter-spacing: 0;
    color: var(--text1);
    font-size: 15px;
    line-height: 24px;
    overflow: hidden;
}

.toggle-btn {
    margin-top: 10px;
    font-size: 13px;
    line-height: 18px;
}

.toggle-btn-text {
    cursor: pointer;
    color: var(--text2);
}

.toggle-btn-text:hover {
    color: var(--brand_pink);
}

.video-tag-container {
    padding-bottom: 6px;
    margin: 16px 0 20px 0;
    border-bottom: 1px solid var(--line_regular);
    display: flex;
    flex-wrap: wrap;
}

.tag-container {
    margin: 0px 12px 8px 0;
}

.tag-link {
    color: var(--text2);
    background: var(--graph_bg_regular);
    height: 28px;
    line-height: 28px;
    border-radius: 14px;
    font-size: 13px;
    padding: 0 12px;
    box-sizing: border-box;
    transition: all .3s;
    display: -ms-inline-flexbox;
    display: inline-flex;
    -ms-flex-align: center;
    align-items: center;
    cursor: pointer;
}

.right-container {
    width: 350px;
    flex: none;
    margin-left: 30px;
    position: relative;
    pointer-events: none;
}

.right-container-inner {
    padding-bottom: 250px;
    position: sticky;
}

.right-container-inner * {
    pointer-events: all;
}

.up-info-container {
    box-sizing: border-box;
    height: 104px;
    display: flex;
    align-items: center;
}

.up-avatar-wrap {
    width: 48px;
    height: 48px;
    flex-shrink: 0;
    display: flex;
    justify-content: center;
    align-items: center;
}

.up-avatar {
    display: block;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: var(--graph_weak);
}

.up-info--right {
    margin-left: 12px;
    flex: 1;
}

.up-info__detail {
    margin-bottom: 5px;
}

.up-detail-top {
    display: flex;
    align-items: center;
}

.up-name {
    font-size: 15px;
    color: var(--text1);
    font-weight: 500;
    position: relative;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    margin-right: 12px;
    max-width: calc(100% - 12px - 56px);
}

.send-msg {
    color: var(--text2);
    font-size: 13px;
    transition: color 0.3s;
    flex-shrink: 0;
    cursor: pointer;
}

.send-msg:hover {
    color: var(--brand_pink);
}

.up-description {
    margin-top: 2px;
    font-size: 13px;
    line-height: 16px;
    height: 16px;
    color: var(--text3);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.up-info__btn-panel {
    clear: both;
    display: flex;
    margin-top: 5px;
    white-space: nowrap;
}

.up-info__btn-panel .default-btn {
    box-sizing: border-box;
    padding: 0;
    line-height: 30px;
    height: 30px;
    border-radius: 6px;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    background: var(--graph_weak);
    position: relative;
    transition: 0.3s all;
}

.follow-btn {
    width: 200px;
}

.follow-btn.following {
    color: var(--text3);
    background-color: var(--graph_bg_thick);
}

.follow-btn.following:hover {
    background-color: var(--graph_bg_regular);
}

.follow-btn.not-follow {
    background: var(--brand_pink);
    color: var(--text_white);
}

.follow-btn.not-follow:hover {
    background: var(--Pi4);
}

.follow-btn .iconfont {
    font-size: 14px;
    margin-right: 2px;
}

.following-dropdown {
    padding: 8px 0px;
}

.following-dropdown .dropdown-item:hover {
    color: var(--brand_pink);
}

.recommend-list {
    margin-top: 18px;
}

.rec-title {
    font-size: 15px;
    -webkit-font-smoothing: antialiased;
    color: var(--text1);
    display: flex;
    justify-content: space-between;
    margin-bottom: 12px;
    line-height: 20px;
}

.next-button {
    color: var(--text3);
    font-size: 13px;
    line-height: 16px;
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}

.next-button .txt {
    margin-right: 4px;
    vertical-align: middle;
}

.next-button .switch-button {
    margin: 0;
    display: inline-block;
    position: relative;
    width: 30px;
    height: 20px;
    outline: none;
    border-radius: 10px;
    box-sizing: border-box;
    cursor: pointer;
    transition: border-color .2s, background-color .2s;
    vertical-align: middle;
    background: var(--text3);
    border: 1px solid var(--text3);
}

.next-button .switch-button.on {
    background: var(--brand_pink);
    border: 1px solid var(--brand_pink);
}

.next-button .switch-button:after {
    content: "";
    position: absolute;
    top: 1px;
    left: 1px;
    border-radius: 100%;
    width: 16px;
    height: 16px;
    background-color: #fff;
    transition: all .2s;
}

.next-button .switch-button.on:after {
    left: 11px;
}

.split-line {
    width: 100%;
    height: 1px;
    background: var(--line_regular);
}

.rec-list {
    margin-top: 18px;
}

.video-page-card-small {
    margin-bottom: 12px;
}

.video-page-card-small a {
    color: #222;
    background-color: transparent;
    text-decoration: none;
    outline: none;
    cursor: pointer;
    transition: color .3s;
    -webkit-text-decoration-skip: objects;
}

.video-page-card-small .card-box {
    display: flex;
}

.video-page-card-small .card-box .pic-box {
    position: relative;
    width: 141px;
    height: 80px;
    border-radius: 6px;
    background: var(--graph_weak);
    flex: 0 0 auto;
}

.video-page-card-small .card-box .pic-box .pic {
    position: relative;
    overflow: hidden;
    border-radius: 6px;
    width: 100%;
    height: 100%;
    cursor: pointer;
}

.video-page-card-small .card-box .pic-box .pic img {
    display: block;
    width: 100%;
    height: 100%;
    object-fit: cover;
    image-rendering: crisp-edges;
}

.video-page-card-small .card-box .pic-box .pic .duration {
    position: absolute;
    bottom: 6px;
    right: 6px;
    color: #fff;
    height: 20px;
    line-height: 20px;
    transition: opacity 0.3s;
    z-index: 5;
    font-size: 13px;
    background-color: rgba(0, 0, 0, 0.4);
    border-radius: 2px;
    padding: 0 4px;
}

.video-page-card-small .card-box .info {
    margin-left: 10px;
    flex: 1;
    font-size: 13px;
    line-height: 15px;
}

.video-page-card-small .card-box .info .title {
    cursor: pointer;
    color: var(--text1);
    display: block;
    font-size: 15px;
    line-height: 19px;
    transition: color 0.3s;
    display: -webkit-box;
    overflow: hidden;
    -webkit-box-orient: vertical;
    text-overflow: -o-ellipsis-lastline;
    text-overflow: ellipsis;
    word-break: break-word;
    -webkit-line-clamp: 2;
    -webkit-font-smoothing: antialiased;
}

.video-page-card-small .card-box .info .title:hover {
    color: var(--brand_pink);
}

.video-page-card-small .card-box .info .upname {
    cursor: pointer;
    margin: 2px 0;
    height: 20px;
    color: var(--text3);
    transition: color 0.3s;
    display: flex;
    align-items: center;
}

.video-page-card-small .card-box .info .upname:hover {
    color: var(--brand_pink);
}

.video-page-card-small .card-box .info .upname svg {
    margin-right: 4px;
    fill: var(--text3);
    transition: fill 0.3s;
}

.video-page-card-small .card-box .info .upname:hover svg {
    fill: var(--brand_pink);
}

.video-page-card-small .card-box .info .upname .name {
    display: -webkit-box;
    overflow: hidden;
    -webkit-box-orient: vertical;
    text-overflow: -o-ellipsis-lastline;
    text-overflow: ellipsis;
    word-break: break-word;
    -webkit-line-clamp: 1;
}

.video-page-card-small .card-box .info .playinfo {
    color: var(--text3);
    fill: var(--text3);
    display: inline-flex;
    align-items: center;
}

.video-page-card-small .card-box .info .playinfo svg {
    margin-right: 4px;
}

.playinfo-dm {
    margin-left: 8px;
}

@media (min-width: 1681px) {
    .video-info-container {
        height: 108px;
    }

    .up-info-container {
        height: 108px;
    }

    .video-info-container .video-title {
        font-size: 22px;
        line-height: 34px;
    }

    .right-container {
        width: 411px;
    }

    .up-name {
        font-size: 16px;
        max-width: calc(100% - 12px - 60px);
    }

    .send-msg {
        font-size: 14px;
    }

    .up-description {
        font-size: 14px;
    }

    .follow-btn {
        width: 230px;
    }
}
</style>