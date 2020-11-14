import { Bar } from "vue-chartjs";

export default {
  extends: Bar,
  props: {
    data: Object
  },
  mounted() {
    this.renderChart(this.data, 
      {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true,
              fontColor: "#FEFEFE"
            },
            gridLines: {
              display: true
            }
          }],
          xAxes: [{
            ticks: {
              beginAtZero: true,
              fontColor: "#FEFEFE"
            },
            gridLines: {
              display: false
            }
          }]
        },
        legend: {
          display: false,
        },
        tooltips: {
          enabled: true,
          mode: 'single',
          callbacks: {
            label: function(tooltipItems) {
              return tooltipItems.yLabel;
            }
          }
        },
        responsive: true,
        maintainAspectRatio: true,
      })
  }
};