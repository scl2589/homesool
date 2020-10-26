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
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

import AnonymousPanel from '@/components/meetingpage/multipanel/AnonymousPanel'
import SnapShotPanel from '@/components/meetingpage/multipanel/SnapShotPanel'
import SingingPanel from '@/components/meetingpage/multipanel/SingingPanel'

export default {
  name: 'MultiPanel',
  components: {
    AnonymousPanel,
    SnapShotPanel,
    SingingPanel
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