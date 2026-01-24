// stores/app.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { get } from '@/network/request'

export const useAppStore = defineStore('app', () => {
  // 状态
  const isLoading = ref(false)
  const openLogin = ref(false)
  const isChatPage = ref(false)
  const chatId = ref(-1)
  const trendings = ref([])
  const matchingCount = ref([0, 0])
  
  // 计算属性
  const isChatting = computed(() => chatId.value !== -1)
  
  // actions
  const setOpenLogin = (value) => {
    openLogin.value = value
  }
  
  const setLoading = (value) => {
    isLoading.value = value
  }
  
  const setChatPage = (value) => {
    isChatPage.value = value
  }
  
  const setChatId = (value) => {
    chatId.value = value
  }
  
  const updateTrendings = (newTrendings) => {
    trendings.value = newTrendings
  }
  
  const updateMatchingCount = (newCount) => {
    matchingCount.value = newCount
  }
  
  return {
    // 状态
    isLoading,
    openLogin,
    isChatPage,
    chatId,
    trendings,
    matchingCount,
    
    // 计算属性
    isChatting,
    
    // actions
    setOpenLogin,
    setLoading,
    setChatPage,
    setChatId,
    updateTrendings,
    updateMatchingCount,
  }
})