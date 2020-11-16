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
          <h6 class="mt-5 d-flex justify-content-center">술게임을<br>골라주세요!</h6>
          <br />
        </div>
        <div class="game-list">
          <li @click="selectGameId(1)">Up & Down</li>
          <li @click="selectGameId(2)">자음 퀴즈</li>
          <li @click="selectGameId(3)">라이어 게임</li>
          <li @click="selectGameId(4)">웃으면 술이와요</li>
          <li @click="selectGameId(5)">나술안취했어</li>
        </div>
      </div>
      <div class="col-8 my-1">
        <transition name="slide" mode="out-in">
          <div class="descriptions d-flex flex-column justify-content-between">
            <div v-if="selectedGameId == 1">
              <UpAndDownDescription />
            </div>
            <div v-if="selectedGameId == 2">
              <ConsonantQuizDescription />
            </div>
            <div v-if="selectedGameId == 3">
              <LiarGameDescription />
            </div>
            <div v-if="selectedGameId == 4">
              <SmileLeadsToAlcoholDescription />
            </div>
            <div v-if="selectedGameId == 5">
              <FindOutDrunkenDescription />
            </div>

            <div
              class="d-flex justify-content-between align-items-center mx-2 mt-auto mb-2"
              v-if="selectedGameId"
            >
              <div class="penalty">
                <v-select
                  v-model="penalty"
                  :items="penalties"
                  label="벌칙"
                  width="10px"
                  hide-details
                  dense
                  append-icon=""
                  solo
                  @input="checkInput"
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
import Swal from 'sweetalert2'
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
      selectedGameId: null,
      penalty: '',
      items: [
        '술 한 잔 마시기',
        '노래 부르기', 
        '댄스댄스',
        '직접 입력'
      ],
      notDrunkenItems: [
        '나는 고주망태'
      ]
    }
  },
  computed: {
    ...mapGetters('meetingStore', ['notModeHost']),
    penalties() {
      if (this.selectedGameId == 5) {
        return this.notDrunkenItems;
      } else {
        return this.items;
      }
    }
  },
  methods: {
    ...mapActions('meetingStore', ['sendGameRequest']),
    clickStartGame() {
      if (!this.penalty || this.penalty === '직접 입력') {
        Swal.fire({
          icon: 'warning',
          html: '벌칙을 선택해주세요',
        })
        return;
      }
      var request = new Object();
      request.gameId=this.selectedGameId;
      request.panelty=this.penalty;
      request.gameStatus=1;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    },
    selectGameId(value) {
      this.selectedGameId = value;
    },
    checkInput() {
      if (this.penalty === '직접 입력') {
        Swal.fire({
          title: '벌칙을 입력해주세요.',
          input: 'text',
          inputAttributes: {
            autocapitalize: 'off'
          },
          showCancelButton: false,
          confirmButtonText: '확인',
          showLoaderOnConfirm: true,
        }).then((result) => {
          if (result.value) {
            this.items.splice(-1, 0, result.value);
            this.penalty = result.value;
          }
        })
      }
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
p,
li {
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
  color: black;
}

li:hover > a {
  color: black;
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

.descriptions {
  height: 45vh;
  background-color: black;
  border-left: 1px solid #707070;
  border-radius: 15px;
}

.penalty {
  max-width: 70%;
}
</style>