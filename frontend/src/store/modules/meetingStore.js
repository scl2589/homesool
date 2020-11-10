// import router from "../../router";
import SERVER from '@/api/api';
import secrets from '@/secrets';
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import moment from 'moment';

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
  },
  getters: {
    notModeHost(state) {
      if (state.publisher.stream.connection.connectionId !== state.modeHost.id) {
        return state.modeHost;
      } else {
        return false;
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
    }
  },
  actions: {
    changeMode({ state, getters }, mode) {
      // 진행 중인 노래가 있거나, 게임이 있거나, 스냅샷이 있거나 할 경우 여기서 막아줌
      if (state.selectedSong || state.selectedGame || (state.currentMode === 'snapshot')) {
        if (getters.notModeHost) {
          alert('지금은 다른 모드로 전환할 수 없습니다.');
          return
        } else {
          if (!confirm('현재 모드를 중단하시겠습니까?')) {
            return
          }
        }
      } else {
        // 만약 현재 모드가 켜져있는 상태에서 특정 모드로 전환하고자 할 시 한 번 확인
        if (mode && state.currentMode && mode !== state.currentMode) {
          if (!confirm('현재 모드를 중단하시겠습니까?')) {
            return
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
    enterSession({ state, rootGetters, commit }, enterData) {
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

            state.session.signal({
              type: 'firstenter',
              data: null,
              to: [],
            })

            state.session.on('signal:firstenter', (event) => {
              if (state.publisher.stream.connection.connectionId !== event.from.connectionId) {
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
                  to: [event.from],
                })
              }
            })

            state.session.on('signal:status', (event) => {
              let status = JSON.parse(event.data);
              commit('SET_THEME', status.theme);
              commit('SET_CURRENT_MODE', status.currentMode);
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
              }
            })

            state.session.on('signal:mode', (event) => {
              let mode = event.data
              
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
                alert('진실의 방 모드가 켜졌습니다!');
                commit('SET_CURRENT_MODE', mode);
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
              console.log(event.type)
              console.log(event.penaltyId)
              console.log(event.data)
              if(event.data.gameStatus == 1){
                //게임 시작
                commit('SET_SELECTED_GAME', event.data.gameId);
                commit('SET_GAME_STATUS', event.data.gameStatus);
              }

              commit('SET_GAME_STATUS',event.data.gameStatus);

              if(event.data.turn >= 0){
                commit('SET_GAME_TURN',event.data.turn);
              }
              if(event.data.word){
                commit('SET_GAME_WORD',event.data.word);
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
              }, 700);
            })
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
    sendGameRequest({ state }, request){
      state.session.signal({
        data: request,
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
    }
  }
}

export default meetingStore