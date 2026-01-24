<template>
  <div class="header-bar" :class="isFixHeaderBar ? 'slide-down' : ''">
    <!-- 左边 -->
    <div class="left-entry">
      <div
        class="entry-title"
        v-if="isFixHeaderBar"
        @mouseenter="isOpen = true"
        @mouseleave="isOpen = false"
        @click="router.push('/')"
      >
        <picture class="logo">
          <img src="@/assets/img/teriteri-pink.png" alt="">
        </picture>
        <span>首页</span>
        <i class="iconfont icon-xiajiantou" :class="isOpen ? 'arrow-down' : ''"></i>
      </div>
      <div class="entry-title" v-else @click="router.push('/')">
        <i class="iconfont icon-dianshi"></i>
        <span>首页</span>
      </div>
      <div class="default-entry" @click="noPage">
        <span>番剧</span>
      </div>
      <div class="default-entry" @click="noPage">
        <span>漫画</span>
      </div>
      <div class="default-entry" @click="noPage">
        <span>直播</span>
      </div>
      <div class="default-entry" @click="noPage">
        <span>游戏中心</span>
      </div>
      <div class="default-entry" @click="noPage">
        <span>会员购</span>
      </div>
      <div class="download-entry" @click="noPage" v-if="!isFixHeaderBar">
        <i class="iconfont icon-xiazai"></i>
        <span>下载客户端</span>
      </div>
    </div>
    <!-- 中间 -->
    <div class="center-search-container" :style="isShowSearchInput ? '' : 'display: none;'">
      <div class="center-search__bar" :class="isSearchPopShow ? 'is-focus' : ''">
        <!-- 输入框 -->
        <div
          id="nav-searchform"
          :class="isSearchPopShow ? 'nav-searchform-active' : ''"
          ref="searchFormRef"
        >
          <div class="nav-search-content">
            <el-input
              class="nav-search-input"
              :class="isSearchPopShow ? 'nav-search-input-active' : ''"
              v-model="searchInput"
              placeholder="请输入搜索内容"
              @focus="searchPopShow"
              @keyup.enter="goSearch"
              @input="handleInput"
              @compositionstart="isComposite = true"
              @compositionend="compositionend"
            ></el-input>
          </div>
          <div
            class="nav-search-clean"
            :style="searchInput == '' ? 'display: none;' : ''"
            @click.stop="searchInput = ''"
          >
            <el-icon size="16"><CircleCloseFilled /></el-icon>
          </div>
          <div class="nav-search-btn" @click="goSearch">
            <i class="iconfont icon-sousuo"></i>
          </div>
        </div>
        <!-- 气泡框 -->
        <div class="search-panel" :style="isSearchPopShow ? '' : 'display: none;'" ref="searchPopRef">
          <div class="history" v-if="searchInput == ''">
            <div class="header">
              <div class="title">搜索历史</div>
              <div class="clear" @click.stop="removeAllHistories">清空</div>
            </div>
            <div class="histories-wrap" :style="isHistoryOpen ? 'max-height: 171px;' : 'max-height: 91px;'">
              <div class="histories">
                <div class="history-item" v-for="(item, index) in histories" :key="index">
                  <div class="history-text" @click.stop="clickItemToSearch(item)">
                    {{ item }}
                  </div>
                  <div class="close" @click.stop="removeHistory(index)">
                    <i class="iconfont icon-close"></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="history-fold" v-if="isHistoryOpen" @click.stop="isHistoryOpen = false">
              <div class="fold-text">收起</div>
              <i class="iconfont icon-xiajiantou" style="transform: rotate(180deg);"></i>
            </div>
            <div class="history-fold" v-else @click.stop="isHistoryOpen = true">
              <div class="fold-text">展开更多</div>
              <i class="iconfont icon-xiajiantou"></i>
            </div>
          </div>
          <div class="trending" v-if="searchInput == ''">
            <div class="header">
              <div class="title">teriteri热搜</div>
            </div>
            <div class="trendings-double" v-if="screenWidth >= 1450">
              <div class="trendings-col" style="max-width: calc(50% - 5px);">
                <div
                  class="trending-item"
                  v-for="(item, index) in headerStore.trendings.filter((itm, idx) => idx % 2 === 0)"
                  :key="index"
                >
                  <div class="trending-wrap" @click.stop="clickItemToSearch(item.content)">
                    <div class="trendings-rank" :class="index < 2 ? 'topThree' : ''">{{ index * 2 + 1 }}</div>
                    <div class="trendings-text">{{ item.content }}</div>
                    <img src="@/assets/img/icon_new.png" alt="" class="trending-mark" v-if="item.type === 1">
                    <img src="@/assets/img/icon_hot.png" alt="" class="trending-mark" v-if="item.type === 2">
                  </div>
                </div>
              </div>
              <div class="trendings-col" style="max-width: calc(50% - 5px);">
                <div
                  class="trending-item"
                  v-for="(item, index) in headerStore.trendings.filter((itm, idx) => idx % 2 !== 0)"
                  :key="index"
                >
                  <div class="trending-wrap" @click.stop="clickItemToSearch(item.content)">
                    <div class="trendings-rank" :class="index < 1 ? 'topThree' : ''">{{ index * 2 + 2 }}</div>
                    <div class="trendings-text">{{ item.content }}</div>
                    <img src="@/assets/img/icon_new.png" alt="" class="trending-mark" v-if="item.type === 1">
                    <img src="@/assets/img/icon_hot.png" alt="" class="trending-mark" v-if="item.type === 2">
                  </div>
                </div>
              </div>
            </div>
            <div class="trendings-double" v-else>
              <div class="trendings-col" style="margin-right: unset;">
                <div
                  class="trending-item"
                  v-for="(item, index) in headerStore.trendings"
                  :key="index"
                >
                  <div class="trending-wrap" @click.stop="clickItemToSearch(item.content)">
                    <div class="trendings-rank" :class="index < 3 ? 'topThree' : ''">{{ index + 1 }}</div>
                    <div class="trendings-text">{{ item.content }}</div>
                    <img src="@/assets/img/icon_new.png" alt="" class="trending-mark" v-if="item.type === 1">
                    <img src="@/assets/img/icon_hot.png" alt="" class="trending-mark" v-if="item.type === 2">
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="suggestions" v-if="searchInput != ''">
            <div class="suggest-item"
              v-for="(item, index) in matchingWord"
              :key="index"
              v-html="highlightKeyword(searchInput, item)"
              @click.stop="clickItemToSearch(item)"
            ></div>
          </div>
        </div>
      </div>
    </div>
    <!-- 右边 -->
    <div class="right-entry">
      <!-- 未登录状态 -->
      <div class="header-avatar-wrap" v-if="!headerStore.isLogin">
        <div class="default-login" @click="dialogVisible = true">
          登录
        </div>
      </div>
      <!-- 登录后显示头像 -->
      <div v-else class="header-avatar-wrap" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
        <a :href="`/space/${headerStore.user.uid}`" target="_blank" class="header-avatar-wrap--container mini-avatar--small">
          <picture class="v-img">
            <img :src="headerStore.user.avatar_url" :alt="`${headerStore.user.nickname}的头像`" />
          </picture>
        </a>
        <div class="v-popover to-bottom">
          <div class="avatar-panel-popover" :class="isPopoverShow ? 'popShow' : 'popHide'" :style="{ display: popoverDisplay }">
            <a :href="`/space/${headerStore.user.uid}`" target="_blank" class="nickname" :class="headerStore.user.vip !== 0 ? 'vip-name' : ''">
              <span>{{ headerStore.user.nickname }}</span>
            </a>
            <div class="vip-level-tag">
              <div class="vip-tag" v-if="headerStore.user.vip !== 0">
                {{ headerStore.user.vip === 1 ? '月度' : headerStore.user.vip === 2 ? '季度' : '年度' }}大会员
              </div>
              <VLevel class="level" :level="handleLevel(headerStore.user.exp)" :size="12"></VLevel>
              <div class="gender female" v-if="headerStore.user.gender == 0"><el-icon size="12"><Female /></el-icon></div>
              <div class="gender male" v-if="headerStore.user.gender == 1"><el-icon size="12"><Male /></el-icon></div>
            </div>
            <div class="coins">
              <span class="coins-text">硬币: </span>
              <span class="coins-num">{{ headerStore.user.coin }}</span>
            </div>
            <div class="counts">
              <a :href="`/space/${headerStore.user.uid}/fans/follow`" target="_blank" class="counts-item">
                <div class="count-num">{{ handleNum(headerStore.user.followsCount) }}</div>
                <div class="count-text">关注</div>
              </a>
              <a :href="`/space/${headerStore.user.uid}/fans/fans`" target="_blank" class="counts-item">
                <div class="count-num">{{ handleNum(headerStore.user.fansCount) }}</div>
                <div class="count-text">粉丝</div>
              </a>
              <a :href="`/space/${headerStore.user.uid}/dynamic`" target="_blank" class="counts-item">
                <div class="count-num">{{ handleNum(0) }}</div>
                <div class="count-text">动态</div>
              </a>
            </div>
            <div class="single-item middle" @click="openNewPage('/account')">
              <div class="single-item-left">
                <el-icon size="16"><User /></el-icon>
                <span>个人中心</span>
              </div>                            
              <el-icon><ArrowRightBold /></el-icon>
            </div>
            <div class="single-item middle" @click="openNewPage('/platform/upload-manager')">
              <div class="single-item-left">
                <el-icon size="16"><Document /></el-icon>
                <span>投稿管理</span>
              </div>                            
              <el-icon><ArrowRightBold /></el-icon>
            </div>
            <div class="single-item middle" @click="noPage">
              <div class="single-item-left">
                <el-icon size="16"><Star /></el-icon>
                <span>推荐服务</span>
              </div>                            
              <el-icon><ArrowRightBold /></el-icon>
            </div>
            <div class="placeholder"></div>
            <div class="single-item logout" @click="logout">
              <i class="iconfont icon-dengchu"></i>
              <span>退出登录</span>
            </div>
          </div>
        </div>
      </div>
      <div class="vip-wrap">
        <div class="right-entry--outside" @click="noPage">
          <i class="iconfont icon-huiyuan1"></i>
          <span>大会员</span>
        </div>
      </div>
      <div class="v-popover-wrap">
        <VPopover pop-style="padding-top: 17px;">
          <template #reference>
            <div class="red-num--dynamic" v-if="headerStore.user.uid && headerStore.totalMsgUnread > 0">
              {{ headerStore.totalMsgUnread > 99 ? '99+' : headerStore.totalMsgUnread }}
            </div>
            <div class="right-entry--outside" @click="headerStore.isLogin ? openNewPage('/message') : dialogVisible = true">
              <i class="iconfont icon-xinfeng"></i>
              <span>消息</span>
            </div>
          </template>
          <template #content>
            <div class="message-entry-popover" v-if="headerStore.isLogin">
              <div class="message-inner-list">
                <div class="message-inner-list__item" @click="openNewPage('/message/reply')">
                  回复我的
                  <span class="notify-number" v-if="headerStore.msgUnread[0] > 0">
                    {{ headerStore.msgUnread[0] <= 99 ? headerStore.msgUnread[0] : '99+' }}
                  </span>
                </div>
                <div class="message-inner-list__item" @click="openNewPage('/message/at')">
                  @ 我的
                  <span class="notify-number" v-if="headerStore.msgUnread[1] > 0">
                    {{ headerStore.msgUnread[1] <= 99 ? headerStore.msgUnread[1] : '99+' }}
                  </span>
                </div>
                <div class="message-inner-list__item" @click="openNewPage('/message/love')">
                  收到的赞
                  <span class="notify-number" v-if="headerStore.msgUnread[2] > 0">
                    {{ headerStore.msgUnread[2] <= 99 ? headerStore.msgUnread[2] : '99+' }}
                  </span>
                </div>
                <div class="message-inner-list__item" @click="openNewPage('/message/system')">
                  系统消息
                  <span class="notify-number" v-if="headerStore.msgUnread[3] > 0">
                    {{ headerStore.msgUnread[3] <= 99 ? headerStore.msgUnread[3] : '99+' }}
                  </span>
                </div>
                <div class="message-inner-list__item" @click="openNewPage('/message/whisper')">
                  我的消息
                  <span class="notify-number" v-if="headerStore.msgUnread[4] > 0">
                    {{ headerStore.msgUnread[4] <= 99 ? headerStore.msgUnread[4] : '99+' }}
                  </span>
                </div>
              </div>
            </div>
            <div class="not-login" v-else>
              <p class="not-login-tips">登录即可查看消息记录</p>
              <div class="not-login-btn" @click="dialogVisible = true">
                立即登录
              </div>
            </div>
          </template>
        </VPopover>
      </div>
      <div class="v-popover-wrap">
        <VPopover pop-style="padding-top: 17px;">
          <template #reference>
            <div class="right-entry--outside" @click="headerStore.isLogin ? noPage() : dialogVisible = true">
              <i class="iconfont icon-fengche"></i>
              <span>动态</span>
            </div>
          </template>
          <template #content>
            <div style="height: 557.3px; width: 371.6px;" v-if="headerStore.isLogin">
              
            </div>
            <div class="not-login" v-else>
              <p class="not-login-tips">登录即可查看关注动态</p>
              <div class="not-login-btn" @click="dialogVisible = true">
                立即登录
              </div>
            </div>
          </template>
        </VPopover>
      </div>
      <div class="v-popover-wrap">
        <VPopover :pop-style="headerStore.isLogin ? 'padding-top: 17px; margin-left: -20px;' : 'padding-top: 17px;'">
          <template #reference>
            <div class="right-entry--outside" @click="headerStore.isLogin ? noPage() : dialogVisible = true">
              <i class="iconfont icon-shoucang"></i>
              <span>收藏</span>
            </div>
          </template>
          <template #content>
            <div style="height: 556.6px; width: 521.6px;" v-if="headerStore.isLogin">
              2322
            </div>
            <div class="not-login" v-else>
              <p class="not-login-tips">登录即可查看我的收藏</p>
              <div class="not-login-btn" @click="dialogVisible = true">
                立即登录
              </div>
            </div>
          </template>
        </VPopover>
      </div>
      <div class="v-popover-wrap">
        <VPopover :pop-style="headerStore.isLogin ? 'padding-top: 17px; margin-left: -5px;' : 'padding-top: 17px;'">
          <template #reference>
            <div class="right-entry--outside" @click="headerStore.isLogin ? noPage() : dialogVisible = true">
              <i class="iconfont icon-lishijilu"></i>
              <span>历史</span>
            </div>
          </template>
          <template #content>
            <div style="height: 556.6px; width: 371.6px;" v-if="headerStore.isLogin">
              
            </div>
            <div class="not-login" v-else>
              <p class="not-login-tips">登录即可查看历史记录</p>
              <div class="not-login-btn" @click="dialogVisible = true">
                立即登录
              </div>
            </div>
          </template>
        </VPopover>
      </div>

      <div class="vip-wrap">
        <div class="right-entry--outside" @click="noPage">
          <i class="iconfont icon-huiyuan1"></i>
          <span>创作中心</span>
        </div>
      </div>

        <div class="v-popover-wrap">
          <a href="" target="_blank" data-mod="top_bar" data-idx="upload">
            <div class="header-upload-entry">
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg" class="header-upload-entry__icon">
                <path d="M12.0824 10H14.1412C15.0508 10 15.7882 10.7374 15.7882 11.6471V12.8824C15.7882 13.792 15.0508 14.5294 14.1412 14.5294H3.84707C2.93743 14.5294 2.20001 13.792 2.20001 12.8824V11.6471C2.20001 10.7374 2.93743 10 3.84707 10H5.90589" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M8.99413 11.2353L8.99413 3.82353" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M12.0823 6.29413L8.9941 3.20589L5.90587 6.29413" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
              <span class="header-upload-entry__text">投稿</span>
            </div>
          </a>
        </div>
    </div>
  </div>
  <!-- 登录框 -->
  <el-dialog v-model="dialogVisible" :close-on-click-modal="false" destroy-on-close align-center>
    <LoginRegister @loginSuccess="handleLoginSuccess"></LoginRegister>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted, watch, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { useHeaderStore} from '@/stores/headerStore'
