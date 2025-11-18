let chart;

document.addEventListener("DOMContentLoaded", () => {
    drawMaterialPieChart();
});

const calcDrawDate = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = `${now.getMonth() + 1}`.padStart(2, "0");
    const date = `${now.getDate()}`.padStart(2, "0");

    const hour = now.getHours();
    const minute = now.getMinutes();
    const seconds = now.getSeconds();
    return `${year}-${month}-${date} ${hour}:${minute}:${seconds}`
}

const drawPie = (data, product = '') => {
    const option = {
        title: {
            text: product + '원자재 현 보유량',
            left: 'center'
        },
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

    chart.setOption(option);
}

const drawMaterialPieChart = (serialCode = '', product = '') => {
    const chartDom = document.getElementById('material_total_Pie');
    chart = echarts.init(chartDom);

    fetch(`/dash/materials?serialCode=${serialCode}`, {method: 'GET'})
        .then(response => response.json())
        .then(json => {
            const data = json.map(obj => ({name: obj['materialName'], value: obj['qty']}));
            drawPie(data, product);

            let drawTime = document.getElementById("material_total_Pie_time")
            drawTime.innerText = calcDrawDate();
        });
}
