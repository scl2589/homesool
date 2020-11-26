<template>
  <v-dialog
    v-model="meetingLogDialog"
    persistent
    max-width="600px"
    v-if="calendarDetail"
    >
    <v-card 
      class="d-flex flex-column justify-content-between scroll-sect"
      :class="{ 'h-75': (calendarDetail.roomInfo && calendarDetail.srcs.length >= 1), 'h-50': (calendarDetail.roomInfo && calendarDetail.srcs.length === 0)}"
    >
      <v-card-title class="d-flex justify-content-center">
        <h4>
          <span v-if="calendarDetail.roomInfo">
            {{`${calendarDetail.roomInfo.startTime.slice(0, 4)}년 ${calendarDetail.roomInfo.startTime.slice(5, 7)}월 ${calendarDetail.roomInfo.startTime.slice(8, 10)}일`}}
          </span>
          <span class="detail-title">술자리 기록</span> 
        </h4>
        <hr>
      </v-card-title>
      
      <v-container class="d-flex flex-column align-items-start px-5 py-0">
        <v-row
          class="d-flex mb-3 no-gutters"
          v-if="calendarDetail.roomInfo"
        >
          <h4 class="mb-0">{{calendarDetail.roomInfo.roomName}}</h4>
        </v-row>
        <v-row
          class="d-flex mb-3 mx-2 no-gutters"
          v-if="calendarDetail.host"
        >
          <h5 class="mb-0">호스트:</h5>
          <p class="mb-0 ml-3">{{calendarDetail.host}}</p>
        </v-row>
        <v-row
          class="d-flex mb-3 mx-2 no-gutters"
          v-if="calendarDetail.users && calendarDetail.users.length"
        >
          <h5 class="mb-0">
            참여자
            <img
              width="30px"
              src="@/assets/images/friends.png"
              alt="participants"
            >
          </h5>
          <p
            class="mb-0 ml-2"
            v-for="user in calendarDetail.users" :key="user"
          >
            <span class="badge badge-participants">{{user}}</span>
          </p>
        </v-row>
        <v-row class="d-flex mb-3 mx-2 no-gutters">
          <h5 class="mb-0 mr-2">
            음주량
            <img
              width="30px"
              src="@/assets/images/beer.png"
              alt="shot"
            >
          </h5>
          <span v-for="(record,index) in calendarDetail.records" :key="index">
            <p 
              class="mb-0 mr-2" 
              v-if="record.liquorLimit > 0"
            >
              <span class="badge badge-drinks">{{record.liquorName}} {{record.liquorLimit}}잔</span>
            </p>
          </span>            
        </v-row>
        <v-row
          class="mb-3 mx-2"
          v-if="calendarDetail.roomInfo && calendarDetail.srcs.length"
        >
          <h5 class="mb-0">사진</h5>
          <v-carousel
            v-model="model"
            dark
            cycle
            hide-delimiter-background
            height="250"
          >
            <v-carousel-item
              v-for="(src,index) in calendarDetail.srcs"
              :key="index"
            >
              <img
                class="h-100 mw-100"
                :src="`https://firebasestorage.googleapis.com/v0/b/homesuli.appspot.com/o/${calendarDetail.roomInfo.code}%2Fsnapshot%2F${src}.jpg?alt=media&token=942e1b59-2774-4d79-b0e7-098d76168b49`"
              >
            </v-carousel-item>
          </v-carousel>
        </v-row>
      </v-container>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          color="indigo"
          text
          @click="closeCalendarDetail"
        >
          Close
        </v-btn>
      </v-card-actions>
      
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'CalendarDetail',
  data() {
    return {
      model: 0
    };
  },
  computed: {
    ...mapState('mypageStore', [
      'meetingLogDialog',
      'calendarDetail'
    ])
  },
  methods: {
    ...mapActions('mypageStore', ['closeCalendarDetail'])
  }
}
</script>

<style scoped>
  .scroll-sect {
    max-height: 70vh !important;
    overflow: auto;
  }

  .scroll-sect::-webkit-scrollbar {
    width: 8px; 
    height: 8px;
  }

  .scroll-sect::-webkit-scrollbar-track {
    background: #37474F;
  }

  .scroll-sect::-webkit-scrollbar-corner {
    background: #37474F; 
  }

  .scroll-sect::-webkit-scrollbar-thumb {
    background: #b0a2c8;
  }

  .scroll-sect::-webkit-scrollbar-button {
    background-color: #37474F;
    height: 0;
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