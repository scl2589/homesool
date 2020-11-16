<template>
  <div>
    <div class="row no-gutters theme-background" style="height:91vh;">
      <!-- LeftPanel -->
      <div 
        id="capture"
        :class="{'col-8' : (isMultiPanel || isChatPanel), 'col-12' : !isMultiPanel && !isChatPanel, 'basic-theme' : theme === 'basic', 'christmas-theme' : theme === 'christmas', 'birthday-theme' : theme === 'birthday', 'spring-theme' : theme === 'spring'}"
      >
        <LeftPanel class="h-100"></LeftPanel>
      </div>

      <!-- RightPanel -->
      <div 
        class="right-panel"
        :class="{ 'col-4' : (isMultiPanel || isChatPanel), 'col-0' : !isMultiPanel && !isChatPanel }"
        v-if="isMultiPanel || isChatPanel"
      >
          <MultiPanel
            class="multi-panel d-flex align-items-center"
            :class="{ 'half-height' : isChatPanel, 'full-height' : !isChatPanel }"
            v-if="isMultiPanel"
          >
          </MultiPanel>
          <ChatPanel
            class="chat-panel"
            :class="{ 'half-height' : isMultiPanel, 'full-height' : !isMultiPanel }"
            v-if="isChatPanel"
          >
          </ChatPanel>
      </div>
    </div>

    <!-- Footer -->
    <div class="footer d-flex justify-content-between align-items-center w-100 px-2" style="height:9vh;">
      <!-- Theme BGM -->
      <div class="d-flex align-items-center">
        <button
          class="btn mr-1"
          @click="toggleBGM"
        >
          <img
            src="@/assets/images/bgm_on.png"
            alt="bgm_on"
            v-if="isPlaying"
          >
          <img
            src="@/assets/images/bgm_off.png"
            alt="bgm_off"
            v-else
          >
        </button>
        <input
          id="vol-control"
          type="range"
          min="0"
          max="100"
          step="1"
          value="10"
          @input="setVolume"
        >
      </div>

      <!-- Toggle Video -->
      <div>
        <button 
          class="btn mr-2"
          @click="clickMuteVideo"
        >
          <img 
            src="@/assets/images/webcam.png" 
            alt="webcam"
            v-if="publisher.stream.videoActive"
          >
          <img 
            src="@/assets/images/webcam_off.png" 
            alt="webcam_off"
            v-else
          >
        </button>

        <!-- Toggle Audio -->
        <button 
          class="btn mr-2"
          @click="clickMuteAudio"
        >
          <img 
            src="@/assets/images/voice.png" 
            alt="voice"
            v-if="publisher.stream.audioActive"
          >
          <img 
            src="@/assets/images/voice_off.png" 
            alt="voice_off"
            v-else
          >
        </button>

        <!-- ScreenShare -->
        <button 
          class="btn mr-2"
          @click="toggleShareScreen"
        >
          <img 
            src="@/assets/images/screenshare.png" 
            alt="screenshare"
            v-if="screenPublisher"
          >
          <img 
            src="@/assets/images/screenshare_off.png" 
            alt="screenshare"
            v-else
          >
        </button>

        <!-- Snapshot -->
        <button
          class="btn mr-2"
          @click="changeMode('snapshot')"
        >
          <img
            src="@/assets/images/snapshot.png"
            alt="snapshot"
          >
        </button>

        <!-- Fun Mode -->
        <div class="btn-group dropup">
          <button
            class="btn"
            type="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            <img
              src="@/assets/images/funmode.png"
              alt="funmode"
            >
          </button>
          <div class="dropdown-menu text-center">
            <li
              class="dropdown-item"
              @click="changeMode('game')"
            >
              술게임 모드
            </li>
            <li
              class="dropdown-item"
              @click="changeMode('singing')"
            >
              노래방 모드
            </li>
            <li
              class="dropdown-item"
              @click="changeMode('anonymous')"
            >
              진실의 방 모드
            </li>
          </div>
        </div>
      </div>

      <!-- Toggle Chat -->
      <div>
        <button
          class="btn mr-2"
          @click="toggleChatPanel"
        >
          <img
            src="@/assets/images/chat.png"
            alt="chat"
          >
        </button>
        
        <!-- meeting setting -->
        <div class="btn-group dropup">
          <button
            class="btn mr-2"
            type="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            <img
              src="@/assets/images/setting.png"
              alt="setting"
            >
          </button>
          <div class="dropdown-menu dropdown-menu-right text-center">
            <li 
              class="dropdown-item" 
              @click="clickChangeTheme"
            >
              테마 변경
            </li>
            <li
              class="dropdown-item"
              @click="clickCopyURL"
            >
              미팅 링크 복사
            </li>
            <li 
              class="dropdown-item"
              @click="leaveRoom"
            >
              미팅 나가기
            </li>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
