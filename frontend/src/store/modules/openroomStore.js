import SERVER from '@/api/api';
import axios from 'axios';

const openroomStore = {
  namespaced: true,
  state: {
    rooms : null,
    roomCount: 0,
    searchedRooms: null,
  },
  getters: {
  },
  mutations: {
    SET_ROOMS(state, value) {
      state.rooms = value
    },
    SET_ROOMCOUNT(state, value) {
      state.roomCount = Math.ceil(value/12)
    },
    SET_SEARCHED_ROOMS(state, value) {
      state.searchedRooms = value
    },
  },
  actions: {
    findRoomCount({ commit, rootGetters }) {
      axios.get(SERVER.URL + SERVER.ROUTES.roomCount, rootGetters.config)
        .then((res) => {
          commit('SET_ROOMCOUNT', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    fetchRooms({commit, rootGetters}, num) {
      axios.get(SERVER.URL + SERVER.ROUTES.rooms + num, rootGetters.config)
        .then((res) => {
          commit('SET_ROOMS', res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    searchRoom({ commit, rootGetters }, data) {
      axios.get(SERVER.URL + SERVER.ROUTES.searchRoom + data.search + '/' + data.pageNum , rootGetters.config)
      .then((res) => {
        commit('SET_SEARCHED_ROOMS', res.data)
      })
      .catch((err) => {
        console.log(err)
      })
    },
  }
}

export default openroomStore