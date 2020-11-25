<template>
  <!-- 입장하기 모달 -->
  <v-row justify="center">
    <v-dialog
      v-model="meetingDialog"
      persistent
      max-width="600px"
      v-if="user && publisher"
    >
      <div class="scroll-sect">
        <!-- 만약 주량 등록이 되어있다면  -->
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
                      alt="kakao"
                    />
                  </div>
                </v-col>
                <!-- 방제목 지정 -->
                <v-col cols="6" v-if="ishost">
                  <v-text-field
                    v-model="roomName"
                    label="방 제목"
                    hint="방 제목을 입력해주세요"
                    persistent-hint
                    required
                    :rules="[(v) => !!v || '필수항목입니다.']"
                    color="#84669a"
                  ></v-text-field>
                </v-col>
                <!-- 닉네임 지정 -->
                <v-col cols="6">
                  <v-text-field
                    v-model="nickName"
                    label="닉네임"
                    hint="미팅에서 사용할 닉네임을 입력해주세요"
                    persistent-hint
                    required
                    :rules="[(v) => !!v || '필수항목입니다.']"
                    color="#84669a"
                  ></v-text-field>
                </v-col>
                <!-- 공개여부 지정 -->
                <v-col cols="6">
                  <v-select
                    v-model="currentDrink"
                    :items="user.drinks"
                    item-text="liquorName"
                    item-value="liquorName"
                    label="오늘의 술"
                    hint="미팅에서 마실 술 종류를 입력해주세요"
                    persistent-hint
                    required
                    :rules="[(v) => !!v || '필수항목입니다.']"
                    color="#84669a"
                  >
                  </v-select>
                </v-col>
                <v-col cols="6" v-if="ishost">
                  <v-select
                    v-model="isPublic"
                    :items="publicItems"
                    item-text="title"
                    item-value="value"
                    label="공개 여부"
                    hint="미팅의 공개 여부를 입력해주세요"
                    persistent-hint
                    required
                    color="#84669a"
                  >
                  </v-select>
                </v-col>
                <!-- 태그 지정 -->
                <v-col cols="12" v-if="ishost">
                  <v-combobox
                    v-model="tags"
                    :items="allTags"
                    :search-input.sync="searchTag"
                    hide-selected
                    counter="5"
                    :rules="[
                      (v) =>
                        v.length < 6 || '최대 5개의 태그를 고를 수 있습니다.',
                    ]"
                    color="blue-grey lighten-2"
                    label="태그"
                    multiple
                    item-text="tagName"
                    item-value="tagName"
                    :return-object="false"
                    persistent-hint
                    small-chips
                    hint="미팅을 설명하는 태그를 작성해주세요"
                  >
                    <template v-slot:selection="data">
                      <v-chip
                        v-bind="data.attrs"
                        close
                        @click:close="remove(tags, data.item)"
                      >
                        {{ data.item }}
                      </v-chip>
                    </template>
                    <template v-slot:no-data>
                      <v-list-item>
                        <v-list-item-content>
                          <v-list-item-title>
                            "<strong>{{ searchTag }}</strong
                            >"를 찾을 수 없습니다. <kbd>enter</kbd>를 눌러
                            새로운 태그를 만들어보세요.
                          </v-list-item-title>
                        </v-list-item-content>
                      </v-list-item>
                    </template>
                  </v-combobox>
                </v-col>

                <v-col cols="6">
                  <div id="video-container">
                    <user-video :stream-manager="publisher"/>
                  </div>
                </v-col>
                <v-col
                  class="d-flex justify-content-around align-items-center"
                  cols="6"
                >
                  <div class="btn" @click="clickMuteVideo">
                    <img
                      src="@/assets/images/webcam.png"
                      alt="webcam"
                      v-if="publisher.stream.videoActive"
                    />
                    <img
                      src="@/assets/images/webcam_off.png"
                      alt="webcam_off"
                      v-else
                    />
                  </div>
                  <div class="btn" @click="clickMuteAudio">
                    <img
                      src="@/assets/images/voice.png"
                      alt="voice"
                      v-if="publisher.stream.audioActive"
                    />
                    <img
                      src="@/assets/images/voice_off.png"
                      alt="voice_off"
                      v-else
                    />
                  </div>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="indigo" text @click="clickClose"> Close </v-btn>
            <v-btn color="indigo" text :disabled="!valid" @click="clickEnter">
              Enter
            </v-btn>
          </v-card-actions>
        </v-card>
        <!-- 만약 주량 등록이 되어있지 않다면 -->
        <v-card v-else>
          <div
            class="d-flex flex-column justify-content-center align-items-center"
          >
            <p>마이페이지에서 주량을 등록해주세요!</p>
            <v-btn color="blue darken-1" text @click="clickClose(publisher)">
              Close
            </v-btn>
          </div>
        </v-card>
      </div>
    </v-dialog>
  </v-row>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import UserVideo from '@/components/meetingpage/UserVideo';
import Swal from 'sweetalert2'

export default {
  name: 'EnterModal',
  components: {
    UserVideo
  },
  computed: {
    ...mapState(['user']),
    ...mapState('meetingStore', ['mySessionId', 'session', 'publisher', 'meetingDialog', 'allTags'])
  },
  props: [
    'ishost'
  ],
  data() {
    return {
      nickName: null,
      roomName: null,
      currentDrink: null,
      valid: true,
      lazy: false,
      publicItems: [{ title: '공개', value: 1}, { title: '비공개', value: 0}],
      isPublic: 1,
      tags: [],
      searchTag: null,
    }
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
      'leaveSession',
      'clickMuteVideo',
      'clickMuteAudio',
      'enterSession',
      'changeMeetingDialog',
      'updateUserNickname',
      'fetchAllTags'
    ]),
    clickClose() {
      this.leaveSession();
      this.changeMeetingDialog(false);
      this.nickName = this.user.name;
      this.roomName = `${this.nickName}의 방`;
      this.currentDrink = null;
      this.isPublic = 1;
    },
    clickEnter() {
      const enterData = {
        nickName: this.nickName,
        currentDrink: this.currentDrink
      }
      //호스트라면 방 제목, 공개여부, 태그
      if(this.ishost){
        enterData.roomName = this.roomName;
        enterData.isPublic = this.isPublic;
        enterData.tags = this.tags;
      }

      this.enterSession(enterData)
        .then(() => {
          this.$router.push({ name: 'MeetingPage', params: { sessionId: this.mySessionId }});
          this.changeMeetingDialog(false);
        })
    },
    remove (data, item) {
      const index = data.indexOf(item)
      if (index >= 0) data.splice(index, 1)
    },
    clickCopyURL() {
      const copyText = document.getElementById("copySessionId");
      copyText.select();
      copyText.setSelectionRange(0, 99999); /*For mobile devices*/
      document.execCommand("copy");
      Swal.fire({
          icon: 'success',
          text: '입장코드가 복사되었습니다'
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
            mobileWebUrl: `https://homesuli.com/meet/${this.mySessionId}`,
            webUrl: `https://homesuli.com/meet/${this.mySessionId}`,
          },
        }
      })
    },
  },
  mounted() {
    if (this.user) {
      this.nickName = this.user.name;
      this.roomName = `${this.nickName}의 방`;
      this.tags = [];
    }
    this.fetchAllTags();
  }
};
</script>