import { handleNum, handleLevel, highlightKeyword } from '@/utils/utils.js';
import VPopover from '@/components/popover/VPopover.vue';
import LoginRegister from '@/components/loginRegister/LoginRegister.vue'
import VLevel from '@/components/UserCard/VLevel.vue';
import { ElMessage } from 'element-plus';
import {
  CircleCloseFilled,
  Female,
  Male,
  User,
  Document,
  Star,
  ArrowRightBold
} from '@element-plus/icons-vue';

const props = defineProps({
  // 是否是固钉导航栏
  isFixHeaderBar: {
    type: Boolean,
    default: false
  },
  // 是否显示搜索输入框
  isShowSearchInput: {
    type: Boolean,
    default: true
  }
});

const router = useRouter();
const headerStore = useHeaderStore();

// 响应式数据
const isOpen = ref(false);
const searchInput = ref('');
const isComposite = ref(false);
const matchingWord = ref([]);
const isSearchPopShow = ref(false);
const histories = ref([]);
const isHistoryOpen = ref(false);
const popoverDisplay = ref('none');
const isPopoverShow = ref(false);
const dialogVisible = ref(false);
const screenWidth = ref(window.innerWidth);

// 模板引用
const searchFormRef = ref(null);
const searchPopRef = ref(null);

