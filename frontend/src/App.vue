<template>
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
</template>
<script>
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
      if (confirm("홈술이를 로그아웃 하시겠습니까?")) {
        this.$store.dispatch('kakaoLogout');
      }
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
