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
</style>