// 节流计时器
let inTimer;
let outTimer;

// 获取搜索推荐
const getMatchingWord = async () => {
  if (searchInput.value.trim() === "") return;
  const keyword = encodeURIComponent(searchInput.value);
  // 这里需要根据你的实际API修改
  // const res = await $get("/search/word/get", {params: {keyword: keyword}});
  // matchingWord.value = res.data.data;
};

// 边输入边查询相关搜索词条
const handleInput = () => {
  if (isComposite.value) return;
  getMatchingWord();
};

// 完成中文输入
const compositionend = () => {
  isComposite.value = false;
  handleInput();
};

// 前往搜索的回调
const goSearch = () => {
  searchPopHide();
  let input = searchInput.value.trim();
  const index = histories.value.indexOf(input);
  if (index !== -1) {
    histories.value.splice(index, 1);
  }
  histories.value.unshift(input);
  saveToLocalStorage();
  
  if (input === "") {
    openNewPage("/search");
  } else {
    openNewPage(`/search/video?keyword=${input}`);
  }
};

// 显示搜索气泡框
const searchPopShow = () => {
  isSearchPopShow.value = true;
  getMatchingWord();
};

// 隐藏搜索气泡框
const searchPopHide = () => {
  isSearchPopShow.value = false;
};

// 点击搜索框和气泡框外的空白处关闭气泡
const handleOutsideClick = (event) => {
  const formElement = searchFormRef.value;
  const popoverElement = searchPopRef.value;
  if (
    !formElement?.contains(event.target) &&
    !popoverElement?.contains(event.target)
  ) {
    searchPopHide();
  }
};

