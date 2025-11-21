let pieChart;

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialPieChart();
});

const drawPie = (data, product = '') => {
    const option = {
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
                center: ['55%', '60%'],
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

    pieChart.setOption(option);
}

const drawMaterialPieChart = (serialCode = '', product = '') => {
    const chartDom = document.getElementById('material_total_Pie');
    pieChart = echarts.init(chartDom);

    fetch(`/dash/materials?serialCode=${serialCode}`, {method: 'GET'})
        .then(response => response.json())
        .then(json => {
            const data = json.map(obj => ({name: obj['materialName'], value: obj['qty']}));
            drawPie(data, product);

            const now = new Date();
            const drawTime = document.getElementById("material_total_Pie_time")
            drawTime.innerText = calcDrawDate(now);
        });
}
