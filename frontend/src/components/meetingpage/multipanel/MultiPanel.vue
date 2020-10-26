<template>
  <div>
    <div class="text-right">
      <button
        class="btn close-btn"
        @click="closeMultiPanel"
      >
        <i class="fas fa-times"></i>
      </button>
    </div>
    <AnonymousPanel v-if="isAnonymousMode">
    </AnonymousPanel>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

import AnonymousPanel from '@/components/meetingpage/multipanel/AnonymousPanel'

export default {
  name: 'MultiPanel',
  components: {
    AnonymousPanel
  },
  computed: {
    ...mapState('meetingStore', ['isGameMode', 'isMusicMode', 'isAnonymousMode', 'isSnapshotMode']),
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