<template>
  <div class="chat-panel">
    <button
      class="btn close-btn"
      @click="closeChatPanel"
    >
      <i 
        class="fas fa-times"
        style="color:white;"
      ></i>
    </button>
    <!-- 채팅 보내기 -->
    <div class="pt-10">
      <input type="text" v-model="message" style="background-color:white">
      <button
        class="btn btn-yellow"
        @click="sendMessage(message)"
      >
        보내기
      </button>
    </div>
    <!-- 채팅 내역 -->
    <div id="chat-area" style="background-color:white">
      <div 
        v-for="(message, i) of messages"
        :key="i"
      >
        {{message.message}}
        {{message.sender}}
        {{message.sender.clientData}}
      </div>
      
    </div>
  </div>
</template>

<script>
import { mapState, mapActions }from 'vuex'
export default {
  name: 'ChatPanel',
  data() {
    return {
      message: ""
    }
  },
  computed: {
    ...mapState('meetingStore', ['isChatPanel', 'messages'])
  },
  methods: {
    ...mapActions('meetingStore', ['clickChatPanel', 'sendMessage', 'receiveMessage']),
    closeChatPanel() {
      this.clickChatPanel(false)
    },
  },
  mounted() {
    this.receiveMessage()
  }
}
</script>

<style scoped>
.chat-panel {
  position: relative;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
}

</style>