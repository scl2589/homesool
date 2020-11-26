<template>
  <div
    class="container mx-auto"
    v-if="user"
  >
    <div class="d-flex mx-auto profile">
      <img 
        src="@/assets/images/host.png" 
        alt="프로필" 
      />
    </div>
    <p class="mb-0 mx-auto text-white">{{ user.name }}</p>
    <div class="d-flex justify-content-between">
      <div 
        :class="{ 'box-selected': ($route.name === 'Calendar'), 'box': ($route.name !== 'Calendar')}" 
        @click="clickCalendar"
      >
        <h3 class="mb-0">음주 달력</h3>
      </div>
      <div class="name d-flex justify-content-center">
        <button 
          class="btn px-2 edit-profile" 
          @click="clickEditProfile"
        >
          프로필 수정
        </button>
      </div>
      <div 
        :class="{ 'box-selected': ($route.name === 'Analysis'), 'box': ($route.name !== 'Analysis')}" 
        @click="clickAnalysis"
      >
        <h3 class="mb-0">음주 분석</h3>
      </div>
    </div>
    <router-view class="mt-5"></router-view>
  </div>  
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'MyPage',
  computed: {
    ...mapState(['user'])
  },
  methods: {
    ...mapActions(['getMyInfo']),
    clickEditProfile() {
      this.$router.push({ name: 'ProfilePage'})
    },
    clickCalendar() {
      this.$router.push({ name: 'Calendar'})
    },
    clickAnalysis() {
      this.$router.push({ name: 'Analysis'})
    }
  },
  created() {
    this.getMyInfo();
  },
  mounted() {
    if (this.$route.name !== 'Calendar') {
      this.$router.push({ name: 'Calendar'});
    }
  }
}
</script>

<style scoped>
.margin-left {
  margin-left: 10vw;
}

.box {
  background-color: #FFFF00;
  width: 30vw;
  cursor: pointer;
  padding-top: 1vh;
  padding-bottom: 1vh;
}

.box-selected {
  background-color: #ffd000;
  width: 30vw;
  cursor: pointer;
  padding-top: 1vh;
  padding-bottom: 1vh;
}

.name {
  width: 20vw;
  color: white;
  font-size: 2rem;
}

.profile {
  background-color: #8B8B8B;
  width: 10vw;
  border-radius: 50%;
}

img {
  max-width: 10vw;
  border-radius: 50%;
}

.link {
  font-size: 2rem;
}

.link:active, .link:focus, .link:hover, .link:visited {
  color: black !important;
  text-decoration: none;
}

.edit-profile {
  border: 1px solid white;
  color: white;
}
</style>