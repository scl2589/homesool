<template>
  <div>
    <div>
      <vc-calendar
        class="custom-calendar max-w-full"
        is-expanded
        disable-page-swipe
        :attributes='calendarLogs'
        :max-date="new Date()"
        locale="ko-kr"
      >
      <template v-slot:day-content="{ day, attributes }">
        <div class="flex flex-col h-full z-10 overflow-hidden">
          <span class="day-label text-sm text-gray-900">{{ day.day }}</span>
          <div class="flex-grow overflow-y-auto overflow-x-auto custom-overflow" >
            <div
              class="text-xs leading-tight rounded-sm p-1 mt-0 mb-1"
              :class="attr.customData.class"
              v-for="attr in attributes"
              :key="attr.key"
            >
              <p
                class="mb-0"
                @click="showCalendarDetail(attr.key)"
              >
                {{ attr.customData.title }}
              </p>
            </div>
          </div>
        </div>
      </template>
      </vc-calendar>
    </div>

    <CalendarDetail v-if="meetingLogDialog" />

  </div>  
</template>

<script>
import { mapActions, mapGetters,mapState } from 'vuex';
import CalendarDetail from '@/components/mypage/CalendarDetail';

export default {
  name: 'Calendar',
  components: {
    CalendarDetail
  },
  computed: {
    ...mapGetters(['getId']),
    ...mapState('mypageStore',[
      'meetingLogDialog',
      'calendarLogs'
    ])
  },
  methods: {
    ...mapActions('mypageStore',[
      'changeMeetingLogDialog',
      'fetchRoomLogs',
      'clearRoomLogs',
      'showCalendarDetail'
    ]),
  },
  created() {
    this.fetchRoomLogs();
  },
  beforeDestroy() {
    this.clearRoomLogs();
  }
}
</script>

<style lang="postcss" scoped>
::-webkit-scrollbar {
  width: 0px;
}
::-webkit-scrollbar-track {
  display: none;
}
/deep/ .custom-calendar.vc-container {
  --day-border: 1px solid rgb(0, 0, 0, 0.2) !important;
  --day-border-highlight: 1px solid rgb(0, 0, 0, 0.2) !important;
  --day-width: 90px;
  --day-height: 90px;
  --weekday-bg: #f8fafc;
  --weekday-border: 1px solid rgb(0, 0, 0, 0.2) !important;
  border-radius: 0;
  width: 100%;
  & .vc-header {
    background-color: #f1f5f8;
    padding: 10px 0;
  }
  & .vc-weeks {
    padding: 0;
  }
  & .vc-weekday {
    background-color: var(--weekday-bg);
    border-bottom: var(--weekday-border);
    border-top: var(--weekday-border);
    padding: 5px 0;
  }
  & .vc-day {
    padding: 0 5px 3px 5px;
    text-align: left;
    height: var(--day-height);
    min-width: var(--day-width);
    background-color: white;
    &.weekday-1,
    &.weekday-7 {
      background-color: #eff8ff;
    }
    &:not(.on-bottom) {
      border-bottom: var(--day-border);
      &.weekday-1 {
        border-bottom: var(--day-border-highlight);
      }
    }
    &:not(.on-right) {
      border-right: var(--day-border);
    }
  }
  & .vc-day-dots {
    margin-bottom: 5px;
  }
}
</style>

<style scoped>
.vc-pane-container {
  width: 50%;
  position: relative;
}

.custom-overflow {
  height: 15vh;
}

.custom-calendar {
  max-height: 760px;
  overflow: hidden;
}

.vc-grid-cell {
  border: 1px solid rgb(0, 0, 0, 0.2) !important;
}

.detail-title {
  color: #b0a2c8;
}

h4 {
  font-family: 'Do Hyeon', sans-serif;
}

.badge-participants {
  background-color: #f48fb1;
  color: white;
  font-size: 0.9rem;
}

.badge-drinks {
  background-color: #ffcc80;
  color: white;
  font-size: 0.9rem;
}

hr {
  margin-bottom: 5px;
  margin-top: 0;
}

button:focus {
  outline: none;
}

.h-50 {
  height: 50vh !important;
}

.h-75 {
  height: 75vh !important;
}
</style>
