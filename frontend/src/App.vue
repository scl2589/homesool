<template>
  <div>
      <v-app id="app">
      <div id="nav" v-if="!meetingpages.includes($route.name)">
        <img 
          class="pointer"
          id="logo" 
          src="@/assets/images/basic_title.png" 
          alt="술이술이홈술이 로고"
          @click="clickLogo"
        >
        <span id="login" v-if="!token">
          <button id="kakao_login" @click="login()">
            <img id="kakao_img" src="@/assets/images/kakao_login_large.png"/>
          </button>
        </span>
        <span class="d-flex justify-content-between align-items-center h-100" v-else id="afterlogin">
          <p class="color-white mr-3 mb-0 pointer" @click="clickMyPage" >마이페이지</p>
          <p class="color-white mr-2 mb-0 pointer" @click="clickLogout" >로그아웃</p>
        </span>
      </div>
      <router-view/>
    </v-app>

    <v-app id="app2">
      <div class="h-100 d-flex flex-column justify-content-center">
        <div class="d-flex justify-content-center mt-10">
          <img 
            class="pointer"
            id="logo2" 
            src="@/assets/images/basic_title.png" 
            alt="술이술이홈술이 로고"
          >
        </div>

        <div class="d-flex justify-content-center">
          <a
            class="mx-auto"
            style="cursor:none"
          >
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <p class="text-center text-white m-0">
              술이술이 홈술이는<br>더 큰 화면에서 지원합니다.
            </p>
          </a>
        </div>
      </div>
    </v-app>
  </div>

</template>
<script>
import Swal from 'sweetalert2'
import { mapActions, mapState } from 'vuex'
export default {
  data(){
    return{
      meetingpages : [
        'MeetingPage', 
        'SmileLeadsToAlcoholDescription', 
        'UpAndDownDescription', 
        'StrawberryGameDescription', 
        'LiarGameDescription', 
        'ConsonantQuizDescription',
        'FindOutDrunkenDescription'
      ],
    }
  },
  computed: {
    ...mapState(['token']),
    getToken() {
      return this.$store.getters.getToken;
    },
  },
  watch: {
    token(value) {
      if (value) {
        this.getMyInfo();
      }
    }
  },
  methods : {
    ...mapActions(['getMyInfo']),
    clickLogo() {
      this.$router.push('/')
    },
    login(){
      window.Kakao.Auth.login({
        success: this.kakaoLoginStore,
      });
    },
    kakaoLoginStore(authObj) {
      this.$store.dispatch('kakaoLogin', {
        access_token: authObj.access_token,
      });
    },
    clickMyPage() {
      this.$router.push({ name: 'MyPage'}).catch(()=>{});
    },
    clickLogout(){
      Swal.fire({
        html: "홈술이를 로그아웃 하시겠습니까?",
        showCancelButton: true,
        confirmButtonText: '네',
        cancelButtonText: '아니요',
        icon: "warning",
      })
      .then((result) => {
        if (result.value) {
          this.$store.dispatch('kakaoLogout');
        }
      });
    },
    clickResize() {
    }
  },
  mounted() {
    if (this.token) {
      this.getMyInfo();
    }
  }
}
</script>
<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: black;
  height:100%;
}

$header-height:35px;
$header-margin:5px;
#nav {
  height:$header-height + 2 * $header-margin;
  padding: $header-margin;
  p {
    font-size: 1.2rem;
    .pointer {
      cursor:pointer;
    }
  }
  #logo{
    padding: auto 0;
    float:left;
    height:$header-height;
  }
  #kakao_img {
    float: left;
    position: inherit;
    height: $header-height;
  }
  #kakao_login{
    float:right;
  }
  #kakao_font {
    padding-right: 5px;
    font-weight: 100;
    line-height: $header-height;
    vertical-align: middle;
    font-size:$header-height / 2;
    font-family: Arial;
  }
  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
  #afterlogin{
    float:right;
    .pointer {
      cursor: pointer;
    }
  }
}


</style>
<style scoped>
#app2 {
  background: black;
}

.swal2-confirm {
  background-color: #3e91df !important;
}

.swal2-cancel {
  background-color: #f85858 !important;
}

@media (min-width: 600px) {
  #app2 {
    display: none;
  }
}
 @media (max-width: 600px) {
  #app {
    display: none;
  }
}
@import url('https://fonts.googleapis.com/css2?family=Raleway:wght@400;700&display=swap');
*{
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}
body{
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: #050801;
    font-family: 'Raleway', sans-serif;
    font-weight: bold;
}
a{
    position: relative;
    display: inline-block;
    padding: 25px 30px;
    margin: 40px 0;
    color: #03e9f4;
    text-decoration: none;
    text-transform: uppercase;
    transition: 0.5s;
    letter-spacing: 4px;
    overflow: hidden;
    margin-right: 50px;
   
}
a:hover{
    background: #03e9f4;
    color: #050801;
    box-shadow: 0 0 5px #03e9f4,
                0 0 25px #03e9f4,
                0 0 50px #03e9f4,
                0 0 200px #03e9f4;
     -webkit-box-reflect:below 1px linear-gradient(transparent, #0005);
}
a:nth-child(1){
    filter: hue-rotate(270deg);
}
a:nth-child(2){
    filter: hue-rotate(110deg);
}
a span{
    position: absolute;
    display: block;
}
a span:nth-child(1){
    top: 0;
    left: 0;
    width: 100%;
    height: 2px;
    background: linear-gradient(90deg,transparent,#03e9f4);
    animation: animate1 1s linear infinite;
}
@keyframes animate1{
    0%{
        left: -100%;
    }
    50%,100%{
        left: 100%;
    }
}
a span:nth-child(2){
    top: -100%;
    right: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(180deg,transparent,#03e9f4);
    animation: animate2 1s linear infinite;
    animation-delay: 0.25s;
}
@keyframes animate2{
    0%{
        top: -100%;
    }
    50%,100%{
        top: 100%;
    }
}
a span:nth-child(3){
    bottom: 0;
    right: 0;
    width: 100%;
    height: 2px;
    background: linear-gradient(270deg,transparent,#03e9f4);
    animation: animate3 1s linear infinite;
    animation-delay: 0.50s;
}
@keyframes animate3{
    0%{
        right: -100%;
    }
    50%,100%{
        right: 100%;
    }
}


a span:nth-child(4){
    bottom: -100%;
    left: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(360deg,transparent,#03e9f4);
    animation: animate4 1s linear infinite;
    animation-delay: 0.75s;
}
@keyframes animate4{
    0%{
        bottom: -100%;
    }
    50%,100%{
        bottom: 100%;
    }
}

#logo2 {
  width: 80vw
}
</style>
