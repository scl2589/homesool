<template>
  <div class="drunken" data-app>
    <div v-if="!selectParticipant">
      <h5 class="pt-3">나술안취했어🤢</h5>
      <div class="area d-flex flex-column justify-content-between">
        <div 
          id="carouselExampleControls" 
          class="carousel slide m-1 p-1" 
          data-ride="carousel"
        >
          <div class="carousel-inner">
            <div class="carousel-item active">
              <img 
                :src="require('@/assets/images/sample.png')" 
                class="d-block w-100" 
                alt="sample image"
              >
              <p class="description">
                1. 누가 나술안취했어 게임을 할 지 <br> 참가자를 선택합니다.
              </p>
            </div>
            <div class="carousel-item">
              <img 
                :src="require('@/assets/images/sample.png')" 
                class="d-block w-100" 
                alt="sample image"
              >
              <p class="description">
                2. 참가자는 주어진 문장을<br> 시간 안에 읽습니다.
              </p>
            </div>
            <div class="carousel-item">
              <img 
                :src="require('@/assets/images/sample.png')" 
                class="d-block w-100" 
                alt="sample image"
              >
              <p class="description">
                3. 만약 제대로 읽지 못한다면<br> 취한 당신은 벌칙 당첨!! 🍺
              </p>
            </div>
          </div>
          <button class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
          </button>
          <button class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
          </button>
        </div>
        <div class="d-flex justify-content-between align-items-center mx-2 mt-auto">
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
              @click="clickStartGame"
            >
              시작하기
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="p-3" v-else>
      <div>
        <h5>참가자를 선택해주세요</h5>
      </div>
      <div class="d-flex row no-gutters mt-5">
        <div class="col-6 box my-2 py-2">
          <button 
            class="btn"
            @click="clickParticipant(publisher.stream.connection.connectionId)"
          >
            {{ publisher.stream.connection.data.slice(15, -2)}}
          </button>
        </div>
        <div 
          v-for="(subscriber, id) in subscribers" 
          :key="id"
          class="col-6 box my-2 py-2"
        >
          <button 
            class="btn"
            @click="clickParticipant(subscriber.stream.connection.connectionId)"
          >
            {{ subscriber.stream.connection.data.slice(15, -2)}}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'UpAndDownDescription',
  data() {
    return {
      penalty: null,
      items: [
        '술 한 잔 마시기', 
        '5분동안 음소거',
        '5분동안 카메라 정지'
      ],
      selectParticipant: false
    }
  },
  computed: {
    ...mapState('meetingStore', ['subscribers', 'publisher'])
  },
  methods: {
    ...mapActions('meetingStore', ['sendGameRequest']),
    clickStartGame() {
      this.selectParticipant = true
    },
    clickParticipant(id) {
      var request = new Object();
      request.gameId = 5;
      request.participantPublicId = id;
      request.paneltyId = 0;
      request.gameStatus = 1;
      var jsonRequest = JSON.stringify(request);
      this.sendGameRequest(jsonRequest);
    }
  }
}
</script>

<style scoped>
p, h1, h2, h3, h4, h5, h6, button {
  color: white;
}

.drunken {
  height: 100%;
  max-height: 45vh;
  background-color: black;
  border-left: 1px solid #707070;
  border-radius: 15px;
}

.description {
  margin-top: 20px;
  padding: 0 10px;
}

.carousel {
  border: 1px solid black;
  height: 30vh;
}

.area {
  height: 38vh;
}

.penalty {
  width: 70%;
}
.v-input__slot {
  padding: 0 !important;
  margin: 0 !important;
  height: 5vh !important;
}

.v-select__selection--comma {
  margin: 0 !important;
}

.box {
  background-color: yellow;
  border-radius: 10px;
}

button {
  color: black;
}
</style>