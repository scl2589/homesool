<template>
  <div class="panel drunken">
    <div class="h-100 d-flex justify-content-center align-items-center flex-column selectgame" v-if="gameStatus == 1">
      <div class="h-100 d-flex justify-content-center align-items-center" v-if="notModeHost">
        <p class="select-participant">
          <span class="color-yellow">{{ notModeHost.name }}</span>님이 참가자를 고르는 중입니다.
        </p>
      </div>
      <div class="p-3 h-100" v-else>
        <div class="d-flex justify-content-center align-items-center">
          <p class="select-participant">참가자를 선택해주세요</p>
        </div>
        <div class="d-flex justify-space-around  row no-gutters mt-5">
            <button 
              class="btn-yellow rounded"
              @click="clickParticipant(publisher.stream.connection.connectionId)"
            >
              {{ publisher.stream.connection.data.slice(15, -2)}}
            </button>
             <button 
              class="btn-yellow rounded"
              @click="clickParticipant(subscriber.stream.connection.connectionId)"
              v-for="(subscriber, id) in subscribers" 
              :key="id"
            >
              {{ subscriber.stream.connection.data.slice(15, -2)}}
            </button>
          </div>
      </div>
    </div>
    <div class="h-100" v-if="gameStatus == 2">
      <div class="p-3 h-100 d-flex justify-content-between flex-column">
        <div class="panel-title" v-if="notCurrentPlayer">
          <user-video
            class="w-50 video"
            :stream-manager="notCurrentPlayer"
          />
          <p><span class="color-yellow">{{ notCurrentPlayer.stream.connection.data.slice(15,-2) }}</span>님이 문장을 읽는 중입니다.</p>
        </div>
        <div v-else class="panel-title">
          <p><span class="color-yellow">{{ publisher.stream.connection.data.slice(15,-2) }}</span>님 문장을 바로 읽어주세요.</p>
        </div>
        <div>
          <p class="sentence">{{sentence}}</p>
        </div>
        <div>
          <p v-if="drunkenText">{{drunkenText}}</p>
        </div>
      </div>
    </div>

    <loser-panel class="w-100 d-flex justify-content-center align-items-center" v-if="gameStatus == 3"/>

  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";
import UserVideo from '@/components/meetingpage/UserVideo';
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';

export default {
  name: "GamePanel",
  components: {
    UserVideo,
    LoserPanel
  },
  computed: {
    ...mapState("meetingStore", [
      "gameStatus",
      "selectedGame",
      "subscribers",
      "publisher",
      "sentence",
      "drunkenText",
    ]),
    ...mapGetters("meetingStore", ["notModeHost", "notCurrentPlayer"]),
  },
  data() {
    return {
      picked: null,
    };
  },
  watch: {
    drunkenText: function (val) {
      if (val) {
        this.sendResult()
      }
    }
  },
  methods: {
    ...mapActions("meetingStore", ["sendGameRequest"]),
    clickParticipant(id) {
      var request = new Object();
      request.gameId = 5;
      request.participantPublicId = id;
      request.gameStatus = 2;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    },
    sendResult( ){
      var request = new Object();
      request.gameId = 5;
      request.gameStatus = 2;
      request.sentence = this.drunkenText;
      request.participantPublicId = this.publisher.stream.connection.connectionId;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest)
    }
  },
};
</script>

<style scoped>

.drunken {
  height: 100%;
  max-height: 45vh;
  background-color: black;
  border-left: 1px solid #707070;
  border-radius: 15px;
}

.panel {
  background-color: black;
  height: 100%;
  max-height: 46vh;
}
.panel-title {
  font-size: 2rem;
  color: yellow;
}

p, h1, h2, h3, h4, h5, h6, button {
  color: white;
}

.select-participant {
  font-size: 2.2rem;
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

.sentence {
  font-size: 1.0rem;
}

.video {
  margin-left: auto;
  margin-right: auto;
}
</style>