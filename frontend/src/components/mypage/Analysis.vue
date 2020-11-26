<template>
  <div class="mx-auto mt-3">
    <div class="row no-gutters">
      <div class="col-5">
        <h3 class="analysis-title">최근 음주 통계</h3>
        <div v-if="recentData">
          <recent
            class="analysis"
            :data="recentData"
          />
        </div>
      </div>
      <div class="col-5 offset-2">
        <h3 class="analysis-title">주종 통계</h3>
        <div v-if="alcoholTypeData">
          <alcohol-type
            class="analysis"
            :data="alcoholTypeData"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import Recent from "@/components/mypage/RecentChart.js";
import AlcoholType from "@/components/mypage/AlcoholTypeChart.js";

export default {
  name: 'Analysis',
  components: {
    Recent,
    AlcoholType
  },
  computed: {
    ...mapState(['user']),
    ...mapState('mypageStore', ['recentData', 'alcoholTypeData'])
  },
  watch: {
    user() {
      this.fetchAnalysisData();
    }
  },
  methods: {
    ...mapActions('mypageStore', ['fetchAnalysisData'])
  },
  created() {
    this.fetchAnalysisData();
  }
}
</script>

<style scoped>
.analysis-title {
  color: white !important;
}

.analysis {
  max-width: 25vw;
  margin-left: auto;
  margin-right: auto;
}
</style>