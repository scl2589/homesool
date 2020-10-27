<template>
  <div>
    <div class="row no-gutters theme-background" style="height:91vh;">
      <div 
        id="capture"
        class="left-panel" 
        :class="{'col-8' : (isMultiPanel || isChatPanel), 'col-12' : !isMultiPanel && !isChatPanel }"
      >
        <LeftPanel></LeftPanel>
      </div>

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


    <div class="footer d-flex justify-content-between align-items-center w-100 px-2" style="height:9vh;">
      <div class="d-flex align-items-center">
        <button class="btn mr-1" @click="clickBGM">
          <img v-if="playing" src="@/assets/images/bgm_on.png" alt="bgm_on">
          <img v-else src="@/assets/images/bgm_off.png" alt="bgm_off">
        </button>
        <input id="vol-control" type="range" min="0" max="100" step="1" @input="setVolume">
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
            <li class="dropdown-item" @click="startSingingMode">노래방 모드</li>
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
            <li class="dropdown-item" @click="clickChangeTheme">테마 변경</li>
            <li class="dropdown-item">미팅 링크 복사</li>
            <li class="dropdown-item">미팅 나가기</li>
          </div>
        </div>
      </div>
      
    </div>
  </div>
</template>

<script>
// Background Music
let basic = ['French Guitar Jazz V2.mp3', 'Jazz.mp3', 'jazz_20160226.mp3', 'Easy Jazz.mp3', 'The Jazz.mp3'];
let christmas = ['christmas_1.wav', 'O Holy Night.wav', 'Slient Night.mp3', 'Christmas Tale.mp3', 'Christmas Jazz.mp3'];
let playList = basic;
let currentSong = 0;
let song = new Audio(require('@/assets/musics/French Guitar Jazz V2.mp3'));

song.addEventListener("ended", function() {
  currentSong++;
  if (currentSong >= playList.length) {
    currentSong = 0;
  }
  song.src = require('@/assets/musics/' + playList[currentSong]);
  song.play();
})

import Swal from 'sweetalert2'
import { mapState, mapActions } from 'vuex' 
import MultiPanel from '@/components/meetingpage/multipanel/MultiPanel'
import ChatPanel from '@/components/meetingpage/ChatPanel'
import LeftPanel from '@/components/meetingpage/LeftPanel'

export default {
  name: 'MeetingPage',
  data() {
    return {
      playing: false,
    }
  },
  components: {
    MultiPanel,
    ChatPanel,
    LeftPanel
  },
  computed: {
    ...mapState('meetingStore', ['isGameMode', 'isSingingMode', 'isAnonymousMode', 'isSnapshotMode', 'isChatPanel']),
    isMultiPanel() {
      if (this.isGameMode || this.isSingingMode || this.isAnonymousMode || this.isSnapshotMode) {
        return true
      } else {
        return false
      }
    }
  },
  methods: {
    ...mapActions('meetingStore', ['startGameMode', 'startSingingMode', 'startAnonymousMode', 'startSnapshotMode', 'clickChatPanel']),
    clickChatMode() {
      if (this.isChatPanel === true) {
        this.clickChatPanel(false)
      } else {
        this.clickChatPanel(true)
      }
    },
    clickBGM() {
      if (song.paused) {
        song.play()
        this.playing = true;
      } else {          
        song.pause()
        this.playing = false;
      }
    },
    setVolume(e) {
      song.volume = e.target.value / 100;
    },
    clickChangeTheme() {
      Swal.fire({
        title: '테마를 골라주세요 :)',
        text: "테마에 맞춰 배경 이미지와 음악이 바뀝니다!",
        input: 'select',
        inputOptions: {
          basic: '기본',
          christmas: '크리스마스'
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
    changeTheme(theme) {
      if (song) {
        song.pause();
      }
      currentSong = 0;
      if (theme == 'christmas') {
        playList = christmas;
        song.src = require('@/assets/musics/' + playList[currentSong]);
        if (this.playing) {
          song.play();
        }
      } else if (theme == 'basic') {
        playList = basic;
        song.src = require('@/assets/musics/' + playList[currentSong]);
        if (this.playing) {
          song.play();
        }
      }
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
  background-color: black;
}

.multi-panel {
  background-color: #232323;
  max-width: 100%;
  border: 5px solid #606060;
  border-radius: 10px;
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