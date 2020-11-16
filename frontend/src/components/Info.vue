<template>
  <div id="myinfo">
    <h1 id="title" v-if="$route.name === 'RegisterPage'">회원가입</h1>
    <h1 id="title" v-if="$route.name === 'ProfilePage'">프로필</h1>
    <div class="d-flex flex-column form">
      <div class="row no-gutters">
        <div class="col-sm-4 infotitle">
          <p>닉네임</p>
        </div>
        <div class="col-sm-8 infosub">
          <input 
            class="info-inputbox w-100"
            placeholder="홈술이"
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
            placeholder="ssafy@ssafy.com"
            v-model="signupData.email"
          >
        </div>
      </div>
      <div class="row no-gutters">
        <div class="col-sm-4 infotitle">
          <p>주량</p>
        </div>
        <div class="col-sm-8 infosub" id="juryang">
          <div 
            class="row no-gutters"
            v-for="(drink, i) in signupData.drinks"
            :key=i
          >
            <div class="col-sm-3">{{drink.liquorName}}</div>
            <div class="col-sm-3">{{drink.liquorLimit}}잔</div>
            <div class="col-sm-3">
              <button 
                class="remove-col mr-1"
                @click="clickSubtract(drink.liquorName)"
              >
                -
              </button>
              <button 
                class="add-col"
                @click="clickAdd(drink.liquorName)"
              >
                +
              </button>
            </div>
            <div class="col-sm-3 text-right">
              <i 
                class="fas fa-trash delete"
                @click="clickDelete(i)"
              ></i>
            </div>
          </div>
          <div class="d-flex justify-content-start mt-2">
            <div class="col-sm-6">
              <input
                class="new-liquor"
                type="text"
                v-model="newLiquor"
                placeholder="주종 입력"
                @keyup.enter="clickAddLiquor"
              >
            </div>
            <div class="offset-sm-3 col-sm-3">
              <button 
                class="btn btn-secondary"
                @click="clickAddLiquor"
              >
                추가하기
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-center mt-auto">
        <button
          class="btn btn-lg btn-yellow"
          @click="clickSignup"
          v-if="$route.name === 'RegisterPage'"
        >
          가입하기
        </button>
        <button
          class="btn btn-lg btn-yellow"
          @click="clickSignup"
          v-if="$route.name === 'ProfilePage'"
        >
          수정하기
        </button>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import SERVER from '@/api/api'
import Swal from 'sweetalert2'
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'Info',
  data() {
    return {
      signupData: {
        name: '',
        email: '',
        drinks: [
        ]
      },
      newLiquor: null
    }
  },
  props: {
    username: String,
    useremail: String,
    userdrinks: Object
  },
  computed: {
    ...mapGetters(['getId'])
  },
  methods: {
    ...mapActions(['getMyInfo']),
    clickSubtract(liquorName) {
      for (var drink of this.signupData.drinks) {
        if (drink.liquorName == liquorName) {
          drink.liquorLimit--
          if (drink.liquorLimit === -1) {
            drink.liquorLimit = 0
          }
        }
      }
    },
    clickAdd(liquorName) {
      for (var drink of this.signupData.drinks) {
        if (drink.liquorName == liquorName) {
          drink.liquorLimit++
        }
      }
    },
    clickAddLiquor() {
      if (this.newLiquor !== null) {
        this.signupData.drinks.push({
          liquorName: this.newLiquor,
          liquorLimit: 1,
        })
        this.newLiquor = null
      } else {
        var swal = Swal.mixin({
          customClass: {
            confirmButton: 'btn btn-danger btn-lg',
          },
          buttonsStyling: false
        })

        swal.fire({
          title: "주종을 입력해주세요",
          icon: "warning",
        })
      }
    },
    clickDelete(index) {
      this.signupData.drinks.splice(index, 1)
    },
    clickSignup() {
      if (this.signupData.drinks.length) {
        axios.put(SERVER.URL + SERVER.ROUTES.user + '/' + this.getId, this.signupData, 
        {
          headers: {'X-AUTH-TOKEN' : this.$store.state.token}
        })
          .then(() => {
            if (this.$route.name === 'RegisterPage') {
              this.getMyInfo()
              this.$router.push('/')
            } else if (this.$route.name === 'ProfilePage') {
              this.$router.push({ name: 'MyPage'})
            }
          })
      } else {
        Swal.fire({
          title: "한 종류 이상의 주량을 등록해주세요!",
          icon: "warning",
        })
      }
    }
  },
  mounted() {
    this.signupData.name = this.username;
    this.signupData.email = this.useremail;
    this.signupData.drinks = this.userdrinks;
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
  padding: 0 10%;
  border-radius: 10%;
}
.add-col {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 0 10%;
  border-radius: 10%;
}

#juryang {
  .col-sm-3, .col-sm-6 {
    padding: 0;
  }
}

.new-liquor {
  font-size: 0.8em;
  color: white;
  width: 50%;
}

.new-liquor:active, .new-liquor:link, .new-liquor:focus {
  outline-style: none;
  border-bottom: 1px solid white;
}

.form {
  height: 50vh;
}

.row {
  flex: 0 0 0 !important;
}

.delete {
  cursor: pointer;
}
</style>