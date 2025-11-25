let pieChart;
let productSerial = "";

document.addEventListener("DOMContentLoaded", () => {
    renderPieTitle();
    drawMaterialPieChart();
});

const drawPie = (data) => {
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

const drawPieTitle = (data) => {
    const titleBox = document.getElementById("material_total_Pie_title");
    const formGroup = document.createElement("div");
    formGroup.className = "form-group";

    const selectBox = document.createElement("select");
    selectBox.addEventListener("change", (event) => {
        const optionElements = event.currentTarget.options;
        const selectedIndex = optionElements.selectedIndex;
        productSerial = optionElements[selectedIndex].value;
        drawMaterialPieChart();
    });
    selectBox.innerHTML = `<option value="">전체</option>`
    Object.entries(data).forEach(([name, serialCode]) => {
        isSelected = productSerial === serialCode ? "selected" : "";
        selectBox.innerHTML += `<option value="${serialCode}" ${isSelected}>${name}</option>`
    });
    formGroup.appendChild(selectBox);
    titleBox.appendChild(formGroup);

    const title = document.createElement("h3");
    title.innerHTML = "원자재 현 보유량";
    title.style.marginLeft = "10px";
    titleBox.appendChild(title);
}

const drawMaterialPieChart = () => {
    const chartDom = document.getElementById('material_total_Pie');
    pieChart = echarts.init(chartDom);

    fetch(`/dash/materials?serialCode=${productSerial}`, {method: 'GET'})
        .then(response => response.json())
        .then(json => {
            const data = json.map(obj => ({name: obj['materialName'], value: obj['qty']}));
            drawPie(data);

            const now = new Date();
            const drawTime = document.getElementById("material_total_Pie_time")
            drawTime.innerText = calcDrawDate(now);
        });
}

const renderPieTitle = () => {
    fetch("/dash/products", {method: "GET"})
        .then(response => response.json())
        .then(drawPieTitle);
}
