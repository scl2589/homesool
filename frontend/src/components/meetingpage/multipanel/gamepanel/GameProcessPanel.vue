<template>
  <div class="panel">
    <SmileLeadsToAlcohol v-if="selectedGame==0">
    </SmileLeadsToAlcohol>
    <LiarGame v-if="selectedGame==3">
    </LiarGame>
    <FindOutDrunken v-if="selectedGame==5">
    </FindOutDrunken>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import SmileLeadsToAlcohol from '@/components/meetingpage/multipanel/gamepanel/gameprocess/SmileLeadsToAlcohol'
import LiarGame from '@/components/meetingpage/multipanel/gamepanel/gameprocess/LiarGame'
import FindOutDrunken  from '@/components/meetingpage/multipanel/gamepanel/gameprocess/FindOutDrunken'

export default {
  name: 'GameProcessPanel',
  data() {
    return {

    }
  },

  components:{
    SmileLeadsToAlcohol,
    LiarGame,
    FindOutDrunken
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
  /* height: 100%; */
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