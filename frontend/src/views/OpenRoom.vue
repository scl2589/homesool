<template>
  <div>
    <div class="search-bar">
      <input 
        v-model="search"
        type="text" 
        placeholder="" 
        required
        @keyup.enter="enterSearch(search)"
      >
      <div class="search-icon"></div>
    </div>

    <div 
      class="d-flex justify-content-between align-items-center row no-gutters cards p-5"
      v-if="flag"
    >
      <div 
        class="card2 mb-3 rounded mx-2" 
        v-for="(room, i) in rooms" 
        :key="i"
      >
        <div 
          class="card-top mt-5"
          @click="clickRoom(room.roominfo.code)"
        >
          <p class="text-right text-muted">주최자 {{ room.host }}</p>
          <hr>
          <img width="50px"
          :src="require(`@/assets/images/${anonyMousImg(i)}.png`)"
          >
        </div>
        <div class="d-flex justify-content-start align-items-start flex-column pl-4">
          <small class="text-muted" v-if="room.users">{{ room.numberOfElements }}명 입장 중</small>
          <p class="strong">{{ room.roominfo.roomName }}</p>
        </div>
        <div class="pl-4 pb-2 d-flex justify-content-start">
          <small class="text-muted">
            <span 
              v-for="tag in room.roominfo.tags" 
              :key="tag.tagId"
              >
              #{{ tag.tagName }}
            </span>
          </small>
        </div>
      </div>
    </div>
    <div 
      class="d-flex justify-content-between align-items-center row no-gutters cards p-5"
      v-if="!flag"
    >
      <div 
        class="card2 mb-3 rounded mx-2" 
        v-for="(room, i) in searchedRooms" 
        :key="i"
      >
        <div 
          class="card-top mt-5"
          @click="clickRoom(room.roominfo.code)"
        >
          <p class="text-right text-muted">주최자 {{ room.host }}</p>
          <hr>
          <img width="50px"
          :src="require(`@/assets/images/${anonyMousImg(i)}.png`)"
          >
        </div>
        <div class="d-flex justify-content-start align-items-start flex-column pl-4">
          <small class="text-muted" >{{ room.numberOfElements }}명 입장 중</small>
          <p class="strong">{{ room.roominfo.roomName }}</p>
        </div>
        <div class="pl-4 pb-2 d-flex justify-content-start">
          <small class="text-muted">
            <span 
              v-for="tag in room.roominfo.tags" 
              :key="tag.tagId"
              >
              #{{ tag.tagName }}
            </span>
          </small>
        </div>
      </div>
    </div>
    <div class="d-flex justify-content-center">
      <div 
        @click="clickPage(num)"
        v-for="num in roomCount"
        :key="num"
      >
        <span class="mr-3 color-white pointer">{{ num }}</span>
      </div>
    </div>

  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  name: 'OpenRoom',
  data() {
    return {
      anonymousImages: [
        '001-unicorn',
        '002-magic-hat',
        '003-wizard',
        '004-magic-hat-1',
        '005-illusionist',
        '006-moon',
        '007-magician',
        '008-witch',
        '009-fortune-teller',
				'010-cat',
				'011-fortune-teller-1',
				'012-magician-1',
				'013-magician-2',
				'014-witch-1',
				'015-elf',
				'016-gnome',
				'017-knight',
				'018-fairy',
				'019-witch-2',
				'020-little-red-riding-hood',
				'021-wolf',
				'022-giant',
				'023-dwarf',
				'024-wizard-1',
				'025-yeti',
				'026-king',
				'027-wizard-2',
				'028-dwarf-1',
				'029-little-red-riding-hood-1',
				'030-witch-3',
				'031-king-1',
				'032-viking',
				'033-queen',
				'034-santa-claus',
				'035-witch-4',
				'036-vampire',
				'037-witch-5',
				'038-owl',
				'039-santa-claus-1',
				'040-pirate',
				'041-nurse',
				'042-queen-1',
				'043-clown',
				'044-cowboy',
				'045-jack-o-lantern',
				'046-clown-1',
				'047-scarecrow',
				'048-knight-1',
				'049-mummy',
				'050-ninja'
      ],
      search: null,
      pageNum: 1,
      flag: true,
      // live: new Array()
    }
  },
  computed: {
    ...mapState('openroomStore', ['rooms', 'roomCount', 'searchedRooms', 'liveMembers']),
  },
  methods: {
    ...mapActions('openroomStore', ['fetchRooms', 'findRoomCount', 'searchRoom', 'findLiveMembers']),
    anonyMousImg(index) {
      return this.anonymousImages[index % 50]
    },
    clickRoom(code) {
      this.$router.push({ name: 'MeetingPage', params: { sessionId: code } })
    },
    clickPage(num) {
      this.pageNum = num
      if (this.search !== "") {
        this.fetchRooms(num)
      }
    },
    enterSearch(search) {
      if (search === null || search === "") {
        this.flag = true
        this.fetchRooms(
          this.pageNum)
      } else {
        let data = new Object()
        data.search = search 
        data.pageNum = this.pageNum
        this.searchRoom(data)
        this.flag = false
        // this.live = []
      }
    }
  },
  created() {
    this.fetchRooms(1)
    this.findRoomCount()
  }
}
</script>

