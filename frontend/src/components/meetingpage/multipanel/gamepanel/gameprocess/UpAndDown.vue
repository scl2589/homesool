<template>
    <div class="panel">
      <div class="startgame" v-if="gameStatus==1 || gameStatus==2">
        <div class="showWord">
          <p>숫자를 맞춰 주세요 </p>
          <div v-if="gameUpDownNumber >= 0">
              <p> 이전 번호 : {{this.gameUpDownNumber}}</p>
          </div>    
          <div>{{this.gameParticipantData}} 의 차례입니다</div>
          <div v-if="gameStatus==2">
            <p>{{this.gameUpDownResult}}</p>
          </div>
        </div>
          <div class="chat-box p-2 d-flex flex-column h-50" v-if="publisher.stream.connection.connectionId == participantPublicId">      
            <div class="footer d-flex mt-auto">
              <div class="col-10 px-1 py-0">
                <input 
                  @keyup.enter="clickSendNum()"
                  class="text-box"
                  v-model="number"
                >
              </div>
              <div class="col-2 p-0">
                <button
                  class="send-btn"
                  @click="clickSendNum()"
                >
                  <i class="fas fa-paper-plane"></i>
                </button>
              </div>
            </div>
          </div> 
      </div>
      <div class="endgame" v-if="gameStatus==3">
          <h5> 게임이 종료되었습니다 </h5>
          <h5> 벌칙자 : {{this.gameParticipantData}} </h5>
      </div>
      <div class="paneltygame" v-if="gameStatus==4">
          <h5> 벌칙화면 </h5>
            <user-video 
              class="my-2 px-2 sub-video" 
              :stream-manager="gamePaneltyPublisher" 
              @click.native="updateMainVideoStreamManager(gamePaneltyPublisher)"
            />
          <div class="d-flex justify-content-around" v-if="publisher.session.connection.connectionId == participantPublicId">
            <button
              class="btn btn-yellow"
              @click="changeMode(null)"
            >
              술게임 모드 끝내기
            </button>
            <button
              class="btn btn-yellow"
              @click="clickFinishgame()"
            >
              술게임 고르기
            </button>
        </div>
       </div>
    </div>

</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'

export default {
 name: "GamePanel",
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame','gameUpDownResult','gameUpDownIndex','participantPublicId', 'gameParticipantData','subscribers','publisher','gameUpDownNumber']),
    ...mapGetters('meetingStore', ['notModeHost'])
  },
  data(){
    return{
      number: 0,
    }
  },
  methods:{
       ...mapActions("meetingStore", [
      "sendGameRequest",
    ]),
    clickSendNum(){
      var request = new Object();
      request.gameId=this.selectedGame;
      request.number=this.number;
      request.gameStatus=2;
      request.index=this.gameUpDownIndex;
      this.number = 0;
      var jsonRequest = JSON.stringify(request);
      console.log(jsonRequest);
      this.sendGameRequest(jsonRequest);
    },
     clickFinishgame(){
      var request = new Object();
      request.gameId=this.selectedGame;
      request.gameStatus=4;
      var jsonRequest = JSON.stringify(request);
      console.log(jsonRequest);
      this.sendGameRequest(jsonRequest);
    },
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

.chat-box {
  height: 100%;
}

.finish-btn {
  color: white;
}

.text-box {
  background-color: #D1D1D1;
  width: 100%;
  border-radius: 20px;
  color: black;
}

.send-btn {
  color: white;
}

#chat-area {
  overflow-y: auto;
}
</style>