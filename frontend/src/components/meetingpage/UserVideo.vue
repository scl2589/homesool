<template>
<div v-if="streamManager">
	<div>
		<div class="overlay d-flex justify-content-center align-items-center" v-if="anonymousHost">
			<img height="100%" src="@/assets/images/host.png" alt="">
		</div>
		<ov-video :stream-manager="streamManager"/>
	</div>
	<div v-if="!$route.name==='HomePage'">
		<p>{{clientData}}</p>
	</div>
</div>
</template>

<script>
import { mapState } from 'vuex';
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
		...mapState('meetingStore', ['anonymousHost']),
		clientData () {
			const { clientData } = this.getConnectionData();
			return clientData;
		},
	},
	methods: {
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

</style>