let chart;

let base = [
    { value: 1048, name: 'Search Engine' },
    { value: 735, name: 'Direct' },
    { value: 580, name: 'Email' },
    { value: 484, name: 'Union Ads' },
    { value: 300, name: 'Video Ads' }
]

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialPieChart();
});

const drawPie = (data) => {
    const option = {
        title: {
            text: '원자재 현 보유량',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                type: 'pie',
                radius: '50%',
                data: data,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    chart.setOption(option);
}

function drawMaterialPieChart() {
    const chartDom = document.getElementById('material_total_Pie');
    chart = echarts.init(chartDom);

    drawPie(base)
}

function updateMaterialPieChart() {
    if (!chart) return;
    let newData = [...base];
    base.forEach(({name, value}, index) => {
        newData[(index + 1) % base.length] = {name: name, value: value}
    })
    base = newData
    console.log(base)

    drawPie(base)
}