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
              :src="require('@/assets/images/sample.png')" 
              class="d-block w-100" 
              alt="sample image"
            >
            <p class="description">
              1. 먼저 특정 주제를 선택합니다.
            </p>
          </div>
          <div class="carousel-item">
            <img 
              :src="require('@/assets/images/sample.png')" 
              class="d-block w-100" 
              alt="sample image"
            >
            <p class="description">
              2. 각 참여자들은 이를 가리고 <br>주어진 단어를 읽습니다.
            </p>
          </div>
          <div class="carousel-item">
            <img 
              :src="require('@/assets/images/sample.png')" 
              class="d-block w-100" 
              alt="sample image"
            >
            <p class="description">
              3. 이 과정에서 이를 보인다면<br> 당신은...벌칙 당첨!
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
  name: 'SmileLeadsToAlcoholDescritpion'
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