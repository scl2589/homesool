<template>
  <div class="d-flex flex-column justify-content-center p-2 px-5 w-100">
    <h3>홈술이 노래방</h3>
    <div
      class="d-flex justify-content-center mt-3"
      v-if="!selectedSong & flag"
    >
      <img
        src="@/assets/images/microphone.png"
        width="100vh"
        @click="clickSelectSong(song)"
      >
    </div>

    <!-- 노래 중 -->
    <div class="song-screen" v-if="selectedSong">
      <div class="embed-responsive embed-responsive-16by9">
        <iframe
          class="embed-responsive-item"
          id="player"
          :src="`https://www.youtube.com/embed/${selectedSong.id.videoId}?rel=0&amp;autoplay=1&amp;enablejsapi=1&amp;version=3&amp;playerapiid=ytplayer`"
          frameborder="0"
          allow="autoplay; encrypted-media"
        ></iframe>
      </div>
    </div>

    <!-- 노래 없음 -->
    <div v-else>
      <!-- 노래가 막 끝났을 경우 -->
      <div v-if="isSongEnded">
        <p>노래가 끝났습니다.</p>
        <div class="d-flex justify-content-around">
          <button
            class="btn btn-yellow"
            @click="changeMode(null)"
          >
            노래방 끝내기
          </button>
          <button
            class="btn btn-yellow"
            @click="changeMode('singing')"
          >
            노래 고르기
          </button>
        </div>
      </div>
      
      <div v-else>
        <!-- 선곡 중(!modeHost) -->
        <div 
          class="mt-3"
          v-if="notModeHost"
        >
          <p><span class="color-yellow">{{ notModeHost.name }}</span>님이 선곡 중입니다 :)</p>
        </div>

        <!-- 선곡 중(modeHost) -->
        <div class="song-select" v-else>
          <v-text-field
            v-model="songKeyword"
            dark
            label="노래를 검색하세요 :)"
            color="#BDBDBD"
            @keyup.enter="enterSong(songKeyword)"
          ></v-text-field>
          <div class="row">
            <div
              class="my-1"
              :class="{'col-6' : isGumyoung(song), 'col-0' : !isGumyoung(song) }"
              v-for="song in songs"
              :key="song.etag"
              v-show="isGumyoung(song)"
            >
              <img
                class="song-thumbnail"
                :src="song.snippet.thumbnails.medium.url"
                :alt="song.snippet.title"
                @click="clickSelectSong(song)"
              >
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex'
export default {
  name: 'SingingPanel',

  data() {
    return {
      songKeyword: null,
      ytPlayer: null,
      checksync: null,
      flag: true
    }
  },

  computed: {
    ...mapState('meetingStore', [
      'selectedSong',
      'songs',
      'currentSongTime',
      'isSongEnded',
    ]),
    ...mapGetters('meetingStore', ['notModeHost'])
  },

  watch: {
    selectedSong(value) {
      if (value) {
        setTimeout(() => {
          this.ytPlayer = new window.YT.Player('player', {});
        }, 0.5);
      } else {
        this.ytPlayer = null;
        this.checksync = null;
      }
    },
    ytPlayer() {
      if (this.ytPlayer && !this.notModeHost) {
        this.checksync = setInterval(() => {
          if (!this.selectedSong) {
            this.ytPlayer = null;
            clearInterval(this.checksync);
          } else {
            if (this.ytPlayer.getPlayerState() === 0) {
              this.selectSong(null);
              clearInterval(this.checksync);
            } else if (this.ytPlayer.getPlayerState() === 1) {
              this.checkSongSync(this.ytPlayer.getCurrentTime());
            }
          }
        }, 500);
      }
    },
    currentSongTime(value) {
      if (this.notModeHost) {
        if (!this.ytPlayer) {
          this.ytPlayer = new window.YT.Player('player', {});
        }
        if (this.selectedSong) {
          if (Math.abs(value - this.ytPlayer.getCurrentTime()) > 0.5) {
            this.ytPlayer.seekTo(value);
          }
        }
      }
    }
  },

  methods: {
    ...mapActions('meetingStore', [
      'searchSong',
      'selectSong',
      'checkSongSync',
      'changeMode',
      'endSingingMode'
    ]),
    isGumyoung(song) {
      if (song.snippet.channelTitle === '금영 노래방 공식 유튜브 채널') {
        return true
      } else {
        return false
      }
    },
    clickSelectSong(song) {
      this.selectSong(song);
      this.songKeyword = null;
    },
    enterSong(song){
      this.searchSong(song)
      this.flag = false
    }
  },

  beforeDestroy() {
    clearInterval(this.checksync);
    this.endSingingMode();
  }
}
</script>

<style scoped>
h3, p {
  color: white;
  margin: 0;
  padding: 0;
}

.song-thumbnail {
  max-width: 10vw;
  max-height: 10vh;
  cursor: pointer;
}
</style>