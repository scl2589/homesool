<template>
<div v-if="streamManager">
	<div class="container">
		<div v-if="!($route.name==='HomePage')">
			<div class="drink-overlay" v-if="isPublisher">
				<img class="drink-minus" src="@/assets/images/minus.png" alt="한잔 덜 마셨어요" @click="updateUserDrinkRecord(-1)"> 
				<img class="drink" :src="getImgsrc" alt="현재주종" @click="setShowOthers">
				<img class="drink-plus" src="@/assets/images/plus.png" alt="한잔 더 마셨어요" @click="updateUserDrinkRecord(1)">
				<div class="select-other" v-if="showOthers">
					<div class="other" v-for="drink in user.drinks" :key="drink.liquorName"
						:v-model="pickedDrink" @click="setCurrentDrink(drink.liquorName)">
						{{drink.liquorName}}
					</div>
				</div>
			</div>
		</div>
		<div class="overlay d-flex justify-content-center align-items-center" v-if="currentMode === 'anonymous'">
			<img height="100%" src="@/assets/images/host.png" alt="">
		</div>
		<ov-video :stream-manager="streamManager"/>
	</div>
	<div v-if="!($route.name==='HomePage')">
		<!-- <p>{{userManager.stream.connection.data.slice(15,-2)}}</p> -->
	</div>
</div>
</template>

<script>
import { mapState , mapActions , mapGetters } from 'vuex';
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
		...mapState('meetingStore', ['currentMode','user','currentDrink','publisher']),
		...mapState(['user']),
		...mapGetters("meetingStore", ['getImgsrc']),
		clientData () {
			const { clientData } = this.getConnectionData();
			return clientData;
		},
	},
	methods: {
		...mapActions('meetingStore', ['updateUserDrinkRecord', 'changeCurrentDrink']),
		getConnectionData() {
			const { connection } = this.streamManager.stream;
			return JSON.parse(connection.data);
		},
		setShowOthers() {
			this.showOthers = !this.showOthers;
		},
		setCurrentDrink(value){
			this.showOthers = !this.showOthers;
			this.changeCurrentDrink(value);
		}
	},
	data(){
		return{
			showOthers : false,
			pickedDrink: null,
		}
	}
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

.select-other{
  background-color: white;
  width: 10%;
  position: absolute;
  top: 30%;
  left: 50%;
  color: black;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 30;
  padding : 10px;
}

.other{
	color: black;
	position: relative;
	z-index: 30;
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