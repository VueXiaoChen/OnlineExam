<template>
    <div class="user-card">
        <div class="user-card-bg" :style="`background-image: url('${props.user.bg_url}')`"></div>
        <div class="user-info-wrapper">
            <a :href="`/space/${props.user.uid}`" target="_blank" class="user-info-left">
                <VAvatar :img="props.user.avatar_url" :size="48"></VAvatar>
            </a>
            <div class="info">
                <div class="user">
                    <a :href="`/space/${props.user.uid}`" target="_blank"
                        class="name" :class="props.user.vip !== 0 ? 'vip-name' : ''"
                    >{{ props.user.nickname }}</a>
                    <div class="gender female" v-if="props.user.gender === 0">
                        <el-icon size="12"><Female /></el-icon>
                    </div>
                    <div class="gender male" v-if="props.user.gender === 1">
                        <el-icon size="12"><Male /></el-icon>
                    </div>
                    <VLevel :level="level" :size="12"></VLevel>
                    <span class="vip" v-if="props.user.vip !== 0">
                        {{ vipText }}
                    </span>
                </div>
                <p class="social">
                    <a :href="`/space/${props.user.uid}/fans/follow`" target="_blank">
                        <span class="num">{{ formattedFollowsCount }}</span>
                        <span class="text">关注</span>
                    </a>
                    <a :href="`/space/${props.user.uid}/fans/fans`" target="_blank">
                        <span class="num">{{ formattedFansCount }}</span>
                        <span class="text">粉丝</span>
                    </a>
                    <span>
                        <span class="num">{{ formattedLoveCount }}</span>
                        <span class="text">获赞</span>
                    </span>
                </p>
                <div class="official" v-if="props.user.auth === 1 || props.user.auth === 2">
                    <svg v-if="props.user.auth === 1" t="1700121982187" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="14" height="14" p-id="3631">
                        <path d="M0 512a512 512 0 1 0 1024 0A512 512 0 1 0 0 512z" fill="#FFFFFF" p-id="3632"></path>
                        <path d="M93.09 512a418.91 418.91 0 1 0 837.82 0 418.91 418.91 0 1 0-837.82 0z" fill="#FFC62E" p-id="3633"></path>
                        <path d="M474.112 763.95a33.28 33.28 0 0 1-14.057 4.05 21.039 21.039 0 0 1-21.318-20.806 19.55 19.55 0 0 1 1.21-5.865l40.495-187.438-167.657 0.326a21.225 21.225 0 0 1-21.876-20.806 20.433 20.433 0 0 1 9.495-16.85l250.182-255.767c4.608-2.793 9.542-4.747 14.662-4.794a21.132 21.132 0 0 1 21.364 20.852 19.968 19.968 0 0 1-2.792 9.775l-40.216 184.32h167.843a21.178 21.178 0 0 1 21.644 20.852 20.294 20.294 0 0 1-9.495 16.943L474.112 763.95z" fill="#FFFFFF" p-id="3634"></path>
                    </svg>
                    <svg v-if="props.user.auth === 2" t="1700122376038" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="14" height="14" p-id="3782">
                        <path d="M0 512a512 512 0 1 0 1024 0A512 512 0 1 0 0 512z" fill="#FFFFFF" p-id="3783"></path>
                        <path d="M93.09 512a418.91 418.91 0 1 0 837.82 0 418.91 418.91 0 1 0-837.82 0z" fill="#4AC7FF" p-id="3784"></path>
                        <path d="M474.112 763.95a33.28 33.28 0 0 1-14.057 4.05 21.039 21.039 0 0 1-21.318-20.806 19.55 19.55 0 0 1 1.21-5.865l40.495-187.438-167.657 0.326a21.225 21.225 0 0 1-21.876-20.806 20.433 20.433 0 0 1 9.495-16.85l250.182-255.767c4.608-2.793 9.542-4.747 14.662-4.794a21.132 21.132 0 0 1 21.364 20.852 19.968 19.968 0 0 1-2.792 9.775l-40.216 184.32h167.843a21.178 21.178 0 0 1 21.644 20.852 20.294 20.294 0 0 1-9.495 16.943L474.112 763.95z" fill="#FFFFFF" p-id="3785"></path>
                    </svg>
                    <span>teriteri{{ props.user.auth === 1 ? '个人' : '机构' }}认证：{{ props.user.authMsg }}</span>
                </div>
                <p class="sign">{{ props.user.description }}</p>
                <div class="btn-box">
                    <a class="not-follow" v-if="true">关注</a>
                    <a class="following" v-else @mouseenter="mouseEnter = true" @mouseleave="mouseEnter = false">
                        {{ mouseEnter ? '取消关注' : '已关注' }}
                    </a>
                    <a class="message" @click="createChat">发消息</a>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/useUserStore';
