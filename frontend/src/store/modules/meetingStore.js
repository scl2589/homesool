import router from "../../router"
import SERVER from '@/api/api'
import secrets from '@/secrets'
import axios from 'axios'

const meetingStore = {
  namespaced: true,
  state: {
    isGameMode: false,
    isSingingMode: false,
    isAnonymousMode: false,
    isSnapshotMode: false,
    isChatPanel: false,
    selectedSong: null,
    songs: null,
    theme: 'basic',
  },
  getters: {
  },
  mutations: {
    SET_ISGAME_MODE(state, value) {
      state.isGameMode = value
    },
    SET_ISSINGING_MODE(state, value) {
      state.isSingingMode = value
    },
    SET_ISANONYMOUS_MODE(state, value) {
      state.isAnonymousMode = value
    },
    SET_ISSNAPSHOT_MODE(state, value) {
      state.isSnapshotMode = value
    },
    SET_CHATPANEL(state, value) {
      state.isChatPanel = value
    },
    SET_SELECTED_SONG(state, song) {
      state.selectedSong = song
    },
    SET_SONGS(state, songs) {
      state.songs = songs
    },
    SET_THEME(state, theme) {
      state.theme = theme
    }
  },
  actions: {
    startGameMode({ commit }) {
      commit('SET_ISANONYMOUS_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISGAME_MODE', true)
    },
    startSingingMode({ commit }) {
      commit('SET_ISANONYMOUS_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSINGING_MODE', true)
    },
    startAnonymousMode({ commit }) {
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISANONYMOUS_MODE', true)
    },
    startSnapshotMode({ commit }) {
      commit('SET_ISSNAPSHOT_MODE', true)
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISANONYMOUS_MODE', false)
      
    },
    closeMultiPanel({ commit }) {
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISANONYMOUS_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      router.push({ name: 'MeetingPage'})
    },
    clickChatPanel({ commit }, value) {
      commit('SET_CHATPANEL', value)
    },
    searchSong({ commit }, keyword) {
      axios.get(SERVER.YOUTUBE_URL, {
        params: {
          key: secrets.YOUTUBE.SECRET_KEY[Math.floor(Math.random() * secrets.YOUTUBE.SECRET_KEY.length)],
          part: 'snippet',
          type: 'video',
          q: '[KY 금영노래방]' + keyword,
          maxResults: 4
        }
      })
        .then(res => {
          res.data.items.forEach(item => {
            const parser = new DOMParser()
            const doc = parser.parseFromString(item.snippet.title, 'text/html')
            item.snippet.title = doc.body.innerText
          })
          commit('SET_SONGS', res.data.items)
        })
    },
    selectSong({ commit }, song) {
      commit('SET_SELECTED_SONG', song)
      console.log(document.getElementById('test'))
    },
    closeSingingPanel({ commit }) {
      commit('SET_SONGS', null)
      commit('SET_SELECTED_SONG', null)
    },
    changeTheme({ commit }, theme) {
      commit('SET_THEME', theme)
    }
  }

}

export default meetingStore