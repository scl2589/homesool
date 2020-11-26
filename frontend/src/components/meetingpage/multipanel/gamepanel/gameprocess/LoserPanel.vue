<template>
  <div 
    class="d-flex flex-column justify-space-around align-items-center h-100" 
    v-if="loser"
  >
    <div 
      class="w-100" 
      v-if="selectedGame===1"
    >
      <p class="answers color-white"> 번호 : <span class="color-yellow">{{this.gameUpDownNumber}}</span></p>
    </div>
    <div 
      class="w-100" 
      v-if="selectedGame===2"
    >
      <div class="answerList mb-2">
        <p 
          class="color-white other-user"
          v-if="gameAnswerWords.length"
        >
          ⭐다른 유저의 정답⭐
        </p>
        <div class="d-flex row no-gutters answers">
          <p 
            class="col-4 answer2 color-white"
            v-for="word in gameAnswerWords" 
            :key="word.word"
          >
            <span class="nickname">{{word.nickName.slice(0,6)}}</span> : {{word.word}}
          </p>
        </div>
      </div>
    </div>
    <div 
      class="w-100 mt-3" 
      v-if="selectedGame===3"
    >
      <div v-if="gameVoteData===gameLiarData">
        <h3 class="result3"><span class="color-yellow">시민</span> 승!!!</h3>
      </div>
      <div v-else>
        <h3 class="result3"><span class="color-yellow">라이어</span> 승!!!</h3>
      </div>
    </div>
    <img 
      class="w-50" 
      :src="smileURL" 
      alt="smile" 
      v-if="selectedGame===4&&smileURL"
    >
    <div 
      class="w-100 mt-3" 
      v-if="selectedGame===5"
    >
      <p class="w-100 color-gray answers p-0"><span class="highlight">읽은 문장</span>: {{sentence}}</p>
    </div>
    <user-video
      class="w-50"
      :class="{'w-45': game2, 'w-35':six, 'w-20':nine}"
      :stream-manager="loser"
      v-if="selectedGame!==4"
    />
    <div v-if="selectedGame===1">
      <p class="color-white"><span class="color-yellow">{{ loser.stream.connection.data.slice(15,-2) }}</span> 당첨!!! </p>
    </div>
    <div 
      class="w-100" 
      v-if="selectedGame===2"
    >
      <p class="color-white"><span class="color-yellow">{{ loser.stream.connection.data.slice(15,-2).slice(0, 10) }}</span>님이 <u>꼴찌</u> 입니다!!!</p>
    </div>
    <div 
      class="w-100 mt-auto d-flex row no-gutters" 
      v-if="selectedGame===3"
    >
      <p class="col-4 color-white"><span class="color-gray nanum-font">최다득표자 :</span> {{this.gameVoteData}} </p>
      <p class="col-4 color-white"><span class="color-gray nanum-font">라이어 :</span> {{this.gameLiarData}} </p>
      <p class="col-4 color-white"><span class="color-gray nanum-font">벌칙자 :</span> {{ loser.stream.connection.data.slice(15,-2).slice(0, 6) }}</p>
    </div>
    <div v-if="selectedGame===4">
      <p class="color-white"><span class="color-yellow">{{ loser.stream.connection.data.slice(15,-2) }}</span>님이 끝내 웃음을 참지 못했습니다.</p>
    </div>
    <div v-if="selectedGame===5">
      <div class="w-100">
        <p class="color-white"><span class="color-yellow">{{ loser.stream.connection.data.slice(15,-2) }}</span>님은 {{findDrunken}}</p>
      </div>
    </div>

    <div class="w-100">
      <p 
        class="color-white" 
        v-if="findDrunken!=='아직 안취했습니다.'"
      >
        벌칙은 <span class="color-yellow">{{ penalty }}</span> 입니다.
      </p>
    </div>

    <div class="w-100 mb-3">
      <button
        class="btn btn-sm btn-yellow mx-2"
        @click="changeMode(null)"
      >
        술게임 끝내기
      </button>
      <button
        class="btn btn-sm btn-yellow mx-2"
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
      game2: false,
      six: false,
      nine: false
    }
  },
  watch: {
    gameAnswerWords() {
      this.findWidth()
    },
    selectedGame() {
      if (this.selectedGame === 2) {
        this.game2 = true
      } 
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
      'smileURL',
      'gameAnswerWords'
    ]),
    ...mapGetters('meetingStore', ['findDrunken'])
  },
  methods: {
    ...mapActions('meetingStore', ['changeMode', 'endGameProcess', 'sendGameRequest']),
    selectNewGame() {
      var request = new Object();
      request.gameStatus = 0;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    },
    findWidth() {
      if ( this.gameAnswerWords ) {
        if ( this.gameAnswerWords.length <= 3 ) {
          this.six = false
          this.nine = false
        } else if ( this.gameAnswerWords.length <= 6 ) {
          this.six = true
          this.nine = false
        } else {
          this.nine = true
          this.six = false
        }
      }

    },
    created() {
      this.findWidth()
    }
  }
}
</script>

<style scoped>
.answers {
  border-bottom: 1px solid white;
}

.answer2 {
  font-size: 0.8em;
}

.w-35 {
  width: 30% !important;
}

.w-20 {
  width: 20% !important;
}

.result3 {
  color: white;
}

p {
  margin: 0;
}

.nickname {
  font-family: 'Nanum Gothic', sans-serif;
}

button {
  font-family: 'Nanum Gothic', sans-serif;
  font-weight: 600;
}

.nanum-font {
  font-family: 'Nanum Gothic', sans-serif;
  font-weight:600;
}

.highlight {
  color:#ffffa4;
}
</style>