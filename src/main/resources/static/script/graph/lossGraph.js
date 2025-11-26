let lossChart;

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialLossChart();
});

function convertToSeries(list) {
    // case 별 그룹화
    const groups = {};
    const xLabels = new Set();
    for (const item of list) {
        if (!groups[item.caseName]) groups[item.caseName] = [];
        groups[item.caseName].push(item);
        xLabels.add(item.date);
    }

    const series = [];

    for (const caseName in groups) {
        const items = groups[caseName];

        items.sort((a, b) => a.date - b.date);

        const inputType = items[0].type;
        const seriesType = inputType === "humidity" ? "line" : "bar";

        const yAxisIndex = inputType === "humidity" ? 0 : 1;

        const layer = inputType === "humidity"
            ? { zlevel: 1, z: 3 }   // line → 위쪽
            : { zlevel: 0, z: 1 };  // bar → 아래쪽

        series.push({
            name: caseName,
            type: seriesType,
            yAxisIndex,
            tooltip: {
                formater: (value) => value + "%"
            },
            data: items.map(v => v.percent),
            ...layer
        });
    }

    return {series: series, xLabels: [...xLabels]};
}

const drawChart = (data) => {
    const {series, xLabels} = convertToSeries(data);

    const option = {
        title: {
            text: '최근 5년간 습도 및 로스율'
        },
        tooltip: {
            trigger: "axis"
        },
        legend: {
            formatter: (label) => label
        },
        xAxis: {
            type: "category",
            data: xLabels
        },
        yAxis: [
            { type: "value", name: "습도(%)" },
            { type: "value", name: "로스율(%)" }
        ],
        series
    };


    lossChart.setOption(option);
}

const drawMaterialLossChart = () => {
    const chartDom = document.getElementById('material_loss_Graph');
    lossChart = echarts.init(chartDom);

    fetch('/dash/loss', {method: 'GET'})
        .then(response => response.json())
        .then(data => {
            drawChart(data);

            const now = new Date();
            const drawTime = document.getElementById("material_loss_Graph_time")
            drawTime.innerText = calcDrawDate(now);
        });
}
