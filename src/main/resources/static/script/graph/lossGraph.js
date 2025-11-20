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

const series = dataObject.map(city => ({
    name: city.name,
    type: "bar",
    data: city.data,
    label: { show: true },
    markPoint: { label: { show: false }, data: [] }   // markPoint 박스 제거
}));

const drawChart = (data) => {
    const yAxisRich = Object.fromEntries(
        Object.entries(weatherFA).map(([key, val]) => [
            `${key}Icon`,
            {
                fontFamily: val.fontFamily,
                fontWeight: val.fontWeight,
                fontSize: 28,
                color: val.color,
                align: "center",
                lineHeight: 32
            }
        ])
    );

    const option = {
        title: {
            text: "Weather Statistics",
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
            left: 120,
            top: 100,
            right: 50,
            bottom: 50
        },
        xAxis: {
            type: "value",
            name: "Days"
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

        series: series
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