<template>
  <div class="panel drunken">
    <div class="selectgame" v-if="gameStatus == 1">
      <div v-if="notModeHost">
        <p>{{ notModeHost.name }}님이 주제를 고르는 중입니다.</p>
      </div>
    </div>
    <div>
      <div v-if="gameStatus == 2" class="p-3">
        <div class="panel-title">
          <p>문장을 읽어주세요.</p>
        </div>
        <div>
          <p>{{sentence}}</p>
        </div>
        <div>
          <p v-if="drunkenText">{{drunkenText}}</p>
        </div>
      </div>
    </div>
    <div v-if="gameStatus===3">
      <h1>결과</h1>
      <p>{{sentence}}</p>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";

export default {
  name: "GamePanel",
  computed: {
    ...mapState("meetingStore", [
      "gameStatus",
      "selectedGame",
      "gameTurn",
      "gameWord",
      "subscribers",
      "gameLiar",
      "myself",
      "publisher",
      "sentence",
      "drunkenText"
    ]),
    ...mapGetters("meetingStore", ["notModeHost"]),
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
    sendResult( ){
      var request = new Object();
      request.gameId = 5;
      request.gameStatus = 2;
      request.sentence = this.drunkenText;
      request.paneltyId = 0;
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

p, h1, h2, h3, h4, h5, h6 {
  color: white;
}
</style>