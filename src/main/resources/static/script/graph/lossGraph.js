let lossChart;

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialLossChart();
});

const weatherSVG = {
    Sunny: "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='25' height='25' fill='%23f5c542' viewBox='0 0 512 512'><path d='M256 384a128 128 0 1 0 0-256 128 128 0 0 0 0 256zm0-224a96 96 0 1 1 0 192 96 96 0 0 1 0-192zM256 16c8.8 0 16 7.2 16 16v48a16 16 0 1 1-32 0V32c0-8.8 7.2-16 16-16zm0 416c8.8 0 16 7.2 16 16v48a16 16 0 1 1-32 0v-48c0-8.8 7.2-16 16-16zm240-160c0 8.8-7.2 16-16 16h-48a16 16 0 1 1 0-32h48c8.8 0 16 7.2 16 16zM96 256c0 8.8-7.2 16-16 16H32a16 16 0 1 1 0-32h48c8.8 0 16 7.2 16 16zm311 151a16 16 0 0 1-22.6 0 16 16 0 0 1 0-22.6l34-34a16 16 0 1 1 22.6 22.6l-34 34zM128.6 127.4a16 16 0 1 1-22.6-22.6l34-34a16 16 0 1 1 22.6 22.6l-34 34zm0 257.2-34 34a16 16 0 1 1-22.6-22.6l34-34a16 16 0 1 1 22.6 22.6zM406.6 105.4l34-34a16 16 0 1 1 22.6 22.6l-34 34a16 16 0 1 1-22.6-22.6z'/></svg>",
    Cloudy: "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='25' height='25' fill='%239aa0a6' viewBox='0 0 640 512'><path d='M240 160c0-53 43-96 96-96 42.3 0 78.2 27.2 91.5 65.1 7.1-1.4 14.4-2.1 21.8-2.1 53 0 96 43 96 96s-43 96-96 96H160c-53 0-96-43-96-96s43-96 96-96c5.5 0 10.9 .5 16.1 1.4C184.1 187.7 209 160 240 160z'/></svg>",
    Showers: "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='25' height='25' fill='%233a7bd5' viewBox='0 0 512 512'><path d='M416 128c-5.3 0-10.5 .3-15.6 1C382.9 82.5 343.2 48 296 48c-53.5 0-98.1 37.7-109.5 88.2C181.1 133 174.6 128 167 128c-44.2 0-80 35.8-80 80s35.8 80 80 80h249c44.2 0 80-35.8 80-80s-35.8-80-80-80zm-63.2 256a16 16 0 1 1 27.3 16l-32 48a16 16 0 0 1-27.3-16l32-48zm-96 0a16 16 0 1 1 27.3 16l-32 48a16 16 0 0 1-27.3-16l32-48zm-96 0a16 16 0 1 1 27.3 16l-32 48a16 16 0 0 1-27.3-16l32-48z'/></svg>"
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
        Object.entries(weatherSVG).map(([key, svg]) => [
            `${key}Icon`,
            {
                height: 25,
                width: 25,
                align: "center",
                backgroundColor: {
                    image: svg
                }
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
            text: "날씨별 제품 로스율",
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
            data: Object.keys(weatherSVG),
            axisLabel: {
                formatter : (value) => `{${value}Icon| }`,
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
    // fetch('/dash/loss', {method: 'GET'})
    //     .then(response => response.json())
    //     .then(json => {
    //         const data = json.map(data => ({
    //             ...data,
    //             type: "bar",
    //             label: { show: true },
    //         }));
    //
    //         drawChart(data);
    //
    //         const now = new Date();
    //         const drawTime = document.getElementById("material_loss_Graph_time")
    //         drawTime.innerText = calcDrawDate(now);
    //     });
}