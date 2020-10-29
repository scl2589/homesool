import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import vueMoment from 'vue-moment'
import secrets from './secrets'

Vue.use(vueMoment);
// kakao login
window.Kakao.init(secrets['KAKAO']['CLIENT_ID']);

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
