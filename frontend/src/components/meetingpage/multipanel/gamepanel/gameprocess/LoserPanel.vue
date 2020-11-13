<template>
  <div class="d-flex flex-column justify-content-center align-items-center" v-if="loser">
    <img class="w-50" :src="smileURL" alt="" v-if="selectedGame == 4 && smileURL">
    <user-video
      class="w-50"
      :stream-manager="loser"
      @click.native="updateMainVideoStreamManager(loser)"
      v-if="selectedGame != 4"
    />
    <div v-if="selectedGame == 1">
      <p> 번호 : {{this.gameUpDownNumber}}</p>
      <p> <span class="color-yellow">{{ loser.stream.connection.data.slice(15,-2) }}</span> 당첨!!! </p>
    </div>
    <div v-if="selectedGame == 2">
      <p>{{ loser.stream.connection.data.slice(15,-2) }}님이 꼴찌 입니다!!!</p>
    </div>
    <div v-if="selectedGame == 3">
      <p> 투표 결과 : {{this.gameVoteData}} </p>
      <p> 라이어 : {{this.gameLiarData}} </p>
      <p> 벌칙자 : {{ loser.stream.connection.data.slice(15,-2) }} </p>
    </div>
    <div v-if="selectedGame == 4">
      <p>{{ loser.stream.connection.data.slice(15,-2) }}님이 끝내 웃음을 참지 못했습니다.</p>
    </div>
    <div v-if="selectedGame == 5">
      <h1>결과</h1>
      <p>읽은 문장: {{sentence}}</p>
      <p>{{ loser.stream.connection.data.slice(15,-2) }}은 {{findDrunken}}</p>
    </div>

    <div>
      <p>벌칙은 <span class="color-yellow">{{ penalty }}</span> 입니다.</p>
    </div>

    <div>
      <button
        class="btn btn-yellow mx-2"
        @click="changeMode(null)"
      >
        술게임 끝내기
      </button>
      <button
        class="btn btn-yellow mx-2"
        @click="selectNewGame"
      >
        술게임 고르기
      </button>
    </div>
  </div>
</template>

<script>
import UserVideo from '@/components/meetingpage/UserVideo';
import { mapState, mapGetters, mapActions } from 'vuex';

export default {
  name: 'LoserPanel',
  data() {
    return {
    }
  },
  components: {
    UserVideo
  },
  computed: {
    ...mapState('meetingStore', [
      'loser',
      'selectedGame',
      'penalty',
      'gameVoteData',
      'gameLiarData',
      'gameUpDownNumber',
      'sentence',
      'smileURL'
    ]),
    ...mapGetters('meetingStore', ['findDrunken'])
  },
  methods: {
    ...mapActions('meetingStore', ['changeMode', 'endGameProcess', 'sendGameRequest']),
    selectNewGame() {
      var request = new Object();
      request.gameStatus=0;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    }
  }
}
</script>

<style scoped>
p {
  color: white;
}

</style>