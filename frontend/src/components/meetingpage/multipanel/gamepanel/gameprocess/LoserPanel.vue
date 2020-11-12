<template>
  <div class="d-flex flex-column justify-content-center align-items-center" v-if="loser">
    <user-video
      class="w-50"
      :stream-manager="loser"
      @click.native="updateMainVideoStreamManager(loser)"
    />
    <div v-if="selectedGame == 1">
      <p> 번호 : {{this.gameUpDownNumber}}</p>
      <p> {{ loser.stream.connection.data.slice(15,-2) }} 당첨!!! </p>
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

    </div>

    <div>
      <p>벌칙은 {{ penalties[penaltyId] }} 입니다.</p>
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
import { mapState, mapActions } from 'vuex';

export default {
  name: 'LoserPanel',
  data() {
    return {
      penalties: ['술 한잔 마시기', '노래 부르기', '춤추기', '직접 정하기']
    }
  },
  components: {
    UserVideo
  },
  computed: {
    ...mapState('meetingStore', ['loser', 'selectedGame', 'penaltyId', 'gameVoteData','gameLiarData', 'gameUpDownNumber'])
  },
  methods: {
    ...mapActions('meetingStore', ['changeMode', 'endGameProcess', 'sendGameRequest']),
    selectNewGame() {
      var request = new Object();
      request.gameStatus=4;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    }
  }
}
</script>

<style scoped>

</style>