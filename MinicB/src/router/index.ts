import { createRouter,createWebHashHistory,createWebHistory  } from "vue-router";
const Index = () => import('@/views/IndexVue.vue')
const Platform = () => import('@/views/platform/PlatformView.vue')
import { useHeaderStore } from '@/stores/headerStore'
import { useUserStore } from '@/stores/useUserStore'
import { useMessageStore } from '@/stores/useMessageStore'
const VideoDetail = () => import('@/views/detail/VideoDetail.vue')
const Search = () => import('@/views/search/SearchView.vue')
const SearchUser = () => import('@/views/search/children/SearchUser.vue')
const SearchVideo = () => import('@/views/search/children/SearchVideo.vue')

const routes = [
    { path: '/', redirect: '' },
    { path: '', name: "index", component: Index, meta: { requestAuth: true  } },
    {
        path: '/platform',
        redirect: '/platform/home',
        component: Platform,
        children: [
            
        ]
    },
    {
      path: '/video/:vid',
      name: 'VideoDetail',
      component: VideoDetail,
      meta: { requestAuth: true }
    },
    {
      path: '/search',
      name: 'Search',
      component: Search,
      meta: { requestAuth: false },
      props: route => ({ keyword: route.query.keyword }),
      children: [
        {
          path: 'video', // 注意：这里是相对路径，不是绝对路径
          name: 'SearchVideo',
          component: SearchVideo,
          meta: { requestAuth: false },
          props: route => ({ keyword: route.query.keyword })
        },
        {
          path: 'user', // 注意：这里是相对路径，不是绝对路径
          name: 'SearchUser',
          component: SearchUser,
          meta: { requestAuth: false },
          props: route => ({ keyword: route.query.keyword })
        }
      ]
    }
]
 
const router = createRouter({
    //history:createWebHashHistory(), // 跳转方式
    history:createWebHistory(),
    routes :routes // 路由配置
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const headerStore = useHeaderStore();
  const userStore = useUserStore();
  const messageStore = useMessageStore()
  
    // 如果访问需要认证的路由
    if (to.meta.requestAuth) {   
      if (userStore.isLoading) {
        // 已登录，放行
       // messageStore.connectWebSocket()
        //messageStore.getMsgUnread()
        next();
      } else {
        // 尝试自动登录
        const success = await userStore.login();
        if (success) {
          console.log("success");
          next();
        } else {
          // 跳转到登录页
          next('/');
        }
      }
    } else {
      console.log("需要检测");
      
      // 不需要认证的路由，直接放行
      next();
    }
  });

export default router