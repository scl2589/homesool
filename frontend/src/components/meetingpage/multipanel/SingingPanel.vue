<template>
  <div class="d-flex flex-column justify-content-between p-2 px-5 w-100">
    <h3>노래방</h3>
    <!-- <div id="player"></div> -->
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
    <div class="song-select" v-else>
      <v-text-field
        v-model="songKeyword"
        label="노래를 검색하세요 :)"
        color="#BDBDBD"
        dark
        @keyup.enter="searchSong(songKeyword)"
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
</template>

<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'SingingPanel',
  data() {
    return {
      songKeyword: null,
      ytPlayer: null,
      timerId: null,
      checksync: null
    }
  },
  computed: {
    ...mapState('meetingStore', ['selectedSong', 'songs', 'currentSongTime', 'singingHost', 'publisher'])
  },
  watch: {
    selectedSong(value) {
      if (value) {
        this.ytPlayer = new window.YT.Player('player', {});
      }
    },
    ytPlayer() {
      if (this.publisher.stream.connection.connectionId === this.singingHost) {
        this.checksync = setInterval(() => {
          if (!this.selectedSong) {
            clearInterval(this.checksync);
          }
          if (this.ytPlayer.getPlayerState() === 1) {
            let currentSongTime = this.ytPlayer.getCurrentTime();
            this.checkSongSync(currentSongTime);
          } else if (this.ytPlayer.getPlayerState() === 0) {
            this.selectSong(null);
            clearInterval(this.checksync);
          }
        }, 1000);
      }
    },
    currentSongTime(value) {
      if (this.selectedSong && !this.ytPlayer) {
        this.ytPlayer = new window.YT.Player('player', {});
      }
      console.log(Math.abs(value - this.ytPlayer.getCurrentTime()))
      if (Math.abs(value - this.ytPlayer.getCurrentTime()) > 1) {
        this.ytPlayer.seekTo(value);
      }
    }
  },
  methods: {
    ...mapActions('meetingStore', ['searchSong', 'selectSong', 'closeSingingPanel', 'checkSongSync']),
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
    }
  },
  beforeDestroy() {
    this.closeSingingPanel()
  }
}
</script>

<style scoped>
* {
  color: white;
  margin: 0;
  padding: 0;
}

.song-thumbnail {
  max-width: 150px;
  max-height: 84px;
  cursor: pointer;
}
</style>