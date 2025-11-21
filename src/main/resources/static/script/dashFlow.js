const FLOW_API = "/dash/flow"
const labels = {
    orderRequest: {
        name: "제품 생산 요청",
        icon: `<i class="fa-regular fa-comment-dots"></i>`
    },
    metrialRequest: {
        name: "재료 발주 요청",
        icon: `<i class="fa-regular fa-comment-dots"></i>`
    },
    metrialRequestClear: {
        name: "생산 대기",
        icon: `<i class="fa-solid fa-boxes-packing"></i>`
    },
    productionReady: {
        name: "제품 생산중",
    icon: `<i class="fa-solid fa-industry"></i>`
},
    todayDelivered: {
        name: "당일 생산완료",
        icon: `<i class="fa-solid fa-truck"></i>`
}
};

window.addEventListener("load", () => {
    updateTime();
    setInterval(updateTime, 1000);
    refreshTimeline();
});

function updateFlowLine() {
    const line = document.querySelector(".line");
    const boxes = document.querySelectorAll(".mini-box");

    if (boxes.length < 2) return;

    const first = boxes[0].getBoundingClientRect();
    const last = boxes[boxes.length - 1].getBoundingClientRect();
    const parent = line.parentElement.getBoundingClientRect();

    const start = (first.left + first.width / 2) - parent.left;
    const end = (last.left + last.width / 2) - parent.left;

    line.style.left = `${start}px`;
    line.style.width = `${end - start}px`;
}

function renderTimeline(items) {
    const container = document.getElementById("flow_items");
    container.innerHTML = "";

    items.forEach(({label, count, key}) => {
        const box = document.createElement("div");
        box.className = "mini-box";
        if (count > 5) box.className += " box-over";
        else if (count === 0) box.className += " box-none";

        const icon = document.createElement("div");
        icon.className = "mini-top";
        icon.innerHTML = label.icon;

        if (count > 5) icon.style.color = "deeppink";
        else if (count === 0) icon.style.color = "black";

        box.appendChild(icon);

        const text = document.createElement("div");
        text.className = "center-text";
        text.innerHTML = `${label.name} <span class='count'>${count}개</span>`;
        box.appendChild(text);

        container.appendChild(box);
    });

    updateFlowLine();
}

function refreshTimeline() {
    fetch(FLOW_API, {method: 'GET'})
        .then(response => response.json())
        .then(json => {
            const resolvedJson = [];
            for (let key in json) {
                resolvedJson.push({key: key, value: json[key]})
            }

            const data = resolvedJson.map(({key, value}) => {
                return {label: labels[key], count: value, case: key};
            });

            renderTimeline(data);

            const now = new Date();
            const drawTime = document.getElementById("refresh_time");
            drawTime.innerText = calcDrawDate(now);
        });
}