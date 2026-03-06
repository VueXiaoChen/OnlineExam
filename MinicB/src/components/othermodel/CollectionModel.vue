
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { useUserStore } from '@/stores/useUserStore'
import axios from 'axios';
import { handleTime, handleNum as utilsHandleNum, handleDate as utilsHandleDate } from '@/utils/utils.js'

const activeIndex = ref(0)
const favorites = ref()
const favoritesvideo = ref([])
const userStore = useUserStore()
favorites.value = userStore.favorites

const vids:any = ref([])

const Selects = async (index,fid) => {
    activeIndex.value = index
    vids.value=[]
    console.log(favorites.value);
    
    try {
        const res = await axios.get('/api/favoriteVideo/find', {
        params: { fid: fid},
      })
      if (res.data?.data) {
        for (let i = 0; i < res.data.data.list.length; i++) {
            vids.value.push(res.data.data.list[i].vid)   
        }
      }
      if(vids.value!=null){
        const favorite = await axios.get('/api/video/favoritevideos', {
            params: { vid: vids.value.join(',')},
        })
        if(favorite!=null){
            favoritesvideo.value = favorite.data.data
            console.log(favoritesvideo.value);
        }
    }
    } catch (error) {
      console.error('获取视频信息失败:', error)
    } 
  };
  
  
  const getFavoRitevideo = (vid)=>{

  }

</script>
<template>
<div class="v-popover-content header-favorite-popover" style="padding-top: 15px; ">
    <div class="favorite-panel-popover">
        <div class="favorite-panel-popover__nav">
            <el-scrollbar max-height="500px">
                <div class="tab-item" v-for="(item,index) in favorites" :key="index" :class="{ 'tab-item--active': activeIndex === index }" @click="Selects(index,item.fid)">
                    <span class="tab-item__title">{{item.title}}</span>
                    <span class="tab-item__num">{{item.count}}</span>
                </div>
            </el-scrollbar>
        </div>
        <div class="favorite-panel-popover__content">
            <el-scrollbar max-height="500px">
              <a href="" class="header-fav-card" v-for="(items,indexs) in favoritesvideo" :key="indexs">
                <div class="header-fav-card__image">
                  <picture class="v-img">
                    <source :srcset='items.video.coverUrl' type="image/avif">
                    <source :srcset='items.video.coverUrl' type="image/webp">
                    <img :src='items.video.coverUrl' loading="lazy" onload="" onerror="typeof window.imgOnError === 'function' &amp;&amp; window.imgOnError(this)">
                  </picture>
                  <div class="header-fav-card__duration">
                    <span class="header-fav-card__duration--text">
                      {{handleTime(items.video.duration)}}
                    </span>
                  </div>

                </div>
                <div class="header-fav-card__info">
                  <div title="看到这个 Vue 后台 v3.0，我直接把旧项目删了…" href="//www.bilibili.com/video/BV1SJkXBBEX5/" target="_blank" class="header-fav-card__info--title">{{items.video.title}}</div>
                  <span class="header-fav-card__info--name" href="//space.bilibili.com/425500936" title="SuperManCT" target="_blank">
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg" class="up-icon"><path fill-rule="evenodd" clip-rule="evenodd" d="M1.33334 5.16669C1.33334 3.78597 2.45263 2.66669 3.83334 2.66669H12.1667C13.5474 2.66669 14.6667 3.78597 14.6667 5.16669V10.8334C14.6667 12.2141 13.5474 13.3334 12.1667 13.3334H3.83334C2.45263 13.3334 1.33334 12.2141 1.33334 10.8334V5.16669ZM3.83334 3.66669C3.00492 3.66669 2.33334 4.33826 2.33334 5.16669V10.8334C2.33334 11.6618 3.00492 12.3334 3.83334 12.3334H12.1667C12.9951 12.3334 13.6667 11.6618 13.6667 10.8334V5.16669C13.6667 4.33826 12.9951 3.66669 12.1667 3.66669H3.83334ZM4.33334 5.50002C4.60949 5.50002 4.83334 5.72388 4.83334 6.00002V8.50002C4.83334 9.05231 5.28106 9.50002 5.83334 9.50002C6.38563 9.50002 6.83334 9.05231 6.83334 8.50002V6.00002C6.83334 5.72388 7.0572 5.50002 7.33334 5.50002C7.60949 5.50002 7.83334 5.72388 7.83334 6.00002V8.50002C7.83334 9.60459 6.93791 10.5 5.83334 10.5C4.72877 10.5 3.83334 9.60459 3.83334 8.50002V6.00002C3.83334 5.72388 4.0572 5.50002 4.33334 5.50002ZM9.00001 5.50002C8.72387 5.50002 8.50001 5.72388 8.50001 6.00002V10C8.50001 10.2762 8.72387 10.5 9.00001 10.5C9.27615 10.5 9.50001 10.2762 9.50001 10V9.33335H10.5833C11.6419 9.33335 12.5 8.47523 12.5 7.41669C12.5 6.35814 11.6419 5.50002 10.5833 5.50002H9.00001ZM10.5833 8.33335H9.50001V6.50002H10.5833C11.0896 6.50002 11.5 6.91043 11.5 7.41669C11.5 7.92295 11.0896 8.33335 10.5833 8.33335Z" fill="#999999"></path></svg>
                    <span>{{items.user.nickname}}</span>
                  </span>
                </div>
              </a>
            </el-scrollbar >
            <div class="content-bottom">
                <a target="_blank" href="https://space.bilibili.com/235576894/favlist?fid=146944594&ftype=create&spm_id_from=333.1007.0.0" class="content-bottom__btn content-bottom__btn--view">查看全部 </a>
                <a target="_blank" href="//www.bilibili.com/list/ml146944594" class="content-bottom__btn content-bottom__btn--play">
                    <p>
                        <svg width="13" height="14" viewBox="0 0 13 14" fill="none" xmlns="http://www.w3.org/2000/svg" class="play-icon"><path fill-rule="evenodd" clip-rule="evenodd" d="M11.6068 6.27338C12.159 6.60035 12.159 7.3995 11.6068 7.72647L1.27458 13.8445C0.711718 14.1778 0 13.7721 0 13.118L0 0.881893C0 0.22776 0.711718 -0.177942 1.27458 0.155344L11.6068 6.27338Z" fill="#00AEEC"></path></svg>
                        <span class="play-text">播放全部</span>
                    </p>
                </a>
            </div>
        </div>
    </div>
  </div>
