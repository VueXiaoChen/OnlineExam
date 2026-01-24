// stores/video.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useVideoStore = defineStore('video', () => {
  // 状态
  const danmuList = ref([])
  const attitudeToVideo = ref({})
  const likeComment = ref([])
  const dislikeComment = ref([])
  const favorites = ref([])
  const userFavList = ref([])
  
  // actions
  const initVideoData = () => {
    attitudeToVideo.value = {}
    likeComment.value = []
    dislikeComment.value = []
    favorites.value = []
    userFavList.value = []
  }
  
  const updateDanmuList = (newDanmuList) => {
    danmuList.value = newDanmuList
  }
  
  const updateAttitudeToVideo = (atv) => {
    attitudeToVideo.value = atv
  }
  
  const updateLikeComment = (lc) => {
    likeComment.value = lc
  }
  
  const updateDislikeComment = (dlc) => {
    dislikeComment.value = dlc
  }
  
  const updateFavorites = (newFavorites) => {
    favorites.value = newFavorites
  }
  
  const updateUserFavList = (newUserFavList) => {
    userFavList.value = newUserFavList
  }
  
  return {
    // 状态
    danmuList,
    attitudeToVideo,
    likeComment,
    dislikeComment,
    favorites,
    userFavList,
    
    // actions
    initVideoData,
    updateDanmuList,
    updateAttitudeToVideo,
    updateLikeComment,
    updateDislikeComment,
    updateFavorites,
    updateUserFavList,
  }
})