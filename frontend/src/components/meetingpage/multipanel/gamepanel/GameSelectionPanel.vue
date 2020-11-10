<template>
  <div class="panel">
    <!-- !modeHost -->
    <div v-if="notModeHost">
      <p>{{ notModeHost.name }}님이 게임을 고르는 중입니다 :)</p>
    </div>

    <!-- modeHost -->
    <div class="d-flex row no-gutters" v-else>
      <div class="col-4 game-selection my-1">
        <div class="top">
          <h6 class="mt-5">술게임을 골라주세요!</h6>
          <br />
        </div>
        <div class="game-list">
          <li><router-link class="my-3" :to="{name: 'SmileLeadsToAlcoholDescription'}">웃으면 술이와요</router-link></li>
          <li><router-link :to="{ name: 'UpAndDownDescription' }">Up & Down</router-link></li>
          <li><router-link :to="{ name: 'ConsonantQuizDescription' }">자음 퀴즈</router-link></li>
          <li><router-link :to="{ name: 'LiarGameDescription' }">라이어 게임</router-link></li>
          <li><router-link :to="{ name: 'StrawberryGameDescription' }">딸기 게임</router-link></li>
        </div>
      </div>
      <div class="col-8 my-1">
        <transition name="slide" mode="out-in">
          <router-view></router-view>
        </transition>
      </div>
    </div>

  </div>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  name: "GameSelectionPanel",
  computed: {
    ...mapGetters('meetingStore', ['notModeHost'])
  }
};
</script>

<style scoped>
.panel {
  background-color: #707070;
  height: 100%;
  max-height: 46vh;
}

h1,
h2,
h3,
h4,
h5,
h6,
p {
  color: white;
}

.top {
  height: 22%
}

.game-list {
  overflow-y: auto;
  height: 78%;
}

li{
  list-style: none;
  padding-top: 5px;
  padding-bottom: 5px;
}

li:hover {
  background-color: #d2d2d2;
}

.router-link-active, li > a {
  font-size: 0.8em;
  text-decoration: none;
  color: white;
}

li:hover > a {
  color: black;
}

.router-link-exact-active {
  color: #ECFF1E;
  text-shadow: 1px 1px 2px rgb(0, 0, 0, 0.7);
}

.game-selection {
  height: 45vh;
  background-color: black;
  border-right: 1px solid #707070;
  border-radius: 15px;
}

.slide-leave-active {
  transition: opacity 0.3s ease;
  opacity: 0;
  animation: slide-out 0.3s ease-out forwards;
}
.slide-leave {
  opacity: 1;
  transform: translateX(0);
}
.slide-enter-active {
  animation: slide-in 0.3s ease-out forwards;
}
@keyframes slide-out {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-30px);
  }
}
@keyframes slide-in {
  0% {
    transform: translateY(-30px);
  }
  100% {
    transform: translateY(0);
  }
}
</style>