<template>
  <div>
    <div class="d-flex justify-content-center align-items-center mt-3">
      <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
      <img class="theme-title" :src="require(`@/assets/images/${theme}_title.png`)" alt="theme-title">
      <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
    </div>
    <div id="session" v-if="session">
			<!-- <div id="session-header">
				<h1 id="session-title">{{ mySessionId }}</h1>
				<input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session">
			</div> -->
			<!-- <div id="main-video" class="col-md-6">
				<user-video :stream-manager="mainStreamManager"/>
			</div> -->
      <div class="d-flex row no-gutters">
        <user-video class="my-2 px-2" :class="{ 'col-12': one, 'col-6' : two, 'col-4' : three, 'col-3' : eight, 'col-2' : twelve }" :stream-manager="publisher" @click.native="updateMainVideoStreamManager(publisher)"/>
        <user-video class="my-2 px-2" :class="{ 'col-12': one, 'col-6' : two, 'col-4' : three, 'col-3' : eight, 'col-2' : twelve }" v-for="(sub, index) in subscribers" :key="index" :stream-manager="sub" @click.native="updateMainVideoStreamManager(sub)"/>
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
      one: true,
      two: false,
      three: false,
      eight: false,
      twelve: false
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
    },
    subscribers() {
      let count = this.subscribers.length + 1
      if (count == 1) {
        this.one = true
        this.two = false
        this.three = false
        this.eight = false
        this.twelve = false
      } else if (count == 2 || count == 4) {
        this.one = false
        this.two = true
        this.three = false
        this.eight = false
        this.twelve = false
      } else if (count == 3 || count == 5 || count == 6) {
        this.one = false
        this.two = false
        this.three = true
        this.eight = false
        this.twelve = false
      } else if (count == 7 || count == 8) {
        this.one = false
        this.two = false
        this.three = false
        this.eight = true
        this.twelve = false
      } else {
        this.one = false
        this.two = false
        this.three = false
        this.eight = false
        this.twelve = true
      }
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
