<template>
  <div class="d-flex flex-column align-items-center justify-content-between p-2">
    <h3>진실의 방</h3>
    <img
      class="my-3"
      :src="require('@/assets/images/freedom.png')" 
      alt="heart unlocked"
    >
    <p>
      속에 있는 이야기를 마음껏 해보세요.<br>
      홈술이가 화면, 목소리, 채팅 다 가려 드립니다.
    </p>
    <button
      class="btn btn-yellow"
      @click="clickEndAnonymousMode"
    >
      진실의 방 끝내기
    </button>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  name: 'AnonymousPanel',
  
  methods: {
    ...mapActions('meetingStore', [
      'changeMode',
      'endAnonymousMode'
    ]),
    clickEndAnonymousMode() {
      this.changeMode(null);
    }
  },

  beforeDestroy() {
    this.endAnonymousMode();
  }
}
</script>

<style scoped>
* {
  color: white;
  margin: 0;
  padding: 0;
}

img {
  max-width: 30%;
}
</style>