const themeBGM = {
  basic: [
    'French Guitar Jazz V2.mp3',
    'Jazz.mp3',
    'jazz_20160226.mp3',
    'Easy Jazz.mp3',
    'The Jazz.mp3'
  ],
  christmas: [
    'christmas_1.wav',
    'O Holy Night.wav',
    'Slient Night.mp3',
    'Christmas Tale.mp3',
    'Christmas Jazz.mp3'
  ],
  birthday: [
    'birthday1.wav',
    'birthday2.wav',
    'birthday3.mp3',
    'birthday4.mp3',
    'birthday5.mp3'
  ],
  spring: [
    'spring1.mp3',
    'spring2.wav',
    'spring3.mp3',
    'spring4.mp3',
    'spring5.mp3'
  ]
};

let playList = themeBGM.basic;
let bgmIndex = 0;
let currentBGM = new Audio(require('@/assets/musics/French Guitar Jazz V2.mp3'));
currentBGM.volume = 0.1

currentBGM.addEventListener("ended", function() {
  bgmIndex++;
  if (bgmIndex >= 5) {
    bgmIndex = 0;
  }
  currentBGM.src = require('@/assets/musics/' + playList[bgmIndex]);
  currentBGM.play();
})

import { mapState, mapActions } from 'vuex' 
import Swal from 'sweetalert2'
import MultiPanel from '@/components/meetingpage/multipanel/MultiPanel'
import ChatPanel from '@/components/meetingpage/ChatPanel'
import LeftPanel from '@/components/meetingpage/LeftPanel'

export default {
  name: 'MeetingPage',

  components: {
    MultiPanel,
    ChatPanel,
    LeftPanel
  },

  data() {
    return {
      isPlaying: false
    }
  },

  computed: {
    ...mapState('meetingStore', [
      'isChatPanel',
      'theme',
      'mySessionId',
      'publisher',
      'screenPublisher',
      'currentMode',
      'isChatPanel',
      'messages',
    ]),
    isMultiPanel() {
      if (this.currentMode) {
        return true;
      } else {
        return false;
      }
    }
  },

  watch: {
    currentMode(value) {
      if (value === 'singing') {
        if (!currentBGM.paused) {
          currentBGM.pause();
          this.isPlaying = false;
        }
      }
    },
    theme(value) {
      if (currentBGM) {
        currentBGM.pause();
      }
      bgmIndex = 0;
      playList = themeBGM[value] 
      currentBGM.src = require('@/assets/musics/' + playList[bgmIndex]);
      if (this.isPlaying) {
        currentBGM.play();
      }
    },
    messages() {
      if (!this.isChatPanel) {
        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: false,
          onOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
          }
        })

        let newMessage = this.messages[this.messages.length - 1]
        Toast.fire({
          html: `<span style="color: #0764FF">${newMessage.sender}</span><span>님이 메시지를 보냈습니다</span>`
        })
      }
    }
  },

  methods: {
    ...mapActions('meetingStore', [
      'toggleChatPanel',
      'changeTheme',
      'leaveSession',
      'clickMuteVideo',
      'clickMuteAudio',
      'startShareScreen',
      'stopShareScreen',
      'changeMode',
      'changeIsNewbie'
    ]),
    toggleBGM() {
      if (currentBGM.paused) {
        currentBGM.play()
        this.isPlaying = true;
      } else {          
        currentBGM.pause()
        this.isPlaying = false;
      }
    },
    setVolume(e) {
      currentBGM.volume = e.target.value / 100;
    },
    clickChangeTheme() {
      Swal.fire({
        title: '테마를 골라주세요 :)',
        text: "테마에 맞춰 배경 이미지와 음악이 바뀝니다!",
        input: 'select',
        inputOptions: {
          basic: '기본',
          spring: '봄',
          // summer: '여름',
          // fall: '가을',
          // winter: '겨울',
          christmas: '크리스마스',
          birthday: '생일'
        },
        inputPlaceholder: '테마',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        center: true
      }).then((result) => {
        if (result.isConfirmed && result.value) {
          this.changeTheme(result.value);
          Swal.fire(
            '테마 변경!',
            `테마가 ${result.value}(으)로 변경 되었습니다.`,
            'success'
          )
        }
      })
    },
    leaveRoom() {
      this.$router.push({ name: 'HomePage' });
    },
    clickCopyURL() {
      const copyText = document.createElement("input");
      copyText.value = `https://k3a503.p.ssafy.io/meet/${this.mySessionId}`
      document.body.appendChild(copyText)
      copyText.select();
      document.execCommand("copy");
      document.body.removeChild(copyText)
      Swal.fire({
          icon: 'success',
          html: `<p>https://k3a503.p.ssafy.io/meet/${this.mySessionId}</p><h5>주소가 복사되었습니다</h5>`
        })
    },
    toggleShareScreen() {
      if (this.screenPublisher) {
        Swal.fire({
          html: "화면공유를 중단 하시겠습니까?",
          showCancelButton: true,
          confirmButtonText: '네',
          cancelButtonText: '아니요',
          icon: "warning",
        })
        .then((result) => {
          if (result.value) {
            this.stopShareScreen();
          }
        });
      } else {
        Swal.fire({
          html: "화면공유를 시작 하시겠습니까?",
          showCancelButton: true,
          confirmButtonText: '네',
          cancelButtonText: '아니요',
          icon: "warning",
        })
        .then((result) => {
          if (result.value) {
            this.startShareScreen();
          }
        });
      }
    }
  },

  beforeMount() {
    window.addEventListener('beforeunload', this.leaveSession);
  },

  mounted() {
    setTimeout(() => {
      this.changeIsNewbie();
    }, 3000);
  },

  beforeRouteLeave (to, from, next) {
    Swal.fire({
      html: "술자리에서 나가시겠습니까?",
      showCancelButton: true,
      confirmButtonText: '네',
      cancelButtonText: '아니요',
      icon: "warning",
    })
    .then((result) => {
      if (result.value) {
        this.leaveSession();
        next();
      }
    });
  },

  beforeDestroy() {
    if (!currentBGM.paused) {
      currentBGM.pause();
    }
    if (this.isChatPanel) {
      this.toggleChatPanel();
    }
    window.removeEventListener('beforeunload', this.leaveSession);
  }
}
</script>

