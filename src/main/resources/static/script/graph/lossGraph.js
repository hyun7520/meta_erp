const colors = ['#1ABC9C', '#3498DB', '#F39C12', '#9B59B6', '#34495E'];
let lossChart;

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialLossChart();
});

function convertToSeries(list) {
    const groups = {};
    const xLabels = new Set();

    for (const item of list) {
        if (!groups[item.caseName]) groups[item.caseName] = [];
        groups[item.caseName].push(item);
        xLabels.add(item.date);
    }

    const sortedX = [...xLabels].sort((a, b) => (a > b ? 1 : -1));
    const series = [];

    for (const caseName in groups) {
        const items = groups[caseName];

        // 🔥 문자열 날짜 정렬
        items.sort((a, b) => (a.date > b.date ? 1 : -1));

        const inputType = items[0].type;
        const seriesType = inputType === "humidity" ? "line" : "bar";
        const yAxisIndex = inputType === "humidity" ? 0 : 1;
        const layer = inputType === "humidity"
            ? { zlevel: 1, z: 3 }
            : { zlevel: 0, z: 1 };

        series.push({
            name: caseName,
            type: seriesType,
            yAxisIndex,
            tooltip: {
                valueFormatter: (value) => value + "%"
            },
            data: items.map(v => v.percent),
            markArea: {
                silent: true,
                itemStyle: { color: 'rgba(255, 173, 177, 0.4)' },
                data: [
                    [
                        {
                            name: '습도 집중 관리 구간',
                            yAxis: 30,
                            label: { position: 'top', color: '#C0392B', fontWeight: 'bold', fontSize: 13 }
                        },
                        { yAxis: 50 }
                    ]
                ]
            },
            ...layer
        });
    }

    return { series, xLabels: sortedX };
}

const drawChart = (series, xLabels) => {
    const option = {
        title: {
            text: '최근 1년간 습도 및 로스율',
            left: 'center',
            top: 10
        },
        tooltip: {
            trigger: "axis"
        },
        legend: {},
        grid: { top: 70, left: 50, right: 50, bottom: 110 },
        xAxis: {
            type: "category",
            data: xLabels,
            axisLabel: {
                margin: 20
            }
        },
        yAxis: [
            {
                type: 'value',
                name: '예측 로스율 (%)',
                position: 'left',
                axisLine: { show: true, lineStyle: { color: '#333' } },
                axisLabel: { formatter: '{value}%' }
            },
            {
                type: 'value',
                name: '예상 습도 (%)',
                position: 'right',
                interval: 10,
                axisLine: { show: true, lineStyle: { color: colors[4] } },
                axisLabel: { formatter: '{value}%' }
            }
        ],
        dataZoom: [
            {
                type: 'slider',
                xAxisIndex: 'all',
                left: '10%',
                right: '10%',
                bottom: 55,
                height: 20,
                throttle: 120
            },
            {
                type: 'inside',
                xAxisIndex: 'all',
                throttle: 120
            }
        ],
        series: series
    };

    lossChart.setOption(option);
}

const drawMaterialLossChart = () => {
    const mainDom = document.getElementById("material_loss_Graph");

    if (lossChart) lossChart.dispose();

    lossChart = echarts.init(mainDom);

    fetch('/dash/loss', {method: 'GET'})
        .then(response => response.json())
        .then(data => {

            const {series, xLabels} = convertToSeries(data);

            drawChart(series, xLabels);
            const now = new Date();
            document.getElementById("material_loss_Graph_time").innerText = calcDrawDate(now);
        });
}
