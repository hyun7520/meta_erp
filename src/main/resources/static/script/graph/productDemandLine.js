/*
* 각 html 파일 CDN 설정
* <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
* */
let lineChart;

document.addEventListener("DOMContentLoaded", () => {
    drawDemandLineChart();
});

const drawLine = (data, labels) => {
    const datasetWithFilters = [];
    const seriesList = [];

    const xLabels = data.map(({date}) => date).sort((a, b) => (a > b ? 1 : -1));
    const now = new Date();
    const year = now.getFullYear();
    const pastYear = year - 1;
    const startIdx = xLabels.findIndex(item => item === `${pastYear}-${now.getMonth() + 1}`) + 1;
    const endIdx = xLabels.findIndex(item => item === `${year}-${now.getMonth() + 1}`) + 1;

    echarts.util.each(labels, function (label) {
        const datasetId = 'dataset_' + label;

        datasetWithFilters.push({
            id: datasetId,
            fromDatasetId: 'dataset_raw',
            transform: {
                type: 'filter',
                config: {
                    and: [
                        { dimension: 'product', '=': label }
                    ]
                }
            }
        });

        seriesList.push({
            type: 'line',
            datasetId: datasetId,
            showSymbol: false,
            name: label,
            endLabel: {
                show: true,
                formatter: (params) => `${params.data.product}: ${params.data.demand}`
            },
            emphasis: {
                focus: 'series'
            },
            encode: {
                x: 'date',
                y: 'demand',
                label: ['product', 'demand'],
                itemName: 'date',
                tooltip: ['demand']
            },
            markArea: {
                silent: true,
                itemStyle: { color: 'rgba(255, 173, 177, 0.4)' },
                data: [
                    [
                        {
                            name: '예측 구역',
                            xAxis: '2025-12',
                            label: { position: 'top', color: '#C0392B', fontWeight: 'bold', fontSize: 13 }
                        },
                        { xAxis: '2027-12' }
                    ]
                ]
            },
        });
    });

    const option = {
        animationDuration: 5000,
        dataset: [
            {
                id: 'dataset_raw',
                source: data
            },
            ...datasetWithFilters
        ],
        title: {
            text: '최근 5년간 제품 수요량 및 예측 그래프'
        },
        tooltip: {
            order: 'valueDesc',
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            nameLocation: 'middle'
        },
        yAxis: {
            name: '수요량(Box)'
        },
        grid: {
            top: 100,
            right: 100,
            bottom: 0,
        },
        dataZoom: [
            {
                type: 'slider',
                xAxisIndex: 'all',
                start: (startIdx / (xLabels.length - 1)) * 100,
                end: (endIdx / (xLabels.length - 1)) * 100,
                left: '10%',
                right: '10%',
                bottom: 55,
                height: 20,
                throttle: 120
            },
            {
                type: 'inside',
                xAxisIndex: 'all',
                start: (startIdx / (xLabels.length - 1)) * 100,
                end: (endIdx / (xLabels.length - 1)) * 100,
                throttle: 120
            }
        ],
        series: seriesList
    };

    lineChart.setOption(option);
};

const drawDemandLineChart = () => {
    const chartDom = document.getElementById('product_demand_Line');
    lineChart = echarts.init(chartDom);

    fetch(`/dash/demands`, {method: 'GET'})
        .then(response => response.json())
        .then(json => {
            const labels = new Set();
            const data = json.map(({name, demand, requestDate}) => {
                labels.add(name)
                return {date: requestDate.split(" ")[0], product: name, demand: demand}
            })
            drawLine(data, [...labels]);

            const now = new Date();
            const drawTime = document.getElementById("product_demand_Line_time")
            drawTime.innerText = calcDrawDate(now);
        });

}