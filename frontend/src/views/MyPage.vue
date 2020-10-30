<template>
  <div>
    <h3 style="color:white;">Hello</h3>
    <router-link :to="{ name: 'Calendar' }">캘린더</router-link>
    <router-link :to="{ name: 'Analysis' }">통계</router-link>
    <router-view></router-view>
  </div>  
</template>

<script>
export default {
  name: 'MyPage'
}
</script>

<style>

</style>