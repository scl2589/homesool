import Vue from 'vue';
import Vuex from 'vuex';

import SERVER from '@/api/api';
import jwt_decode from 'jwt-decode';
import axios from 'axios';
import router from '@/router';
import cookies from 'vue-cookies';

import Swal from "sweetalert2";

import meetingStore from '@/store/modules/meetingStore';
import mypageStore from '@/store/modules/mypageStore';
import openroomStore from '@/store/modules/openroomStore'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isNew: null,
    token: cookies.get('auth-token'),
    user: null,
    invitedSessionId: null
  },
  getters: {
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
    SET_TOKEN(state, payload) {
      state.token = payload;
    },
    SET_IS_NEW(state, payload) {
      state.isNew = payload;
    },
    SET_USER(state, payload) {
      state.user = payload;
    },
    SET_INVITED_SESSIONID(state, sessionId) {
      state.invitedSessionId = sessionId;
    }
  },
  actions: {
    kakaoLogin({ state, commit, dispatch }, { access_token }) {
      axios.post(SERVER.URL + SERVER.ROUTES.login, { accessToken: access_token })
        .then(({ data }) => {
          commit('SET_IS_NEW', data.new);
          commit('SET_TOKEN', data.token);
          dispatch('getMyInfo');
          if (data.new === true) {
            cookies.set('auth-token', data.token)
            router.push({ name: 'RegisterPage' })
          } else {
            cookies.set('auth-token', data.token)
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 2000,
              timerProgressBar: true,
              onOpen: (toast) => {
                toast.addEventListener("mouseenter", Swal.stopTimer);
                toast.addEventListener("mouseleave", Swal.resumeTimer);
              },
            });
            Toast.fire({
              icon: "success",
              title: "로그인에 성공하였습니다.",
            });
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
          commit('SET_USER', res.data);
        })
        .catch((err) => {
          dispatch('kakaoLogout');
          console.error(err.response.data);
        })
    },
    kakaoLogout({ commit }) {
      commit('SET_IS_NEW', null);
      commit('SET_TOKEN', null);
      commit('SET_USER', null);
      cookies.remove('auth-token');
      const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2000,
        timerProgressBar: true,
        onOpen: (toast) => {
          toast.addEventListener("mouseenter", Swal.stopTimer);
          toast.addEventListener("mouseleave", Swal.resumeTimer);
        },
      });
      Toast.fire({
        icon: "success",
        title: "로그아웃에 성공하였습니다.",
      });
      router.push({ name: 'HomePage' }).catch(() => {});
    },
  },
  modules: {
    meetingStore: meetingStore,
    mypageStore: mypageStore,
    openroomStore: openroomStore,
  },
});