</template>

<style scoped>
    .v-popover.is-bottom {
    transform: translate3d(-50%,0,0);
    
}
.v-popover.is-bottom {
    top: 100%;
    left: 50%;
}
.v-popover {
    position: absolute;
    transition: .3s;
    z-index: 1;
}

.header-favorite-popover {
    overflow: hidden;
}

.v-popover-content {
    position: relative;
    background-color: #FFFFFF;
    box-shadow: 0 0 30px rgba(0,0,0,.1);
    border-radius: 8px;
    border: 1px solid #E3E5E7;
    color: #18191C;
}
.favorite-panel-popover {
    display: flex;
    width: 520px;
    text-align: left;
}

.v-popover-content a {
    text-decoration: none;
    background-color: transparent;
    color: inherit;
}
.favorite-panel-popover__nav {
    overflow-y: auto;
    flex-shrink: 0;
    box-sizing: border-box;
    padding: 12px 0;
    width: 150px;
    height: 540px;
    border-right: 1px solid #E3E5E7;
    overscroll-behavior: none;
}
.favorite-panel-popover__content {
    position: relative;
    flex: 1 auto;
    box-sizing: border-box;
    height: 540px;
    overflow: hidden;
    border-radius: 8px;
}
/* .favorite-panel-popover__content .content-scroll {
    overflow-y: auto;
    height: 493px;
    padding: 12px 0;
    overscroll-behavior: none;
} */


