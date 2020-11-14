<template>
  <div class="drunken" data-app>
    <div>
      <h5 class="pt-3">나술안취했어🤢</h5>
      <div 
        id="carouselExampleControls" 
        class="carousel slide m-1 p-1" 
        data-ride="carousel"
      >
        <div class="carousel-inner">
          <div class="carousel-item active">
            <img 
              :src="require('@/assets/images/drunken1.png')" 
              class="d-block w-50 mx-auto" 
              alt="sample image"
            >
            <p class="description">
              1. 누가 나술안취했어 게임을 할 지 <br> 참가자를 선택합니다.
            </p>
          </div>
          <div class="carousel-item">
            <img 
              :src="require('@/assets/images/drunken2.png')" 
              class="d-block w-50 mx-auto" 
              alt="sample image"
            >
            <p class="description">
              2. 참가자는 주어진 문장을<br> 시간 안에 읽습니다.
            </p>
          </div>
          <div class="carousel-item">
            <img 
              :src="require('@/assets/images/drunken3.png')" 
              class="d-block w-50 mx-auto" 
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
    </div>
  </div>
</template>

<script>
export default {
  name: 'UpAndDownDescription'
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

img {
  border: 1px solid white;
}
</style>