import Vue from 'vue';
import Vuex from 'vuex';
// import http from '../utils/http-common.js';
// import jwt_decode from 'jwt-decode';

Vue.use(Vuex);

export default new Vuex.Store({
  // state: {
  //   isNew: null,
  //   token: null,
  //   user: null,
  // },
  // getters: {
  //   getToken: function(state) {
  //     return state.token;
  //   },
  //   getIsNew: function(state) {
  //     return state.isNew;
  //   },
  //   getUser: function(state) {
  //     return state.user;
  //   },
  // },
  // mutations: {
  //   setToken(state, payload) {
  //     state.token = payload;
  //   },
  //   setIsNew(state, payload) {
  //     state.isNew = payload;
  //   },
  //   setUser(state, payload) {
  //     state.user = payload;
  //   },
  // },
  // actions: {
  //   kakaoLogin(context, { access_token }) {
  //     console.log('로그인 실행');
  //     // http
  //     //   .post('login', { accessToken: access_token })
  //     //   .then(({ data }) => {
  //     //     context.commit('setIsNew', data.new);
  //     //     context.commit('setToken', data.token);
  //     //     var token = data.token;
  //     //     var id = jwt_decode(token).sub;
  //     //     console.log('jwt : ' + data.token);
  //     //     console.log('id : ' + id);
  //     //     http
  //     //       .get('user/' + id, {
  //     //         headers: { 'X-AUTH-TOKEN': data.token },
  //     //       })
  //     //       .then(({ data }) => {
  //     //         context.commit('setUser', data);
  //     //       });
  //     //   })
  //     //   .catch((err) => {
  //     //     console.error(err);
  //     //   });
  //   },
  // },
  // modules: {},
});
