<template>
  <div>
    <div>
      <vc-calendar
        class="custom-calendar max-w-full"
        :attributes='attributes'
        is-expanded
        disable-page-swipe
        :max-date="new Date()"
        locale="ko-kr"
      >
      <template v-slot:day-content="{ day, attributes }">
        <div class="flex flex-col h-full z-10 overflow-hidden">
          <span class="day-label text-sm text-gray-900">{{ day.day }}</span>
          <div class="flex-grow overflow-y-auto overflow-x-auto custom-overflow" >
            <div
              v-for="attr in attributes"
              class="text-xs leading-tight rounded-sm p-1 mt-0 mb-1"
              :class="attr.customData.class"
              :key="attr.key"
            >
              <p class="mb-0" @click="showDetail(attr.key)">{{ attr.customData.title }}</p>
            </div>
          </div>
        </div>
      </template>
      </vc-calendar>
    </div>
    <v-dialog
        v-model="meetingLogDialog"
        persistent
        max-width="600px"
      >
      <v-card>
        <v-card-title class="d-flex justify-content-center">
          <h3>미팅 기록 조회</h3>
        </v-card-title>
        
        <v-container class="d-flex flex-column align-items-start px-5 py-0">
          <v-row class="d-flex mb-3 mx-2" v-if="logs.roomInfo">
            <h4 class="mb-0">날짜:</h4>
            <p class="mb-0 ml-3">{{`${logs.roomInfo.startTime.slice(0, 4)}년 ${logs.roomInfo.startTime.slice(5, 7)}월 ${logs.roomInfo.startTime.slice(8, 10)}일`}}</p>
          </v-row>
          <v-row class="d-flex mb-3 mx-2" v-if="logs.roomInfo">
            <h4 class="mb-0">방제목:</h4>
            <p class="mb-0 ml-3">{{logs.roomInfo.roomName}}</p>
          </v-row>
          <v-row class="d-flex mb-3 mx-2">
            <h4 class="mb-0">호스트:</h4>
            <p class="mb-0 ml-3">{{logs.host}}</p>
          </v-row>
          <v-row class="d-flex mb-3 mx-2" v-if="logs.users && logs.users.length">
            <h4 class="mb-0">참여자:</h4>
            <p class="mb-0 ml-3" v-for="user in logs.users" :key="user">
              {{user}},
            </p>
          </v-row>
          <v-row class="d-flex mb-3 mx-2">
            <h4 class="mb-0">음주량:</h4>
            <span v-for="(record,index) in logs.records" :key="index">
              <p class="mb-0 ml-3" v-if="record.liquorLimit > 0">{{record.liquorName}} {{record.liquorLimit}}잔,</p>
            </span>            
          </v-row>
          <v-row class="mb-3 mx-2" v-if="logs.roomInfo && logs.srcs.length">
            <h4 class="mb-0">사진</h4>
            <v-carousel
              v-model="model"
              dark
              cycle
              height="250"
              hide-delimiter-background
            >
              <v-carousel-item
                class=""
                v-for="(src,index) in logs.srcs"
                :key="index"
              >
                <img class="h-100 mw-100" :src="`https://firebasestorage.googleapis.com/v0/b/homesuli.appspot.com/o/${logs.roomInfo.code}%2Fsnapshot%2F${src}.jpg?alt=media&token=942e1b59-2774-4d79-b0e7-098d76168b49`">
              </v-carousel-item>
            </v-carousel>
          </v-row>
        </v-container>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-1"
            text
            @click="clickClose"
          >
            Close
          </v-btn>
        </v-card-actions>
        
      </v-card>
    </v-dialog>
  </div>  
</template>

<script>
import SERVER from '@/api/api'
import axios from 'axios'
import { mapActions, mapGetters,mapState } from 'vuex';
export default {
  name: 'Calendar',
  components: {
  },
  data() {
    return {
      attributes: [
      ],
      logs : {},
      color : ["red","blue","indigo","teal","pink","orange"],
      model: 0
    };
  },
  computed: {
    ...mapGetters(['getId']),
    ...mapState('meetingStore',['meetingLogDialog'])
  },
  methods: {
    ...mapActions('meetingStore',['changeMeetingLogDialog']),
    showDetail(roomId){
      this.changeMeetingLogDialog(true);
      axios.get(SERVER.URL + SERVER.ROUTES.user + '/' + this.getId + '/room/' + roomId,
       {
        headers: {'X-AUTH-TOKEN' : this.$store.state.token}
      })
        .then((event) => {
          this.logs = event.data
          console.log(event.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },
    clickClose(){
      this.changeMeetingLogDialog(false);
      this.logs={};
    },
  },
  created() {
      axios.get(SERVER.URL + SERVER.ROUTES.user + '/' + this.getId + '/rooms', 
      {
        headers: {'X-AUTH-TOKEN' : this.$store.state.token}
      })
        .then((event) => {
          for(var i=0 ;i<event.data.length;i++){
            let log = new Object()
            log.key = event.data[i].roomId
            log.customData = {
              title: event.data[i].roomName,
              class: this.color[Math.floor(Math.random()*6)]+' lighten-3 text-white',
            }
            let year = event.data[i].startTime.slice(0,4)
            let month = event.data[i].startTime.slice(5,7)
            let day = event.data[i].startTime.slice(8,10)
            log.dates = new Date(year,month-1,day)
            this.attributes.push(log)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
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

/* .vc-grid-container > .vc-grid-cell {
  border: 1px solid black;
} */

.vc-grid-cell {
  border: 1px solid rgb(0, 0, 0, 0.2) !important;
}
</style>
