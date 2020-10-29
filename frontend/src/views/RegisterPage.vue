<template>
  <div id="wrapregister">
    <info />
  </div>
</template>

<script>
import Info from "@/components/Info.vue";
export default {
  name: "RegisterPage",
  components: {
    Info,
  },
  data: () => {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss">
#wrapregister {
  background-color: rgba(255, 255, 255, 0.1);
  margin: auto;
  width: 60%;
  height: 70%;
  border-radius: 25px;
}
</style>