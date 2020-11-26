<template>
  <div class="panel">
    <UpAndDown v-if="selectedGame==1" />
    <ConsonantQuiz v-if="selectedGame==2" />
    <LiarGame v-if="selectedGame==3" />
    <SmileLeadsToAlcohol v-if="selectedGame==4" />
    <FindOutDrunken v-if="selectedGame==5" />
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import SmileLeadsToAlcohol from '@/components/meetingpage/multipanel/gamepanel/gameprocess/SmileLeadsToAlcohol'
import LiarGame from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LiarGame'
import FindOutDrunken  from '@/components/meetingpage/multipanel/gamepanel/gameprocess/FindOutDrunken'
import ConsonantQuiz from '@/components/meetingpage/multipanel/gamepanel/gameprocess/ConsonantQuiz'
import UpAndDown from '@/components/meetingpage/multipanel/gamepanel/gameprocess/UpAndDown'

export default {
  name: 'GameProcessPanel',

  components:{
    SmileLeadsToAlcohol,
    LiarGame,
    ConsonantQuiz,
    UpAndDown,
    FindOutDrunken,
  },

  computed: {
    ...mapState('meetingStore', ['selectedGame']),
  },

  methods: {
    ...mapActions('meetingStore', ['endGameProcess'])
  },

  beforeDestroy() {
    this.endGameProcess();
  }
}
</script>

<style scoped>
.panel {
  background-color: black;
  height: 46vh;
}
.panel-title{
  padding : 30px;
}
p{
  color:yellow;
  font-size : 3.3rem;
}
</style>