// 将搜索历史存到浏览器
const saveToLocalStorage = () => {
  localStorage.setItem("historiesSearch", JSON.stringify(histories.value));
};

// 从浏览器中加载搜索历史
const loadFromLocalStorage = () => {
  const storedList = localStorage.getItem("historiesSearch");
  if (storedList) {
    histories.value = JSON.parse(storedList);
  }
};

// 删除单个搜索历史
const removeHistory = (index) => {
  histories.value.splice(index, 1);
  saveToLocalStorage();
};

// 清空全部搜索历史
const removeAllHistories = () => {
  histories.value = [];
  localStorage.removeItem("historiesSearch");
};

// 点击条目搜索
const clickItemToSearch = (value) => {
  searchInput.value = value;
  goSearch();
};

// 悬浮头像时，气泡的显隐
const handleMouseEnter = () => {
  clearTimeout(outTimer);
  inTimer = setTimeout(() => {
    popoverDisplay.value = "";
    isPopoverShow.value = true;
  }, 100);
};

const handleMouseLeave = () => {
  clearTimeout(inTimer);
  isPopoverShow.value = false;
  outTimer = setTimeout(() => {
    popoverDisplay.value = "none";
  }, 200);
};

// 更新屏幕宽度
const updateScreenWidth = () => {
  screenWidth.value = window.innerWidth;
};