import { handleNum, handleLevel } from '@/utils/utils.js';
//import VAvatar from '@/components/avatar/VAvatar.vue';
import VLevel from '@/components/UserCard/VLevel.vue';
import { Female, Male } from '@element-plus/icons-vue';



// 定义 Props
const props:any = defineProps();

// 使用 Pinia store
const userStore = useUserStore();
const router = useRouter();

// 响应式数据
const mouseEnter = ref<boolean>(false);

// 计算属性
const level = computed(() => {
  return handleLevel(props.user.exp);
});

const vipText = computed(() => {
  switch (props.user.vip) {
    case 1:
      return '月度大会员';
    case 2:
      return '季度大会员';
    case 3:
      return '年度大会员';
    default:
      return '';
  }
});

const formattedFollowsCount = computed(() => {
  return handleNum(props.user.followsCount);
});

const formattedFansCount = computed(() => {
  return handleNum(props.user.fansCount);
});

const formattedLoveCount = computed(() => {
  return handleNum(props.user.loveCount);
});

// 方法
const createChat = () => {
  // 检查当前用户是否已登录
  if (!userStore.user?.uid) {
    // 这里可以触发登录弹窗，根据你的项目实现
    // 例如: userStore.setOpenLogin(true);
    return;
  }
  
  openNewPage(`/message/whisper/${props.user.uid}`);
};

const openNewPage = (route: string) => {
  window.open(router.resolve(route).href, '_blank');
};
</script>

<style scoped>
.user-card {
    font-size: 12px;
    border-radius: 8px;
    width: 366px;
    cursor: default;
}

.user-card-bg {
    width: 100%;
    height: 85px;
    border-radius: 8px 8px 0 0;
    overflow: hidden;
    background-color: var(--graph_bg_regular);
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
}

.user-info-wrapper {
    position: relative;
    display: flex;
    padding: 0 20px 16px 10px;
}

.user-info-left {
    position: relative;
    top: 10px;
    border-radius: 50%;
    width: 48px;
    height: 48px;
    background-color: var(--graph_weak);
    margin-right: 12px;
}

.info p {
    color: var(--text1);
}

.user {
    margin-top: 12px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
}

.name {
    color: var(--text1);
    max-width: 158px;
    margin-right: 6px;
    font-size: 16px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.gender {
    height: 14px;
    width: 14px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 6px;
}

.female {
    background-color: var(--Pi2);
    color: var(--Pi5);
}

.male {
    background-color: var(--Lb2);
    color: var(--Lb5_u);
}

.vip {
    display: inline-block;
    box-sizing: border-box;
    max-width: 58px;
    height: 16px;
    color: #fff;
    background: var(--brand_pink);
    border-radius: 2px;
    line-height: 16px;
    font-size: 10px;
    padding: 0 3px;
    margin-left: 6px;
    overflow: hidden;
    white-space: nowrap;
    font-weight: 400;
    cursor: pointer;
}

.social {
    margin-bottom: 10px;
}

.social a {
    color: var(--text1);
    margin-right: 18px;
}

.social .text {
    color: var(--text3);
    margin-left: 3px;
}

.official {
    height: 18px;
    margin-bottom: 8px;
    display: flex;
    align-items: center;
}

.official span {
    font-size: 12px;
    color: var(--text3);
    overflow: hidden;
    text-overflow: -o-ellipsis-lastline;
    text-overflow: ellipsis;
    word-break: break-word;
    -webkit-line-clamp: 2;
}

.official svg {
    margin-right: 2px;
}

.sign {
    line-height: 17px;
    color: var(--text3) !important;
    display: -webkit-box;
    overflow: hidden;
    -webkit-box-orient: vertical;
    text-overflow: -o-ellipsis-lastline;
    text-overflow: ellipsis;
    word-break: break-word;
    -webkit-line-clamp: 2;
}

.btn-box {
    display: flex;
    margin-top: 16px;
}

.btn-box a {
    box-sizing: border-box;
    cursor: pointer;
    text-decoration: none;
    width: 100px;
    height: 30px;
    line-height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 8px;
    border-radius: 4px;
    transition: all 0.3s;
    font-size: 14px;
}

.not-follow {
    color: #fff;
    background-color: var(--brand_pink);
}

.not-follow:hover {
    background-color: var(--Pi4);
}

.following {
    color: var(--text3);
    background-color: var(--graph_bg_thick);
}

.following:hover {
    background-color: var(--graph_bg_regular);
}

.message {
    border: 1px solid var(--text3);
    color: var(--text2);
    background-color: transparent;
}

.message:hover {
    color: var(--brand_pink);
    border-color: var(--brand_pink);
}
</style>