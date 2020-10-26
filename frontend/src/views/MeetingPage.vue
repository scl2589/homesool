<template>
  <div>
    <div class="row no-gutters" style="height:92vh;">
      <div 
        class="left-panel" 
        :class="{'col-8' : (isMultiPanel || isChatPanel), 'col-12' : !isMultiPanel && !isChatPanel }"
      >

      </div>

      <div 
        class="right-panel" 
        :class="{ 'col-4' : (isMultiPanel || isChatPanel), 'col-0' : !isMultiPanel && !isChatPanel }"
        v-if="isMultiPanel || isChatPanel"
      >
          <div
            class="multi-panel"
            :class="{ 'half-height' : isChatPanel, 'full-height' : !isChatPanel }"
            v-if="isMultiPanel"
          >
            
          </div>

          <div
            class="chat-panel"
            :class="{ 'half-height' : isMultiPanel, 'full-height' : !isMultiPanel }"
            v-if="isChatPanel"
          >

          </div>

      </div>
    </div>


    <div class="footer" style="height:8vh;">
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex' 
export default {
  name: 'MeetingPage',
  data() {
    return {
    }
  },
  computed: {
    ...mapState('meetingStore', ['isMultiPanel', 'isChatPanel'])
  }
}
</script>

<style scoped>
.left-panel {
  background-color: red;
}

.right-panel {
  background-color: yellow;
}

.multi-panel {
  background-color: green;
  max-width: 100%;
}

.chat-panel {
  background-color: blue;
  max-width: 100%;
}

.footer {
  background-color: pink;
  height: 20vh;
}

.half-height {
  height: 50%;
}

.full-height {
  height: 100%;
}
</style>