.favorite-panel-popover__content .header-fav-card {
    display: flex;
    padding: 10px 10px 10px 20px;
    transition: background-color .3s;
}

.favorite-panel-popover__content .header-fav-card:hover {
    background-color: #E3E5E7;
}

.favorite-panel-popover__content .header-fav-card__image {
    position: relative;
    margin-right: 10px;
    background: #F1F2F3;
    border-radius: 4px;
    width: 128px;
    height: 72px;
}

.favorite-panel-popover__content .header-fav-card__image .v-img {
    position: relative;
    flex-shrink: 0;
    width: 128px;
    height: 72px;
    border-radius: 4px;
}
.v-img {
    display: inline-block;
    line-height: 1;
    width: 100%;
    height: 100%;
    vertical-align: middle;
    background-color: #F1F2F3;
}
.favorite-panel-popover__content .header-fav-card__image .v-img img {
    border-radius: 4px;
}
.v-img img {
    display: block;
    width: 100%;
    height: 100%;
    object-fit: inherit;
}
.favorite-panel-popover__content .header-fav-card__duration {
    position: absolute;
    right: 4px;
    bottom: 4px;
    padding: 0;
    border-radius: 2px;
    background: rgba(0,0,0,.4);
    line-height: 17px;
}

.favorite-panel-popover__content .header-fav-card__duration--text {
    display: inline-block;
    color: #fff;
    font-size: 12px;
    line-height: 14px;
    transform: scale(.85);
    transform-origin: center top;
}


.favorite-panel-popover__content .header-fav-card__info {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.favorite-panel-popover__content .header-fav-card__info--title {
    height: 36px;
    color: #18191C;
    font-weight: 500;
    font-size: 14px;
    line-height: 18px;
    display: -webkit-box;
    overflow: hidden;
    -webkit-box-orient: vertical;
    text-overflow: -o-ellipsis-lastline;
    text-overflow: ellipsis;
    word-break: break-word!important;
    word-break: break-all;
    line-break: anywhere;
    -webkit-line-clamp: 2;
}

.favorite-panel-popover__content .content-bottom {
    position: absolute;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: space-around;
    width: 100%;
    border-top: 1px solid #F1F2F3;
}
.favorite-panel-popover__content .content-bottom__btn--view {
    border-right: 1px solid #E3E5E7;
}
.favorite-panel-popover__content .content-bottom__btn {
    flex: 1;
    height: 45px;
    color: #18191C;
    text-align: center;
    font-size: 14px;
    line-height: 45px;
}
.favorite-panel-popover__content .content-bottom__btn--play {
    color: #00AEEC;
    display: flex;
    align-items: center;
    justify-content: center;
}

.favorite-panel-popover__content .content-bottom__btn .play-icon {
    vertical-align: middle;
    margin-right: 12px;
    margin-top: -2px;
}
.favorite-panel-popover__content .header-fav-card__info--name {
    color: #9499A0;
    font-size: 12px;
    line-height: 16px;
    display: flex;
    align-items: center;
}
.favorite-panel-popover__content .header-fav-card__info--name .up-icon {
    margin-right: 5px;
    flex-shrink: 0;
}
.favorite-panel-popover__content svg{
    color: inherit;
}
.favorite-panel-popover__content span{
    color: inherit;
}





.favorite-panel-popover__nav .tab-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    height: 46px;
    color: #18191C;
    font-size: 14px;
    line-height: 22px;
    cursor: pointer;
    transition: background-color .3s,color .3s;
}

.favorite-panel-popover__nav .tab-item--active {
    background-color: #00AEEC;
    color: #fff;
}
.favorite-panel-popover__nav .tab-item__title {
    overflow: hidden;
    width: 85px;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-weight: 500;
}
.favorite-panel-popover__nav .tab-item--active .tab-item__num {
    color: #fff;
}
.favorite-panel-popover__nav .tab-item__num {
    color: #9499A0;
    word-break: normal;
    font-size: 12px;
    line-height: 20px;
}
</style>