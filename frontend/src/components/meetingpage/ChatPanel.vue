<template>
  <div class="chat-panel">
    <div class="chat-box p-2 d-flex flex-column h-100">      
      <div class="header text-left">
        <span class="title">
          채팅
        </span>
        <button class="btn close-btn" @click="closeChatPanel">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <!-- 채팅 내역 -->
        <div id="chat-area">
          <div 
            class="mt-2 text-left message" 
            v-for="(message, i) of messages" 
            :key="i"
          >
            <div class="message-title">
              <span class="mr-2">{{ message.sender }}</span>
              <span>{{ message.time }}</span>
            </div>
            <div>
              {{ message.message }}
            </div>
          </div>
        </div>
        <div class="footer d-flex mt-auto">
          <div class="col-10 px-1 py-0">
            <input 
              @keyup.enter="clickSendMessage(message)"
              class="text-box"
              v-model="message"
            >
          </div>
          <div class="col-2 p-0">
            <button
              class="send-btn"
              @click="clickSendMessage(message)"
            >
              <i class="fas fa-paper-plane"></i>
            </button>
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  name: "ChatPanel",
  data() {
    return {
      message: "",
    };
  },
  computed: {
    ...mapState("meetingStore", ["isChatPanel", "messages"]),
  },
  methods: {
    ...mapActions("meetingStore", [
      "clickChatPanel",
      "sendMessage",
    ]),
    closeChatPanel() {
      this.clickChatPanel(false);
    },
    clickSendMessage() {
      this.sendMessage(this.message)
      this.message = ""
    }
  },
};
</script>


<style scoped>
.chat-box {
  height: 100%;
}

.close-btn {
  position: absolute;
  color: white;
  top: 10px;
  right: 10px;
}

.text-box {
  background-color: #D1D1D1;
  width: 100%;
  border-radius: 20px;
  color: black;
}

.title {
  padding-left: 5%;
  font-family: 'Jua' !important;
  font-size: 1.0rem !important;
  color: white;
}

.header {
  width: 100%;
  border-radius: 20px;
  box-shadow: 3px 3px 3px rgb(0, 0, 0, 0.3);
  height: 4vh;
}
.message-title {
  font-size: 0.8rem;
}

.message {
  color: white;
}

.send-btn {
  color: white;
}

#chat-area {
  height: 80% !important;
  overflow-y: auto;
}
</style>