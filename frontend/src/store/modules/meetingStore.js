// import router from "../../router";
import SERVER from '@/api/api';
import secrets from '@/secrets';
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import moment from 'moment';
import Swal from 'sweetalert2'

const OPENVIDU_SERVER_SECRET = "MY_SECRET";

const meetingStore = {
  namespaced: true,
  state: {
    // pre meeting 
    meetingDialog: false,
    currentDrink: null,
    nickName: null,
    mySessionId: null,
    roomId: null,
    myself: null,

    // openvidu
    OV: undefined,
    ovToken: null,
    session: undefined,
    mainStreamManager: undefined,
    publisher: undefined,
    subscribers: [],
    
    // mode
    currentMode: null,
    modeHost: null,

    //chatting
    messages: [],
    isChatPanel: false,

    // singing
    songs: null,
    selectedSong: null,
    currentSongTime: null,
    isSongEnded: false,

    // game
    selectedGame: null,
    gameStatus: 0,
    gameTurn: 0,
    gameWord: '',
    gameLiar:'',
    gamePlayer: '',
    loser: null,
    penaltyId: null,

    gameLiarData:'',
    gameVoteId:'',
    gameVoteData:'',  //걸린사람 이름
    gameParticipantId:'', //벌칙자
    gameParticipantData:'', //벌칙자이름
    gamePaneltyId:0,
    gamePaneltyOV:undefined,
    gamePaneltySubscriber: [],
    gamePaneltySession: undefined,
    gamePaneltyStreamManager: undefined,
    gamePaneltyPublisher: undefined,

    gameInitialWord:'',
    gameIsCorrect: 1,
    participantPublicId:'',

    gameUpDownResult:'',
    gameUpDownIndex:0,
    gameUpDownNumber:-1,

    // theme
    theme: 'basic',

    // screen share
    screenOV: undefined,
    screenSession: undefined,
    screenMainStreamManager: undefined,
    screenPublisher: undefined,
    screenSubscribers: [],
    screenOvToken: null,
    isSharingMode: false,

    //capture
    screenshotInfo: null,

    //game
    sentence: null
  },
  getters: {
    notModeHost(state) {
      if (state.modeHost) {
        if (state.publisher.stream.connection.connectionId !== state.modeHost.id) {
          return state.modeHost;
        } else {
          return false;
        }
      } else {
        return true;
      }
    },
    notGamePlayer(state) {
      if (state.gamePlayer) {
        if (state.gamePlayer !== state.publisher.stream.connection.connectionId) {
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    }
  },
  mutations: {
    // pre meeting
    SET_MEETING_DIALOG(state, value) {
      state.meetingDialog = value;
    },
    SET_CURRENT_DRINK(state, drinkId) {
      state.currentDrink = drinkId;
    },
    SET_NICKNAME(state, nickName) {
      state.nickName = nickName;
    },
    SET_MYSELF(state, subscriber) {
      state.myself = subscriber;
    },
    SET_MYSESSIONID(state, sessionId) {
      state.mySessionId = sessionId;
    },
    SET_ROOMID(state, roomId) {
      state.roomId = roomId;
    },

    // Openvidu
    SET_OV(state, OV) {
      state.OV = OV;
    },
    SET_SESSION(state, session) {
      state.session = session;
    },
    SET_MAINSTREAMMANAGER(state, mainStreamManager) {
      state.mainStreamManager = mainStreamManager;
    },
    SET_PUBLISHER(state, publisher) {
      state.publisher = publisher;
    },
    SET_SUBSCRIBERS(state, subscribers) {
      state.subscribers = subscribers;
    },
    SET_OVTOKEN(state, token) {
      state.ovToken = token;
    },

    // mode
    SET_CURRENT_MODE(state, mode) {
      state.currentMode = mode
    },
    SET_MODE_HOST(state, host) {
      state.modeHost = host
    },

    // chatting
    SET_IS_CHATPANEL(state, value) {
      state.isChatPanel = value;
    },
    SET_MESSAGES(state, data) {
      state.messages.push(data);
    },
    SET_CLEARMESSAGES(state) {
      state.messages = [];
    },

    // singing
    SET_SELECTED_SONG(state, song) {
      state.selectedSong = song;
    },
    SET_SONGS(state, songs) {
      state.songs = songs;
    },
    SET_CURRENT_SONGTIME(state, currentSongTime) {
      state.currentSongTime = currentSongTime
    },
    SET_IS_SONG_ENDED(state, value) {
      state.isSongEnded = value
    },

    // game
    SET_SELECTED_GAME(state, value) {
      state.selectedGame = value
    },
    SET_GAME_STATUS(state, value){
      state.gameStatus = value
    },
    SET_GAME_TURN(state, value){
      state.gameTurn = value
    },
    SET_GAME_WORD(state, value){
      state.gameWord = value
    },
    SET_GAME_LIAR(state, value){
      state.gameLiar = value
    },
    SET_GAME_PLAYER(state, value) {
      state.gamePlayer = value
    },
    SET_LOSER(state, value) {
      state.loser = value
    },
    SET_PENALTY_ID(state, value) {
      state.penaltyId = value
    },
    SET_GAME_LIAR_DATA(state, value){
      state.gameLiarData = value
    },
    SET_GAME_VOTE_ID(state, value){
      state.gameVoteId = value
    },
    SET_GAME_PARTICIPANT_ID(state, value){
      state.gameParticipantId = value
    },
    SET_GAME_PARTICIPANT_DATA(state, value){
      state.gameParticipantData = value
    },
    SET_GAME_VOTE_DATA(state, value){
      state.gameVoteData = value
    },
    SET_GAME_PANELTY_ID(state,value){
      state.gamePaneltyId = value
    },
    SET_GAME_PANELTY_SUBSCRIBER(state, subscriber){
      state.gamePaneltySubscriber = subscriber
    },
    SET_GAME_PANELTY_OV(state,gamePaneltyOV){
      state.gamePaneltyOV = gamePaneltyOV
    },
    SET_GAME_PANELTY_SESSION(state, gamePaneltySession){
      state. gamePaneltySession =  gamePaneltySession
    },
    SET_GAME_PANELTY_STREAM_MANAGER(state,gamePaneltyStreamManager){
      state.gamePaneltyStreamManager = gamePaneltyStreamManager
    },
    SET_GAME_PANELTY_PUBLISHER(state,gamePaneltyPublisher){
      state.gamePaneltyPublisher = gamePaneltyPublisher
    },
    

    SET_GAME_INITIALWORD(state, value){
      state.gameInitialWord = value
    },
    SET_GAME_ISCORRECT(state, value){
      state.gameIsCorrect = value
    },
    SET_GAME_PARTICIPANTPUBLICID(state, value){
      state.participantPublicId = value
    },
    RESET_GAME_ISCORRECT(state){
      state.gameIsCorrect = 1
    },
    SET_GAME_UPDOWN_RESULT(state,value){
      state.gameUpDownResult = value
    },
    SET_GAME_UPDOWN_INDEX(state,value){
      state.gameUpDownIndex = value
    },
    SET_GAME_UPDOWN_NUMBER(state,value){
      state.gameUpDownNumber = value
    },
    
    // theme
    SET_THEME(state, theme) {
      state.theme = theme;
    },

    // screen share
    SET_SCREEN_OV(state, OV) {
      state.screenOV = OV;
    },
    SET_SCREEN_SESSION(state, session) {
      state.screenSession = session;
    },
    SET_SCREEN_MAINSTREAMMANAGER(state, mainStreamManager) {
      state.screenMainStreamManager = mainStreamManager;
    },
    SET_SCREEN_PUBLISHER(state, publisher) {
      state.screenPublisher = publisher;
    },
    SET_SCREEN_SUBSCRIBERS(state, subscribers) {
      state.screenSubscribers = subscribers;
    },
    SET_SCREEN_OVTOKEN(state, token) {
      state.screenOvToken = token;
    },
    SET_IS_SHARING_MODE(state, value) {
      state.isSharingMode = value;
    },

    //screenshot
    SET_SCREENSHOT_INFO(state, data) {
      state.screenshotInfo = data;
    },

    //game
    SET_SENTENCE(state, data) {
      state.sentence = data;
    }
  },
  actions: {
    changeMode({ state, getters }, mode) {
      if (getters.notModeHost) {
        // modeHost가 아닌 경우
        if (state.currentMode && state.modeHost) {          
          // 현재 진행 중인 mode와 modeHost가 있는 경우
          if (state.selectedSong || state.selectedGame || state.currentMode === 'snapshot') {
            // 현재 멈추면 안되는 상황인 경우
            alert('지금은 다른 모드로 전환할 수 없습니다.');
            return;
          } else {
            // 현재 모드를 중단해도 되는 경우
            if (state.currentMode !== mode) {
              if (!confirm('현재 모드를 중단하시겠습니까?')) {
                return;
              }
            }
          }
        } else {
          if (state.modeHost) {
            // 현재 currentMode는 없지만 modeHost가 null 값이 아닌 경우(실제 snapshot 모드가 진행 중인 경우)
            alert('지금은 다른 모드로 전환할 수 없습니다.');
            return;
          } else {
            // modeHost가 중간에 나가버린 경우
            if (state.currentMode && state.currentMode !== mode) {
              if (!confirm('현재 모드를 중단하시겠습니까?')) {
                return;
              }
            }
          }
        }
      } else {
        // modeHost인 경우
        if (state.currentMode && state.currentMode !== mode) {
          if (!confirm('현재 모드를 중단하시겠습니까?')) {
            return;
          }
        }
      }

      state.session.signal({
        type: 'mode',
        data: mode,
        to: [],
      })
        .then(() => {
          console.log(`init ${mode} mode`);
        })
        .catch((err) => {
          console.log(err)
        })
    },
    endAnonymousMode({ state }) {
      state.publisher.stream.removeFilter("GStreamerFilter");
    },
    endSingingMode({ state, commit }) {
      if (state.selectedSong) {
        state.publisher.stream.removeFilter("GStreamerFilter");
        commit('SET_SELECTED_SONG', null);
        commit('SET_CURRENT_SONGTIME', null);
      }
      commit('SET_SONGS', null);
      commit('SET_IS_SONG_ENDED', false);
    },
    endGameProcess({ commit }) {
      commit('SET_SELECTED_GAME', null);
      commit('SET_GAME_STATUS', 0);
      commit('SET_GAME_TURN', 0);
      commit('SET_GAME_WORD', '');

      commit('SET_GAME_ISCORRECT',1);
    },
    endGameSignal({ state }) {
      state.session.signal({
        type: 'endGame',
        to: [],
      })
    },
    endSnapshotMode() {
      // 스냅샷 모드가 꺼졌을 경우 후처리해야할 부분
    },
    toggleChatPanel({ state, commit }) {
      commit('SET_IS_CHATPANEL', !state.isChatPanel);
    },
    searchSong({ commit }, keyword) {
      axios.get(SERVER.YOUTUBE_URL, {
        params: {
          key: secrets.YOUTUBE.SECRET_KEY[Math.floor(Math.random() * secrets.YOUTUBE.SECRET_KEY.length)],
          part: 'snippet',
          type: 'video',
          q: '[KY 금영노래방]' + keyword,
          maxResults: 4
        }
      })
        .then(res => {
          res.data.items.forEach(item => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(item.snippet.title, 'text/html');
            item.snippet.title = doc.body.innerText;
          });
          commit('SET_SONGS', res.data.items);
        });
    },
    selectSong({ state }, song) {
      state.session.signal({
        type: 'song',
        data: JSON.stringify(song),
        to: [],
      })
        .then(() => {
          console.log("song started");
        })
        .catch((err) => {
          console.log(err)
        })
    },
    checkSongSync({ state }, currentSongTime) {
      state.session.signal({
        type: 'songsync',
        data: currentSongTime + 0.05,
        to: [],
      })
        .then(() => {
          console.log("songsync");
        })
        .catch((err) => {
          console.log(err)
        })
    },
    changeTheme({ state }, theme) {
      state.session.signal({
        type: 'theme',
        data: theme,
        to: [],
      })
        .then(() => {
          console.log("theme changed");
        })
        .catch((err) => {
          console.log(err)
        })
    },
    changeMeetingDialog({ commit }, value) {
      commit('SET_MEETING_DIALOG', value);
    },
    createSessionId({ rootGetters, commit, dispatch }) {
      const ct = new Date();
      const createData = {
        "hostId": rootGetters.getId,
        "startTime": moment(ct).format('YYYY-MM-DDTHH:mm:ss')
      };
      axios.post(SERVER.URL + SERVER.ROUTES.room, createData, rootGetters.config)
        .then(res => {
          commit('SET_ROOMID', res.data.roomId);
          dispatch('joinSession', res.data.code);
        })
        .catch(err => {
          console.log(err.response.data)
        })
    },
    checkSessionId({ rootGetters, commit, dispatch }, sessionId) {
      axios.post(`${SERVER.URL + SERVER.ROUTES.room}/${sessionId}/with/${rootGetters.getId}`, null, rootGetters.config)
        .then(res => {
          commit('SET_ROOMID', res.data);
          dispatch('joinSession', sessionId);
          return true;
        })
        .catch(err => {
          console.log(err.response.data)
          alert('초대코드가 유효하지 않습니다.')
        })
    },

    // openvidu
    joinSession ({ commit, dispatch }, mySessionId) {
      commit('SET_MYSESSIONID', mySessionId);
			// --- Get an OpenVidu object ---
			const OV = new OpenVidu();
			// --- Init a session ---
			const session = OV.initSession();
			// --- Specify the actions when events take place in the session ---
			// On every new Stream received...
      const subscribers = [];
			session.on('streamCreated', ({ stream }) => {
        const subscriber = session.subscribe(stream);
        commit('SET_MYSELF', subscriber)
				subscribers.push(subscriber);
			});
			// On every Stream destroyed...
			session.on('streamDestroyed', ({ stream }) => {
				const index = subscribers.indexOf(stream.streamManager, 0);
				if (index >= 0) {
					subscribers.splice(index, 1);
				}
			});
			// --- Connect to the session with a valid user token ---
			// 'getToken' method is simulating what your server-side should do.
			// 'token' parameter should be retrieved and returned by your own backend
			dispatch('getToken', mySessionId).then(token => {
        let publisher = OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
          publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
          resolution: '640x480',  // The resolution of your video
          frameRate: 30,			// The frame rate of your video
          insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
          mirror: true,       	// Whether to mirror your local video or not
        });
        commit('SET_OV', OV);
        commit('SET_MAINSTREAMMANAGER', publisher);
        commit('SET_PUBLISHER', publisher);
        commit('SET_SESSION', session);
        commit('SET_SUBSCRIBERS', subscribers);
        commit('SET_OVTOKEN', token);
			});
		},
		leaveSession ({ state, commit }) {
			// --- Leave the session by calling 'disconnect' method over the Session object ---
			if (state.session) state.session.disconnect();
      commit('SET_OV', undefined);
      commit('SET_SESSION', undefined);
      commit('SET_SUBSCRIBERS', []);
      commit('SET_MAINSTREAMMANAGER', undefined);
      commit('SET_PUBLISHER', undefined);
      commit('SET_MYSESSIONID', null);
      commit('SET_CLEARMESSAGES');
      commit('SET_OVTOKEN', null);
      if (state.screenSession) state.screenSession.disconnect();
      commit('SET_SCREEN_OV', undefined);
      commit('SET_SCREEN_SESSION', undefined);
      commit('SET_SCREEN_SUBSCRIBERS', []);
      commit('SET_SCREEN_MAINSTREAMMANAGER', undefined);
      commit('SET_SCREEN_PUBLISHER', undefined);
      commit('SET_SCREEN_OVTOKEN', null);
		},
		updateMainVideoStreamManager ({ state, commit }, stream) {
			if (state.mainStreamManager === stream) return;
      commit('SET_MAINSTREAMMANAGER', stream);
		},
		/**
		 * --------------------------
		 * SERVER-SIDE RESPONSIBILITY
		 * --------------------------
		 * These methods retrieve the mandatory user token from OpenVidu Server.
		 * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
		 * the API REST, openvidu-java-client or openvidu-node-client):
		 *   1) Initialize a session in OpenVidu Server	(POST /api/sessions)
		 *   2) Generate a token in OpenVidu Server		(POST /api/tokens)
		 *   3) The token must be consumed in Session.connect() method
		 */
		getToken ({ dispatch }, mySessionId) {
			return dispatch('createSession', mySessionId).then(sessionId => dispatch('createToken', sessionId));
		},
		// See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-apisessions
		createSession ({ state }, sessionId) {
      console.log(state.mySessionId)
			return new Promise((resolve, reject) => {
				axios
					.post(`${SERVER.OPENVIDU_URL}/openvidu/api/sessions`, JSON.stringify({
            customSessionId: sessionId,
					}), {
            headers: {
              'Content-Type': 'application/json'
            },
						auth: {
							username: 'OPENVIDUAPP',
							password: OPENVIDU_SERVER_SECRET,
						},
					})
					.then(response => response.data)
					.then(data => resolve(data.id))
					.catch(error => {
						if (error.response.status === 409) {
							resolve(sessionId);
						} else {
							console.warn(`No connection to OpenVidu Server. This may be a certificate error at ${SERVER.OPENVIDU_URL}`);
							if (window.confirm(`No connection to OpenVidu Server. This may be a certificate error at ${SERVER.OPENVIDU_URL}\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${SERVER.OPENVIDU_URL}"`)) {
								location.assign(`${SERVER.OPENVIDU_URL}/accept-certificate`);
							}
							reject(error.response);
						}
					});
			});
		},
		// See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-apitokens
		createToken ({ state }, sessionId) {
      console.log(state.mySessionId)
			return new Promise((resolve, reject) => {
				axios
					.post(`${SERVER.OPENVIDU_URL}/api/tokens`, JSON.stringify({
            "session": sessionId,
            "kurentoOptions": {
              "allowedFilters": ["GStreamerFilter", "FaceOverlayFilter"]
            }
					}), {
            headers: {
              'Content-Type': 'application/json'
            },
						auth: {
							username: 'OPENVIDUAPP',
							password: OPENVIDU_SERVER_SECRET,
						},
					})
					.then(response => response.data)
					.then(data => resolve(data.token))
					.catch(error => reject(error.response));
			});
    },
    clickMuteVideo({ state }) {
      if (state.publisher.stream.videoActive) {
        state.publisher.publishVideo(false)
      } else {
        state.publisher.publishVideo(true) 
      }
    },
    clickMuteAudio({ state }) {
      if (state.publisher.stream.audioActive) {
        state.publisher.publishAudio(false)
      } else {
        state.publisher.publishAudio(true) 
      }
    },
    enterSession({ state, rootGetters, commit, dispatch }, enterData) {
      commit('SET_CURRENT_DRINK', enterData.currentDrink);
      const drinkData = {
        "liquorLimit": 0,
        "liquorName": enterData.currentDrink,
        "recordId": 0
      }
      axios.put(`${SERVER.URL + SERVER.ROUTES.user}/${rootGetters.getId}/record/${state.roomId}`, drinkData, rootGetters.config)
        .then(() => {
          state.session.connect(state.ovToken, { clientData: enterData.nickName })
					.then(() => {
            commit('SET_NICKNAME', enterData.nickName);
            state.session.publish(state.publisher);

            state.session.on('streamCreated', (event) => {
              let status = {
                theme: state.theme,
                currentMode: state.currentMode,
                modeHost: state.modeHost,
                selectedSong: state.selectedSong,
                selectedGame: state.selectedGame,
                gameStatus: state.gameStatus
              }
              state.session.signal({
                type: 'status',
                data: JSON.stringify(status),
                to: [event.stream.connection.connectionId],
              })
            })

            state.session.on('signal:status', (event) => {
              let status = JSON.parse(event.data);
              if (!state.currentMode && !state.modeHost) {
                commit('SET_THEME', status.theme);
                commit('SET_MODE_HOST', status.modeHost);
                commit('SET_SELECTED_SONG', status.selectedSong);
                commit('SET_SELECTED_GAME', status.selectedGame);
                commit('SET_GAME_STATUS', status.gameStatus);
                if (status.currentMode === 'anonymous') {
                  setTimeout(() => {
                    let pitchs = ['0.76', '0.77', '0.78', '0.79', '0.80', '1.3', '1.4', '1.5', '1.6', '1.7']
                    let pitch = pitchs[Math.floor(Math.random() * pitchs.length)]
                    state.publisher.stream.applyFilter("GStreamerFilter", {"command": `pitch pitch=${pitch}`});
                  }, 1000);
                } else if (status.currentMode === 'snapshot') {
                  return;
                }
                commit('SET_CURRENT_MODE', status.currentMode);
              }
            })

            state.session.on('signal:mode', (event) => {
              let mode = event.data
              
              if (mode === 'hostleave') {
                commit('SET_MODE_HOST', null);
                return;
              }

              if (mode) {
                let modeHost = {
                  'id': event.from.connectionId,
                  'name': event.from.data.slice(15,-2)
                }
                commit('SET_MODE_HOST', modeHost);
              } else {
                commit('SET_MODE_HOST', null);
              }

              if (mode === 'anonymous') {
                let pitchs = ['0.76', '0.77', '0.78', '0.79', '0.80', '1.3', '1.4', '1.5', '1.6', '1.7']
                let pitch = pitchs[Math.floor(Math.random() * pitchs.length)]
                state.publisher.stream.applyFilter("GStreamerFilter", {"command": `pitch pitch=${pitch}`});
                commit('SET_CURRENT_MODE', mode);
                Swal.fire({
                  icon: 'success',
                  text: '진실의 방 모드가 켜졌습니다!'
                });
              } else if (mode === 'singing') {
                commit('SET_IS_SONG_ENDED', false);
                commit('SET_CURRENT_MODE', mode);
              } else if (mode === 'snapshot') {
                if (state.currentMode === 'snapshot') {
                  commit('SET_CURRENT_MODE', null);
                  setTimeout(() => {
                    commit('SET_CURRENT_MODE', mode);
                  }, 100);
                } else {
                  commit('SET_CURRENT_MODE', mode);
                }
              } else {
                commit('SET_CURRENT_MODE', mode);
              }

            });

            state.session.on('signal:chat', (event) => {
              let data = new Object()
              let time = new Date()
              data.message = event.data
              if (state.currentMode === 'anonymous') {
                const animals = [
                  '코끼리', '사자', '하마', '표범', '가젤',
                  '개미핥기', '치타', '기린', '얼룩말', '코뿔소',
                  '호랑이', '늑대', '판다', '코알라', '다람쥐',
                  '곰', '사슴', '원숭이', '너구리', '침팬지',
                  '미어캣', '낙타', '목도리도마뱀', '타조', '사막여우',
                  '전갈', '순록', '북극곰', '흰올빼미', '팽귄',
                  '북극여우', '바다코끼리', '돌고래', '가오리', '나비고기',
                  '상어', '문어', '오징어', '바다거북', '흰동가리',
                  '고래', '불가사리', '해마', '게', '독수리',
                  '갈매기', '큰부리새', '원앙', '부엉이', '홍학',
                  '두루미', '비둘기', '벌새', '사다새', '공작',
                  '참새', '고양이', '개', '푸들나방', '별코두더지'
                ]
                data.sender = animals[event.from.connectionId.slice(-10, ).charCodeAt(0) % 60];
              } else {
                data.sender = event.from.data.slice(15,-2)
              }
              data.time = moment(time).format('HH:mm')
              commit('SET_MESSAGES', data)
            });
            state.session.on('signal:endGame',(event) =>{
              console.log(event)
              // endGameProcess 를 어떻게 부르죠??,,,
              commit('SET_SELECTED_GAME', null);
              commit('SET_GAME_STATUS', 0);
              commit('SET_GAME_TURN', 0);
              commit('SET_GAME_WORD', '');
              commit('SET_GAME_ISCORRECT',1);
            });
            state.session.on('signal:theme', (event) => {
              commit('SET_THEME', event.data)
            });

            state.session.on('signal:songsync', (event) => {
              commit('SET_CURRENT_SONGTIME', event.data);
            });

            state.session.on('signal:song', (event) => {
              const song = JSON.parse(event.data);
              if (song) {
                state.publisher.stream.applyFilter("GStreamerFilter", {"command": "audioecho delay=75000000 intensity=0.3 feedback=0.4"});
              } else {
                commit('SET_CURRENT_SONGTIME', null);
                commit('SET_IS_SONG_ENDED', true);
                state.publisher.stream.removeFilter("GStreamerFilter");
              }
              commit('SET_SELECTED_SONG', song);
              commit('SET_SONGS', null);
            });
            
            state.session.on('signal:game', (event) => {
              if(event.data.gameStatus == 1) {
                //게임 시작
                commit('SET_SELECTED_GAME', event.data.gameId);
                commit('SET_GAME_STATUS', event.data.gameStatus);
                commit('SET_PENALTY_ID', event.data.penaltyId)
              }

              if(event.data.gameStatus == 3) {
                commit('SET_GAME_STATUS', event.data.gameStatus);
                if (state.publisher.stream.connection.connectionId === event.from.connectionId) {
                  commit('SET_LOSER', state.publisher);
                } else {
                  state.subscribers.forEach(subscriber => {
                    if (subscriber.stream.connection.connectionId === event.from.connectionId) {
                      commit('SET_LOSER', subscriber);
                      // break;
                    }
                  });
                }
              }

              commit('SET_GAME_STATUS', event.data.gameStatus);

              //초기화
              if(event.data.gameStatus == 0){
                commit('SET_SELECTED_GAME', null);
                commit('SET_GAME_STATUS', 0);
                commit('SET_GAME_TURN', 0);
                commit('SET_GAME_WORD', '');
              }

              if(event.data.gameStatus==3 && event.data.gameId != 4){
                setTimeout(() => {
                  commit('SET_GAME_STATUS', 4);
                }, 5000);
              }
              
              if(event.data.paneltyId){
                commit('SET_GAME_PANELTY_ID',event.data.paneltyId);
              }

              if(event.data.turn >= 0) {
                commit('SET_GAME_TURN', event.data.turn);
              }
              if(event.data.word) {
                commit('SET_GAME_WORD', event.data.word);
              }
              if(event.data.liar) {
                commit('SET_GAME_LIAR', event.data.liar);
              }
              if(event.data.player) {
                commit('SET_GAME_PLAYER', event.data.player);
              }
              if(event.data.liarId){
                commit('SET_GAME_LIAR',event.data.liarId);
                //commit('SET_GAME_LIAR',event.data.liar);
                //라이어의 닉네임
                for(let i=0; i<state.subscribers.length; i++){
                  if(state.subscribers[i].stream.connection.connectionId == event.data.liarId){
                    commit('SET_GAME_LIAR_DATA',state.subscribers[i].stream.connection.data.slice(15,-2));
                  }
                }
                if(state.publisher.session.connection.connectionId == event.data.liarId){ //본인체크
                  commit('SET_GAME_LIAR_DATA',state.publisher.session.connection.data.slice(15,-2));
                }
              }
              if(event.data.voteId){
                commit('SET_GAME_VOTE_ID',event.data.voteId);
                //당선자??의 닉네임도 찾아서 넣어줘야함
                for(let i=0; i<state.subscribers.length; i++){
                  if(state.subscribers[i].stream.connection.connectionId == event.data.voteId){
                    commit('SET_GAME_VOTE_DATA',state.subscribers[i].stream.connection.data.slice(15,-2));
                  }
                }
                if(state.publisher.session.connection.connectionId == event.data.voteId){ //본인체크
                  commit('SET_GAME_VOTE_DATA',state.publisher.session.connection.data.slice(15,-2));
                }
              }
              if(event.data.participantPublicId){
                commit('SET_GAME_PARTICIPANT_ID',event.data.participantPublicId);
                //벌칙자의 닉네임도 찾아서 넣어줘야함
                for(let i=0; i<state.subscribers.length; i++){
                  if(state.subscribers[i].stream.connection.connectionId == event.data.participantPublicId){
                    commit('SET_GAME_PARTICIPANT_DATA',state.subscribers[i].stream.connection.data.slice(15,-2));
                    //벌칙자 stream 생성
                    //this.setPaneltyScreen();
                  }
                }
                if(state.publisher.session.connection.connectionId == event.data.participantPublicId){ //본인체크
                  commit('SET_GAME_PARTICIPANT_DATA',state.publisher.session.connection.data.slice(15,-2));
                }
              }
              if(event.data.initialWord){
                commit('SET_GAME_INITIALWORD',event.data.initialWord);
              }
              if(event.data.isCorrect){
                console.log("-----iscorrect------")
                
                if(event.from.connectionId == state.publisher.stream.connection.connectionId){
                  console.log(event.from.connectionId)
                  console.log(state.publisher.stream.connection.connectionId)  
                  commit('SET_GAME_ISCORRECT',event.data.isCorrect);
                }
              }
              if(event.data.participantPublicId){
                commit('SET_GAME_PARTICIPANTPUBLICID',event.data.participantPublicId)
              }
              if(event.data.updown){
                commit('SET_GAME_UPDOWN_RESULT',event.data.updown)
              }
              if(event.data.index >= 0){
                commit('SET_GAME_UPDOWN_INDEX',event.data.index)
              }
              if(event.data.number >= 0){
                commit('SET_GAME_UPDOWN_NUMBER',event.data.number)
              }

              if (event.data.sentence) {
                commit('SET_SENTENCE', event.data.sentence)
                dispatch('recordVoice')
              }
            });

            state.session.on('signal:share', (event) => {
              console.log("EVENT.DATA", event.data)
              if ( event.data === "F") {
                commit('SET_IS_SHARING_MODE', false)
              } else {
                commit('SET_IS_SHARING_MODE', true)
              }
            });

            state.session.on('signal:attachImage', (event) => {
              setTimeout(() => {
                var image = document.createElement('img')  
                image.src = "https://firebasestorage.googleapis.com/v0/b/homesuli.appspot.com/o/snapshot_" + state.mySessionId + "%2F" + event.data + ".jpg?alt=media&token=942e1b59-2774-4d79-b0e7-098d76168b49"
                image.style.maxWidth="90%"
                document.getElementById('preview').appendChild(image)
              }, 1500);
            });

            state.session.on('streamDestroyed', (event) => {
              if (state.modeHost) {
                if (state.modeHost.id === event.stream.connection.connectionId) {
                  commit('SET_MODE_HOST', null);
                }
              }
            });

            return true;
					})
					.catch(error => {
            console.log('There was an error connecting to the session:', error.code, error.message);
            alert('오류가 발생했습니다. 입장 정보를 다시 한 번 확인해주세요.');
					});
        })
        .catch(err => {
          console.log(err.response.data)
          alert('오류가 발생했습니다. 입장 정보를 다시 한 번 확인해주세요.');
        })
    },
    sendMessage({ state }, message) {
      state.session.signal({
        type: 'chat',
        data: message,
        to: [],
      })
        .then(() => {
          console.log("Message successfully sent");
        })
        .catch((err) => {
          console.log(err)
        })
    },
    startShareScreen({ state, commit, dispatch }) {
      if (state.isSharingMode) {
        return
      } 
      // --- Get an OpenVidu object ---
			const screenOV = new OpenVidu();
			// --- Init a session ---
			const screenSession = screenOV.initSession();
			// --- Specify the actions when events take place in the session ---
			// On every new Stream received...
      const screenSubscribers = [];
			screenSession.on('streamCreated', ({ stream }) => {
        const subscriber2 = screenSession.subscribe(stream);
				screenSubscribers.push(subscriber2);
			});
			// On every Stream destroyed...
			screenSession.on('streamDestroyed', ({ stream }) => {
				const index2 = screenSubscribers.indexOf(stream.streamManager, 0);
				if (index2 >= 0) {
					screenSubscribers.splice(index2, 1);
				}
			});
      dispatch('getToken', state.mySessionId).then(token2 => {
        let screenPublisher = screenOV.initPublisher(undefined, {
          audioSource: false, // The source of audio. If undefined default microphone
          videoSource: 'screen', // The source of video. If undefined default webcam
          publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
          publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
          resolution: '1920x1080',  // The resolution of your video
          frameRate: 30,			// The frame rate of your video
          insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
          mirror: false,       	// Whether to mirror your local video or not
        });
        screenSession.connect(token2, { clientData: state.nickName + 'screen' })
        .then(() => {
          screenPublisher.once('accessAllowed', () => {
            screenPublisher.stream.getMediaStream().getVideoTracks()[0].addEventListener('ended', () => {
              dispatch('stopShareScreen');
            });
            screenSession.publish(screenPublisher);
            commit('SET_SCREEN_OV', screenOV);
            commit('SET_SCREEN_MAINSTREAMMANAGER', screenPublisher);
            commit('SET_SCREEN_PUBLISHER', screenPublisher);
            commit('SET_SCREEN_SESSION', screenSession);
            commit('SET_SCREEN_SUBSCRIBERS', screenSubscribers);
            commit('SET_SCREEN_OVTOKEN', token2);
            state.session.signal({
              data: 'T',
              to: [],
              type: 'share' 
            })
          });
          screenPublisher.once('accessDenied', () => {
            console.warn('ScreenShare: Access Denied');
          });
        })
        .catch(error => {
          console.log('There was an error connecting to the session:', error.code, error.message);
        });
			});
    },
    stopShareScreen({ state, commit }) {
      if (state.screenSession) state.screenSession.disconnect();
      commit('SET_SCREEN_OV', undefined);
      commit('SET_SCREEN_SESSION', undefined);
      commit('SET_SCREEN_SUBSCRIBERS', []);
      commit('SET_SCREEN_MAINSTREAMMANAGER', undefined);
      commit('SET_SCREEN_PUBLISHER', undefined);
      commit('SET_SCREEN_OVTOKEN', null);
      state.session.signal({
        data: 'F',
        to: [],
        type: 'share' 
      })
    },
    sendGameRequest({ state }, data){
      state.session.signal({
        data: data,
        to: [],
        type: 'game'
      })
        .then(() => {
          console.log("Message successfully sent");
        })
        .catch((err) => {
          console.log(err)
      })
    },
    attachImage({ state }, file) {
      state.session.signal({
        data: file,
        to: [],
        type: 'attachImage'
      })
    },
    setPaneltyScreen({ state, commit, dispatch }){
      if (state.isSharingMode) {
        return
      } 
      // --- Get an OpenVidu object ---
			const PaneltyOV = new OpenVidu();
			// --- Init a session ---
			const paneltySession = PaneltyOV.initSession();
			// --- Specify the actions when events take place in the session ---
			// On every new Stream received...
      const paneltySubscribers = [];
			paneltySession.on('streamCreated', ({ stream }) => {
        const subscriber2 = paneltySession.subscribe(stream);
				paneltySubscribers.push(subscriber2);
			});
			// On every Stream destroyed...
			paneltySession.on('streamDestroyed', ({ stream }) => {
				const index2 = paneltySubscribers.indexOf(stream.streamManager, 0);
				if (index2 >= 0) {
					paneltySubscribers.splice(index2, 1);
				}
			});
        dispatch('getToken', state.mySessionId).then(token => {
          let paneltyPublisher = PaneltyOV.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
            publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
            resolution: '640x480',  // The resolution of your video
            frameRate: 30,			// The frame rate of your video
            insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
            mirror: true,       	// Whether to mirror your local video or not
          });
        paneltySession.publish(paneltyPublisher);
        commit('SET_GAME_PANELTY_OV', PaneltyOV);
        commit('SET_GAME_PANELTY_STREAM_MANAGER', paneltyPublisher);
        commit('SET_GAME_PANELTY_PUBLISHER', paneltyPublisher);
        commit('SET_GAME_PANELTY_SESSION', paneltySession);
        commit('SET_GAME_PANELTY_SUBSCRIBER', paneltySubscribers);
        commit('SET_OVTOKEN', token);
      });
    },
    saveScreenshotInfo({ commit }, data) {
      commit('SET_SCREENSHOT_INFO', data)
    },
    saveScreenshot({ state, rootGetters }) {
      axios.post(SERVER.URL + SERVER.ROUTES.photo, state.screenshotInfo, rootGetters.config)
        .then(() => {
          console.log("SUCCESSFUL - uploading screenshot")
        })
        .catch((err) => {
          console.log(err)
        })
    },
    checkIsSmile({ state }) {
      console.log(state.selectedGame);
      let myVideo = document.getElementById('myVideo').childNodes[0].childNodes[1];
      let canvas = document.createElement("CANVAS");
      let ctx = canvas.getContext('2d');
      canvas.width = myVideo.videoWidth;
      canvas.height = myVideo.videoHeight;

      ctx.fillRect(0, 0, myVideo.videoWidth, myVideo.videoHeight);
      ctx.drawImage(myVideo , 0, 0, myVideo.videoWidth, myVideo.videoHeight);
      
      let converting = canvas.toDataURL("image/jpeg");
      let arr = converting.split(',');
      let mime = arr[0].match(/:(.*?);/)[1];
      let bstr = atob(arr[1]);
      let n = bstr.length;
      let u8arr = new Uint8Array(n);
      while(n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      let file = new File([u8arr], 'temp', {type:mime});
      let frm = new FormData()
      frm.append('files', file);

      axios.post(
        'https://k3a503.p.ssafy.io:5000/emotion',
        frm,
        {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(res => {
          console.log(res);
          if (res.data === 'smile') {
            let request = {
              gameStatus: 3
            }
            state.session.signal({
              data: JSON.stringify(request),
              to: [],
              type: 'game'
            })
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    recordVoice({state}) {
      console.log(state)
      const sdk = require("microsoft-cognitiveservices-speech-sdk");
      const speechConfig = sdk.SpeechConfig.fromSubscription("9bd552b2504c45e1802217ac626d6508", "koreacentral");
      speechConfig.speechRecognitionLanguage = "ko-KR";
      function fromMic() {
          let audioConfig = sdk.AudioConfig.fromDefaultMicrophoneInput();
          let recognizer = new sdk.SpeechRecognizer(speechConfig, audioConfig);
          
          console.log('Speak into your microphone.');
          recognizer.recognizeOnceAsync(result => {
              console.log(`RECOGNIZED: Text=${result.text}`);
          });
      }
      fromMic();
    }
  }
}

export default meetingStore