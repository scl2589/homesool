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
    
    <v-row justify="center">
      <v-dialog
        v-model="meetingDialog"
        persistent
        max-width="600px"
        v-if="user && publisher"
      >
      <div class="scroll-sect">
        <v-card v-if="user.drinks.length">
          <v-card-title>
            <h3 class="m-0 enter-title">입장하기</h3>
          </v-card-title>
          <v-form v-model="valid" :lazy-validation="lazy">  
            <v-container>
              <v-row>
                <v-col
                  class="d-flex justify-content-between align-items-center enter-code"
                  cols="12"
                >
                  <h5 class="my-0">입장 코드</h5>
                  <v-text-field
                    class="ml-3 mr-5"
                    id="copySessionId"
                    :value="mySessionId"
                    readonly
                    append-icon="far fa-clone"
                    @click:append="clickCopyURL"
                    color="#84669a"
                  ></v-text-field>
                  <div class="mb-2 pointer" @click="clickKakaoShare">
                    <img
                      width="32vw"
                      src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png"
                    />
                  </div>
                </v-col>
                
                <v-col
                  class="room-name"
                  cols="12"
                  v-if="ishost"
                >
                  <v-text-field
                    v-model="roomName"
                    hint="방 제목을 입력해주세요"
                    persistent-hint
                    required
                    :rules="[v => !!v || '필수항목입니다.']"
                    color="#84669a"
                  ></v-text-field>
                </v-col>

                <v-col
                  cols="6"
                >
                  <v-text-field
                    v-model="nickName"
                    label="닉네임"
                    hint="미팅에서 사용할 닉네임을 입력해주세요"
                    persistent-hint
                    required
                    :rules="[v => !!v || '필수항목입니다.']"
                    color="#84669a"
                  ></v-text-field>
                </v-col>

                <v-col
                  cols="6"
                >
                  <v-select
                    v-model="currentDrink"
                    :items="user.drinks"
                    item-text="liquorName"
                    item-value="liquorName"
                    label="오늘의 술"
                    hint="미팅에서 마실 술 종류를 입력해주세요"
                    persistent-hint
                    required
                    :rules="[v => !!v || '필수항목입니다.']"
                    color="#84669a"
                  >
                  </v-select>
                </v-col>

                <v-col
                  cols="6"
                >
                  <div id="video-container">
                    <user-video :stream-manager="publisher" @click.native="updateMainVideoStreamManager(publisher)"/>
                  </div>
                </v-col>
                <v-col cols="6" class="d-flex justify-content-around align-items-center">
                  <div class="btn" @click="clickMuteVideo">
                    <img src="@/assets/images/webcam.png" alt="webcam" v-if="publisher.stream.videoActive">
                    <img src="@/assets/images/webcam_off.png" alt="webcam_off" v-else>
                  </div>
                  <div class="btn" @click="clickMuteAudio">
                    <img src="@/assets/images/voice.png" alt="voice" v-if="publisher.stream.audioActive">
                    <img src="@/assets/images/voice_off.png" alt="voice_off" v-else>
                  </div>
                </v-col>

              </v-row>
            </v-container>
          </v-form>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="indigo"
              text
              @click="clickClose"
            >
              Close
            </v-btn>
            <v-btn
              color="indigo"
              text
              :disabled="!valid"
              @click="clickEnter"
            >
              Enter
            </v-btn>
          </v-card-actions>
        </v-card>
        <v-card v-else>
          <div class="d-flex flex-column justify-content-center align-items-center">
            <p>마이페이지에서 주량을 등록해주세요!</p>
            
            <v-btn
              color="blue darken-1"
              text
              @click="clickClose(publisher)"
            >
              Close
            </v-btn>
          </div>
        </v-card>
      </div>

      </v-dialog>
    </v-row>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import HomeBanner from "@/components/homepage/HomeBanner.vue";
import UserVideo from '@/components/meetingpage/UserVideo';
import Swal from 'sweetalert2'

export default {
  name: "HomePage",
  components: {
    HomeBanner,
    UserVideo
  },
  data: () => {
    return {
      inputSessionId: '',
      nickName: null,
      currentDrink: null,
      valid: true,
      lazy:false,
      ishost:false,
      roomName : null,
    };
  },
  computed: {
    ...mapGetters(['getId']),
    ...mapState(['token', 'user']),
    ...mapState('meetingStore', ['mySessionId', 'session', 'mainStreamManager', 'publisher', 'subscribers', 'meetingDialog'])
  },
  watch: {
    user(value) {
      if (value) {
        this.nickName = value.name;
        this.roomName = `${this.nickName}의 방`
      }
    }
  },
  methods: {
    ...mapActions('meetingStore', [
      'createSessionId',
      'checkSessionId',
      'updateMainVideoStreamManager',
      'leaveSession',
      'clickMuteVideo',
      'clickMuteAudio',
      'enterSession',
      'changeMeetingDialog',
      'updateUserNickname'
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
    clickClose() {
      this.leaveSession();
      this.changeMeetingDialog(false);
      this.nickName = this.user.name;
      this.roomName = `${this.nickName}의 방`;
      this.currentDrink = null;
    },
    clickEnter() {
      const enterData = {
        nickName: this.nickName,
        currentDrink: this.currentDrink
      }
      //호스트라면 방 제목 변경
      if(this.ishost){
        enterData.roomName = this.roomName
      }

      this.enterSession(enterData)
        .then(() => {
          this.$router.push({ name: 'MeetingPage', params: { sessionId: this.mySessionId }});
          this.changeMeetingDialog(false);
        })
    },
    clickCopyURL() {
      const copyText = document.getElementById("copySessionId");
      copyText.select();
      copyText.setSelectionRange(0, 99999); /*For mobile devices*/
      document.execCommand("copy");
      Swal.fire({
          icon: 'success',
          text: '주소가 복사되었습니다'
        })
    },
    clickKakaoShare() {
      window.Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          title: `${this.user.name}님이 술자리 미팅을 초대하셨습니다!`,
          description: '링크로 들어와 술자리 미팅에 참여해주세요 :)',
          imageUrl: 'https://user-images.githubusercontent.com/57381062/97659870-6c195600-1ab3-11eb-9084-05a7a2e01c96.png',
          link: {
            mobileWebUrl: `https://k3a503.p.ssafy.io/meet/${this.mySessionId}`,
            webUrl: `https://k3a503.p.ssafy.io/meet/${this.mySessionId}`,
          },
        }
      })
    }
  },
  mounted() {
    if (this.user) {
      this.nickName = this.user.name;
      this.roomName = `${this.nickName}의 방`
    }
  }
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