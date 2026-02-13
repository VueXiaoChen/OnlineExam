<script setup lang="ts">
import { useMessageStore } from '@/stores/useMessageStore'  
import { ref, onMounted, onUnmounted, watch, nextTick, computed } from 'vue'


const MessageStore = useMessageStore()




  // 生命周期
  onMounted(() => {
    MessageStore.connectWebSocket()
  })
</script>

<template>
<RouterView></RouterView>
</template>

<style scoped>

</style>
