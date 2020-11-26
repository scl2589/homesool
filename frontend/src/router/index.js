import Vue from 'vue';
import VueRouter from 'vue-router';
import store from "../store";

import HomePage from '@/views/HomePage';
import RegisterPage from '@/views/RegisterPage';
import ProfilePage from '@/views/ProfilePage';
import MeetingPage from '@/views/MeetingPage';
import OpenRoom from '@/views/OpenRoom/'
// MyPage
import MyPage from '@/views/MyPage';
import Analysis from '@/components/mypage/Analysis'
import Calendar from '@/components/mypage/Calendar'

import Swal from 'sweetalert2'


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
    path: '/profile',
    name: 'ProfilePage',
    component: ProfilePage,
  },
  {
    path: '/meet/:sessionId',
    name: 'MeetingPage',
    component: MeetingPage,
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
  }, 
  {
    path:'/open',
    name: 'OpenRoom',
    component: OpenRoom
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
    Swal.fire({
      title: "로그인을 해주세요 😊",
      icon: "error",
    })
    // 로그인이 되어있지 않지만 Meeting Page 코드와 함께 들어가는 경우
    if (to.name === "MeetingPage") {
      // store에 sessionId를 저장해둔다.
      // 카카오 로그인 시 sessionId가 저장되어 있다면 meetingPage로 해당 코드와 함께 모달을 띄운다.
      store.commit("SET_INVITED_SESSIONID", to.params.sessionId);
    }
    next({ name: "HomePage" });
  } else {
    // 이미 로그인이 되어있는데 meeting page로 들어가는 경우/새로고침하는 경우
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
