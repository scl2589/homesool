<template>
	<video class="video" autoplay/>
</template>

<script>
export default {
	name: 'OvVideo',
	props: {
		streamManager: Object,
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
  height: 65vh !important;
}

.height30 {
	height: 30vh !important;
}

.height15 {
	height: 15vh !important;
}
</style>