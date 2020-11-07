<template>
  <div class="smile" data-app>
    <h5 class="pt-3">웃으면 술이와요😉 </h5>
    <div class="area d-flex flex-column justify-content-between">
      <div 
        id="carouselExampleControls" 
        class="carousel slide m-1 p-1" 
        data-ride="carousel"
      >
        <div class="carousel-inner">
          <div class="carousel-item active">
            <img 
              src="../../../../assets/images/sample.png" 
              class="d-block w-100" 
              alt="sample image"
            >
            <p class="description">
              1. 먼저 특정 주제를 선택합니다.
            </p>
          </div>
          <div class="carousel-item">
            <img 
              src="../../../../assets/images/sample.png" 
              class="d-block w-100" 
              alt="sample image"
            >
            <p class="description">
              2. 각 참여자들은 이를 가리고 <br>주어진 단어를 읽습니다.
            </p>
          </div>
          <div class="carousel-item">
            <img 
              src="../../../../assets/images/sample.png" 
              class="d-block w-100" 
              alt="sample image"
            >
            <p class="description">
              3. 이 과정에서 이를 보인다면<br> 당신은...벌칙 당첨!
            </p>
          </div>
          <div class="carousel-item">
            <button
            class="btn-yellow rounded"
            @click="clickSendTheme()"
          >
            주제선택
          </button>
          <button
            class="btn-yellow rounded"
            @click="clickStopGame()"
          >
            게임종료
          </button>
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
          <!-- Default dropup button -->
          <!-- <div class="btn-group dropup">
            <button type="button" class="btn btn-sm btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              벌칙 - {{ penalty }}
            </button>
            <div class="dropdown-menu">
              <li class="dropdown-item" @click="changePenalty('술 한 잔 마시기')">
                술 한 잔 마시기 
              </li>
              <li class="dropdown-item">
                5분동안 음소거
              </li>
              <li class="dropdown-item">
                5분동안 카메라 정지
              </li>
            </div>
          </div> -->
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

  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: 'SmileLeadsToAlcoholDescritpion',
  data() {
    return {
      penalty: null,
      items: [
        '술 한 잔 마시기', 
        '5분동안 음소거',
        '5분동안 카메라 정지'
      ],
    }
  },
   methods:{
     ...mapActions("meetingStore", [
      "clickChatPanel",
      "sendGameRequest",
    ]),
    clickStartGame() {
     alert("게임시작");
     var request = new Object();
     request.gameId=0;
     request.paneltyId=0;
     request.gameStatus=1;

     var jsonRequest = JSON.stringify(request);
     console.log(jsonRequest);
     this.sendGameRequest(jsonRequest);
    },
    clickSendTheme(){
      alert("주제선택");
      var request = new Object();
      request.gameId=0;
      request.theme="열대과일";
      request.gameStatus=2;

      var jsonRequest = JSON.stringify(request);
      console.log(jsonRequest);
      this.sendGameRequest(jsonRequest);
    },
    clickStopGame() {
     alert("게임종료");
     var request = new Object();
     request.gameId=0;
     request.paneltyId=0;
     request.gameStatus=3;

     var jsonRequest = JSON.stringify(request);
     console.log(jsonRequest);
     this.sendGameRequest(jsonRequest);
    },
  }
}
</script>

<style scoped>
p, h1, h2, h3, h4, h5, h6, button {
  color: white;
}

.smile {
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
/* 
.v-input {
  width: 60% !important;
}

.v-input__slot {
  width: 60% !important;
}

.v-input__control {
  width: 60% !important;
} */
</style>