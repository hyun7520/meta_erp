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

    fetch("/noti", {method: "GET"})
        .then(response => response.json())
        .then(data => {
            const {date, humidity, status} = data;
            const [stat, advice] = status.replace(")", "").split(" (");
            display = "";

            if (stat.includes("이상")) {
                display = `
                    습도가 ${humidity}%로 예상되며 ${stat} 발생 예정 입니다.<br/>
                    ${advice} 하기에 주의하세요.
                `;
            } else if (parseFloat(humidity.trim()) > 50) {
                display = `
                    ${stat} 이지만 습도가 ${humidity}%로 예상됩니다. <br/>
                    평균 로스율이 높은 습도이니 주의하세요.
                `;
            }
            document.getElementById("alert-date").innerText = date + " 예보";
            document.getElementById("alert-text").innerHTML = display
            document.getElementById("alert-box").style.display = "block";
        });
}

function closeAlert() {
    const chk = document.getElementById("chk-hide");
    if (chk.checked) {
        const today = new Date().toISOString().split("T")[0];
        localStorage.setItem("hideAlertDate", today);
    }
    document.getElementById("alert-box").style.display = "none";
}
