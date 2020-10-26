<template>
  <div>
    <div class="row no-gutters" style="height:91vh;">
      <div 
        class="left-panel" 
        :class="{'col-8' : (isMultiPanel || isChatPanel), 'col-12' : !isMultiPanel && !isChatPanel }"
      >

      </div>

      <div 
        class="right-panel" 
        :class="{ 'col-4' : (isMultiPanel || isChatPanel), 'col-0' : !isMultiPanel && !isChatPanel }"
        v-if="isMultiPanel || isChatPanel"
      >
          <div
            class="multi-panel"
            :class="{ 'half-height' : isChatPanel, 'full-height' : !isChatPanel }"
            v-if="isMultiPanel"
          >
            
          </div>

          <div
            class="chat-panel"
            :class="{ 'half-height' : isMultiPanel, 'full-height' : !isMultiPanel }"
            v-if="isChatPanel"
          >

          </div>

      </div>
    </div>


    <div class="footer d-flex justify-content-between align-items-center w-100 px-2" style="height:9vh;">
      <div>
        <button class="btn">
          <img src="@/assets/images/bgm.png" alt="bgm">
        </button>
      </div>

      <div>
        <button class="btn mr-2">
          <img src="@/assets/images/webcam.png" alt="webcam">
        </button>
        <button class="btn mr-2">
          <img src="@/assets/images/voice.png" alt="voice">
        </button>
        <button class="btn mr-2">
          <img src="@/assets/images/screenshare.png" alt="screenshare">
        </button>
        <button class="btn mr-2" @click="startSnapshotMode">
          <img src="@/assets/images/snapshot.png" alt="snapshot">
        </button>
        <div class="btn-group dropup">
          <button type="button" class="btn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <img src="@/assets/images/funmode.png" alt="funmode">
          </button>
          <div class="dropdown-menu text-center">
            <li class="dropdown-item" @click="startGameMode">술게임 모드</li>
            <li class="dropdown-item" @click="startMusicMode">노래방 모드</li>
            <li class="dropdown-item" @click="startAnonymousMode">진실의 방 모드</li>
          </div>
        </div>
      </div>

      <div>
        <button class="btn mr-2" @click="clickChatMode">
          <img src="@/assets/images/chat.png" alt="chat">
        </button>
        <div class="btn-group dropup">
          <button type="button" class="btn mr-2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <img src="@/assets/images/setting.png" alt="setting">
          </button>
          <div class="dropdown-menu dropdown-menu-right text-center">
            <li class="dropdown-item">테마 변경</li>
            <li class="dropdown-item">미팅 링크 복사</li>
            <li class="dropdown-item">미팅 나가기</li>
          </div>
        </div>
      </div>
      
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex' 
export default {
  name: 'MeetingPage',
  data() {
    return {
      isChatPanel: false
    }
  },
  computed: {
    ...mapState('meetingStore', ['isGameMode', 'isMusicMode', 'isAnonymousMode', 'isSnapshotMode']),
    isMultiPanel() {
      if (this.isGameMode || this.isMusicMode || this.isAnonymousMode || this.isSnapshotMode) {
        return true
      } else {
        return false
      }
    }
  },
  methods: {
    ...mapActions('meetingStore', ['startGameMode', 'startMusicMode', 'startAnonymousMode', 'startSnapshotMode']),
    clickChatMode() {
      this.isChatPanel = !this.isChatPanel
    }
  }
}
</script>

<style scoped>
.left-panel {
  background-image: 
    linear-gradient(to bottom, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),
    url('../assets/images/basic_back.png');
  background-size: contain;
  background-repeat: repeat;
}

.right-panel {
  background-color: yellow;
}

.multi-panel {
  background-color: green;
  max-width: 100%;
}

.chat-panel {
  background-color: blue;
  max-width: 100%;
}

.footer {
  background-color: #D1D1D1;
  height: 20vh;
}

.half-height {
  height: 50%;
}

.full-height {
  height: 100%;
}

.btn {
  padding: 0;
}

.btn > img {
  width: 7vh;
  height: auto;
}
</style>