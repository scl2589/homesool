<template>
<div v-if="streamManager">
	<div>
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
	<div v-if="!$route.name==='HomePage'">
		<p>{{clientData}}</p>
	</div>
</div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import OvVideo from './OvVideo';
export default {
	name: 'UserVideo',
	components: {
		OvVideo,
	},
	props: {
		streamManager: Object,
	},
	computed: {
		...mapState('meetingStore', ['currentMode', 'gotWasted']),
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
		...mapActions('meetingStore', ['offGotWasted']),
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

</style>