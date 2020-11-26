<template>
  <div 
    class="liar" 
    data-app
  >
    <h5 class="pt-3">라이어 게임🙄 </h5>
    <div 
      id="carouselExampleControls" 
      class="carousel slide m-1 p-1" 
      data-ride="carousel"
    >
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img 
            class="d-block w-50  mx-auto" 
            :src="require('@/assets/images/liar1.png')" 
            alt="sample image"
          >
          <p class="description">
            1. 먼저 특정 주제를 선택합니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/liar2.png')" 
            alt="sample image"
          >
          <p class="description">
            2. 각 참여자에게 제시어가 주어집니다.<br>라이어에게는 빈 화면이 보여집니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/liar3.png')" 
            alt="sample image"
          >
          <p class="description">
            3. 참여자들은 한 번씩 돌아가면서 <br>해당 제시어에 대해 설명합니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/liar5.png')" 
            alt="sample image"
          >
          <p class="description">
            4. 라이어일 경우 제시어를 추측해<br>아는 척 합니다😉.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/liar4.png')" 
            alt="sample image"
          >
          <p class="description">
            5. 참여자들은 라이어 같은 사람을 <br>투표합니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            :src="require('@/assets/images/liar6.png')" 
            class="d-block w-50 mx-auto" 
            alt="sample image"
          >
          <p class="description">
            6. 라이어를 맞히지 못한 시민들 중 <br> 한 명만 벌칙 당첨!!
          </p>
        </div>
      </div>
      <button 
        class="carousel-control-prev" 
        href="#carouselExampleControls" 
        role="button" 
        data-slide="prev"
      >
        <span 
          class="carousel-control-prev-icon" 
          aria-hidden="true"
        ></span>
        <span class="sr-only">Previous</span>
      </button>
      <button 
        class="carousel-control-next" 
        href="#carouselExampleControls" 
        role="button" 
        data-slide="next"
      >
        <span 
          class="carousel-control-next-icon" 
          aria-hidden="true"
        ></span>
        <span class="sr-only">Next</span>
      </button>
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

img {
  border: 1px solid white;
}
</style>