<template>
  <div>
    <home-banner />
    <div class="row no-gutters">
      <div 
        id="wrapper" 
        class="d-flex justify-content-center"
      >
        <!-- 주최하기 버튼 -->
        <div 
          class="p-1 bd-highlight"
          id="host"
        >
          <img 
            src="@/assets/images/host.png" 
            alt="host" 
          />
          <button @click="hostbtn">주최하기</button>
        </div>
        <!-- 입장 코드 입력란 -->
        <div
          class="p-1 bd-highlight" 
          id="guest"
        >
          <img 
            src="@/assets/images/meeting.png" 
            alt="guest" 
          />
          <input 
            placeholder="입장 코드를 입력하세요" 
            v-model="inputSessionId"
            @keyup.enter="guestbtn"
          />
          <button 
            v-show="inputSessionId" 
            @click="guestbtn"
          >
            입장하기
          </button>
        </div>
        <!-- 공개방 보기 버튼 -->
        <div 
          class="p-1 bd-highlight"
          id="entrance"
        >
          <img 
            src="@/assets/images/guest.png" 
            alt="호스트" 
          />
          <button @click="openbtn">공개방 보기</button>
        </div>
      </div>
    </div>
    
    <!-- 입장하기 모달 -->
    <EnterModal 
      v-if="meetingDialog"
      :ishost="ishost"
    ></EnterModal>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import HomeBanner from "@/components/homepage/HomeBanner.vue";
import EnterModal from '@/components/homepage/EnterModal.vue';
import Swal from 'sweetalert2'

export default {
  name: "HomePage",
  components: {
    HomeBanner,
    EnterModal
  },
  data: () => {
    return {
      inputSessionId: '',
      ishost: false,
    };
  },
  computed: {
    ...mapGetters(['getId']),
    ...mapState(['user']),
    ...mapState('meetingStore', ['mySessionId', 'meetingDialog'])
  },
  methods: {
    ...mapActions('meetingStore', [
      'createSessionId',
      'checkSessionId',
      'changeMeetingDialog'
    ]),
    hostbtn() {
      if (!this.getId) {
        Swal.fire({
          title: "먼저 로그인을 해주세요!",
          icon: "warning",
        })
        return false;
      }
      this.createSessionId();
      //주최한다는 새로운 변수 추가
      this.ishost = true;
      this.changeMeetingDialog(true);
    },
    guestbtn() {
      if (!this.getId) {
        Swal.fire({
          title: "먼저 로그인을 해주세요!",
          icon: "warning",
        })
        return false;
      }
      this.checkSessionId(this.inputSessionId)
        .then(() => {
          //주최 안한다는 새로운 변수 추가
          this.ishost = false;
          this.changeMeetingDialog(true);
        })
    },
    openbtn() {
      this.$router.push({ name: 'OpenRoom' })
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
  div {
    color: white;
    img {
      margin: 0 auto;
      display: block;
      padding: 10%;
      max-height: 40vh;
      max-width: 20vw;
    }
    button {
      display: block;
      height: $buttonheight;
      font-size: 1.2em;
      margin: 0 auto;
      width: 40%;
      border-radius: 5px;
      background-color: rgba(255, 255, 255, 0.109);
    }
    input {
      display: block;
      width: auto;
      height: $buttonheight;
      font-size: 1.2em;
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
  }
  #guest {
    position: relative;
    height: 100%;
    width: 25%;
    button {
      margin-top: 3%;
    }
  }
  #entrance {
    position: relative;
    height: 100%;
    width: 25%;
  }
}

.btn {
  padding: 0;
}

.btn > img {
  width: 7vh;
  height: auto;
}

.pointer {
  cursor: pointer;
}

.container {
  padding-top: 0 !important;
}

.v-input {
  padding: 8px 0 5px 0 !important;
  margin: 0 !important;
}

.scroll-sect {
  height: 80vh !important;
  overflow: auto;
}

.scroll-sect::-webkit-scrollbar {
  width: 8px; 
  height: 8px;
}

.scroll-sect::-webkit-scrollbar-track {
  background: #37474F;
}

.scroll-sect::-webkit-scrollbar-corner {
  background: #37474F; 
}

.scroll-sect::-webkit-scrollbar-thumb {
  background: #b0a2c8;
}

.scroll-sect::-webkit-scrollbar-button {
  background-color: #37474F;
  height: 0;
}

button:focus {
  outline: none;
}

.enter-title {
  color: #b0a2c8;
}

.container, .v-card__title {
  padding-bottom: 0!important;
}

.v-input__slot, .v-card__actions {
  margin: 0!important;
  padding-top: 0!important;
}

.room-name {
  padding-top: 0;
}

.enter-code {
  padding-bottom: 0;
}

.v-text-field__details {
  padding-top: 2px;
}

@import url('https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap');
.v-messages__message {
  color: #b097c3;
  font-family: 'Nanum Gothic', sans-serif;
  font-size: 1.0em;
  font-weight: 600;
}

.v-application .primary--text {
  color: #84669a !important;
  caret-color: #84669a !important;
}
</style>