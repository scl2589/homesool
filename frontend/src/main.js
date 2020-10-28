import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import router from './router'
import store from './store'

import secrets from './secrets'

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

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
