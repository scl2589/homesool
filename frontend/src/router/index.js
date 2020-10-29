import Vue from 'vue';
import VueRouter from 'vue-router';

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

export default router;
