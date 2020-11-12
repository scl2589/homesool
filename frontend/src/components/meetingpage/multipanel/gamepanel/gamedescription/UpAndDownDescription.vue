<template>
  <div class="upanddown" data-app>
    <h5 class="pt-3">Up🔺 & Down🔻 </h5>
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
            1. 각 참여자들은 순서대로 <br>추측하는 숫자를 기입합니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            :src="require('@/assets/images/sample.png')" 
            class="d-block w-100" 
            alt="sample image"
          >
          <p class="description">
            2. 추측한 숫자보다 <br> Up인지 Down인지 확인합니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            :src="require('@/assets/images/sample.png')" 
            class="d-block w-100" 
            alt="sample image"
          >
          <p class="description">
            3. 숫자를 맞힌다면 Win!!!<br> 이전 순서의 사람이 벌칙 당첨!!
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
</style>