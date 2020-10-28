<template>
  <div id="myinfo">
    <h1 id="title">회원가입</h1>
    <div class="row no-gutters">
      <div class="col-sm-4 infotitle">
        <p>닉네임</p>
      </div>
      <div class="col-sm-8 infosub">
        <input 
          class="info-inputbox w-100"
          v-model="signupData.name"
        >
      </div>
    </div>
    <div class="row no-gutters">
      <div class="col-sm-4 infotitle">
        <p>이메일</p>
      </div>
      <div class="col-sm-8 infosub">
        <input 
          class="info-inputbox w-100" 
          placeholder="ex) ssafy@naver.com"
          v-model="signupData.email"
        >
      </div>
    </div>
    <div class="row no-gutters">
      <div class="col-sm-4 infotitle">
        <p>주량</p>
      </div>
      <div class="col-sm-8 infosub" id="juryang">
        <div class="row no-gutters">
          <div class="col-sm-3">소주</div>
          <div class="col-sm-3">{{signupData.drinks[0].liquorLimit}}잔</div>
          <div class="col-sm-3">이미지</div>
          <div class="col-sm-3">
            <button 
              class="remove-col"
              @click="clickSubtract('soju')"
            >
              -
            </button>
            <button 
              class="add-col"
              @click="clickAdd('soju')"
            >
              +
            </button>
          </div>
        </div>
        <div class="row no-gutters">
          <div class="col-sm-3">맥주</div>
          <div class="col-sm-3">{{signupData.drinks[1].liquorLimit}}잔</div>
          <div class="col-sm-3">이미지</div>
          <div class="col-sm-3">
            <button 
              class="remove-col"
              @click="clickSubtract('beer')"
            >
              -
            </button>
            <button 
              class="add-col"
              @click="clickAdd('beer')"
            >
              +
            </button>
          </div>
        </div>
        <div class="row no-gutters">
          <div class="col-sm-3">막걸리</div>
          <div class="col-sm-3">{{signupData.drinks[2].liquorLimit}}잔</div>
          <div class="col-sm-3">이미지</div>
          <div class="col-sm-3">
            <button 
              class="remove-col"
              @click="clickSubtract('makegeolli')"
            >
              -
            </button>
            <button 
              class="add-col"
              @click="clickAdd('makegeolli')"
            >
              +
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="d-flex justify-content-center mt-3">
      <button
        class="btn btn-lg btn-yellow"
        @click="clickSignup"
      >
        가입하기
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/api'

export default {
  name: 'Info',
  data() {
    return {
      signupData: {
        name: '',
        email: '',
        drinks: [
          {
            liquorName: '소주',
            liquorLimit: 7,
          },
          {
            liquorName: '맥주',
            liquorLimit: 3,
          },
          {
            liquorName: '막걸리',
            liquorLimit: 3
          }
        ]
      }
    }
  },
  methods: {
    clickSubtract(type) {
      if (type === 'soju') {
        this.signupData.drinks[0].liquorLimit--
        if (this.signupData.drinks[0].liquorLimit === -1) {
          this.signupData.drinks[0].liquorLimit = 0
        }
      } else if (type === 'beer') {
        this.signupData.drinks[1].liquorLimit--
        if (this.signupData.drinks[1].liquorLimit === -1) {
          this.signupData.drinks[1].liquorLimit = 0
        }
      } else if (type === 'makegeolli') {
        this.signupData.drinks[2].liquorLimit--
        if (this.signupData.drinks[2].liquorLimit === -1) {
          this.signupData.drinks[2].liquorLimit = 0
        }
      }
    },
    clickAdd(type) {
      if (type === 'soju') {
        this.signupData.drinks[0].liquorLimit++
      } else if (type === 'beer') {
        this.signupData.drinks[1].liquorLimit++
      } else if (type === 'makegeolli') {
        this.signupData.drinks[2].liquorLimit++
      }
    },
    clickSignup() {
      axios.put(SERVER.URL + SERVER.ROUTES.user + '/' + this.$store.state.id, this.signupData, 
      {
        headers: {'X-AUTH-TOKEN' : this.$store.state.token}
      })
        .then(() => {
          console.log("가입 완료")
          this.$router.push('/')
        })
        .catch((err) => {
          console.log(err)
        })
    }
  }
};
</script>

<style  lang="scss">
#myinfo {
  text-align: left;
  padding: 40px 60px;
  color: white;
}

#title {
  font-size: 50px;
  padding-bottom: 10px;
}

.infotitle {
  font-size: 30px;
}

.infosub {
  font-size: 25px;
  padding: 0;
}

.info-inputbox {
  color: white;
}

.info-inputbox:active, .info-inputbox:link, .info-inputbox:focus {
  outline-style: none;
}

.col-sm-8,
.col-sm-4 {
  padding-top: 0;
  padding-bottom: 0;
}

.remove-col {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 10%;
}
.add-col {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 10%;
}

#juryang {
  .col-sm-3 {
    padding: 0px;
  }
}
</style>