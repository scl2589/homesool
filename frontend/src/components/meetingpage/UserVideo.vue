<template>
<div v-if="streamManager">
	<ov-video :stream-manager="streamManager" :is-smile-game="isSmileGame" />

	<div class="drink-container">

		<div class="drink-overlay d-flex justify-content-around align-items-center" v-if="isLeftPanel && isPublisher">
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

		<div class="overlay-anonymous d-flex justify-content-center align-items-center" v-if="currentMode === 'anonymous'">
			<img class="anonymous-img" :src="require(`@/assets/images/${anonyMousImg(streamManager.stream.connection.connectionId)}.png`)" alt="">
		</div>

		<div class="overlay-drunken d-flex justify-content-center align-items-center w-100" v-if="drunkenList.length && drunkenList.includes(streamManager.stream.connection.connectionId) && isLeftPanel && currentMode !== 'anonymous'">
			<img width="10%" src="@/assets/images/drunken.png" alt="">
			<p class="mb-0 mx-2 black">나는 고주망태입니다.</p>
			<img width="10%" src="@/assets/images/drunken.png" alt="">
		</div>

		<div class="d-flex justify-content-between" v-if="isLeftPanel">
			<div class="overlay-name d-flex justify-content-center align-items-center" v-if="nickName">
				<p class="px-2 client-name">{{ clientData }}</p>
			</div>

			<div class="overlay-drink-count d-flex" v-if="currentMode !== 'anonymous'">
				<div v-if="isPublisher">
					<div class="drink-count-container">
						<img width="15px" src="@/assets/images/shot.png" alt=""> x {{totalDrink}}  
					</div>
				</div>
				<div v-else> <!--subscriber-->
					<div v-if="streamManager.totalDrink">
						<div class="drink-count-container">
							<img width="15px" src="@/assets/images/shot.png" alt=""> x {{streamManager.totalDrink}} 
						</div>
					</div>
					<div v-else>
						<div class="drink-count-container">
							<img width="15px" src="@/assets/images/shot.png" alt=""> x 0
						</div>
					</div>
				</div>
			</div>
		</div>

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
		isPublisher: Boolean,
		isLeftPanel: Boolean,
		isSmileGame: Boolean
	},
	data() {
    return {
			showOthers : false,
			pickedDrink: null,
      anonymousImages: [
        '001-unicorn',
        '002-magic-hat',
        '003-wizard',
        '004-magic-hat-1',
        '005-illusionist',
        '006-moon',
        '007-magician',
        '008-witch',
        '009-fortune-teller',
				'010-cat',
				'011-fortune-teller-1',
				'012-magician-1',
				'013-magician-2',
				'014-witch-1',
				'015-elf',
				'016-gnome',
				'017-knight',
				'018-fairy',
				'019-witch-2',
				'020-little-red-riding-hood',
				'021-wolf',
				'022-giant',
				'023-dwarf',
				'024-wizard-1',
				'025-yeti',
				'026-king',
				'027-wizard-2',
				'028-dwarf-1',
				'029-little-red-riding-hood-1',
				'030-witch-3',
				'031-king-1',
				'032-viking',
				'033-queen',
				'034-santa-claus',
				'035-witch-4',
				'036-vampire',
				'037-witch-5',
				'038-owl',
				'039-santa-claus-1',
				'040-pirate',
				'041-nurse',
				'042-queen-1',
				'043-clown',
				'044-cowboy',
				'045-jack-o-lantern',
				'046-clown-1',
				'047-scarecrow',
				'048-knight-1',
				'049-mummy',
				'050-ninja'
      ]
    }
  },
	computed: {
		...mapState('meetingStore', [
			'currentMode',
			'currentDrink',
			'publisher',
			'totalDrink',
			'nickName',
			'drunkenList',
			'changedFlag'
			]),
		...mapState(['user']),
		...mapGetters("meetingStore", ['getImgsrc']),

		clientData () {
			const { clientData } = this.getConnectionData();
			return clientData;
		}
	},
	watch: {
		changedFlag() {
			this.$forceUpdate();
		}
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
		},
		anonyMousImg(connectionId) {
			let result = 0;
			for (let i = 0, len = connectionId.length; i < len; i++ ) {
        result += connectionId[i].charCodeAt(0);
      }
      return this.anonymousImages[result % 50]
    }
	}
};
</script>
<style scoped>
p {
	color: white !important;
}

.overlay-anonymous{
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
  background-color: #323031;
}

/* .overlay-name{
  position: absolute;
  bottom: 0%;
  left: 0%;
  width: 100%;
  height: 10%;
  z-index: 12;
} */

.overlay-drunken{
  position: absolute;
  bottom: 30%;
  left: 0%;
  width: 100%;
  height: 100%;
  z-index: 10;
}

.drink-overlay{
 position: absolute;
 top: 0;
 bottom: 0;
 left: 0;
 right: 0;
 width:100%;
 opacity: 1;
 transition: .3s ease;
}

.drink{
  background-color: white;
  border-radius: 50%;
  height: 30%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 20;
  opacity: 0;
  padding : 10px;
  cursor:pointer;
}

.drink-minus{
  background-color: white;
  border-radius: 50%;
  height: 15%;
  position: absolute;
  top: 50%;
  left: 30%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 20;
  opacity: 0;
  padding : 10px;
  cursor:pointer;
}

.drink-plus{
  background-color: white;
  border-radius: 50%;
  height: 15%;
  position: absolute;
  top: 50%;
  left: 70%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 20;
  opacity: 0;
  padding : 10px;
  cursor:pointer;
}

.select-other{
  background-color: white;
  width: 10vw;
  position: absolute;
  top: 20%;
  left: 50%;
  color: black;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  z-index: 30;
}

.other{
	color: black;
	position: relative;
	z-index: 30;
	cursor:pointer;
	border-bottom: 0.5px solid grey;
	padding:3px;
}

.overlay-drink-count{
	z-index: 30;
}

.drink-count-container{
	background-color: transparent;
	color:white;
	font-size: 0.9rem;
	text-shadow:  #FC0 1px 0 10px;
}

.drink-container {
	max-height: 3vh;
	margin-left: auto;
	margin-right: auto;
}

.drink-container:hover .drink{
	opacity: 0.8;
}
.drink-container:hover .drink-minus{
	opacity: 0.4;
}
.drink-container:hover .drink-plus{
	opacity: 0.4;
}

.anonymous-img {
	max-height: 80%;
	max-width: 80%;
}

.client-name {
	text-shadow: #FC0 1px 0 10px;
}
</style>