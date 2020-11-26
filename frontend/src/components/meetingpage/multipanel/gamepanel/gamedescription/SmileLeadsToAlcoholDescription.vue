<template>
  <div 
    class="smile" 
    data-app
  >
    <h5 class="pt-3">웃으면 술이와요😉 </h5>
    <div 
      class="carousel slide m-1 p-1" 
      id="carouselExampleControls" 
      data-ride="carousel"
    >
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/smile1.png')" 
            alt="smile-leads-to-alcohol-description1"
          >
          <p class="description">
            1. 먼저 특정 주제를 선택합니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/smile2.png')" 
            alt="smile-leads-to-alcohol-description2"
          >
          <p class="description">
            2. 각 참여자들은 이를 가리고 <br>주어진 단어를 읽습니다.
          </p>
        </div>
        <div class="carousel-item">
          <img 
            class="d-block w-50 mx-auto" 
            :src="require('@/assets/images/smile3.png')" 
            alt="smile-leads-to-alcohol-description3"
          >
          <p class="description">
            3. 이 과정에서 웃는다면<br> 당신은...벌칙 당첨!
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
        role="button" data-slide="next"
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