// 退出登录
const logout = () => {
  headerStore.logout();
};

// 打开新标签页
const openNewPage = (route) => {
  window.open(router.resolve(route).href, '_blank');
};

// 登录成功处理
const handleLoginSuccess = () => {
  dialogVisible.value = false;
  
  // 这里可以添加登录成功后的其他逻辑
};

// 未开放页面提示
const noPage = () => {
  ElMessage.warning("该功能暂未开放");
};

// 监听store中的openLogin变化
watch(() => headerStore.openLogin, (curr) => {
  if (curr) {
    dialogVisible.value = true;
    // 重置openLogin状态
    headerStore.setOpenLogin(false);
  }
});

onMounted(() => {
  window.addEventListener("click", handleOutsideClick);
  loadFromLocalStorage();
  window.addEventListener('resize', updateScreenWidth);
});

onUnmounted(() => {
  window.removeEventListener("click", handleOutsideClick);
  window.removeEventListener('resize', updateScreenWidth);
  clearTimeout(inTimer);
  clearTimeout(outTimer);
});
</script>

<style scoped>
/* 样式保持不变 */
.header-bar {
    box-sizing: border-box;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1002;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 24px;
    max-width: 2560px;
    width: 100%;
    height: 64px;
}

.left-entry {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    margin-right: 20px;
    border: 0;
    font-family: inherit;
    font-size: 100%;
    font-weight: 400;
    font-style: normal;
    line-height: 1.25;
}

.slide-down {
    position: fixed;
    top: 0;
    left: unset;
    animation: headerSlideDown .3s linear forwards;
    background: var(--bg1);
    box-shadow: 0 2px 4px rgba(0,0,0,.08);
    background: var(--bg1_float);
}

.logo {
    height: 40%;
    display: flex;
    align-items: center;
    margin-right: 10px;
}

.logo img {
    height: 100%;
}

.icon-xiajiantou {
    margin-left: 5px;
}

.entry-title {
    display: flex;
    align-items: center;
}

.entry-title .icon-xiajiantou {
    transition: transform .3s;
    transform: rotate(0);
    font-weight: 600;
}

.arrow-down {
    transform: rotate(180deg) !important;
}

.default-entry span {
    display: inline-block;
    position: relative;
}

.download-entry {
    margin-left: 10px;
}

.slide-down .entry-title,
.slide-down .default-entry,
.slide-down .download-entry {
    color: var(--text1);
}

.entry-title,
.default-entry,
.download-entry {
    height: 64px;
    line-height: 64px;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
    margin-right: 15px;
}

.icon-dianshi, .icon-xiazai {
    margin-right: 5px;
}

.center-search-container {
    flex: 1 auto;
    height: 40px;
}

.center-search-container .is-focus {
    box-shadow: 0 0 30px rgba(0,0,0,.1);
    border-radius: 8px;
}

