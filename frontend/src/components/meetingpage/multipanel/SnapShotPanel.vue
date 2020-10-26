<template>
  <div class="d-flex flex-column justify-content-between p-2 w-100">
    <section class="countdown">
      <div v-show="!expired" class="timer">
        <h3>Snapshot</h3>
        <div class="box">
          <div class="spacer"></div>
          <p class="value">{{ remain }}</p>
          <p class="label">초 후</p>
        </div>
        <p class="text">사진찍어요~</p>
      </div>

      <div 
        v-show="expired"
        class="expired-timer timer"
      >
        <div class="d-flex justify-content-between">
          <button 
            class="btn btn-yellow"
            @click="retakePhoto"
          >
            재촬영
          </button>
          <h3>Snapshot</h3>
          <button
            class="btn btn-yellow"
            @click="savePhoto"
          >  
            저장
          </button>
        </div>
        <div class="box">
          <div class="spacer"></div>
          <p class="value">
            <img src="../../../assets/images/sample.png">
          </p>
          <p class="label">여러분의 사진입니다! 마음에 드시나요?</p>
        </div>
      </div>
    
    </section>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'SnapShotPanel',
  data() {
    return {
      seconds: 'CAPTUREE!',
      remain: 5,
      expired: false,
    }
  },
  computed: {
    ...mapState('meetingStore', ['isSnapshotMode']),
  },
  watch: {
    remain: {
      handler(value) {
        if (value > 0) {
          setTimeout(() => {
            this.remain--;
          }, 1000)
        } else {
          this.expired = true;
        }
      },
      immediate: true
    }
  },
  methods: {
    ...mapActions('meetingStore', ['closeMultiPanel', 'startSnapshotMode']),
    retakePhoto() {
      this.closeMultiPanel()
      setInterval(() => {
        this.startSnapshotMode()
      }, 500);
    },
    savePhoto() {}
  }
}
</script>

<style scoped>
* {
  color: white;
  margin: 0;
}

p {
  color: black;
}


</style>

<style lang="scss" scoped>
// FONTS
@import url('https://fonts.googleapis.com/css?family=Graduate|Quicksand:300,400,700');
$main-font: 'Quicksand', sans-serif;
$num-family: 'Graduate', sans-serif;

// COLORS
$white: #fff;
$black: #000;
//https://coolors.co/app/ffcdb2-ffb4a2-e5989b-b5838d-6d6875
$palette: #FFCDB2, #FFB4A2, #E5989B, #B5838D, #6D6875;


// GENERAL
*, *::before, *::after{
  box-sizing: border-box;
}
body{
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: $main-font;
  font-size: 16px;
  background: nth($palette, 1);
}


// COUNTDOWN STYLES
.countdown{
  text-align: center;
}
.timer{
  perspective: 1000px;
  .box{
    display: inline-block;
    width: 10rem;
    text-align: center;
    background: nth($palette, 3);
    //needed for firestorm
    -moz-transform-style: preserve-3d; 
    &:nth-child(2) .value{
      animation-delay: 1s;

    }
    &:nth-child(3) .value{
      animation-delay: 1.8s;

    }
    &:nth-child(4) .value{
      animation-delay: 1.3s;

    }
    &:nth-child(5) .value{
      animation-delay: 2s;

    }
    .spacer{
      position: relative;
      content: '';
      display: block;
      height: 5px;
      background: nth($palette, 2);
      &::before, &::after{
        content: '';
        position: absolute;
        top: 5px;
        width: 10px;
        height: 5px;
        background: nth($palette, 1);
        border-radius: 50%;
      }
      &::before{
        left: .8rem;
      }
      &::after{
        right: .8rem;
      }
    }
    .value{
      position: relative;
      margin: 0;
      padding: 0.5rem 0 1rem;
      font-size: 3em;
      color: rgba(nth($palette, 5), .6);
      background: nth($palette, 2);
      transform-origin: top center;
      animation: wind 4s ease-out alternate infinite;
      box-shadow: 0 15px 10px -10px rgba(nth($palette, 4), 0);

      img {
        max-width: 90%;
      }
    }
    .label{
      margin: 0;
      padding: 0.5rem;
      color: rgba($white, .8);
      background: nth($palette, 3);
      &::first-letter{
        text-transform: uppercase;
      }
    }
  }
  .text{
    margin-top: 2rem;
    font-weight: 300;
    font-size: 1.25em;
    text-transform: uppercase;
    letter-spacing: 4px;
    text-align: center;
    color: nth($palette, 4);
  }
}
.expired-timer{
  .box{
    width: 80%;
  }
}


// ANIMATIONS
@keyframes wind{
  0%{
    transform: rotatex(0);
    box-shadow: 0 15px 10px -10px rgba(nth($palette, 4), 0);
    color: rgba(nth($palette, 5), .6);
    background: nth($palette, 2);
  }
  10%{
    transform: rotatex(20deg);
    box-shadow: 0 15px 10px -10px darken(nth($palette, 4), 5%);
    color: rgba(nth($palette, 5), .45);
    background: lighten(nth($palette, 2), 3%);
  }
}

// RESPONSIVE
@media screen and (max-width: 800px){
  .countdown{
    width: 100%;
    max-width: 25rem;
  }
}
@media screen and (max-width: 480px){
  .timer{
    .box{
      margin: 1rem .5rem;
      width: 8rem;
    }
  }
  .expired-timer{
    .box{
      width: 80vw;
    }
  }
}
</style>
