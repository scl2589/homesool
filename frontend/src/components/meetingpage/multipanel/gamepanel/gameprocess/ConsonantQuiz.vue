<template>
    <div class="panel">
      <div class="startgame" v-if="gameStatus==1 || gameStatus==2">
        <div class="showWord">
          <p>{{this.gameInitialWord}}</p>
        </div>
          <div class="chat-box p-2 d-flex flex-column h-50"  v-if="gameIsCorrect == 1">      
            <div class="footer d-flex mt-auto">
              <div class="col-10 px-1 py-0">
                <input 
                  @keyup.enter="clickSendWord(word)"
                  class="text-box"
                  v-model="word"
                >
              </div>
              <div class="col-2 p-0">
                <button
                  class="send-btn"
                  @click="clickSendWord(word)"
                >
                  <i class="fas fa-paper-plane"></i>
                </button>
              </div>
            </div>
          <div>
            <p>{{this.gameWordResult}}</p>
          </div>
          </div>
          <div v-else>
            통과
          </div> 
      </div>
      <loser-panel class="w-100 d-flex justify-content-center align-items-center" v-if="gameStatus == 3"/>
      <div class="answerList">
          <p v-for="word in gameAnswerWords" :key="word.word">
            {{word.nickName}} : {{word.word}}
          </p>
      </div>
    </div>

</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';
export default {
 name: "GamePanel",
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame','gameInitialWord','gameIsCorrect','subscribers','publisher','gameAnswerWords','gameWordResult']),
    ...mapGetters('meetingStore', ['notModeHost'])
  },
  data(){
    return{
      word: "",
    }
  },
  components: {
    LoserPanel
  },
  methods:{
       ...mapActions("meetingStore", [
      "sendGameRequest",'changeMode','endGameSignal'
    ]),
    clickSendWord(word){
      var request = new Object();
      request.gameId=this.selectedGame;
      request.word=word;
      request.gameStatus=2;
      this.word = "";
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

.answerList > p{
  color:yellow;
  font-size : 1rem;
}
</style>