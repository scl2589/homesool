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
  </div>
</template>

<script>
import { mapState, mapActions }from 'vuex'
export default {
  name: 'ChatPanel',
  comptued: {
    ...mapState('meetingStore', ['isChatPanel'])
  },
  methods: {
    ...mapActions('meetingStore', ['clickChatPanel']),
    closeChatPanel() {
      this.clickChatPanel(false)
    }
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