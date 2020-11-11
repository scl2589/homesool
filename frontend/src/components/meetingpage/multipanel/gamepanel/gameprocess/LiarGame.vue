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
            <div class="showWordBox" v-if="gameTurn==0">
              <div class="showWord" v-if="gameLiar == publisher.session.connection.connectionId">
                <p>당신은 라이어입니다 </p>
              </div>
              <div class="showLiar" v-else>
                <p>해당 단어는 {{this.gameWord}} 입니다 </p>
              </div>
            </div>
            <div class="aboutWord" v-if="gameTurn==1">
                <p>단어에 대해<br>서로 얘기해 주세요 </p>
            </div>
            <div class="voteForLiar" v-if="gameTurn==2">
              <div class="voteComplete" v-if="DidVote">
                <p> 투표가 완료되었습니다 </p>
                <h5> 결과 집계중 </h5>
              </div>
              <div class="voteProcess" v-else>
                <p>라이어한테 투표하세요 </p>
                <div class="list">
                  <div v-for="subscriber in subscribers" :key="subscriber.stream.connection.data">
                    <input type="radio" id="{$this.subscriber.stream.connection.connectionId}"
                    :value="subscriber.stream.connection.connectionId" v-model="picked">
                    <label for="subscriber.stream.connection.connectionId"> {{subscriber.stream.connection.data.slice(15,-2)}} </label>
                  </div>
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
        <div class="endgame" v-if="gameStatus==3">
          <h5> 게임이 종료되었습니다 </h5>
          <h5> 당첨자 : {{this.gameVoteData}} </h5>
          <h5> 라이어 : {{this.gameLiarDaita}} </h5>
          <h5> 벌칙자 : {{this.gameParticipantData}} </h5>
        </div>
        <div class="paneltygame" v-if="gameStatus==4">
          <h5> 벌칙화면 </h5>
            <user-video 
              class="my-2 px-2 sub-video" 
              :stream-manager="gamePaneltyPublisher" 
              @click.native="updateMainVideoStreamManager(gamePaneltyPublisher)"
            />
          <div class="d-flex justify-content-around" v-if="!notModeHost">
            <button
              class="btn btn-yellow"
              @click="changeMode(null)"
            >
              술게임 모드 끝내기
            </button>
            <button
              class="btn btn-yellow"
              @click="sendEndgame()"
            >
              술게임 고르기
            </button>
        </div>
       </div>
    </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
import UserVideo from '../../../UserVideo';

export default {
 name: "GamePanel",
 components : {
   UserVideo,
 },
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame', 'gameTurn', 'gameWord', 'gamePaneltySubscriber', 'gamePaneltyPublisher',
                                'subscribers','gameLiar','myself','publisher','gameVoteData','gameParticipantData','gameLiarData']),
    ...mapGetters('meetingStore', ['notModeHost'])
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
      'changeMode',
      'endGameProcess',
    ]),
    clickSendTheme(theme){
      alert("주제선택");
      var request = new Object();
      request.gameId=this.selectedGame;
      request.theme=theme;
      request.gameStatus=2;

      var jsonRequest = JSON.stringify(request);
      console.log(jsonRequest);
      this.sendGameRequest(jsonRequest);
    },
    voteForLiar(){
      this.DidVote = true;
      var request = new Object();
      request.gameId=this.selectedGame;
      request.liarId=this.picked;
      request.gameStatus=5;
      request.voteId=this.publisher.session.connection.connectionId;

      var jsonRequest = JSON.stringify(request);
      console.log("투표"+jsonRequest);
      this.sendGameRequest(jsonRequest);
    },
    sendEndgame(){
      var request = new Object();
      request.gameId=this.selectedGame;
      request.gameStatus=4;

      var jsonRequest = JSON.stringify(request);
      console.log(jsonRequest);
      this.sendGameRequest(jsonRequest);
    }
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