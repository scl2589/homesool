import SERVER from '@/api/api';
import axios from 'axios';

const openroomStore = {
  namespaced: true,
  state: {
    rooms : null,
    roomCount: 0,
    searchNameRooms: null,
    searchTagRooms: null,
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
    SET_SEARCH_NAME(state, value) {
      state.searchNameRooms = value
    },
    SET_SEARCH_TAG(state, value) {
      state.searchTagRooms = value 
    }
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
    searchName({ commit, rootGetters }, search) {
      axios.get(SERVER.URL + SERVER.ROUTES.searchName + search + '/1' , rootGetters.config)
      .then((res) => {
        commit('SET_SEARCH_NAME', res.data)
      })
      .catch((err) => {
        console.log(err)
      })
    },
    searchTag({ commit, rootGetters }, search) {
      axios.get(SERVER.URL + SERVER.ROUTES.searchTag + search + '/1' , rootGetters.config)
      .then((res) => {
        commit('SET_SEARCH_TAG', res.data)
      })
      .catch((err) => {
        console.log(err)
      })
    }
  }
}

export default openroomStore