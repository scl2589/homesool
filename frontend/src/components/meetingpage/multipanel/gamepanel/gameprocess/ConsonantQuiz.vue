<template>
    <div class="panel">
      <div 
        class="startgame h-100 d-flex flex-column justify-content-between" 
        v-if="gameStatus===1 || gameStatus===2"
      >
        <div class="showWord">
          <p class="color-gray">{{this.gameInitialWord}}</p>
        </div>
        <div class="answerList">
          <p 
            class="other-user"
            v-if="gameAnswerWords.length"
          >
            ⭐다른 유저의 정답⭐
          </p>
          <div class="d-flex row no-gutters">
            <p 
              class="color-gray col-4"
              v-for="word in gameAnswerWords" 
              :key="word.word"
            >
              <span class="color-gray">{{word.nickName}} :</span> {{word.word}}
            </p>
          </div>
        </div>
        <div 
          class="p-2 d-flex flex-column" 
          v-if="gameIsCorrect===1"
        > 
          <div>
            <!-- If it is repeated word or is not in a dictionary -->
            <div>
              <p class="color-yellow">{{this.gameWordResult}}</p>
            </div> 
            <div class="footer d-flex mt-auto">
              <div class="col-10 px-1 py-0">
                <input 
                  class="text-box"
                  @keyup.enter="clickSendWord(word)"
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
          </div>      
        </div>
        <div v-else>
          <p class="color-yellow pass">통과</p>
        </div> 
      </div>
      <loser-panel 
        class="w-100 d-flex justify-content-center align-items-center" 
        v-if="gameStatus==3"
      />
    </div>

</template>

<script>
import { mapState, mapActions } from 'vuex'
import LoserPanel from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LoserPanel';
export default {
 name: "GamePanel",
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame', 'gameInitialWord', 'gameIsCorrect', 'gameAnswerWords', 'gameWordResult']),
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
      request.gameId = this.selectedGame;
      request.word = word;
      request.gameStatus = 2;
      this.word = "";
      var jsonRequest = JSON.stringify(request);
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

.showWord > p {
  font-size: 2.2rem;
}

.other-user {
  color: white;
}

.pass {
  font-size: 1.8rem;
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

input:focus {
  outline: none;
}
</style>