.header-bar .center-search-container .center-search__bar {
    position: relative;
    margin: 0 auto;
    min-width: 181px;
    max-width: 500px;
}

.is-focus #nav-searchform {
    background-color: #fff;
    border-bottom: none;
}

#nav-searchform {
    display: flex;
    align-items: center;
    padding: 0 48px 0 4px;
    position: relative;
    z-index: 1;
    overflow: hidden;
    border: 1px solid var(--line_regular);
    height: 40px;
    border-radius: 8px;
    background-color: var(--graph_bg_regular);
    opacity: .9;
    transition: background-color .3s;
}

.nav-searchform-active {
    transition: background-color 0s !important;
    border-radius: 8px 8px 0 0 !important;
}

#nav-searchform:hover {
    background-color: #fff;
    opacity: 1;
}

.nav-search-content {
    flex: 1;
}

.nav-search-input :deep(.el-input__wrapper) {
    background-color: #FFFFFF00;
    border-radius: 6px;
    box-shadow: none;
    padding: 1px 30px 1px 11px;
}

.nav-search-input-active :deep(.el-input__wrapper) {
    background-color: #0000001A;
}

.nav-search-input :deep(.el-input__inner) {
    color: var(--text2);
}

.nav-search-input :deep(.el-input__inner:focus) {
    color: var(--text1);
}

.nav-search-clean {
    position: absolute;
    width: 16px;
    height: 16px;
    right: 55px;
    cursor: pointer;
    color: var(--graph_weak);
}

.nav-search-clean:hover {
    color: var(--graph_icon);
}

.nav-search-btn {
    position: absolute;
    right: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border-radius: 6px;
    color: var(--text1);
    line-height: 32px;
    cursor: pointer;
    transition: background-color .3s;
}

.nav-search-btn:hover {
    background-color: var(--graph_bg_thick);
}

.icon-sousuo {
    font-size: 18px;
}

.header-bar .search-panel {
    width: 100%;
    max-height: 612px;
    overflow-y: auto;
    background: var(--bg1);
    border: 1px solid var(--line_regular);
    border-top: none;
    border-radius: 0 0 8px 8px;
    padding: 13px 0 16px;
    -webkit-font-smoothing: antialiased;
}

.header-bar .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
}

.header-bar .header .title {
    height: 24px;
    font-size: 16px;
    line-height: 24px;
}

.header-bar .header .clear {
    font-size: 12px;
    line-height: 15px;
    height: 15px;
    color: var(--text3);
    cursor: pointer;
}

.header-bar .header .clear:hover {
    color: var(--text2);
}

.header-bar .histories-wrap {
    padding: 0 16px;
    overflow: hidden;
}

.header-bar .histories {
    display: flex;
    flex-wrap: wrap;
    margin-top: 12px;
    margin-right: -10px;
    margin-bottom: 4px;
}

.header-bar .histories .history-item {
    position: relative;
    box-sizing: border-box;
    height: 30px;
    padding: 7px 10px 8px;
    font-size: 12px;
    line-height: 15px;
    background: var(--graph_bg_thin);
    border-radius: 4px;
    margin-right: 10px;
    margin-bottom: 10px;
    cursor: pointer;
}

.header-bar .histories .history-item .history-text {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 96px;
    color: var(--text2);
}

.header-bar .histories .history-item .history-text:hover {
    color: var(--brand_pink);
}

.header-bar .histories .history-item .close {
    display: none;
    box-sizing: border-box;
    position: absolute;
    width: 13px;
    height: 13px;
    top: -6px;
    right: -3px;
    padding: 2px;
}

.header-bar .histories .history-item:hover .close {
    display: block;
}

.header-bar .histories .history-item .close i {
    font-size: 10px;
    color: #fff;
    background-color: #ccc;
    border-radius: 50%;
}

.header-bar .history-fold {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 70px;
    margin: 0 auto 14px;
    color: var(--text3);
    cursor: pointer;
}

.header-bar .history-fold:hover {
    color: var(--brand_pink);
}

.header-bar .fold-text {
    font-size: 12px;
    line-height: 15px;
    height: 15px;
}

.header-bar .trendings-double {
    display: flex;
    
}

.header-bar .trendings-double .trendings-col {
    flex: 1;
}

.header-bar .trendings-double .trendings-col:first-child {
    margin-right: 10px;
}

.header-bar .trending-wrap {
    box-sizing: border-box;
    height: 38px;
    display: flex;
    align-items: center;
    cursor: pointer;
    padding-left: 16px;
}

.header-bar .trending-item:hover {
    background-color: var(--graph_bg_thick);
}

.header-bar .trending-item .trendings-rank {
    margin-right: 10px;
    color: var(--text3);
}

.header-bar .trending-item .topThree {
    color: var(--text2) !important;
}

