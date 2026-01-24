import { createRouter,createWebHashHistory } from "vue-router";
const Index = () => import('@/views/IndexVue.vue')
const Platform = () => import('@/views/platform/PlatformView.vue')




const routes = [
    { path: '/', redirect: '' },
    { path: '', name: "index", component: Index, meta: { requestAuth: false } },
    {
        path: '/platform',
        redirect: '/platform/home',
        component: Platform,
        children: [
            
        ]
    },
]
 
const router = createRouter({
    history:createWebHashHistory(), // 跳转方式
    routes :routes // 路由配置
})
export default router