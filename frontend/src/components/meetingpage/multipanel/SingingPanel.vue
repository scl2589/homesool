<template>
  <div class="d-flex flex-column justify-content-between p-2 px-5 w-100">
    <h3>노래방</h3>
    <div class="song-screen" v-if="selectedSong">
      <div class="embed-responsive embed-responsive-16by9">
        <iframe
          class="embed-responsive-item"
          :src="`https://www.youtube.com/embed/${selectedSong.id.videoId}?autoplay=1`"
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
            @click="selectSong(song)"
          >
          <!-- <p>{{ song.snippet.title }}</p> -->
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
      songKeyword: null
    }
  },
  computed: {
    ...mapState('meetingStore', ['selectedSong', 'songs'])
  },
  methods: {
    ...mapActions('meetingStore', ['searchSong', 'selectSong', 'closeSingingPanel']),
    isGumyoung(song) {
      if (song.snippet.channelTitle === '금영 노래방 공식 유튜브 채널') {
        return true
      } else {
        return false
      }
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