.header-bar .trending-item .trendings-text {
    font-size: 14px;
    line-height: 17px;
    height: 17px;
    margin-right: 6px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    letter-spacing: 0;
    color: var(--text2);
}

.header-bar .trending-item .trending-mark {
    height: 14px;
    margin-right: 16px;
}

.header-bar .suggestions {
    margin-top: -6px;
    margin-bottom: -6px;
}

.header-bar .suggest-item {
    height: 32px;
    display: block;
    line-height: 32px;
    font-size: 14px;
    position: relative;
    text-align: left;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    cursor: pointer;
    padding: 0 16px;
    margin-bottom: 4px;
}

.header-bar .suggest-item:hover, .header-bar .suggest-item:focus {
    outline: none;
    background: var(--graph_bg_thick);
}

.right-entry {
    display: flex;
    align-items: center;
    margin-left: 20px;
    flex-shrink: 0;
}

.default-login {
    position: absolute;
    top: 5px;
    left: 10px;
    z-index: 2;
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    background-color: var(--Pi4);
    text-align: center;
    line-height: 38px;
    color: rgba(255, 255, 255, 0.9);
}

.header-avatar-wrap {
    position: relative;
    box-sizing: content-box;
    padding-right: 10px;
    width: 50px;
    height: 50px;
    cursor: pointer;
}

.header-avatar-wrap--container {
    position: relative;
    z-index: 2;
}

.mini-avatar--small {
    position: absolute;
    top: 5px;
    left: 10px;
    z-index: 2;
    display: block;
    width: 38px;
    height: 38px;
    border-radius: 50%;
    box-sizing: border-box;
    border: 2px solid #fff;
    transition: width 0.3s ease, height 0.3s ease, top 0.3s ease, left 0.3s ease;
}

.v-img {
    position: relative;
    display: inline-block;
    line-height: 1;
    width: 100%;
    height: 100%;
    vertical-align: middle;
    border-radius: 50%;
    background-color: transparent;
}

/* 放大头像 */
.header-avatar-wrap:hover .mini-avatar--small {
    top: 15px;
    left: -35px;
    width: 90px;
    height: 90px;
}

.header-avatar-wrap:hover .mini-avatar--small.shrink {
    animation: shrink 0.3s both;
}

@keyframes shrink {
  to {
    transform: scale(1);
  }
}

.v-img img {
    display: block;
    width: 100%;
    height: 100%;
    object-fit: inherit;
    border-radius: 50%;
    image-rendering: -webkit-optimize-contrast;
}

.v-popover {
    position: absolute;
    z-index: 1;
    padding-top: 20px;
    margin-left: -20px;
    cursor: default;
}

.to-bottom {
    top: 100%;
    left: 50%;
    transform: translate3d(-50%,0,0);
}

.avatar-panel-popover {
    width: 300px;
    background-color: #fff;
    border-radius: 8px;
    padding: 0 24px 18px;
    box-shadow: 0 0 30px rgba(0,0,0,.1);
    border: 1px solid var(--line_regular);
}

.popHide {
    animation: fade-out 0.2s ease-out forwards;
    transform-origin: top;
}

.popShow {
    animation: fade-in 0.2s ease-out forwards;
    transform-origin: top;
}

