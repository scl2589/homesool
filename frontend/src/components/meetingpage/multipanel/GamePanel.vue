<template>
  <div>
    <GameSelectionPanel v-if="!selectedGame" />
    <GameProcessPanel v-else />
  </div>
</template>

<script>
import { mapState } from 'vuex';
import GameSelectionPanel from '@/components/meetingpage/multipanel/gamepanel/GameSelectionPanel'
import GameProcessPanel from '@/components/meetingpage/multipanel/gamepanel/GameProcessPanel'

export default {
  name: "GamePanel",
  computed: {
    ...mapState('meetingStore', ['selectedGame'])
  },
  
  components: {
    GameSelectionPanel,
    GameProcessPanel
  },

};
</script>

<style scoped>

</style>