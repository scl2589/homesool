<template>
  <div>
    <div class="d-flex justify-content-center align-items-center">
      <img class="theme-deco" src="@/assets/images/basic_deco.png" alt="가랜드">
      <img class="theme-title" src="@/assets/images/basic_title.png" alt="술이술이홈술이">
      <img class="theme-deco" src="@/assets/images/basic_deco.png" alt="가랜드">
    </div>
    <div class="row no-gutters d-flex justify-content-around">
      <div class="col-4 personal-screen">
        여기에 사람들 얼굴쓰
      </div>
      <div class="col-4 personal-screen">
        여기에 사람들 얼굴쓰
      </div>
      <div class="col-4 personal-screen">
        여기에 사람들 얼굴쓰
      </div>
      <div class="col-4 personal-screen">
        여기에 사람들 얼굴쓰
      </div>
      <div class="col-4 personal-screen">
        여기에 사람들 얼굴쓰
      </div>
      <div class="col-4 personal-screen">
        여기에 사람들 얼굴쓰
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LeftPanel'
}
</script>

<style scoped>
.theme-title {
  width: 50%;
}

.theme-deco {
  width: 15%;
}

.personal-screen {
  max-width: 20vw;
  height: 20vh;
  background-color: yellow;
  margin-top: 10vh;
}
</style>