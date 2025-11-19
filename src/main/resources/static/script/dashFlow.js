const FLOW_API = "/dash/flow"
const labels = {
    orderRequest: "제품 생산 요청",
    metrialRequest: "재료 발주 요청",
    metrialRequestClear: "생산 대기",
    productionReady: "제품 생산중",
    todayDelivered: "당일 생산완료"
};

window.addEventListener("load", refreshTimeline);

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
        icon.innerHTML = `<i class="fa-regular fa-comment-dots"></i>`;
        box.appendChild(icon);

        const text = document.createElement("div");
        text.className = "center-text";
        text.innerHTML = `${label} <span class='count'>${count}개</span>`;
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