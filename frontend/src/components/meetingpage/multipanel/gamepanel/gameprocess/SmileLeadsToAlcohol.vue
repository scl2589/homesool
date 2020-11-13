<template>
    <div class="panel">
        <div class="selectgame" v-if="gameStatus==1">
          <div v-if="notModeHost">
            <p>{{ notModeHost.name }}님이 주제를 고르는 중입니다.</p>
          </div>
          <div v-else>
            <div class="panel-title">
                <p> 주제를 선택하세요 </p>
            </div>
            <div class="buttons">
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
                야 채
                </button>
            </div>
          </div>
        </div>
        <div class="startgame" v-if="gameStatus==2">
          <div v-if="notCurrentPlayer">
            <user-video
              class="w-50"
              :stream-manager="notCurrentPlayer"
            />
            <p>{{ notCurrentPlayer.stream.connection.data.slice(15,-2) }}님의 차례입니다.</p>
            <p>주어진 단어: {{ gameWord }}</p>
          </div>
          <div v-else>
            <p><strong>{{ gameWord }}</strong>를 읽어주세요!</p>
          </div>
          <small>웃으면 안됩니다!</small>
        </div>
        <loser-panel class="w-100 d-flex justify-content-center align-items-center" v-if="gameStatus == 3"/>
    </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';
import UserVideo from '@/components/meetingpage/UserVideo';

export default {
  name: 'SmileLeadsToAlcohol',
  data() {
    return {
      checkSmile: null
    }
  },
  components: {
    LoserPanel,
    UserVideo
  },
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame', 'gameWord', 'session']),
    ...mapGetters('meetingStore', ['notModeHost', 'notCurrentPlayer'])
  },
  watch: {
    gameStatus(value) {
      if (value == 2) {
        this.checkSmile = setInterval(() => {
          if (!this.selectedGame) {
            clearInterval(this.checkSmile);
          } else {
            this.checkIsSmile();
          }
        }, 3000);
      } else if (value == 3) {
        clearInterval(this.checkSmile);
      }
    }
  },
  methods:{
       ...mapActions("meetingStore", [
      "sendGameRequest",
      "checkIsSmile",
      "updateMainVideoStreamManager"
    ]),
    clickSendTheme(theme){
      alert("주제선택");
      var request = new Object();
      request.gameId=this.selectedGame;
      request.theme=theme;
      request.gameStatus=2;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    },
  },

  beforeDestroy() {
    clearInterval(this.checkSmile);
    var request = new Object();
    request.gameStatus=3;
    this.session.signal({
      data: JSON.stringify(request),
      to: [],
      type: 'game'
    })
    
  }
}
</script>

<style scoped>
.panel {
  background-color: black;
  height: 100%;
  max-height: 46vh;
}
.panel-title{
  padding : 30px;
}
p{
  color:yellow;
  font-size : 3.3rem;
}
</style>