<template>
  <div>
    <div v-if="!isSharingMode">
      <div class="d-flex justify-content-center align-items-center mt-3">
        <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
        <img class="theme-title" :src="require(`@/assets/images/${theme}_title.png`)" alt="theme-title">
        <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
      </div>
      <div id="session" v-if="session">
        <div class="d-flex row no-gutters">
          <user-video 
            class="my-2 px-2" 
            :class="{ 'col-12': one, 'col-6' : two, 'col-4' : three, 'col-3' : eight, 'col-2' : twelve }" 
            id="myVideo"
            :stream-manager="publisher" 
            :isPublisher="isPublisher"
            :isLeftPanel="true"
            @click.native="updateMainVideoStreamManager(publisher)"
          />
          <user-video 
            class="my-2 px-2" 
            :class="{ 'col-12': one, 'col-6' : two, 'col-4' : three, 'col-3' : eight, 'col-2' : twelve }" 
            v-for="sub in subscribers" 
            :key="sub.stream.connection.connectionId" 
            :stream-manager="sub"
            :isLeftPanel="true"
            @click.native="updateMainVideoStreamManager(sub)"
          />
        </div>
        <canvas id="canvas" hidden></canvas>
      </div>
    </div>
    <div v-else>
      <div class="d-flex justify-content-center" v-if="session">
        <!-- 화면공유 -->
        <div id="session" v-if="session">
          <user-video 
            class="my-2 px-2 publisher-width" 
            v-show="screenPublisher"
            :stream-manager="screenPublisher" 
            @click.native="updateMainVideoStreamManager(screenPublisher)"
          />
          <user-video
            class="my-2 px-2 publisher-width"
            v-show="!screenPublisher"
            :stream-manager="sharer"
            @click.native="updateMainVideoStreamManager(sharer)"
          />
          <div class="row no-gutters">
            <user-video 
              class="my-2 px-2 sub-video"
              :stream-manager="publisher" 
              @click.native="updateMainVideoStreamManager(publisher)"
            />
            <user-video 
              class="my-2 px-2 sub-video" 
              v-for="sub in participants" 
              :key="sub.stream.connection.connectionId" 
              :stream-manager="sub" 
              @click.native="updateMainVideoStreamManager(sub)"
            />
          </div>  
        </div>
        <canvas id="canvas" hidden></canvas>
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
      twelve: false,
      participants: null,
      sharer: null,
      isPublisher : true,
      //userName : this.publisher.stream.connection.data.slice(15,-2),
    }
  },
  computed: {
    ...mapState('meetingStore',[ 
      'theme', 
      'session', 
      'mySessionId', 
      'mainStreamManager', 
      'publisher', 
      'screenPublisher',
      'subscribers', 
      'isSharingMode'
      ]
    ),
  },
  methods: {
    ...mapActions('meetingStore', ['leaveSession', 'updateMainVideoStreamManager']),
    findParticipants() {
      let participants = []
      for (let i = 0, len = this.subscribers.length; i < len; i++ ) {
        if (this.subscribers[i].stream.connection.data.slice(-8, -2) !== 'screen') {
          participants.push(this.subscribers[i])
        }
      }
      this.participants = participants
    },
    findSharer() {
      for (let i = 0, len = this.subscribers.length; i < len; i++ ) {
        console.log("SLICINGGGG",this.subscribers[i].stream.connection.data.slice(-8, -2) )
        if (this.subscribers[i].stream.connection.data.slice(-8, -2) === 'screen') {
          console.log("헐 여기이")
          this.sharer = this.subscribers[i]
        }
      }
    },
    addClass() {
      let count = this.subscribers.length + 1
      if (count == 1) {
        this.one = true
        this.two = false
        this.three = false
        this.eight = false
        this.twelve = false
        this.$nextTick(function() {
          let videos =  document.querySelectorAll("video");
          for (let i = 0, len = videos.length; i < len; i++ ) {
            videos[i].classList.add("height70")
            videos[i].classList.remove("height30")
            videos[i].classList.remove("height15")
          }
        })
      } else if (count == 2 || count == 4) {
        this.one = false
        this.two = true
        this.three = false
        this.eight = false
        this.twelve = false
        if (count == 2) {
          this.$nextTick(function() {
            let videos =  document.querySelectorAll("video");
            for (let i = 0, len = videos.length; i < len; i++ ) {
              videos[i].classList.add("height70")
              videos[i].classList.remove("height30")
              videos[i].classList.remove("height15")
            }
          })
        } else {
          this.$nextTick(function() {
            let videos =  document.querySelectorAll("video");
            for (let i = 0, len = videos.length; i < len; i++ ) {
              videos[i].classList.add("height30")
              videos[i].classList.remove("height70")
              videos[i].classList.remove("height15")
            }
          })
        }
      } else if (count == 3 || count == 5 || count == 6) {
        this.one = false
        this.two = false
        this.three = true
        this.eight = false
        this.twelve = false
        if (count == 3) {
          this.$nextTick(function() {
            let videos =  document.querySelectorAll("video");
            for (let i = 0, len = videos.length; i < len; i++ ) {
              videos[i].classList.add("height70")
              videos[i].classList.remove("height30")
              videos[i].classList.remove("height15")
            }
          })
        } else {
          this.$nextTick(function() {
            let videos =  document.querySelectorAll("video");
            for (let i = 0, len = videos.length; i < len; i++ ) {
              videos[i].classList.add("height30")
              videos[i].classList.remove("height70")
              videos[i].classList.remove("height15")
            }
          })
        }
      } else if (count == 7 || count == 8) {
        this.one = false
        this.two = false
        this.three = false
        this.eight = true
        this.twelve = false
        this.$nextTick(function() {
          let videos =  document.querySelectorAll("video");
          for (let i = 0, len = videos.length; i < len; i++ ) {
            videos[i].classList.add("height30")
            videos[i].classList.remove("height70")
            videos[i].classList.remove("height15")
          }
        })
      } else {
        this.one = false
        this.two = false
        this.three = false
        this.eight = false
        this.twelve = true
        if (count <= 12) {
          this.$nextTick(function() {
            let videos =  document.querySelectorAll("video");
            for (let i = 0, len = videos.length; i < len; i++ ) {
              videos[i].classList.add("height30")
              videos[i].classList.remove("height70")
              videos[i].classList.remove("height15")
            }
          })
        } else {
          this.$nextTick(function() {
            let videos =  document.querySelectorAll("video");
            for (let i = 0, len = videos.length; i < len; i++ ) {
              videos[i].classList.add("height15")
              videos[i].classList.remove("height70")
              videos[i].classList.remove("height30")
            }
          })
        }
      }
    }
  },
  watch: {
    theme() {
      this.$forceUpdate();
    },
    publisher(){
      this.$forceUpdate();
    },
    subscribers: {
      handler() {
        if (this.isSharingMode === false) {
          this.addClass()
        }
        else {
          this.$nextTick(function() {
            this.findParticipants()
            this.findSharer()
          })
        }
      } 
    },
    isSharingMode() {
      if (this.isSharingMode === false) {
        this.addClass()
        this.sharer = false
      } else {
        this.$nextTick(function() {
          this.findParticipants()
          this.findSharer()
        })
      }
    }
  },
  created() {
    this.findParticipants()
  },
  mounted() {
    if (this.one === true){
      this.$nextTick(function () {
        // 모든 화면이 렌더링된 후 실행합니다.
        let video = document.querySelector("video")
        video.classList.add("height70")
      })
    }
  },
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

.publisher-width {
  max-width: 60%;
  height: auto!important;
  margin-left: auto;
  margin-right: auto;
}

.sub-video {
  max-width: 10%;
  height: 15vh;
}

</style>