/* 淡入动画 */
@keyframes fade-in {
    0% {
        opacity: 0;
        transform: translateY(-10px);
    }
    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 淡出动画 */
@keyframes fade-out {
    0% {
        opacity: 1;
        transform: translateY(0);
    }
    100% {
        opacity: 0;
        transform: translateY(-10px);
    }
}

.nickname {
    display: flex;
    justify-content: center;
    margin-top: 50px;
    margin-bottom: 6px;
    font-size: 18px;
    color: var(--text1);
}

.vip-level-tag {
    display: flex;
    align-items: center;
    justify-content: center;
}

.vip-tag {
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
    margin-right: 10px;
    overflow: hidden;
    white-space: nowrap;
    font-weight: 400;
    cursor: pointer;
}

.gender {
    height: 14px;
    width: 14px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.female {
    background-color: var(--Pi2);
    color: var(--Pi5);
}

.male {
    background-color: var(--Lb2);
    color: var(--Lb5_u);
}

.vip-level-tag .iconfont {
    font-size: 12px;
    margin-right: 10px;
    line-height: 14px;
}

.level {
    margin-right: 10px;
}

.coins {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    margin: 6px 0;
}

.coins-text {
    margin-right: 5px;
    color: var(--text3);
}

.coins-num {
    color: var(--text1);
}

.counts {
    display: flex;
    justify-content: space-between;
    padding: 0 20px;
    margin: 6px 0 12px 0;
}

.counts-item {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: fit-content;
}

.count-num {
    font-size: 18px;
    color: #222222;
    transition: 0.3s;
}

.count-text {
    font-size: 12px;
    color: var(--text3);
    margin-top: 2px;
    transition: 0.3s;
}

.counts-item:hover .count-num, .counts-item:hover .count-text {
    color: var(--brand_pink);
}

.single-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 14px;
    height: 38px;
    border-radius: 8px;
    color: var(--text2);
    font-size: 14px;
    cursor: pointer;
    transition: background-color .3s;
    margin-bottom: 2px;
}

.single-item:hover {
    background-color: var(--graph_bg_regular);
}

.single-item-left {
    display: flex;
    align-items: center;
}

.single-item span {
    line-height: 14px;
    margin-left: 10px;
}

.placeholder {
    margin: 6px 0 12px 0;
    border-bottom: 1px solid #ddd;
}

.logout {
    display: flex;
    justify-content: initial !important; 
}

.red-num--dynamic {
    cursor: auto;
    user-select: none;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    z-index: 1;
    position: absolute;
    top: -5px;
    left: 25px;
    padding: 0 4px;
    min-width: 15px;
    border-radius: 10px;
    background-color: var(--stress_red);
    color: #fff;
    font-size: 12px;
    line-height: 15px;
}

.slide-down .right-entry--outside .iconfont {
    color: var(--text1);
}

.slide-down .right-entry--outside {
    color: var(--text2);
}

.right-entry--outside {
    display: flex;
    align-items: center;
    flex-direction: column;
    flex-shrink: 0;
    margin-right: 0;
    min-width: 50px;
    text-align: center;
    font-size: 13px;
    color: #fff;
    cursor: pointer;
}

.right-entry--outside .iconfont {
    font-size: 20px;
    display: inline-block;
    position: relative;
}

.message-entry-popover {
    overflow: hidden;
    width: 142px;
}

.message-entry-popover .message-inner-list {
    display: flex;
    flex-direction: column;
    padding: 12px 0;
}

.message-entry-popover .message-inner-list__item {
    position: relative;
    display: flex;
    align-items: center;
    padding: 10px 0 10px 27px;
    color: var(--text2);
    text-align: left;
    font-size: 14px;
    transition: background-color .3s;
    cursor: pointer;
}

.message-entry-popover .message-inner-list__item:hover {
    background-color: var(--graph_bg_thick);
}

.notify-number {
    position: absolute;
    right: 17px;
    padding: 0 5px;
    border-radius: 8px;
    background: var(--stress_red);
    color: #fff;
    font-size: 12px;
    line-height: 16px;
}

.right-entry-item--upload {
    margin-left: 15px;
}

.not-login {
    user-select: none;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    padding: 22px 20px;
    width: 340px;
}

.not-login-tips {
    margin-bottom: 24px;
    font-size: 14px;
    line-height: 20px;
    text-align: center;
    color: var(--text3);
}

.not-login-btn {
    border-radius: 8px;
    color: #fff;
    background-color: var(--brand_pink);
    height: 40px;
    width: 100%;
    text-align: center;
    line-height: 40px;
    cursor: pointer;
    transition: 0.3s;
}

.not-login-btn:hover {
    background-color: var(--Pi4);
}

.upload-buttom {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-left: 16px;
    width: 90px;
    height: 34px;
    border-radius: 8px;
    background: #fb7299;
    color: #fff;
    text-align: center;
    font-size: 14px;
    line-height: 34px;
    cursor: pointer;
    transition: background-color .3s;
}

.upload-buttom:hover {
    background-color: #f992af;
}

.icon-shangchuan {
    margin-right: 5px;
    line-height: 34px;
    margin-top: -2px;
}

@media (max-width: 1279.9px) {
    .right-entry--outside {
        margin: 0 5px;
        min-width: 25px;
    }

    .upload-buttom {
        width: 34px;
        height: 34px;
        margin-left: 0;
    }

    .icon-shangchuan {
        margin-right: 0;
    } 

    .right-entry--outside span, .upload-buttom span {
        display: none;
    }

    .red-num--dynamic {
        left: 17px;
    }
}

/* 跳动效果 */
@keyframes jump {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

.default-entry:hover span, .right-entry--outside:hover .iconfont {
    animation: jump 0.3s;
}




.header-upload-entry{
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 16px;
  width: 90px;
  height: 34px;
  border-radius: 8px;
  background: #fb7299;
  color: #fff;
  text-align: center;
  font-weight: 500;
  font-size: 14px;
  line-height: 20px;
  cursor: pointer;
  transition: background-color .3s;
}

.header-upload-entry .header-upload-entry__icon{
  margin-right: 5px;
  color: #fff;
}
.header-upload-entry span{
  color: inherit;
}
</style>