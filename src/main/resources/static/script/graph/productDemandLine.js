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

    const xLabels = [...labelSet].sort()

    const series = [];
    const now = new Date();
    const startYear = now.getFullYear();
    const endYear = startYear + 1;

    for (const name in groups) {
        const items = groups[name];
        items.sort((a, b) => (a.requestDate > b.requestDate ? 1 : -1));

        series.push({
            name,
            type: "line",
            smooth: true,
            data: items.map(v => v.demand),
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
        title: {
            text: "최근 5년간 제품 수요량 그래프",
            left: 'center',
            top: '2%'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                var result = params[0].name + '<br/>';
                params.forEach(function (item) {
                    result += '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:' + item.color + ';"></span>'
                        + item.seriesName + ': ' + item.value.toLocaleString() + ' 건' + '<br/>';
                });
                return result;
            }
        },
        legend: {
            data: Object.keys(echarts_data),
            bottom: '0%'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xLabels
        },
        yAxis: {
            type: 'value',
            name: '판매량 (건)',
            min: 900,
            axisLabel: {
                formatter: function(value) {
                    return value.toLocaleString();
                }
            },
        },
        series: series
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
