import router from "../../router"
import SERVER from '@/api/api'
import secrets from '@/secrets'
import axios from 'axios'
import { OpenVidu } from 'openvidu-browser';

const OPENVIDU_SERVER_SECRET = "MY_SECRET";

const meetingStore = {
  namespaced: true,
  state: {
    isGameMode: false,
    isSingingMode: false,
    isAnonymousMode: false,
    isSnapshotMode: false,
    isChatPanel: false,
    selectedSong: null,
    songs: null,
    theme: 'basic',

    // openvidu
    OV: undefined,
    session: undefined,
    mainStreamManager: undefined,
    publisher: undefined,
    subscribers: [],
    myUserName: 'Participant' + Math.floor(Math.random() * 100),
    mySessionId: null,
  },
  getters: {
  },
  mutations: {
    SET_ISGAME_MODE(state, value) {
      state.isGameMode = value
    },
    SET_ISSINGING_MODE(state, value) {
      state.isSingingMode = value
    },
    SET_ISANONYMOUS_MODE(state, value) {
      state.isAnonymousMode = value
    },
    SET_ISSNAPSHOT_MODE(state, value) {
      state.isSnapshotMode = value
    },
    SET_CHATPANEL(state, value) {
      state.isChatPanel = value
    },
    SET_SELECTED_SONG(state, song) {
      state.selectedSong = song
    },
    SET_SONGS(state, songs) {
      state.songs = songs
    },
    SET_THEME(state, theme) {
      state.theme = theme
    },
    SET_MYSESSIONID(state, sessionId) {
      state.mySessionId = sessionId
    },
    SET_OV(state, OV) {
      state.OV = OV
    },
    SET_SESSION(state, session) {
      state.session = session
    },
    SET_MAINSTREAMMANAGER(state, mainStreamManager) {
      state.mainStreamManager = mainStreamManager
    },
    SET_PUBLISHER(state, publisher) {
      state.publisher = publisher
    },
    SET_SUBSCRIBERS(state, subscribers) {
      state.subscribers = subscribers
    }
  },
  actions: {
    startGameMode({ commit }) {
      commit('SET_ISANONYMOUS_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISGAME_MODE', true)
    },
    startSingingMode({ commit }) {
      commit('SET_ISANONYMOUS_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSINGING_MODE', true)
    },
    startAnonymousMode({ commit }) {
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISANONYMOUS_MODE', true)
    },
    startSnapshotMode({ commit }) {
      commit('SET_ISSNAPSHOT_MODE', true)
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISANONYMOUS_MODE', false)
      
    },
    closeMultiPanel({ commit }) {
      commit('SET_ISGAME_MODE', false)
      commit('SET_ISSINGING_MODE', false)
      commit('SET_ISANONYMOUS_MODE', false)
      commit('SET_ISSNAPSHOT_MODE', false)
      router.push({ name: 'MeetingPage'})
    },
    clickChatPanel({ commit }, value) {
      commit('SET_CHATPANEL', value)
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
            const parser = new DOMParser()
            const doc = parser.parseFromString(item.snippet.title, 'text/html')
            item.snippet.title = doc.body.innerText
          })
          commit('SET_SONGS', res.data.items)
        })
    },
    selectSong({ commit }, song) {
      commit('SET_SELECTED_SONG', song)
      console.log(document.getElementById('test'))
    },
    closeSingingPanel({ commit }) {
      commit('SET_SONGS', null)
      commit('SET_SELECTED_SONG', null)
    },
    changeTheme({ commit }, theme) {
      commit('SET_THEME', theme)
    },

    createSessionId({ commit }) {
      // axios 요청 보내서 sessionId 가져오기
      commit('SET_MYSESSIONID', 'test')
    },

    // openvidu
    joinSession ({ state, commit, dispatch }, mySessionId) {
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
				session.connect(token, { clientData: state.myUserName })
					.then(() => {
						// --- Get your own camera stream with the desired properties ---
						let publisher = OV.initPublisher(undefined, {
							audioSource: undefined, // The source of audio. If undefined default microphone
							videoSource: undefined, // The source of video. If undefined default webcam
							publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
							publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
							resolution: '640x480',  // The resolution of your video
							frameRate: 30,			// The frame rate of your video
							insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
							mirror: true       	// Whether to mirror your local video or not
						});
						// --- Publish your stream ---
            session.publish(publisher);
            commit('SET_OV', OV);
            commit('SET_MAINSTREAMMANAGER', publisher);
						commit('SET_PUBLISHER', publisher);
            commit('SET_SESSION', session);
            commit('SET_SUBSCRIBERS', subscribers);
            router.push({ name: 'MeetingPage', params: { sessionId: mySessionId }});
					})
					.catch(error => {
						console.log('There was an error connecting to the session:', error.code, error.message);
					});
			});
			// window.addEventListener('beforeunload', dispatch('leaveSession'))
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
			// window.removeEventListener('beforeunload', dispatch('leaveSession'));
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
						session: sessionId,
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
  }

}

export default meetingStore