<template>
  <div>
    <home-banner />
    <div class="row">
      <div id="wrapper" class="d-flex justify-content-center">
        <div id="host" class="p-1 bd-highlight">
          <img src="@/assets/images/host.png" alt="호스트" />
          <button @click="hostbtn">주최하기</button>
        </div>
        <div id="guest" class="p-1 bd-highlight">
          <img src="@/assets/images/guest.png" alt="게스트" />
          <input placeholder="입장 코드를 입력하세요" v-model="inputSessionId" />
          <button v-show="inputSessionId" @click="guestbtn">입장하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HomeBanner from "@/components/homepage/HomeBanner.vue";
import { mapActions, mapGetters, mapState } from 'vuex';

export default {
  name: "HomePage",
  components: {
    HomeBanner,
  },
  data: () => {
    return {
      inputSessionId: ''
    };
  },
  computed: {
    ...mapGetters(['getId']),
    ...mapState(['token']),
    ...mapState('meetingStore', ['mySessionId'])
  },
  methods: {
    ...mapActions('meetingStore', ['createSessionId', 'checkSessionId']),
    hostbtn() {
      if (!this.getId) {
        alert('먼저 로그인을 해주세요!')
        return false;
      }
      this.createSessionId();
    },
    guestbtn() {
      if (!this.getId) {
        alert('먼저 로그인을 해주세요!')
        return false;
      }
      this.checkSessionId(this.inputSessionId);
    }
  },
};
</script>

<style lang="scss">
$buttonheight: 50px;
#wrapper {
  width: 100%;
  height: 60vh;
  position: relative;
  // background-color: red;
  div {
    color: white;
    img {
      margin: 0 auto;
      display: block;
      padding: 10%;
      height: 70%;
    }
    button {
      display: block;
      height: $buttonheight;
      font-size: $buttonheight / 2;
      margin: 0 auto;
      width: 40%;
      border-radius: 5px;
      background-color: rgba(255, 255, 255, 0.109);
    }
    input {
      display: block;
      width: auto;
      height: $buttonheight;
      font-size: $buttonheight / 2;
      margin: 0 auto;
      background-color: transparent;
      border: 2px solid rgba(255, 255, 255, 0.5);
      border-radius: 5px;
      text-align: center;
      color: white;
    }
    input::-webkit-input-placeholder {
      text-align: center;
      color: white;
    }
    input::-moz-placeholder {
      text-align: center;
    }
    input:-ms-input-placeholder {
      text-align: center;
    }
    input:-moz-placeholder {
      text-align: center;
    }
    input::placeholder {
      text-align: center;
      color: white;
    }
  }
  #host {
    position: relative;
    height: 100%;
    width: 25%;
    // background-color:red;
  }
  #guest {
    position: relative;
    height: 100%;
    width: 25%;
    // background-color:yellow;
    button {
      margin-top: 3%;
    }
  }
}
</style>