<style scoped>
p, h3{
  color: white;
  margin: 0 10px 0 0;
}

.cards {
  margin-top: 3vh;
  margin-left: 3vw;
  margin-right: 3vw;
  min-height: 80vh;
}

hr {
  background-color: #979797;
  margin: 5px 0;
}

.card-top {
  position: relative;
  height: 10vh;
}

img {
  position: absolute;
  left: 20px;
  right: 0;
  top: 0;
  bottom: 0;
  background-color: white;
  border-radius: 50%;
  border: 1px solid white;
}

.card2 {
  border: 1px solid white;
  width: 20vw;
  min-height: 23vh;
}

.card2:hover {
  background-color: rgb(1, 1, 1, 0.3)
}

.pointer {
  cursor: pointer;
}
</style>

<style scoped lang="scss">
$size: 20px;

.search-bar {
  height: auto;
  width: auto;
  position: absolute;
  display: inline-block;
  top: 10vh;
  left: 50%;
  transform: translate(-50%, -50%);
  box-sizing: border-box;
  
  input {
    height: 44px;
    width: 44px;
    padding: 10px 20px;
    box-sizing: border-box;
    font-size: 18px;
    border: $size/10 solid transparent;
    border-radius: 44px;
    cursor: pointer;
    background-color: transparent;
    transition: all 0.5s ease-out;
    
    color: transparent;
    &::placeholder {
      color: transparent;
    }
    
    &:invalid {
      box-shadow: none;
    }
    
    &:hover {
      border: $size/10 solid #FFF;
      box-shadow: 0 0 $size/10 $size/10 #EEE;
    }
    
    &:focus, &:valid {
      width: 300px;
      border: $size/10 solid #CCC;
      outline: none;
      cursor: auto;
      background-color: #FFF;
      color: #000;
      
      &::placeholder {
        color: #999;
      }
      
      + .search-icon {
        z-index: 0;
        border-color: #CCC;
        right: 20px;
        
        &:after {
          background-color: #CCC;
        }
      }
    }
  }
}



.search-icon {
  display: inline-block;
  height: $size;
  width: $size;
  border-radius: 50%;
  border: $size/10 solid #FFF;
  position: absolute;
  right: 12px;
  top: 8px;
  z-index: -1;
  
  &:after {
    content: '';
    position: absolute;
    top: $size/2 + $size/4*1.414 + $size/10;
    left: $size/2 + $size/4*1.414;
    transform: rotate(45deg);
    height: $size/10;
    width: $size/2;
    background-color: #FFF;
    border-radius: 10px;
  }
}
</style>