<style scoped>
.basic-theme {
  background-image: 
    linear-gradient(to bottom, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),
    url('../assets/images/basic_back.png');
  background-size: contain;
  background-repeat: repeat;
}

.christmas-theme {
  background-image: 
    linear-gradient(to bottom, rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3)),
    url('../assets/images/christmas_back.png');
  background-size: contain;
  background-repeat: repeat;
}

.birthday-theme {
  background-image: 
    linear-gradient(to bottom, rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.1)),
    url('../assets/images/birthday_back.png');
  background-size: contain;
  background-repeat:repeat;
}

.spring-theme {
  background-image: 
    linear-gradient(to bottom, rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3)),
    url('../assets/images/spring_back.jpg');
  background-size: contain;
  background-repeat:repeat;
}


.right-panel {
  background-color: black;
  height: 100%;
}

.multi-panel {
  background-color: #232323;
  max-width: 100%;
  border: 5px solid #606060;
  border-radius: 10px;
}

.chat-panel {
  background-color: #232323;
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

/* slider css */
input[type=range] {
  height: 28px;
  -webkit-appearance: none;
  margin: 10px 0;
  width: 100%;
}

input[type=range]:focus {
  outline: none;
}

input[type=range]::-webkit-slider-runnable-track {
  width: 100%;
  height: 12px;
  cursor: pointer;
  animate: 0.2s;
  box-shadow: 0px 0px 0px #000000;
  background: #FCFFD6;
  border-radius: 25px;
  border: 1px solid #8A8A8A;
}

input[type=range]::-webkit-slider-thumb {
  box-shadow: 1px 1px 1px #828282;
  border: 1px solid #8A8A8A;
  height: 20px;
  width: 28px;
  border-radius: 6px;
  background: #F6C863;
  cursor: pointer;
  -webkit-appearance: none;
  margin-top: -5px;
}

input[type=range]:focus::-webkit-slider-runnable-track {
  background: #FCFFD6;
}

input[type=range]::-moz-range-track {
  width: 100%;
  height: 12px;
  cursor: pointer;
  animate: 0.2s;
  box-shadow: 0px 0px 0px #000000;
  background: #FFCF67;
  border-radius: 25px;
  border: 1px solid #8A8A8A;
}

input[type=range]::-moz-range-thumb {
  box-shadow: 1px 1px 1px #828282;
  border: 1px solid #8A8A8A;
  height: 20px;
  width: 28px;
  border-radius: 6px;
  background: #F6C863;
  cursor: pointer;
}

input[type=range]::-ms-track {
  width: 100%;
  height: 12px;
  cursor: pointer;
  animate: 0.2s;
  background: transparent;
  border-color: transparent;
  color: transparent;
}

input[type=range]::-ms-fill-lower {
  background: #FFCF67;
  border: 1px solid #8A8A8A;
  border-radius: 50px;
  box-shadow: 0px 0px 0px #000000;
}

input[type=range]::-ms-fill-upper {
  background: #FFCF67;
  border: 1px solid #8A8A8A;
  border-radius: 50px;
  box-shadow: 0px 0px 0px #000000;
}

input[type=range]::-ms-thumb {
  margin-top: 1px;
  box-shadow: 1px 1px 1px #828282;
  border: 1px solid #8A8A8A;
  height: 20px;
  width: 28px;
  border-radius: 6px;
  background: #F6C863;
  cursor: pointer;
}

input[type=range]:focus::-ms-fill-lower {
  background: #FFCF67;
}

input[type=range]:focus::-ms-fill-upper {
  background: #FFCF67;
}
</style>