<template>
  <div class="panel">
    <div 
      class="selectgame h-100" 
      v-if="gameStatus==='1'"
    >
      <div 
        class="d-flex align-items-center justify-content-center h-100"
        v-if="notModeHost"
      >
        <p class="select-theme">
          <span class="color-yellow">{{ notModeHost.name }}</span>님이 주제를 고르는 중입니다.
        </p>
      </div>
      <div v-else>
        <div class="panel-title">
          <p>주제를 선택하세요</p>
        </div>
        <div class="buttons d-flex justify-space-around row no-gutters">
          <button
            class="btn-yellow rounded"
            @click="clickSendTheme('열대과일')"
          >
            열대과일
          </button>
          <button 
            class="btn-yellow rounded" 
            @click="clickSendTheme('야채')"
          >
            야채
          </button>
          <button 
            class="btn-yellow rounded" 
            @click="clickSendTheme('동물')"
          >
            동물
          </button>
          <button 
            class="btn-yellow rounded" 
            @click="clickSendTheme('나라')"
          >
            나라
          </button>
          <button 
            class="btn-yellow rounded" 
            @click="clickSendTheme('음식')"
          >
            음식
          </button>
          <button 
            class="btn-yellow rounded" 
            @click="clickSendTheme('영화')"
          >
            영화
          </button>
        </div>
      </div>
    </div>
    <div 
      class="startgame h-100 d-flex flex-column justify-content-center" 
      v-if="gameStatus===2"
    >
      <div v-if="notCurrentPlayer">
        <user-video 
          class="w-50 mt-3 video" 
          :stream-manager="currentPlayer" 
          :is-smile-game="true" 
        />
        <p class="turn">
          <span class="color-yellow">{{ notCurrentPlayer.stream.connection.data.slice(15, -2) }}</span>님의 차례입니다.
        </p>
        <p class="given-word">
          주어진 단어: <span class="color-yellow">{{ gameWord }}</span>
        </p>
      </div>
      <div v-else>
        <user-video 
          class="w-50 mt-3 video" 
          :stream-manager="currentPlayer" 
          :is-smile-game="true" 
        />
        <p class="read-word">
          <span class="color-yellow"><strong>{{ gameWord }}</strong></span>를 읽어주세요!
        </p>
      </div>
      <p class="no-smile color-gray">
        <small>웃으면 안됩니다!</small>
      </p>
      
    </div>
    <loser-panel
      class="w-100 d-flex justify-space-around align-items-center"
      v-if="gameStatus===3"
    />
  </div>
</template>

<script>
import Swal from 'sweetalert2'
import { mapState, mapActions, mapGetters } from "vuex";
import LoserPanel from "@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel";
import UserVideo from "@/components/meetingpage/UserVideo";

export default {
  name: "SmileLeadsToAlcohol",
  data() {
    return {
      checkSmile: null,
    };
  },
  components: {
    LoserPanel,
    UserVideo,
  },
  computed: {
    ...mapState("meetingStore", [
      "gameStatus",
      "selectedGame",
      "gameWord",
      "session",
      "currentPlayer"
    ]),
    ...mapGetters("meetingStore", ["notModeHost", "notCurrentPlayer"]),
  },
  watch: {
    gameStatus(value) {
      if (value == 2) {
        // 3초에 한 번씩 미소를 체크한다.
        this.checkSmile = setInterval(() => {
          if (!this.selectedGame) {
            // 게임 진행이 끝났다는 것이 감지되면 setInterval을 clear한다.
            clearInterval(this.checkSmile);
          } else {
            // 게임 진행 중이면 미소를 체크한다. (사진 찍어서 AI 서버에 보내는 작업)
            this.checkIsSmile();
          }
        }, 3000);
      // 게임이 끝났을 때 (벌칙자가 나왔을 때)
      } else if (value == 3) {
        clearInterval(this.checkSmile);
      }
    },
  },
  methods: {
    ...mapActions("meetingStore", [
      "sendGameRequest",
      "checkIsSmile",
    ]),
    clickSendTheme(theme) {
      Swal.fire({
        icon: 'info',
        title: '주제가 선택되었습니다.',
        showCancelButton: false,
        confirmButtonText: '확인',
        showLoaderOnConfirm: true,
      })
      .then((result) => {
        if (result.value) {
          var request = new Object();
          request.gameId = this.selectedGame;
          request.theme = theme;
          request.gameStatus = 2;
          var jsonRequest = JSON.stringify(request);
          this.sendGameRequest(jsonRequest);
        } 
      })
    }
  },

  beforeDestroy() {
    // 컴포넌트에서 나가도 interval이 걸려있는 경우를 생각해서 clearInterval을 건다.
    clearInterval(this.checkSmile);
    var request = new Object();
    request.gameStatus = 3;
    this.session.signal({
      data: JSON.stringify(request),
      to: [],
      type: "game",
    });
  },
};
</script>

<style scoped>
.panel {
  background-color: black;
  height: 100%;
  max-height: 46vh;
}

.panel-title {
  padding: 30px;
}

.panel-title > p {
  font-size: 2.2rem;
  color: white;
}

.select-theme {
  font-size: 1.8rem;
  color: white;
}

.btn-yellow {
  margin-bottom: 10px;
  opacity: 0.8;
  transition: 0.3s;
  width: 40%;
}

.btn-yellow:hover {
  opacity: 1;
}

.read-word {
  font-size: 1.8rem;
  color: white;
}

.no-smile {
  font-size: 1.5rem;
}

.video {
  margin-left: auto;
  margin-right: auto;
}

.turn {
  color: white;
  font-size: 1.2rem;
}

.given-word {
  color: white;
  font-size: 1.5rem;
}

.no-smile {
  font-family: 'Nanum Gothic', sans-serif;
  font-weight: 600;
}
</style>