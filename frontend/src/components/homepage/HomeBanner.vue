<template>
  <v-carousel
    cycle
    height="250"
    hide-delimiter-background
  >
  <!-- 이거 for문 돌리고 싶은데 배열에 src 주소 넣고 :src로 돌린다음에 받아오니까 이미지 안뜸 -->
    <v-carousel-item>
      <v-sheet height="100%">
        <v-row
          class="fill-height"
          align="center"
          justify="center"
        >
          <div class="display-3">
            <div> 언제까지 어깨춤을 추게할거야~ </div>
            <img src="../../../public/img/partytemp1.jpg" alt="배너1" class="banner">
          </div>
        </v-row>
      </v-sheet>
    </v-carousel-item>
    <v-carousel-item>
      <v-sheet height="100%">
        <v-row
          class="fill-height"
          align="center"
          justify="center"
        >
          <div class="display-3">
              <div> 이 놀라운 기능들이<br>모두 다 술이술이홈술이라는 사실 </div>
            <img src="@/assets/images/partytemp2.jpg" alt="배너1" class="banner">
          </div>
        </v-row>
      </v-sheet>
    </v-carousel-item>
    <v-carousel-item>
      <v-sheet height="100%">
        <v-row
          class="fill-height"
          align="center"
          justify="center"
        >
          <div class="display-3">
              <div> 언택트 술모임! </div>
            <img src="@/assets/images/partytemp3.jpg" alt="배너1" class="banner">
          </div>
        </v-row>
      </v-sheet>
    </v-carousel-item>
  </v-carousel>
</template>

<script>
  export default {
    data () {
      return {
        colors: [
          'indigo',
          'warning',
          'pink darken-2',
          'red lighten-1',
          'deep-purple accent-4',
        ],
        slides: [
          '../../../public/img/partytemp1.jpg',
          'Second',
          'Third',
          'Fourth',
          'Fifth',
        ],
      }
    },
  }
</script>

<style lang="scss">
$carousel-btn-margin:5px;
$banner-height:250px;
.v-window__prev{
    left:$carousel-btn-margin;
}
.v-window__next{
    right:$carousel-btn-margin;
}
.banner{
    display: block; 
    margin: 0px auto; 
    width:100vw; 
    transform: translate(0,-50%);
}
.display-3 div{
    position:relative; 
    line-height:$banner-height;
    color:white; 
    z-index:1;
}
</style>