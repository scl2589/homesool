<template>
  <div class="text-right">
    <button
      class="btn"
      @click="closeMultiPanel"
    >
      <i class="fas fa-times"></i>
    </button>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'multipanel',
  computed: {
    ...mapState('meetingStore', ['isGameMode', 'isMusicMode', 'isAnonymousMode', 'isSnapshotMode']),
  },
  methods: {
    ...mapActions('meetingStore', ['closeMultiPanel']),
  }
}
</script>

<style scoped>

</style>