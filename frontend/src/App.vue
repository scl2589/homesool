<template>
  <v-app id="app">
    <div id="nav" v-if="!meetingpages.includes($route.name)">
    <!-- <div id="nav" v-if="$route.name!=='MeetingPage'"> -->
      <!-- <span id="logo">술이술이홈술이</span> -->
      <img id="logo" src="@/assets/images/basic_title.png" alt="술이술이홈술이">
      <span id="login" v-if="!token">
        <button id="kakao_login" @click="login()">
            <img id="kakao_img" src="@/assets/images/kakao_login_large.png"/>
            <!-- <img id="kakao_img" src="@/assets/images/kakao.png" />
            <span id="kakao_font">로그인</span> -->
        </button>
      </span>
      <span v-else id="afterlogin">
        <button @click="clickMyPage" >마이페이지</button>
        <button @click="clickLogout" >로그아웃</button>
      </span>
    </div>
    <router-view/>
  </v-app>
</template>
<script>
import { mapState } from 'vuex'
export default {
  data(){
    return{
      meetingpages : [
        'MeetingPage', 
        'SmileLeadsToAlcoholDescription', 
        'UpAndDownDescription', 
        'StrawberryGameDescription', 
        'LiarGameDescription', 
        'ConsonantQuizDescription'
      ],
    }
  },
  computed: {
    ...mapState(['token']),
    getToken() {
      return this.$store.getters.getToken;
    },
  },
   methods : {
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
    // clickLogout(){
		// 	// localStorage.clear();
		// 	this.$store.commit('setToken', null)
    //   this.$store.commit('setUser', null)
    //   this.$store.commit('setId', null)
    //   this.$store.commit('setIsNew', null)
    //   this.$router.push('/').catch(()=>{});
    //   this.$router.push({ name: ''})
    // }
      clickLogout(){
      this.$store.dispatch('kakaoLogout');
    }
   },
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
  height:100vh;
}

$header-height:35px;
$header-margin:5px;
#nav {
  height:$header-height + 2 * $header-margin;
  padding: $header-margin;
  // padding: $header-height;
  #logo{
    padding: auto 0;
    float:left;
    height:$header-height;
  }
  #kakao_img {
    // float:right;
    float: left;
    position: inherit;
    height: $header-height;
    // height: $header-height - 2 * $header-margin;
  }
  #kakao_login{
    float:right;
    // login 2
    // color: #333;
    // background-color: #ffe500;
    // border-radius: $header-height / 6;
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
    // line-height: $header-height / 3 * 2;
    button{
      background-color: rgba(255, 255, 255, 0.50);
      // height: $header-height / 3 * 2;
      vertical-align:middle;
      border-radius: $header-height / 6;
      padding: $header-margin;
      margin: $header-margin;
      color:white;
    }
  }
}
</style>
