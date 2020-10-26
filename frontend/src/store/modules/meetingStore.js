const meetingStore = {
  namespaced: true,
  state: {
    isMultiPanel: false,
    isChatPanel: false,
  },
  getters: {
  },
  mutations: {
    SET_ISMULTIPANEL(state, value) {
      state.isMultiPanel = value
    },
    SET_ISCHATPANEL(state, value) {
      state.isChatPanel = value
    }
  },
  actions: {

  }

}

export default meetingStore