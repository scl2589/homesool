import SERVER from '@/api/api';
import axios from 'axios';

const mypageStore = {
  namespaced: true,
  state: {
    recentData: null,
    alcoholTypeData: null
  },
  getters: {

  },
  mutations: {
    SET_RECENT_DATA(state, value) {
      state.recentData = value
    },
    SET_ALCOHOL_TYPE_DATA(state, value) {
      state.alcoholTypeData = value
    }
  },
  actions: {
    fetchAnalysisData({ commit, rootState, rootGetters }) {
      if (rootState.user) {
        axios.get(SERVER.URL + SERVER.ROUTES.user + `/${rootState.user.id}/statistics`, rootGetters.config)
          .then(res => {
            // recent
            const recentData = {
              labels: [],
              datasets: [
                {
                  xAxisID: 0,
                  yAxisID: 0,
                  label: '최근 음주 통계',
                  backgroundColor: '#58b04a',
                  data: []
                }
              ]
            }
            res.data.recordStatistics10days.forEach(day => {
              recentData.labels.push(day.date)
              let totalValue = 0;
              day.userRecord.forEach(record => {
                totalValue = totalValue + record.liquorLimit
              })
              recentData.datasets[0].data.push(totalValue);
            })
            commit('SET_RECENT_DATA', recentData)
  
            // alcoholType
            const alcoholTypeData = {
              hoverBackgroundColor: "red",
              hoverBorderWidth: 10,
              labels: [],
              datasets: [
                {
                  label: "주종 통계",
                  backgroundColor: [],
                  data: []
                }
              ]
            }

            let typeData = res.data.recordStatistics;
            let drinkColors = ['#CB997E', '#02323A', '#988148', '#8D9365', '#f194ff', '#BF5B04'];
            if (typeData.length > 5) {
              for (let i = 0; i < 5; i++) {
                if (typeData[i].liquorName.includes('소주')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#58b04a');
                } else if (typeData[i].liquorName.includes('맥주')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#e3bc3b');
                } else if (typeData[i].liquorName.includes('와인')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#940620');
                } else if (typeData[i].liquorName.includes('막걸리')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#e3e0cf');
                } else {
                  alcoholTypeData.datasets[0].backgroundColor.push(drinkColors[i]);
                }
                alcoholTypeData.labels.push(typeData[i].liquorName);
                alcoholTypeData.datasets[0].data.push(typeData[i].liquorLimit)
              }
              
              let etcCount = 0
              for (let i = 5; i < typeData.length; i++) {
                etcCount += typeData[i].liquorLimit
              }
              alcoholTypeData.labels.push('기타');
              alcoholTypeData.datasets[0].data.push(etcCount);
              alcoholTypeData.datasets[0].backgroundColor.push('##878787');
              commit('SET_ALCOHOL_TYPE_DATA', alcoholTypeData)
  
            } else {
              for (let j = 0; j < typeData.length - 1; j++) {
                if (typeData[j].liquorName.includes('소주')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#58b04a');
                } else if (typeData[j].liquorName.includes('맥주')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#e3bc3b');
                } else if (typeData[j].liquorName.includes('와인')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#940620');
                } else if (typeData[j].liquorName.includes('막걸리')) {
                  alcoholTypeData.datasets[0].backgroundColor.push('#e3e0cf');
                } else {
                  alcoholTypeData.datasets[0].backgroundColor.push(drinkColors[j]);
                }
                alcoholTypeData.labels.push(typeData[j].liquorName)
                alcoholTypeData.datasets[0].data.push(typeData[j].liquorLimit)
              }
              commit('SET_ALCOHOL_TYPE_DATA', alcoholTypeData)
            }
          })
          .catch(err => {
            console.log(err.response.data)
          })
      }
    }
  }
}

export default mypageStore