window.onload = checkAlert;
function checkAlert() {
    const offset = 1000 * 60 * 60 * 9;
    const today = new Date(new Date().getTime() + offset)
        .toISOString()
        .split("T")[0];
    const hiddenDate = localStorage.getItem("hideAlertDate");
    if (hiddenDate === today) return; // 오늘 안 보기 체크했으면 종료
    // 실제로는 fetch('forecast_alert_v2.json') 사용
    // 여기서는 테스트를 위해 가짜 데이터를 사용합니다.
    const mockData = {
        alert_status: "DANGER",
        forecast: [
            {
                date: "2025-11-25 (화)",
                status: "DANGER",
                advice:
                    "습도가 90%에 육박합니다. 곰팡이 발생 위험이 매우 높으니 창고 환기를 철저히 하세요!",
            },
        ],
    };

    if (mockData.alert_status === "DANGER") {
        // 가장 먼저 도래하는 위험한 날의 정보를 띄움
        const dangerDay = mockData.forecast.find(({status}) => status === "DANGER");
        if (dangerDay) {
            document.getElementById("alert-date").innerText = dangerDay.date + " 예보";
            document.getElementById("alert-text").innerText = dangerDay.advice;
            document.getElementById("alert-box").style.display = "block";
        }
    }
}

function closeAlert() {
    const chk = document.getElementById("chk-hide");
    if (chk.checked) {
        const today = new Date().toISOString().split("T")[0];
        localStorage.setItem("hideAlertDate", today);
    }
    document.getElementById("alert-box").style.display = "none";
}

function testAlert() {
    localStorage.removeItem("hideAlertDate");
    checkAlert();
}