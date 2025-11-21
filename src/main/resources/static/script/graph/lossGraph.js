let lossChart;

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialLossChart();
});

const weatherFA = {
    Sunny: {
        unicode: "\uf185",
        fontFamily: "Font Awesome 6 Free",
        fontWeight: 400,
        color: "#f5c542"
    },
    Cloudy: {
        unicode: "\uf0c2",
        fontFamily: "Font Awesome 6 Free",
        fontWeight: 900,
        color: "#9aa0a6"
    },
    Showers: {
        unicode: "\uf740",
        fontFamily: "Font Awesome 6 Free",
        fontWeight: 900,
        color: "#3a7bd5"
    }
};

const dataObject = [
    { name: "김치찌개", data: [30, 170, 165] },
    { name: "된장찌개",  data: [50, 105, 110] },
    { name: "부대찌개", data: [40, 82, 63] },
    { name: "닭도리탕", data: [120, 82, 30] },
];

const series = dataObject.map(data => ({
    ...data,
    type: "bar",
    label: { show: true },
}));

const drawChart = (data) => {
    const yAxisRich = Object.fromEntries(
        Object.entries(weatherFA).map(([key, val]) => [
            `${key}Icon`,
            {
                fontFamily: val.fontFamily,
                fontWeight: val.fontWeight,
                fontSize: 30,
                color: val.color,
                align: "center",
                lineHeight: 32
            }
        ])
    );

    yAxisRich.label = {
        fontSize: 14,
        color: "#333",
        align: "center"
    };

    const option = {
        title: {
            text: "계절별 제품 로스율",
            top: 10
        },
        tooltip: {
            trigger: "axis",
            axisPointer: {type: "shadow"},
        },
        legend: {
            top: 40,
            data: dataObject.map(({name}) => name)
        },
        grid: {
            top: 100,
            right: 50,
            bottom: 50
        },
        xAxis: {
            type: "value",
            name: "로스율"
        },
        yAxis: {
            type: "category",
            inverse: true,
            data: Object.keys(weatherFA),
            axisLabel: {
                formatter : (value) => `{${value}Icon|${weatherFA[value].unicode}}`,
                margin: 20,
                rich: yAxisRich
            }
        },
        series: data
    }

    lossChart.setOption(option);
}

const drawMaterialLossChart = () => {
    const chartDom = document.getElementById('material_loss_Graph');
    lossChart = echarts.init(chartDom);

    drawChart(series);

    const now = new Date();
    const drawTime = document.getElementById("material_loss_Graph_time")
    drawTime.innerText = calcDrawDate(now);
    // fetch(`그래프 데이터용 API`, {method: 'GET'})
    //     .then(response => response.json())
    //     .then(data => {
    //         drawChart(data);
    //
    //         const now = new Date();
    //         const drawTime = document.getElementById("material_loss_Graph_time")
    //         drawTime.innerText = calcDrawDate(now);
    //     });
}