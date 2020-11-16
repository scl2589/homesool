import Vue from 'vue';
import Vuex from 'vuex';

import jwt_decode from 'jwt-decode';
// import http from '../utils/http-common.js';
import axios from 'axios';
import router from '@/router';
import cookies from 'vue-cookies';

import SERVER from '@/api/api';
import meetingStore from '@/store/modules/meetingStore';
import mypageStore from '@/store/modules/mypageStore';


Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isNew: null,
    token: cookies.get('auth-token'),
    user: null,
    id: null,
    invitedSessionId: null
  },
  getters: {
    getToken: function(state) {
      return state.token;
    },
    getIsNew: function(state) {
      return state.isNew;
    },
    getUser: function(state) {
      return state.user;
    },
    getId: function(state) {
      if (state.token) {
        return jwt_decode(state.token).sub;
      } else {
        return false;
      }
    },
    config: (state) => ({ headers: { 'X-AUTH-TOKEN': state.token } }),
  },
  mutations: {
    setToken(state, payload) {
      state.token = payload;
    },
    setIsNew(state, payload) {
      state.isNew = payload;
    },
    setUser(state, payload) {
      state.user = payload;
    },
    setId(state, payload) {
      state.id = payload;
    },
    SET_INVITED_SESSIONID(state, sessionId) {
      state.invitedSessionId = sessionId;
    }
  },
  actions: {
    kakaoLogin({ state, commit, dispatch }, { access_token }) {
      axios.post(SERVER.URL + SERVER.ROUTES.login, { accessToken: access_token })
        .then(({ data }) => {
          commit('setIsNew', data.new);
          commit('setToken', data.token);
          dispatch('getMyInfo');
          if (data.new === true) {
            cookies.set('auth-token', data.token)
            router.push({ name: 'RegisterPage' })
          } else {
            cookies.set('auth-token', data.token)
          }
          if (state.invitedSessionId) {
            dispatch("meetingStore/checkSessionId", state.invitedSessionId)
              .then(() => {
                dispatch("meetingStore/changeMeetingDialog", true);
                commit('SET_INVITED_SESSIONID', null);
              })
          }
        }) 
        .catch((err) => {
          console.error(err);
        });
    },
    getMyInfo({ commit, getters, dispatch }) {
      axios.get(SERVER.URL + SERVER.ROUTES.user + '/' + getters.getId, getters.config)
        .then((res) => {
          commit('setUser', res.data);
        })
        .catch((err) => {
          dispatch('kakaoLogout');
          console.error(err.response.data);
        })
    },
    kakaoLogout(context) {
      localStorage.clear();
      cookies.set('auth-token', null);
      context.commit('setIsNew', null);
      context.commit('setToken', null);
      context.commit('setUser', null);
      context.commit('setId', null);
      cookies.remove('auth-token');
      router.push({ name: 'HomePage' }).catch(() => {});
    },
  },
  modules: {
    meetingStore: meetingStore,
    mypageStore: mypageStore,
  },
});
