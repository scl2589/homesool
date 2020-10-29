import Vue from 'vue';
import Vuex from 'vuex';

import jwt_decode from 'jwt-decode';
// import http from '../utils/http-common.js';
import axios from 'axios';
import router from '@/router';
import cookies from 'vue-cookies';

import SERVER from '@/api/api';
import meetingStore from '@/store/modules/meetingStore';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isNew: null,
    token: cookies.get('auth-token'),
    user: null,
    id: null,
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
  },
  actions: {
    kakaoLogin(context, { access_token }) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.login, { accessToken: access_token })
        .then(({ data }) => {
          context.commit('setIsNew', data.new);
          context.commit('setToken', data.token);
          var token = data.token;
          var id = jwt_decode(token).sub;
          context.commit('setId', id);
          axios
            .get(SERVER.URL + SERVER.ROUTES.user + '/' + id, {
              headers: { 'X-AUTH-TOKEN': data.token },
            })
            .then(({ data }) => {
              context.commit('setUser', data);
            });
          console.log(data.new);
          if (data.new === true) {
            cookies.set('auth-token', token);
            router.push({ name: 'RegisterPage' });
          } else {
            cookies.set('auth-token', token);
          }
        })
        .catch((err) => {
          console.error(err);
        });
    },
    kakaoLogout(context) {
      localStorage.clear();
      cookies.set('auth-token', null);
      context.commit('setIsNew', null);
      context.commit('setToken', null);
      context.commit('setUser', null);
      context.commit('setId', null);
      cookies.remove('auth-token');
      // cookies.set('auth-token', null);
      router.push({ name: 'HomePage' }).catch(() => {});
      // router.push({ name: '' });
    },
  },
  modules: {
    meetingStore: meetingStore,
  },
});
