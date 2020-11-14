<template>
  <div id="wrapregister">
    <info :username="user.name" :useremail="user.email" :userdrinks="user.drinks" />
  </div>
</template>

<script>
import Info from "@/components/Info.vue";
import { mapState } from 'vuex';
export default {
  name: 'ProfilePage',
  components: {
    Info,
  },
  computed: {
    ...mapState(['user'])
  },
}
</script>

<style scoped>
#wrapregister {
  background-color: rgba(255, 255, 255, 0.1);
  margin: auto;
  width: 60%;
  height: 70%;
  border-radius: 25px;
}
</style>