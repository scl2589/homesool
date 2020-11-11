<template>
  <div>
    <div class="d-flex m-auto profile">
      <img src="@/assets/images/host.png" alt="프로필" />
    </div>
    <div class="d-flex">
      <div class="box margin-left">
        <router-link 
          class="link"
          :to="{ name: 'Calendar' }"
        >음주 달력</router-link>
      </div>
      <div class="name d-flex justify-content-center">
        <p class="m-0 d-flex align-items-center">{{ user.name }}</p>
      </div>
      <div class="box">
        <router-link 
          class="link"
          :to="{ name: 'Analysis' }"
        >음주 분석</router-link>
      </div>
    </div>
    <router-view></router-view>
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
    ...mapActions(['getMyInfo'])
  },
  created() {
    this.getMyInfo
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
</style>