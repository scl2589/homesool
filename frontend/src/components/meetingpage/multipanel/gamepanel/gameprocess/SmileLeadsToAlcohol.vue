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
            
        </div>
    </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
export default {
  name: 'SmileLeadsToAlcohol',
  computed: {
    ...mapState('meetingStore', ['gameStatus', 'selectedGame', 'gameWord']),
    ...mapGetters('meetingStore', ['notModeHost'])
  },
  methods:{
       ...mapActions("meetingStore", [
      "sendGameRequest",
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
  }
}
</script>

<style>
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