<template>
  <div class="w-100 panel">
    <div class="d-flex row no-gutters">
      <div class="col-4 game-selection">
        <h6 class="mt-3">술게임을 골라주세요!</h6>
        <br>
        <li><router-link class="my-3" to="/">웃으면 술이와요</router-link></li>
        <li><router-link to="/">Up & Down</router-link></li>
        <li><router-link to="/">자음 퀴즈</router-link></li>
        <li><router-link to="/">라이어 게임</router-link></li>
        <li><router-link to="/">딸기 게임</router-link></li>

      </div>
      <div class="col-8">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GameSelectionPanel'
}
</script>

<style scoped>
.panel {
  background-color: #D2D2D2;
  height: 100%;
  max-height: 46vh;
}

h1, h2, h3, h4, h5, h6, p {
  color: black;
}

li {
  list-style: none;
  padding-top: 5px;
  padding-bottom: 5px;
}

li:hover {
  background-color: #D2D2D2;
}

.router-link-active {
  font-size: 0.8em;
  text-decoration: none;
  color: black;
}

.game-selection {
  height: 46vh;
  background-color: white;
  border-radius: 10px;
}


</style>