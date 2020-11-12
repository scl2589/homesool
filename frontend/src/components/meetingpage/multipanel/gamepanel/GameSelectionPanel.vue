<template>
  <div class="panel">
    <!-- !modeHost -->
    <div v-if="notModeHost">
      <p>{{ notModeHost.name }}님이 게임을 고르는 중입니다 :)</p>
    </div>

    <!-- modeHost -->
    <div class="d-flex row no-gutters" v-else>
      <div class="col-4 game-selection my-1">
        <div class="top">
          <h6 class="mt-5">술게임을 골라주세요!</h6>
          <br />
        </div>
        <div class="game-list">
          <li @click="selectDescription(1)">Up & Down</li>
          <li @click="selectDescription(2)">자음 퀴즈</li>
          <li @click="selectDescription(3)">라이어 게임</li>
          <li @click="selectDescription(4)">웃으면 술이와요</li>
          <li @click="selectDescription(5)">나술안취했어</li>
        </div>
      </div>
      <div class="col-8 my-1">
        <transition name="slide" mode="out-in">
          <div>
            <div v-if="selectedDescription == 1">
              <UpAndDownDescription />
            </div>
            <div v-if="selectedDescription == 2">
              <ConsonantQuizDescription />
            </div>
            <div v-if="selectedDescription == 3">
              <LiarGameDescription />
            </div>
            <div v-if="selectedDescription == 4">
              <SmileLeadsToAlcoholDescription />
            </div>
            <div v-if="selectedDescription == 5">
              <FindOutDrunkenDescription />
            </div>

            <div
              class="d-flex justify-content-between align-items-center mx-2 mt-auto"
              v-if="selectedDescription"
            >
              <div class="penalty">
                <v-select
                  :items="items"
                  label="벌칙"
                  width="10px"
                  hide-details
                  dense
                  append-icon=""
                  solo
                ></v-select>
                
              </div>
              <div>
                <button
                  class="btn-yellow rounded"
                  @click="clickStartGame()"
                >
                  시작하기
                </button>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>

  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import ConsonantQuizDescription from '@/components/meetingpage/multipanel/gamepanel/gamedescription/ConsonantQuizDescription';
import FindOutDrunkenDescription from '@/components/meetingpage/multipanel/gamepanel/gamedescription/FindOutDrunkenDescription';
import LiarGameDescription from '@/components/meetingpage/multipanel/gamepanel/gamedescription/LiarGameDescription';
import SmileLeadsToAlcoholDescription from '@/components/meetingpage/multipanel/gamepanel/gamedescription/SmileLeadsToAlcoholDescription';
import UpAndDownDescription from '@/components/meetingpage/multipanel/gamepanel/gamedescription/UpAndDownDescription';

export default {
  name: "GameSelectionPanel",
  components: {
    ConsonantQuizDescription,
    FindOutDrunkenDescription,
    LiarGameDescription,
    SmileLeadsToAlcoholDescription,
    UpAndDownDescription
  },
  data() {
    return {
      selectedDescription: null,
      penalty: null,
      items: [
        '술 한 잔 마시기', 
        '5분동안 음소거',
        '5분동안 카메라 정지'
      ],
    }
  },
  computed: {
    ...mapGetters('meetingStore', ['notModeHost'])
  },
  methods: {
    ...mapActions('meetingStore', ['sendGameRequest']),
    clickStartGame() {
      var request = new Object();
      request.gameId=this.selectedDescription;
      request.paneltyId=0;
      request.gameStatus=1;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    },
    selectDescription(value) {
      this.selectedDescription = value;
    }
  }
};
</script>

<style scoped>
.panel {
  background-color: #707070;
  height: 100%;
  max-height: 46vh;
}

h1,
h2,
h3,
h4,
h5,
h6,
p {
  color: white;
}

.top {
  height: 22%
}

.game-list {
  overflow-y: auto;
  height: 78%;
}

li{
  list-style: none;
  padding-top: 5px;
  padding-bottom: 5px;
}

li:hover {
  background-color: #d2d2d2;
}

.router-link-active, li > a {
  font-size: 0.8em;
  text-decoration: none;
  color: white;
}

li:hover > a {
  color: black;
}

.router-link-exact-active {
  color: #ECFF1E;
  text-shadow: 1px 1px 2px rgb(0, 0, 0, 0.7);
}

.game-selection {
  height: 45vh;
  background-color: black;
  border-right: 1px solid #707070;
  border-radius: 15px;
}

.slide-leave-active {
  transition: opacity 0.3s ease;
  opacity: 0;
  animation: slide-out 0.3s ease-out forwards;
}
.slide-leave {
  opacity: 1;
  transform: translateX(0);
}
.slide-enter-active {
  animation: slide-in 0.3s ease-out forwards;
}
@keyframes slide-out {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-30px);
  }
}
@keyframes slide-in {
  0% {
    transform: translateY(-30px);
  }
  100% {
    transform: translateY(0);
  }
}
</style>