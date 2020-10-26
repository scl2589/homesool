<template>
  <div>
    <div class="text-right">
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
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

import AnonymousPanel from '@/components/meetingpage/multipanel/AnonymousPanel'
import SnapShotPanel from '@/components/meetingpage/multipanel/SnapShotPanel'
import SingingPanel from '@/components/meetingpage/multipanel/SingingPanel'
import GameSelectionPanel from '@/components/meetingpage/multipanel/GameSelectionPanel'

export default {
  name: 'MultiPanel',
  components: {
    AnonymousPanel,
    SnapShotPanel,
    SingingPanel,
    GameSelectionPanel
  },
  computed: {
    ...mapState('meetingStore', ['isGameMode', 'isSingingMode', 'isAnonymousMode', 'isSnapshotMode']),
  },
  methods: {
    ...mapActions('meetingStore', ['closeMultiPanel']),
  }
}
</script>

<style scoped>
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
}

</style>