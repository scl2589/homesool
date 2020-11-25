<template>
  <v-row justify="center">
    <v-dialog
      v-model="meetingInfoDialog"
      persistent
      max-width="600px"
    >
      <div>
        <v-card>
          <v-card-title>
            <h3 class="m-0 enter-title">미팅 정보</h3>
          </v-card-title>
          <v-form
            v-model="valid"
            :lazy-validation="lazy"
          >  
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
                    color="#84669a"
                    @click:append="clickCopyURL"
                  ></v-text-field>
                  <div
                    class="mb-2 pointer"
                    @click="clickKakaoShare"
                  >
                    <img
                      width="32vw"
                      src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png"
                    />
                  </div>
                </v-col>
                
                <v-col
                  cols="6"
                >
                  <v-text-field
                    v-model="roomName"
                    label="방 제목"
                    hint="방 제목을 입력해주세요"
                    persistent-hint
                    required
                    color="#84669a"
                    :rules="[v => !!v || '필수항목입니다.']"
                  ></v-text-field>
                </v-col>

                <v-col
                  cols="6"
                >
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

                <v-col
                  cols="12"
                >
                  <v-combobox
                    v-model="tags"
                    :items="allTags"
                    :search-input.sync="searchTag"
                    counter="5"
                    :rules="[
                      v => (v.length < 6) || '최대 5개의 태그를 고를 수 있습니다.'
                    ]"
                    color="blue-grey lighten-2"
                    label="태그"
                    item-text="tagName"
                    item-value="tagName"
                    :return-object="false"
                    hint="미팅을 설명하는 태그를 작성해주세요"
                    hide-selected
                    multiple
                    persistent-hint
                    small-chips
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
                            "<strong>{{ searchTag }}</strong>"를 찾을 수 없습니다. <kbd>enter</kbd>를 눌러 새로운 태그를 만들어보세요. 
                          </v-list-item-title>
                        </v-list-item-content>
                      </v-list-item>
                    </template>
                  </v-combobox>
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
              취소
            </v-btn>
            <v-btn
              color="indigo"
              text
              :disabled="!valid"
              @click="clickUpdate"
            >
              수정
            </v-btn>
          </v-card-actions>
        </v-card>
      </div>
    </v-dialog>
  </v-row>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'MeetingInfo',
  data() {
    return {
      valid: true,
      lazy: false,
      roomName : null,
      publicItems: [{ title: '공개', value: 1}, { title: '비공개', value: 0}],
      isPublic: 1,
      tags: [],
      searchTag: null
    }
  },
  computed: {
    ...mapState(['user']),
    ...mapState('meetingStore', [
      'mySessionId',
      'allTags',
      'meetingInfoDialog',
      'roomInfo'
    ])
  },
  watch: {
    roomInfo(value) {
      if (value.tags) {
        this.roomName = value.roomName;
        let tagNames = [];
        value.tags.forEach(tag => {
          tagNames.push(tag.tagName);
        });
        this.tags = tagNames;
        this.isPublic = value.isPublic;
      }
    }
  },
  methods: {
    ...mapActions('meetingStore', [
      'toggleMeetingInfo',
      'updateRoomInfo',
      'fetchAllTags',
      'clickCopyURL'
    ]),
    remove (data, item) {
      const index = data.indexOf(item)
      if (index >= 0) data.splice(index, 1)
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
    clickClose() {
      this.toggleMeetingInfo();
    },
    clickUpdate() {
      var updateData = Object();
      updateData.isPublic = this.isPublic;
      updateData.roomName = this.roomName;
      updateData.tags = this.tags;
      this.updateRoomInfo(updateData);
      this.toggleMeetingInfo();
    }
  },
  mounted() {
    this.fetchAllTags();
  }
}
</script>

<style scoped>

</style>