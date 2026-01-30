<template>
    <div class="search-user">
      <div class="search-page i_wrapper">
        <!-- 用户列表 -->
        <div class="container">
          <div class="user-card" v-for="(item, index) in userList" :key="item.uid || index">
            <div class="user-info-card flex_start">
              <!-- 用户头像 -->
              <a :href="`/space/${item.uid}`" target="_blank" class="user-info-left">
                <VAvatar :img="item.avatar_url" :size="86" :auth="item.auth" />
              </a>
              
              <!-- 用户信息 -->
              <div class="user-content">
                <h2 class="card-title">
                  <a :href="`/space/${item.uid}`" target="_blank" class="user-name" :title="item.nickname">
                    {{ item.nickname }}
                  </a>
                  <VLevel class="level" :level="handleLevel(item.exp)" :size="16" />
                </h2>
                
                <p class="card-center">
                  {{ handleNum(item.fansCount) }}粉丝 · {{ handleNum(item.videoCount) }}个视频
                  <span style="margin-left: 3px;">
                    {{ item.auth > 0 ? item.authMsg : item.description }}
                  </span>
                </p>
                
                <!-- 关注按钮 -->
                <div class="card-buttom">
                  <button 
                    v-if="!isFollowing(item.uid)" 
                    class="not-follow"
                    @click="followUser(item.uid)"
                  >
                    + 关注
                  </button>
                  <button 
                    v-else 
                    class="following"
                    @click="unfollowUser(item.uid)"
                  >
                    已关注
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div v-if="!loading && userList.length === 0" class="empty-state">
            <div class="empty-content">
              <i class="iconfont icon-wushuju"></i>
              <p>未找到相关用户</p>
            </div>
          </div>
          
          <!-- 加载状态 -->
          <div v-if="loading" class="loading-state">
            <div class="loading-content">
              <i class="iconfont icon-jiazai"></i>
              <p>正在搜索中...</p>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="search-bottom flex_center" v-if="total > pageSize && !loading">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :pager-count="7"
            :current-page="page"
            @current-change="pageChange"
          />
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, watch, onMounted } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { handleNum, handleLevel } from '@/utils/utils.js'
  
  // 导入组件
  import VAvatar from '@/components/avatar/VAvatar.vue'
  import VLevel from '@/components/UserCard/VLevel.vue'
  
  // 导入 Pinia stores
  import { useSearchStore } from '../../../stores/usesearchStore'
  import { useUserStore } from '../../../stores/useUserStore'
  import axios from 'axios'

  
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
  
  // 响应式数据
  const page = ref(1)
  const pageSize = ref(30)
  const userList = ref([])
  const loading = ref(false)
  const total = ref(0)
  const followingUsers = ref(new Set()) // 已关注的用户ID集合
  
  // 计算属性
  const totalFromStore = computed(() => {
    return searchStore.getMatchingCount[1] || 0
  })
  
  // 检查是否已关注
  const isFollowing = (uid) => {
    return followingUsers.value.has(uid)
  }
  
  // 搜索用户
  const searchUsers = async () => {
    if (!props.keyword?.trim()) {
      userList.value = []
      return
    }
    
    loading.value = true
    userList.value = []
    
    try {
      // 编码关键词
      const encodedKeyword = encodeURIComponent(props.keyword.trim())
      
      const res = await axios.get('/search/user', {
        params: {
          keyword: encodedKeyword,
          page: page.value,
          pageSize: pageSize.value
        }
      })
      
      if (res.data) {
        userList.value = res.data.data || []
        total.value = res.data.total || 0
        
        // 如果有总数，更新store
        if (res.data.total !== undefined) {
          searchStore.updateMatchingCount(1, res.data.total)
        }
        
        // 如果是第一页，获取已关注状态
        if (page.value === 1 && userStore.isLoading) {
          getFollowingStatus()
        }
      }
    } catch (error) {
      console.error('搜索用户失败:', error)
      ElMessage.error('搜索失败')
    } finally {
      loading.value = false
    }
  }
  
  // 获取已关注状态
  const getFollowingStatus = async () => {
    if (!userStore.isLoading) return
    
    try {
      // 获取用户ID列表
      const userIds = userList.value.map(user => user.uid)
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
    if (!userStore.isLogin) {
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
        
        // 更新用户列表中的粉丝数
        const user = userList.value.find(u => u.uid === uid)
        if (user) {
          user.fansCount += 1
        }
        
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
        
        // 更新用户列表中的粉丝数
        const user = userList.value.find(u => u.uid === uid)
        if (user && user.fansCount > 0) {
          user.fansCount -= 1
        }
        
        ElMessage.success('已取消关注')
      }
    } catch (error) {
      console.error('取消关注失败:', error)
      ElMessage.error('取消关注失败')
    }
  }
  
  // 分页变化
  const pageChange = (newPage) => {
    page.value = newPage
    searchUsers()
    
    // 滚动到顶部
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
  
  // 监听关键词变化
  watch(() => props.keyword, (newKeyword) => {
    if (newKeyword?.trim()) {
      page.value = 1
      searchUsers()
    } else {
      userList.value = []
    }
  })
  
  // 监听登录状态变化
  watch(() => userStore.isLogin, (isLogin) => {
    if (isLogin && userList.value.length > 0) {
      getFollowingStatus()
    } else if (!isLogin) {
      followingUsers.value.clear()
    }
  })
  
  // 生命周期
  onMounted(() => {
    if (props.keyword?.trim()) {
      searchUsers()
    }
  })
  
  // 工具函数
  const handleNumFn = (number) => handleNum(number)
  const handleLevelFn = (exp) => handleLevel(exp)
  </script>

<style scoped>
.search-page {
    padding-bottom: 30px !important;
    margin-top: 30px !important;
    position: relative;
}

.container {
    grid-gap: 0px;
    display: grid;
    position: relative;
    width: 100%;
}

@media (max-width: 1699.9px) {
    .container {
        grid-column: span 2;
        grid-template-columns: repeat(2,1fr);
    }
}

@media (min-width: 1700px) {
    .container {
        grid-column: span 3;
        grid-template-columns: repeat(3,1fr);
    }
}

.user-card {
    margin-bottom: 60px;
    padding: 0px 8px;
}

.user-info-left {
    margin-right: 15px;
}

.user-content {
    width: calc(100% - 101px);
    padding-right: 15px;
}

.card-title {
    margin-bottom: 5px;
    font-size: 18px;
    font-weight: 600;
    color: var(--text1);
    line-height: 1.25;
    display: flex;
    align-items: center;
}

.user-name {
    font-size: 18px;
    font-weight: 600;
    transition: color .2s;
    max-width: 100%;
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: inline-block;
    color: var(--text1);
}

.user-name:hover {
    color: var(--brand_pink);
}

.level {
    margin-left: 10px;
}

.card-center {
    line-height: 1.35;
    margin: 5px 0;
    color: var(--text2);
    width: auto;
    max-width: 100%;
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.card-buttom button {
    height: 32px;
    padding: 0;
    width: 100px;
    border-radius: 6px;
    font-size: 14px;
    line-height: 1;
    white-space: nowrap;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    cursor: pointer;
    -webkit-transition-duration: .2s;
    transition-duration: .2s;
}

.not-follow {
    color: #fff;
    background: var(--brand_pink);
    border: 1px solid var(--brand_pink);
}

.not-follow:hover {
    color: #fff;
    background: var(--Pi4);
    border: 1px solid var(--Pi4);
}

.following {
    color: var(--text2);
    background: var(--graph_bg_regular);
    border: none;
}

.following:hover {
    background: var(--graph_bg_thick);
}

.search-bottom {
    margin: 50px 0 20px;
}
</style>