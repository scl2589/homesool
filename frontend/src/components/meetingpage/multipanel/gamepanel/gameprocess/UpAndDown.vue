<template>
    <div class="panel">
      <div class="startgame h-100 d-flex flex-column justify-content-between" v-if="gameStatus==1 || gameStatus==2">
        <div class="showWord h-100 d-flex flex-column justify-content-between">
          <p class="text-white description-title">숫자를 맞춰 주세요</p>

          <div v-if="gameUpDownNumber >= 0">
            <p class="previous color-gray"> 이전 번호 : {{this.gameUpDownNumber}}</p>
          </div>
          <!-- up인지 down인지에 따라 이미지 변경 -->
          <div v-if="gameStatus==2">
            <p v-if="gameUpDownResult==='up'">
              <img src="@/assets/images/positivevote.png">
              <br><span class="result color-gray">{{gameUpDownResult}}</span>
            </p>
            <p v-else>
              <img src="@/assets/images/negativevote.png">
              <br><span class="result color-gray">{{gameUpDownResult}}</span>
            </p>
          </div>
          <div v-if="notCurrentPlayer">
            <p class="turn">
              <span class="color-yellow">{{ notCurrentPlayer.stream.connection.data.slice(15,-2) }}</span>의 차례입니다
            </p>
          </div>
        </div>
          <div class="chat-box p-2 d-flex flex-column h-50" v-if="!notCurrentPlayer">      
            <div class="footer d-flex mt-auto">
              <div class="col-10 px-1 py-0">
                <input 
                  class="text-box"
                  @keyup.enter="clickSendNum()"
                  v-model="number"
                  autofocus
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
import Swal from 'sweetalert2'
import { mapState, mapActions, mapGetters } from 'vuex'
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';

export default {
 name: "GamePanel",
 components:{
     LoserPanel
 },
  computed: {
    ...mapState('meetingStore', [
      'gameStatus',
      'selectedGame',
      'gameUpDownResult',
      'gameUpDownIndex',
      'subscribers',
      'publisher',
      'gameUpDownNumber'
    ]),
    ...mapGetters('meetingStore', ['notModeHost', 'notCurrentPlayer']),
  },
  data(){
    return{
      number: '',
    }
  },
  methods:{
       ...mapActions("meetingStore", [
      "sendGameRequest",
    ]),
    clickSendNum(){
      if (this.number >= 1 && this.number <= 100) {
        var request = new Object();
        request.gameId=this.selectedGame;
        request.number=this.number;
        request.gameStatus=2;
        request.index=this.gameUpDownIndex;
        this.number = '';
        var jsonRequest = JSON.stringify(request);
        console.log(jsonRequest);
        this.sendGameRequest(jsonRequest);
      } else {
        Swal.fire({
          icon: 'warning',
          html: '숫자를 다시 입력해주세요.<br>1과 100 사이의 숫자여야 합니다.',
        })
      }

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

.description-title {
  font-size: 2.2rem;
} 

p {
  margin: 10px;
}

img {
  max-width: 20%;
  max-height: 20%;
}

.turn{
  color:white;
  font-size : 1.8rem;
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

input {
  padding-left: 10px;
  padding-right: 10px;
}

input:focus {
  outline: none;
}

.previous {
  font-size: 1.2rem;
}

.result {
  font-size: 1.5rem;
}
</style>