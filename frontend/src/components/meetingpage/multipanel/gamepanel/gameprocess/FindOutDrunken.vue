<template>
  <div class="panel drunken">
    <div class="selectgame" v-if="gameStatus == 1">
      <div v-if="notModeHost">
        <p>{{ notModeHost.name }}님이 주제를 고르는 중입니다.</p>
      </div>
      <div v-else class="p-3">
        <div class="panel-title">
          <p>문장을 읽어주세요.</p>
        </div>
        <div>
          <p>{{sentence}}</p>
        </div>
      </div>
    </div>
    <!-- <div class="startgame" v-if="gameStatus == 2">
      <div class="showWordBox" v-if="gameTurn == 0">
        <div
          class="showWord"
          v-if="gameLiar == publisher.session.connection.connectionId"
        >
          <p>당신은 라이어입니다</p>
        </div>
        <div class="showLiar" v-else>
          <p>해당 단어는 {{ this.gameWord }} 입니다</p>
        </div>
      </div>
      <div class="AboutWord" v-if="gameTurn == 1">
        <p>단어에 대해<br />서로 얘기해 주세요</p>
      </div>
      <div class="VoteForLiar" v-if="gameTurn == 2">
        <p>라이어한테 투표하세요</p>
        <div class="list">
          <div
            v-for="subscriber in subscribers"
            :key="subscriber.stream.connection.data"
          >
            <input
              type="radio"
              id="{$this.subscriber.stream.connection.connectionId}"
              :value="subscriber.stream.connection.connectionId"
              v-model="picked"
            />
            <label for="subscriber.stream.connection.connectionId">
              {{ subscriber.stream.connection.data.slice(15, -2) }}
            </label>
          </div>
        </div>
        <div class="submit">
          <button class="btn-yellow rounded" @click="voteForLiar()">
            투표하기
            {{ picked }}
          </button>
        </div>
      </div>
    </div> -->
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
      "sentence"
    ]),
    ...mapGetters("meetingStore", ["notModeHost"]),
  },
  data() {
    return {
      picked: null,
    };
  },
  methods: {
    ...mapActions("meetingStore", ["sendGameRequest"]),
    clickSendTheme(theme) {
      alert("주제선택");
      var request = new Object();
      request.gameId = this.selectedGame;
      request.theme = theme;
      request.gameStatus = 2;

      var jsonRequest = JSON.stringify(request);
      console.log(jsonRequest);
      this.sendGameRequest(jsonRequest);
    },
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