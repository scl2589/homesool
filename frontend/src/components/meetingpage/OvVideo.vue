<template>
	<video class="video" autoplay/>
</template>

<script>
import { mapState } from 'vuex';
export default {
	name: 'OvVideo',
	props: {
		streamManager: Object,
		isSmileGame: Boolean
	},
	computed: {
		...mapState('meetingStore', ['changedFlag'])
	},
	watch: {
		changedFlag() {
			if (this.isSmileGame) {
				this.streamManager.addVideoElement(this.$el);
			}
		}
	},
	mounted () {
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>
<style scoped>
.video {
	max-width: 100%;
}

.height70 {
  max-height: 65vh !important;
}

.height30 {
	height: 30vh !important;
}

.height15 {
	height: 15vh !important;
}
</style>