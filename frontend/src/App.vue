<template>
  <div id="app">
    <div id="nav" v-if="$route.name==='meet'">
      <!-- <span id="logo">술이술이홈술이</span> -->
      <img id="logo" src="@/assets/images/basic_title.png" alt="술이술이홈술이">
      <span id="login">
        <button id="kakao_login" @click="login()">
            <img id="kakao_img" src="@/assets/images/kakao_login_large.png"/>
            <!-- <img id="kakao_img" src="../public/img/kakao.png" /> -->
            <!-- <span id="kakao_font">로그인</span> -->
        </button>
      </span>
    </div>
    <router-view/>
  </div>
</template>
<script>
// import http from '../utils/http-common.js';
export default {
  computed: {
    // getToken() {
    //   return this.$store.getters.getToken;
    // },
  },
   methods : {
    login(){
      console.log('로그인');
      // Kakao.Auth.login({
      //   success: this.kakaoLoginStore,
      // });
    },
    // kakaoLoginStore(authObj) {
    //   this.$store.dispatch('kakaoLogin', {
    //     access_token: authObj.access_token,
    //   });
    // },
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
  padding: 15px;
  #logo{
    float:left;
    height:$header-height * 1.1;
  }
  #kakao_img {
    // float:right;
    float: left;
    position: inherit;
    height: $header-height;
    // height: $header-height - 2 * $header-margin;
    margin: $header-margin;
  }
  #kakao_login{
    float:right;
    // login 2
    // color: #333;
    // background-color: #ffe500;
    // line-height: $header-height;
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
}
</style>
