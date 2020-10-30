<template>
  <div>
    <div class="d-flex justify-content-center align-items-center mt-3">
      <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
      <img class="theme-title" :src="require(`@/assets/images/${theme}_title.png`)" alt="theme-title">
      <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
    </div>
    <div id="session" v-if="session">
			<div id="session-header">
				<h1 id="session-title">{{ mySessionId }}</h1>
				<input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session">
			</div>
			<!-- <div id="main-video" class="col-md-6">
				<user-video :stream-manager="mainStreamManager"/>
			</div> -->
      <div class="row no-gutters">
        <div id="video-container" class="col-4">
          <user-video :stream-manager="publisher" @click.native="updateMainVideoStreamManager(publisher)"/>
          <user-video v-for="(sub, index) in subscribers" :key="index" :stream-manager="sub" @click.native="updateMainVideoStreamManager(sub)"/>
        </div>
      </div>
		</div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import UserVideo from './UserVideo';

export default {
  name: 'LeftPanel',
  components: {
    UserVideo
  },
  data() {
    return {

    }
  },
  computed: {
    ...mapState('meetingStore', ['theme', 'session', 'mySessionId', 'mainStreamManager', 'publisher', 'subscribers'])
  },
  methods: {
    ...mapActions('meetingStore', ['leaveSession', 'updateMainVideoStreamManager'])
  },
  watch: {
    theme() {
      this.$forceUpdate();
    }
  }
}
</script>

<style scoped>
.theme-title {
  max-width: 30vw;
  max-height: 20vh;
}

.theme-deco {
  width: 10vw;
}

.personal-screen {
  max-width: 20vw;
  height: 20vh;
  background-color: yellow;
  margin-top: 10vh;
}
</style>