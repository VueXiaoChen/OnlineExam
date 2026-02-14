// stores/message.js
import { defineStore } from 'pinia'
import { ref,computed  } from 'vue'
import { ElMessage } from 'element-plus'
import { get } from '@/network/request'
import { useUserStore } from '@/stores/useUserStore'
import { useHeaderStore } from '@/stores/headerStore'
import axios from 'axios'
export const useMessageStore = defineStore('message', () => {
  // 状态
  const msgUnread = ref([0, 0, 0, 0, 0, 0])
  const chatList = ref([])
  const ws = ref(null)
  
  // 其他 store
  const userStore = useUserStore()
  const headerStore = useHeaderStore()
  // actions
  const initMessageData = () => {
    msgUnread.value = [0, 0, 0, 0, 0, 0]
    chatList.value = []
  }
  
  const updateChatList = (newChatList) => {
    chatList.value.push(...newChatList)
  }
  
  const setWebSocket = (websocket) => {
    ws.value = websocket
  }
  
  const handleWsOpen = () => {
    console.log("实时通信websocket已建立")
  }
  
  const handleWsClose = () => {
    console.log("实时通信websocket关闭,请登录并刷新页面重试")
  }
  
  const handleWsMessage = (e) => {
    const data = JSON.parse(e.data)
    console.log("通信数据",data);
    
    switch (data.type) {
      case "error": {
        if (data.data === "登录已过期") {
          userStore.initData()
          localStorage.removeItem("user_stores")
        }
        ElMessage.error(data.data)
        break
      }
      case "reply": {
        let content = data.data
        switch (content.type) {
          case "全部已读": {
            msgUnread.value[0] = 0
            break
          }
          case "接收": {
            msgUnread.value[0]++
            break
          }
        }
        break
      }
      case "at": {
        let content = data.data
        switch (content.type) {
          case "全部已读": {
            msgUnread.value[1] = 0
            break
          }
          case "接收": {
            msgUnread.value[1]++
            break
          }
        }
        break
      }
      case "love": {
        let content = data.data
        switch (content.type) {
          case "全部已读": {
            msgUnread.value[2] = 0
            break
          }
          case "接收": {
            msgUnread.value[2]++
            break
          }
        }
        break
      }
      case "system": {
        let content = data.data
        switch (content.type) {
          case "全部已读": {
            msgUnread.value[3] = 0
            break
          }
          case "接收": {
            msgUnread.value[3]++
            break
          }
        }
        break
      }
      case "whisper": {
        let content = data.data
        switch (content.type) {
          case "全部已读": {
            msgUnread.value[4] = 0
            chatList.value.forEach(item => {
              item.chat.unread = 0
            })
            break
          }
          case "已读": {
            const chatid = content.id
            const count = content.count
            msgUnread.value[4] = Math.max(0, msgUnread.value[4] - count)
            let chat = chatList.value.find(item => item.chat.id === chatid)
            if (chat) {
              chat.chat.unread = 0
            }
            break
          }
          case "移除": {
            const chatid = content.id
            const count = content.count
            msgUnread.value[4] = Math.max(0, msgUnread.value[4] - count)
            let i = chatList.value.findIndex(item => item.chat.id === chatid)
            if (i !== -1) {
              // 如果是当前聊天先关闭窗口
              if (chatList.value[i].user.uid === userStore.chatId) {
                userStore.chatId = -1
              }
              chatList.value.splice(i, 1)
            }
            break
          }
          case "接收": {
            const chat = content.chat
            const detail = content.detail
            const user = content.user
            
            const sortByLatestTime = list => {
              list.sort((a, b) => {
                const timeA = new Date(a.chat.latestTime).getTime()
                const timeB = new Date(b.chat.latestTime).getTime()
                return timeB - timeA
              })
            }
            
            if (detail.userId === userStore.user.uid) {
              // 如果发送方是自己
              let chatItem = chatList.value.find(item => item.chat.userId === detail.anotherId)
              if (chatItem && userStore.isChatPage) {
                chatItem.detail.list.push(detail)
                chatItem.chat.latestTime = chat.latestTime
                sortByLatestTime(chatList.value)
              }
            } else {
              // 如果发送方是别人
              if (!content.online) {
                msgUnread.value[4]++
              }
              
              let chatItem = chatList.value.find(item => item.chat.userId === detail.userId)
              if (chatItem) {
                chatItem.detail.list.push(detail)
                chatItem.chat = chat
                sortByLatestTime(chatList.value)
              } else {
                chatItem = {
                  chat: chat,
                  user: user,
                  detail: {
                    more: true,
                    list: []
                  }
                }
                chatItem.detail.list.push(detail)
                chatList.value.unshift(chatItem)
              }
            }
            break
          }
          case "撤回": {
            const msgId = content.id
            const sendId = content.sendId
            const acceptId = content.acceptId
            let chat
            if (sendId === userStore.user.uid) {
              chat = chatList.value.find(item => item.chat.userId === acceptId)
            } else {
              chat = chatList.value.find(item => item.chat.userId === sendId)
            }
            if (chat) {
              let msg = chat.detail.list.find(item => item.id === msgId)
              if (msg) {
                msg.withdraw = 1
              }
            }
            break
          }
        }
        break
      }
      case "dynamic": {
        let content = data.content
        switch (content.type) {
          case "全部已读": {
            msgUnread.value[5] = 0
            break
          }
          case "接收": {
            msgUnread.value[5]++
            break
          }
        }
        break
      }
    }
  }
  
  const handleWsError = (e) => {
    console.log("实时通信websocket报错: ", e)
  }
  
  // 获取全部未读消息数
  const getMsgUnread = async () => {
    const res = await axios.get("/api/msgUnread/msg-unread/all/"+userStore.user.uid, {
      
    })
    const data:any = res.data.data
    headerStore.msgUnread[0] = data.reply
    headerStore.msgUnread[1] = data.at
    headerStore.msgUnread[2] = data.love
    headerStore.msgUnread[3] = data.system
    headerStore.msgUnread[4] = data.whisper
    headerStore.msgUnread[5] = data.dynamic

    msgUnread.value[0] = data.reply
    msgUnread.value[1] = data.at
    msgUnread.value[2] = data.love
    msgUnread.value[3] = data.system
    msgUnread.value[4] = data.whisper
    msgUnread.value[5] = data.dynamic
 
    
  }
  
  // 初始化websocket实例
  const connectWebSocket = () => {
    return new Promise((resolve) => {
      if (ws.value) {
        ws.value.close()
        ws.value = null
      }
      
    
      //const wsBaseUrl = import.meta.env.VITE_APP_WS_IM_URL || process.env.VUE_APP_WS_IM_URL
      //const websocket = new WebSocket(`${wsBaseUrl}/ws/`+ localStorage.getItem('token'))
      const websocket = new WebSocket(`ws://127.0.0.1:8080/ws/`+ JSON.parse(localStorage.getItem('user_stores')).token)
      
      ws.value = websocket
      
      websocket.addEventListener('open', () => {
        handleWsOpen()
        resolve()
      })
      
      websocket.addEventListener('close', () => handleWsClose())
      websocket.addEventListener('message', e => handleWsMessage(e))
      websocket.addEventListener('error', e => handleWsError(e))
    })
  }
  
  // 关闭后清空 WebSocket 实例
  const closeWebSocket = async () => {
    if (ws.value) {
      await ws.value.close()
      ws.value = null
    }
  }
  
  return {
    // 状态
    msgUnread,
    chatList,
    ws,
    
    // actions
    initMessageData,
    updateChatList,
    setWebSocket,
    handleWsOpen,
    handleWsClose,
    handleWsMessage,
    handleWsError,
    getMsgUnread,
    connectWebSocket,
    closeWebSocket,
    
  }
})