<template>
  <div class="w-100 panel">
    <div class="d-flex row no-gutters">
      <div class="col-4 game-selection">
        <div class="top">
          <h6 class="mt-3">술게임을 골라주세요!</h6>
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
      <div class="col-8">
        <transition name="slide" mode="out-in">
          <router-view></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "GameSelectionPanel",
};
</script>

<style scoped>
.panel {
  background-color: #d2d2d2;
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
  color: black;
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
  color: black;
}

.router-link-exact-active {
  color: #ECFF1E;
  text-shadow: 1px 1px 2px rgb(0, 0, 0, 0.7);
}

.game-selection {
  height: 46vh;
  background-color: white;
  border-radius: 10px;
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