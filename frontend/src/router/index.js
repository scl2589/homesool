import Vue from 'vue';
import VueRouter from 'vue-router';

import HomePage from '@/views/HomePage';
import MeetingPage from '@/views/MeetingPage';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage,
  },
  {
    path: '/meet',
    name: 'MeetingPage',
    component: MeetingPage,
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
