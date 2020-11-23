<template>
  <div>
    <div class="d-flex justify-content-between">
      <button
        class="btn close-btn"
        @click="changeMode(null)"
      >
        <i 
          class="fas fa-times"
          style="color:white;"
        ></i>
      </button>
    </div>
    <AnonymousPanel
      class="w-100"
      v-if="currentMode === 'anonymous'"
    >
    </AnonymousPanel>

    <SnapShotPanel
      class="w-100"
      v-if="currentMode === 'snapshot'"
    >
    </SnapShotPanel>

    <SingingPanel
      class="w-100"
      v-if="currentMode === 'singing'"
    >
    </SingingPanel>
    
    <GamePanel
      class="w-100"
      v-if="currentMode === 'game'"
    >
    </GamePanel>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import AnonymousPanel from '@/components/meetingpage/multipanel/AnonymousPanel'
import SnapShotPanel from '@/components/meetingpage/multipanel/SnapShotPanel'
import SingingPanel from '@/components/meetingpage/multipanel/SingingPanel'
import GamePanel from '@/components/meetingpage/multipanel/GamePanel'

export default {
  name: 'MultiPanel',
  components: {
    AnonymousPanel,
    SnapShotPanel,
    SingingPanel,
    GamePanel
  },
  computed: {
    ...mapState('meetingStore', [
      'isGameStart',
      'currentMode'
    ]),
  },
  methods: {
    ...mapActions('meetingStore', [
      'changeMode'
    ]),
  }
}
</script>

<style scoped>
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 100;
}

.goback-btn {
  position: absolute;
  top: 10px;
  left: 10px;
}
</style>