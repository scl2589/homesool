<template>
  <div>
    <div class="p-3 container">
      <vc-calendar
        class="custom-calendar"
        :attributes='attributes'
        is-expanded
        :max-date="new Date()"
        locale="ko-kr"
      >
      <template v-slot:day-content="{ day, attributes }">
        <div class="flex flex-col h-full z-10 overflow-hidden">
          <span class="day-label text-sm text-gray-900">{{ day.day }}</span>
          <div class="flex-grow overflow-y-auto overflow-x-auto">
            <p
              v-for="attr in attributes"
              class="text-xs leading-tight rounded-sm p-1 mt-0 mb-1"
              :class="attr.customData.class"
              :key="attr.key"
            >
              {{ attr.customData.title }}
            </p>
          </div>
        </div>
      </template>
        <!-- <div
          slot="day-popover"
          slot-scope="{ day, dayTitle, attributes }">
          <div class="text-xs text-gray-300 font-semibold text-center">
            {{ dayTitle }}
          </div>
          <popover-row
            v-for="attr in attributes"
            :key="attr.id"
            :attribute="attr"
          >
            <div class="pointer" v-if="attr.customData.id" @click="clickDiary(attr.customData.id)">{{ attr.customData.title }}</div>
            <div v-else>{{ attr.customData.title }}</div>
          </popover-row>
        </div> -->
      </vc-calendar>
    </div>

  </div>  
</template>

<script>
// import PopoverRow from 'v-calendar/lib/components/popover-row.umd.min'
export default {
  name: 'Calendar',
  components: {
    // PopoverRow
  },
  data() {
    const month = new Date().getMonth();
    const year = new Date().getFullYear();
    return {
      attributes: [
        {
          key: 1,
          customData: {
            title: 'Lunch with mom.',
            class: 'red lighten-3 text-white',
          },
          dates: new Date(year, month, 1),
        },
        {
          key: 2,
          customData: {
            title: 'Take Noah to basketball practice',
            class: 'blue lighten-3 text-white',
          },
          dates: new Date(year, month, 2),
        },
        {
          key: 3,
          customData: {
            title: "Noah's basketball game.",
            class: 'blue lighten-3 text-white',
          },
          dates: new Date(year, month, 5),
        },
        {
          key: 4,
          customData: {
            title: 'Take car to the shop',
            class: 'indigo lighten-3 text-white',
          },
          dates: new Date(year, month, 5),
        },
        {
          key: 4,
          customData: {
            title: 'Meeting with new client.',
            class: 'teal lighten-3 text-white',
          },
          dates: new Date(year, month, 7),
        },
        {
          key: 5,
          customData: {
            title: "Mia's gymnastics practice.",
            class: 'pink lighten-3 text-white',
          },
          dates: new Date(year, month, 11),
        },
        {
          key: 6,
          customData: {
            title: 'Cookout with friends.',
            class: 'orange lighten-3 text-white',
          },
          dates: { months: 5, ordinalWeekdays: { 2: 1 } },
        },
        {
          key: 7,
          customData: {
            title: "Mia's gymnastics recital.",
            class: 'pink lighten-3 text-white',
          },
          dates: new Date(year, month, 22),
        },
        {
          key: 8,
          customData: {
            title: 'Visit great grandma.',
            class: 'red lighten-3 text-white',
          },
          dates: new Date(year, month, 25),
        },
      ],
    };
  },
  // computed: {
  //   attributes() {
  //     const meetingData = []
  //     for (var meeting of this.meetings) {
  //       let inputData = {
  //         dates: meeting.meeting_date,
  //         dot: {
  //           color: 'green',
  //           class: meeting.isComplete ? 'opacity-75' : '',
  //         },
  //         popover: {
  //           label: meeting.description,
  //           visibility: 'focus',
  //           placement: 'auto'
  //         },
  //         customData: {
  //           title: meeting.title,
  //           id: meeting.id
  //         },
  //       }
  //       meetingData.push(inputData)
  //     }
  //     return meetingData
  //   },
  // },
}
</script>

<style scoped>
/* p {
  color: black !important;
} */

.vc-pane-container {
  width: 50%;
  position: relative;
}
</style>

<style lang="postcss" scoped>
::-webkit-scrollbar {
  width: 0px;
}
::-webkit-scrollbar-track {
  display: none;
}
/deep/ .custom-calendar.vc-container {
  --day-border: 1px solid #b8c2cc;
  --day-border-highlight: 1px solid #b8c2cc;
  --day-width: 90px;
  --day-height: 90px;
  --weekday-bg: #f8fafc;
  --weekday-border: 1px solid #eaeaea;
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