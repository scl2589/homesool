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
          </div> 
      </div>
      <div class="finishgame" v-if="gameStatus==3">
        <div class="showName">
          <div v-for="subscriber in subscribers" :key="subscriber.stream.connection.data">
            <div :v-if="subscriber.stream.connection.connectionId==participantPublicId">
              {{subscriber.stream.connection.data.slice(15,-2)}} 패배!!
            </div>
          </div>
          <button class="finish-btn" @click="clickFinishgame()">
            끝내기
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
    ...mapState('meetingStore', ['gameStatus', 'selectedGame','gameInitialWord','gameIsCorrect','participantPublicId','subscribers']),
    ...mapGetters('meetingStore', ['notModeHost'])
  },
  data(){
    return{
      word: "",
    }
  },
  methods:{
       ...mapActions("meetingStore", [
      "sendGameRequest",
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