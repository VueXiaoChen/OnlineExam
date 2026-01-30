import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCommentStore = defineStore('comment', () => {
  // 状态
  const likeComment = ref<number[]>([])  // 用户点赞的评论ID列表
  const dislikeComment = ref<number[]>([]) // 用户点踩的评论ID列表
  
  // getters
  const getLikeComment = computed(() => likeComment.value)
  const getDislikeComment = computed(() => dislikeComment.value)
  
  // actions
  const updateLikeComment = (likes: number[]) => {
    likeComment.value = likes
  }
  
  const updateDislikeComment = (dislikes: number[]) => {
    dislikeComment.value = dislikes
  }
  
  const addLike = (commentId: number) => {
    if (!likeComment.value.includes(commentId)) {
      likeComment.value.push(commentId)
    }
  }
  
  const removeLike = (commentId: number) => {
    likeComment.value = likeComment.value.filter(id => id !== commentId)
  }
  
  const addDislike = (commentId: number) => {
    if (!dislikeComment.value.includes(commentId)) {
      dislikeComment.value.push(commentId)
    }
  }
  
  const removeDislike = (commentId: number) => {
    dislikeComment.value = dislikeComment.value.filter(id => id !== commentId)
  }
  
  const isLiked = (commentId: number) => {
    return likeComment.value.includes(commentId)
  }
  
  const isDisliked = (commentId: number) => {
    return dislikeComment.value.includes(commentId)
  }
  
  const clearAll = () => {
    likeComment.value = []
    dislikeComment.value = []
  }
  
  return {
    likeComment,
    dislikeComment,
    getLikeComment,
    getDislikeComment,
    updateLikeComment,
    updateDislikeComment,
    addLike,
    removeLike,
    addDislike,
    removeDislike,
    isLiked,
    isDisliked,
    clearAll
  }
})