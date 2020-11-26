<template>
    <div class="panel">
      <div 
        class="selectgame h-100" 
        v-if="gameStatus==='1'"
      >
        <div 
          class="h-100 d-flex align-items-center justify-content-center" 
          v-if="notModeHost"
        >
          <p class="select-theme">{{ notModeHost.name }}님이 주제를 고르는 중입니다.</p>
        </div>
        <div v-else>
          <div class="panel-title">
            <p> 주제를 선택하세요 </p>
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
        class="startgame h-100" 
        v-if="gameStatus===2"
      >
        <div 
          class="showWordBox h-100" 
          v-if="gameTurn===0"
        >
          <div 
            class="showWord h-100 d-flex justify-content-center align-items-center" 
            v-if="gameLiar==publisher.session.connection.connectionId"
          >
            <p class="color-white">당신은 <span class="color-yellow">라이어</span>입니다 </p>
          </div>
          <div 
            class="showLiar h-100 d-flex justify-content-center align-items-center" 
            v-else
          >
            <p class="color-white">해당 단어는 <span class="color-yellow">{{this.gameWord}}</span> 입니다 </p>
          </div>
        </div>
        <div 
          class="aboutWord" 
          v-if="gameTurn===1"
        >
          <p class="select-theme color-white"> 주제 : <span class="color-yellow">{{gameTheme}}</span></p>
          <h3>단어에 대해<br>서로 얘기해 주세요 </h3>
          <circular-count-down-timer
            :initial-value="(subscribers.length+1)*20"
            :show-minute="false"
            :show-hour="false"
            :size="150"
          ></circular-count-down-timer>
        </div>
        <div 
          class="voteForLiar h-100" 
          v-if="gameTurn===2"
        >
          <div 
            class="voteComplete h-100 d-flex justify-content-center align-items-center" 
            v-if="DidVote"
          >
            <h5> 결과 집계중 </h5>
            <div class="wrapper">
              <div class="pie spinner"></div>
              <div class="pie filler"></div>
              <div class="mask"></div>
            </div>
          </div>
          <div 
            class="voteProcess d-flex flex-column justify-content-between h-100" 
            v-else
          >
            <p class="vote">라이어한테 <span class="color-yellow">투표</span>하세요 </p>
            <div class="list">
              <v-col
                class="d-flex justify-content-center"
                offset="3"
                cols="6"
              >
                <v-select
                  :items="items"
                  v-model="picked"
                  name="subscriber"
                  item-text="name"
                  item-value="id"
                  label="투표해주세요"
                  solo
                ></v-select>
              </v-col>
            </div>
            <div class="submit">
              <button
                class="btn-yellow rounded"
                @click="voteForLiar()"
              >
              투표하기
              </button>
            </div>
          </div>
        </div>
      </div>
      <loser-panel 
        class="w-100" 
        v-if="gameStatus===3"
      />
  </div>
</template>

<script>
import Swal from 'sweetalert2'
import { mapState, mapActions, mapGetters } from 'vuex'
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';

export default {
  name: "GamePanel",
  components : {
    LoserPanel
  },
  computed: {
    ...mapState('meetingStore', [
      'gameStatus',
      'selectedGame',
      'gameTurn',
      'gameWord',
      'subscribers',
      'gameLiar',
      'publisher',
      'gameTheme'
    ]),
    ...mapGetters('meetingStore', ['notModeHost']),
    items() {
      let array = [] 
      for (let subscriber of this.subscribers) {
        var obj = new Object()
        obj.name = subscriber.stream.connection.data.slice(15,-2)
        obj.id = subscriber.stream.connection.connectionId
        array.push(obj)
      }
      return array
    }
  },
  data(){
    return{
      picked : null,
      DidVote: false,
    }
  },
  methods:{
      ...mapActions("meetingStore", [
      "sendGameRequest",
      "endGameProcess",
    ]),
    clickSendTheme(theme){
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
    },
    voteForLiar(){
      this.DidVote = true;
      var request = new Object();
      request.gameId = this.selectedGame;
      request.liarId = this.gameLiar;
      request.gameStatus = 5;
      request.voteId = this.picked;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    },
    sendEndgame(){
      var request = new Object();
      request.gameId = this.selectedGame;
      request.gameStatus = 4;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    }
  },
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
  font-size: 2.2rem;
  color: white;
}

.showWord p, .showLiar p {
  font-size: 1.8rem;
}

h3 {
  color: white;
}

.select-theme {
  color: white;
  font-size: 1.8rem
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

.vote {
  color: white;
  font-size: 1.8rem;
}

label {
  margin-left: 10px;
  color: white;
}

.vote-result {
  color: white;
  font-size: 1.8rem;
}

.voteComplete > h5 {
  color: white;
}

.select-theme {
  font-size: 1.8rem;
}

button {
  font-family: 'Nanum Gothic', sans-serif;
  font-weight: 600;
}
</style>