<template>
  <v-carousel
    dark
    cycle
    height="250"
    hide-delimiter-background
  >
    <v-carousel-item>
      <v-sheet height="100%">
        <v-row
          class="fill-height"
          align="center"
          justify="center"
        >
          <div class="text-container">
            <img src="@/assets/images/party2.png" alt="배너2" class="banner">
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
          <div class="text-container">
            <img src="@/assets/images/party5.png" alt="배너5" class="banner">
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
          <div class="text-container">
            <img src="@/assets/images/party6.png" alt="배너6" class="banner">
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
          <div class="text-container">
            <img src="@/assets/images/party7.png" alt="배너7" class="banner">
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
@import url('https://fonts.googleapis.com/css2?family=Bowlby+One+SC&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap');
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
    // opacity: 0.3;
}
.text-container div{
    position:relative; 
    color:white; 
    z-index:1;
    
    .homesool {
      font-family: 'Bowlby One SC', cursive;
      font-size:2rem;
      color: #d6f567
    }
    .homesool-offer {
      font-family: 'Do Hyeon', sans-serif;
      font-size: 1.5rem;
    }
    small {
      font-family: 'Do Hyeon', sans-serif;
      color: #d4d4d4;
      font-size: 1rem;
    }
}

   .v-carousel__controls__item{
      color: lightgray !important
   }
</style>