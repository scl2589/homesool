<template>
  <div>
    <div class="d-flex justify-content-between">
      <button
        class="btn goback-btn"
        v-if="selectedSong"
        @click="closeSingingPanel"
      >
        <i 
          class="fas fa-arrow-left"
          style="color:white;"
        ></i>
      </button>
      <button
        class="btn close-btn"
        @click="closeMultiPanel"
      >
        <i 
          class="fas fa-times"
          style="color:white;"
        ></i>
      </button>
    </div>
    <AnonymousPanel v-if="isAnonymousMode">
    </AnonymousPanel>
    <SnapShotPanel v-if="isSnapshotMode">
    </SnapShotPanel>
    <SingingPanel v-if="isSingingMode">
    </SingingPanel>
    <GameSelectionPanel v-if="isGameMode">
    </GameSelectionPanel>
    <GamePanel v-if="isGameStart">
    </GamePanel>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

import AnonymousPanel from '@/components/meetingpage/multipanel/AnonymousPanel'
import SnapShotPanel from '@/components/meetingpage/multipanel/SnapShotPanel'
import SingingPanel from '@/components/meetingpage/multipanel/SingingPanel'
import GameSelectionPanel from '@/components/meetingpage/multipanel/GameSelectionPanel'
import GamePanel from '@/components/meetingpage/multipanel/gamepanel/GamePanel'

export default {
  name: 'MultiPanel',
  components: {
    AnonymousPanel,
    SnapShotPanel,
    SingingPanel,
    GameSelectionPanel,
    GamePanel
  },
  computed: {
    ...mapState('meetingStore', ['isGameMode', 'isSingingMode', 'isAnonymousMode', 'isSnapshotMode', 'selectedSong','isGameStart']),
  },
  methods: {
    ...mapActions('meetingStore', ['closeMultiPanel', 'closeSingingPanel']),
  }
}
</script>

<style scoped>
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
}

.goback-btn {
  position: absolute;
  top: 10px;
  left: 10px;
}

</style>