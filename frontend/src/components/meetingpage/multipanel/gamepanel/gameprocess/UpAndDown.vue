<template>
    <div class="panel">
      <div class="startgame" v-if="gameStatus==1 || gameStatus==2">
        <div class="showWord">
          <p>숫자를 맞춰 주세요</p>
          <div v-if="notCurrentPlayer">{{ notCurrentPlayer.stream.connection.data.slice(15,-2) }} 의 차례입니다</div>
          <div v-if="gameUpDownNumber >= 0">
              <p> 이전 번호 : {{this.gameUpDownNumber}}</p>
          </div>    
          <div v-if="gameStatus==2">
            <p>{{gameUpDownResult}}</p>
          </div>
        </div>
          <div class="chat-box p-2 d-flex flex-column h-50" v-if="!notCurrentPlayer">      
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
      <loser-panel class="w-100" v-if="gameStatus == 3"/>
    </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';

export default {
 name: "GamePanel",
 components:{
     LoserPanel
 },
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame','gameUpDownResult','gameUpDownIndex',
    'notCurrentPlayer',
    'participantPublicData',
    'subscribers',
    'publisher',
    'gameUpDownNumber']),
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