import Vue from 'vue';
import VueRouter from 'vue-router';
import store from "../store";

import HomePage from '@/views/HomePage';
import RegisterPage from '@/views/RegisterPage';
import MeetingPage from '@/views/MeetingPage';
// MyPage
import MyPage from '@/views/MyPage';
import Analysis from '@/components/mypage/Analysis'
import Calendar from '@/components/mypage/Calendar'

// Game Selection
import SmileLeadsToAlcoholDescription from '@/components/meetingpage/multipanel/gamedescription/SmileLeadsToAlcoholDescription';
import UpAndDownDescription from '@/components/meetingpage/multipanel/gamedescription/UpAndDownDescription';
import StrawberryGameDescription from '@/components/meetingpage/multipanel/gamedescription/StrawberryGameDescription';
import LiarGameDescription from '@/components/meetingpage/multipanel/gamedescription/LiarGameDescription';
import ConsonantQuizDescription from '@/components/meetingpage/multipanel/gamedescription/ConsonantQuizDescription';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage,
  },
  {
    path: '/register',
    name: 'RegisterPage',
    component: RegisterPage,
  },
  {
    path: '/meet/:sessionId',
    name: 'MeetingPage',
    component: MeetingPage,
    children: [
      // Game Selection
      {
        path: 'smile',
        component: SmileLeadsToAlcoholDescription,
        name: 'SmileLeadsToAlcoholDescription',
      },
      {
        path: 'upanddown',
        component: UpAndDownDescription,
        name: 'UpAndDownDescription',
      },
      {
        path: 'strawberry',
        component: StrawberryGameDescription,
        name: 'StrawberryGameDescription',
      },
      {
        path: 'liar',
        component: LiarGameDescription,
        name: 'LiarGameDescription',
      },
      {
        path: 'consonant',
        component: ConsonantQuizDescription,
        name: 'ConsonantQuizDescription',
      },
    ],
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPage,
    children: [
      {
        path: 'calendar',
        component: Calendar,
        name: 'Calendar'
      },
      {
        path: 'statistics',
        component: Analysis,
        name: 'Analysis'
      }
    ]
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  const publicPages = [
    "HomePage",
  ]; // Login 안해도 됨
  const authPages = [
  ]; // Login 되어있으면 안됨
  const authRequired = !publicPages.includes(to.name); // 로그인 해야하는 페이지면 true 반환
  const unauthRequired = authPages.includes(to.name);
  const isLoggedIn = Vue.$cookies.isKey("auth-token");

  if (unauthRequired && isLoggedIn) {
    next({ name: "HomePage" });
  }

  if (authRequired && !isLoggedIn) {
    alert('로그인을 해주세요 :)');
    if (to.name === "MeetingPage") {
      store.commit("SET_INVITED_SESSIONID", to.params.sessionId);
    }
    next({ name: "HomePage" });
  } else {
    if (to.name === "MeetingPage") {
      if (!store.state.meetingStore.mySessionId) {
        store.dispatch("meetingStore/checkSessionId", to.params.sessionId)
          .then(() => {
            store.dispatch("meetingStore/changeMeetingDialog", true);
            next({ name: "HomePage" });
          })
      } else {
        next();
      }
    } else {
      next();
    }
  }
});

export default router;
