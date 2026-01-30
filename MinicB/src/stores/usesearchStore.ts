import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSearchStore = defineStore('search', () => {
  // 状态
  const matchingCount = ref([])
  
  // getters
  const getMatchingCount = computed(() => matchingCount.value)
  
  // actions
  const setMatchingCount = (counts) => {
    matchingCount.value = counts
  }
  
  const updateMatchingCount = (index, value) => {
    if (matchingCount.value[index] !== undefined) {
      matchingCount.value[index] = value
    }
  }
  
  return {
    matchingCount,
    getMatchingCount,
    setMatchingCount,
    updateMatchingCount
  }
})