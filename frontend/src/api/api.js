export default {
  URL: 'https://k3a503.p.ssafy.io:8889',
  //URL: 'http://localhost:8888',
  YOUTUBE_URL: "https://www.googleapis.com/youtube/v3/search",
  OPENVIDU_URL: "https://k3a503.p.ssafy.io",
  //OPENVIDU_URL: "https://192.168.219.108:8443",
  ROUTES: {
    user: '/user',
    login: '/user/login',
    room: '/room',
    rooms: '/room/list/',
    roomCount: '/room/list/count',
    searchName: '/room/list/name/',
    searchTag: '/room/list/tag/',
    photo: '/room/photo',
    voice: '/v1/recongnize'
  }
}