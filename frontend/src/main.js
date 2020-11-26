import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import vueMoment from 'vue-moment'
import secrets from './secrets'
import VueCookies from 'vue-cookies'
import CircularCountDownTimer from "vue-circular-count-down-timer";

Vue.use(VueCookies);
Vue.use(vueMoment);
Vue.use(CircularCountDownTimer);

//  calendar
import VCalendar from 'v-calendar';

// Use v-calendar & v-date-picker components
Vue.use(VCalendar, {
  componentPrefix: 'vc',  // Use <vc-calendar /> instead of <v-calendar />
  locales: {
    'ko-KR': {
      firstDayOfWeek: 1,
      masks: {
        L: 'YYYY-MM-DD',
        // ...optional `title`, `weekdays`, `navMonths`, etc
        title: 'YYYY년 MM월',
        dayPopover: 'YYYY년 MM월 DD일(WWW)'
      }
    }
  }
})

// kakao login
window.Kakao.init(secrets['KAKAO']['CLIENT_ID']);

// Firebase
import firebase from 'firebase'
var firebaseConfig = {
  apiKey: secrets['FIREBASE']['SECRET_KEY'],
  authDomain: "homesuli.firebaseapp.com",
  databaseURL: "https://homesuli.firebaseio.com",
  projectId: "homesuli",
  storageBucket: "homesuli.appspot.com",
  messagingSenderId: "650609487122",
  appId: "1:650609487122:web:37764da60a551d5e2317cb",
  measurementId: "G-0S8YBKTQBW"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.analytics();

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
