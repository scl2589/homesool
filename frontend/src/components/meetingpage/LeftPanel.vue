<template>
  <div>
    <div class="d-flex justify-content-center align-items-center mt-3">
      <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
      <img class="theme-title" :src="require(`@/assets/images/${theme}_title.png`)" alt="theme-title">
      <img class="theme-deco" :src="require(`@/assets/images/${theme}_deco.png`)" alt="theme-deco">
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
import { mapState } from 'vuex'
export default {
  name: 'LeftPanel',
  computed: {
    ...mapState('meetingStore', ['theme'])
  },
  watch: {
    theme() {
      this.$forceUpdate();
    }
  }
}
</script>

<style scoped>
.theme-title {
  max-width: 30vw;
  max-height: 20vh;
}

.theme-deco {
  width: 10vw;
}

.personal-screen {
  max-width: 20vw;
  height: 20vh;
  background-color: yellow;
  margin-top: 10vh;
}
</style>