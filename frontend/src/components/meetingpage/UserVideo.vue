<template>
<div v-if="streamManager">
	<div class="container">
		<div v-if="!($route.name==='HomePage')">
			<div class="drink-overlay" v-if="isPublisher">
				<img class="drink-minus" src="@/assets/images/minus.png" alt="한잔 덜 마셨어요" @click="updateUserDrinkRecord(-1)"> 
				<img class="drink" :src="require(`@/assets/images/${currentDrink}.png`)" alt="${currentDrink}">
				<img class="drink-plus" src="@/assets/images/plus.png" alt="한잔 더 마셨어요" @click="updateUserDrinkRecord(1)">
			</div>
		</div>
		<div class="overlay d-flex justify-content-center align-items-center" v-if="currentMode === 'anonymous'">
			<img height="100%" src="@/assets/images/host.png" alt="">
		</div>
		<div class="overlay-drunken d-flex justify-content-center align-items-center w-100" v-if="gotWasted && currentMode !== 'anonymous'">
			<img width="10%" src="@/assets/images/drunken.png" alt="">
			<p class="mb-0 mx-2">나는 고주망태입니다.</p>
			<img width="10%" src="@/assets/images/drunken.png" alt="">
		</div>
		<ov-video :stream-manager="streamManager"/>
	</div>
	<div v-if="!($route.name==='HomePage')">
		<!-- <p>{{userManager.stream.connection.data.slice(15,-2)}}</p> -->
	</div>
</div>
</template>

<script>
import { mapState , mapActions } from 'vuex';
import OvVideo from './OvVideo';
export default {
	name: 'UserVideo',
	components: {
		OvVideo,
	},
	props: {
		streamManager: Object,
		isPublisher : Boolean,
	},
	computed: {
		...mapState('meetingStore', ['currentMode', 'gotWasted', 'user','currentDrink','publisher']),
		clientData () {
			const { clientData } = this.getConnectionData();
			return clientData;
		},
	},
	watch: {
		gotWasted(value) {
			if (value) {
				setTimeout(() => {
					this.offGotWasted();
				}, 120000);
			}
		}
	},
	methods: {
		...mapActions('meetingStore', ['updateUserDrinkRecord', 'offGotWasted']),
		getConnectionData () {
			const { connection } = this.streamManager.stream;
			return JSON.parse(connection.data);
		},
	},
};
</script>
<style scoped>
p {
	color: white !important;
}

.overlay{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
  background-color: rgba(0,0,0,1); /*dim the background*/
}

.overlay-drunken{
  position: absolute;
  top: 10%;
  left: 0%;
  width: 100%;
  height: 10%;
  z-index: 10;
}

.drink-overlay{
 position: absolute;
 top: 0;
 bottom: 0;
 left: 0;
 right: 0;
 height: 100%;
 opacity: 1;
 transition: .3s ease;
}

.drink{
  background-color: white;
  border-radius: 50%;
  width: 7%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 20;
  opacity: 0;
  padding : 10px;
}

.drink-minus{
  background-color: white;
  border-radius: 50%;
  width: 5%;
  position: absolute;
  top: 50%;
  left: 40%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 20;
  opacity: 0;
  padding : 10px;
}

.drink-plus{
  background-color: white;
  border-radius: 50%;
  width: 5%;
  position: absolute;
  top: 50%;
  left: 60%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 20;
  opacity: 0;
  padding : 10px
}

.container:hover .drink{
	opacity: 0.8;
}
.container:hover .drink-minus{
	opacity: 0.4;
}
.container:hover .drink-plus{
	opacity: 0.4;
}
</style>