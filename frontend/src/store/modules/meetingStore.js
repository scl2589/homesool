const meetingStore = {
  namespaced: true,
  state: {
    isGameMode: false,
    isMusicMode: false,
    isAnonymousMode: false,
    isSnapshotMode: false
  },
  getters: {
  },
  mutations: {
    SET_ISGAME_MODE(state, value) {
      state.isGameMode = value
    },
    SET_ISMUSIC_MODE(state, value) {
      state.isGameMode = value
    },
    SET_ISANONYMOUS_MODE(state, value) {
      state.isAnonymousMode = value
    },
    SET_ISSNAPSHOT_MODE(state, value) {
      state.isSnapshotMode = value
    }
  },
  actions: {
    startGameMode({ commit }) {
      commit('SET_ISGAME_MODE', true)
    },
    startMusicMode({ commit }) {
      commit('SET_ISMUSIC_MODE', true)
    },
    startAnonymousMode({ commit }) {
      commit('SET_ISANONYMOUS_MODE', true)
    },
    startSnapshotMode({ commit }) {
      commit('SET_ISSNAPSHOT_MODE', true)
    }
  }

}

export default meetingStore