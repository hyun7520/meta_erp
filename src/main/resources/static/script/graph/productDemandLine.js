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
            }
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
            text: '최근 5년간 제품 수요량 그래프'
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
            name: 'demand'
        },
        grid: {
            top: 100,
            right: 100,
            bottom: 0,
        },
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
