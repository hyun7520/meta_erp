/*
* 각 html 파일 CDN 설정
* <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
* */
let lineChart;

document.addEventListener("DOMContentLoaded", () => {
    drawDemandLineChart();
});

function convertToSeries(list) {
    const groups = {};
    const labelSet = new Set();

    for (const item of list) {
        if (!groups[item.name]) groups[item.name] = [];

        const date = item.requestDate.replace("-", ".");
        item.requestDate = date;

        groups[item.name].push(item);
        labelSet.add(date);
    }

    const xLabels = [...labelSet].sort().concat(months);
    const xIndex = {};
    xLabels.forEach((x, idx) => xIndex[x] = idx);

    const series = [];
    const now = new Date();
    const startYear = now.getFullYear();
    const endYear = startYear + 1;

    for (const name in groups) {
        const items = groups[name];

        items.sort((a, b) => (a.requestDate > b.requestDate ? 1 : -1));

        const aligned = new Array(xLabels.length).fill(0);

        items.forEach(v => {
            const idx = xIndex[v.requestDate];
            if (idx !== undefined) aligned[idx] = v.demand;
        });

        const futureData = echarts_data[name] || [];
        const finalData = aligned.concat(futureData);

        series.push({
            name,
            type: "line",
            tooltip: { valueFormatter: v => v + "건" },
            data: finalData,
            markArea: {
                silent: true,
                itemStyle: { color: 'rgba(255, 173, 177, 0.4)' },
                data: [
                    [
                        {
                            name: '미래 2년 예측 수요량',
                            xAxis: `${startYear}.${now.getMonth() + 1}`,
                            label: { position: 'top', color: '#C0392B', fontWeight: 'bold', fontSize: 13 }
                        },
                        { xAxis: `${endYear}.${now.getMonth() + 1}` }
                    ]
                ]
            }
        });
    }

    return { series, xLabels };
}

const drawLine = (data) => {
    const {series, xLabels} = convertToSeries(data);

    const option = {
        animation: true,
        animationDuration: 1200,
        title: {
            text: "최근 5년간 제품 수요량 그래프",
            left: "center",
            top: 10,
        },
        tooltip: {
            trigger: "axis",
            confine: true,
        },
        legend: {
            top: 50,
            data: Object.keys(echarts_data),
        },
        xAxis: {
            type: "category",
            data: xLabels,
            axisLabel: { rotate: 45 },
        },
        yAxis: {
            type: "value",
            name: "수요량 (건)",
            axisLabel: { formatter: val => val.toLocaleString() + "건" }
        },
        dataZoom: [
            { type: "slider", xAxisIndex: 0, bottom: 35, height: 18 },
            { type: "inside", xAxisIndex: 0 }
        ],
        grid: {
            top: 100,
            left: 50,
            right: 60,
            bottom: 90,
        },
        series
    };

    lineChart.setOption(option);
};

const drawDemandLineChart = () => {
    const chartDom = document.getElementById('product_demand_Line');
    lineChart = echarts.init(chartDom);

    fetch(`/dash/demands`, {method: 'GET'})
        .then(response => response.json())
        .then(data => {
            drawLine(data);

            const now = new Date();
            const drawTime = document.getElementById("product_demand_Line_time")
            drawTime.innerText = calcDrawDate(now);
        });

}

// sample data
const months = [
    '2025.11', '2025.12', '2026.01', '2026.02', '2026.03', '2026.04',
    '2026.05', '2026.06', '2026.07', '2026.08', '2026.09', '2026.10',
    '2026.11', '2026.12', '2027.01', '2027.02', '2027.03', '2027.04',
    '2027.05', '2027.06', '2027.07', '2027.08', '2027.09', '2027.10',
    '2027.11', '2027.12'
];

const echarts_data = {
    '김치찌개': [1200, 1350, 1400, 1300, 1150, 1100, 1050, 1000, 1080, 1150, 1250, 1450, 1500, 1600, 1650, 1550, 1400, 1300, 1250, 1200, 1300, 1380, 1500, 1700, 1750, 1850],
    '된장찌개': [1000, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1300, 1250, 1200, 1150, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1400, 1350, 1300, 1250, 1200, 1150],
    '부대찌개': [1100, 1150, 1200, 1250, 1200, 1150, 1100, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1400, 1350, 1300, 1250, 1300, 1350, 1400, 1450, 1500, 1550],
    '닭볶음탕': [950, 980, 1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1500, 1550, 1600, 1650, 1700, 1750, 1800, 1750, 1700, 1650, 1600, 1550, 1500, 1450, 1400]
};
