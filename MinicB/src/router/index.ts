import { createRouter,createWebHashHistory } from "vue-router";
const Index = () => import('@/views/IndexVue.vue')
const Platform = () => import('@/views/platform/PlatformView.vue')
import { useHeaderStore } from '@/stores/headerStore'
import { useUserStore } from '@/stores/useUserStore'




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
]
 
const router = createRouter({
    //history:createWebHashHistory(), // 跳转方式
    history:createWebHashHistory(),
    routes :routes // 路由配置
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const headerStore = useHeaderStore();
  const userStore = useUserStore();
    // 如果访问需要认证的路由
    if (to.meta.requestAuth) {      
      if (headerStore.isLogin) {
        // 已登录，放行
        next();
      } else {
        // 尝试自动登录
        const success = await userStore.login();
        if (success) {
          console.log("success");
          next();
        } else {
          // 跳转到登录页
          console.log("123123123123");
          
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