let orderId = 0;
let products = [];

window.onclick = function(event) {
    const modal = document.getElementById('order_modal');

    if (event.target === modal) {
        closeOrderModal();
    }
}

function loadProducts() {
    fetch('/products')  // 수정
        .then(response => response.json())
        .then(data => {
            products = data;
            updateProductSelect();
        })
        .catch(error => console.error('제품 목록 로딩 실패:', error));
}

const openOrderModal = (orderId) => {
    const modal = document.getElementById('order_modal')
    if (orderId > 0) {
        modal.querySelector("h3").innerText = "📝 발주 요청 수정";
        modal.querySelector(".btn-submit").innerHTML = "수정";
        fillEditModal(orderId);
    } else {
        modal.querySelector("h3").innerText = "➕ 새 발주 요청";
        modal.querySelector(".btn-submit").innerHTML = "추가";
        fillModal();
    }
    modal.style.display = 'block';
}

const closeOrderModal = () => {
    document.getElementById("order_modal").style.display = "none";
}

const updateProductSelect = (productName = "") => {
    const select = document.getElementById('order_product_select');
    if (!select) return;
    select.innerHTML = '<option value="">선택하세요</option>';
    products.forEach(product => {
        const option = document.createElement('option');
        option.value = product.serialCode;
        option.textContent = product.productName;
        if (product.serialCode === productName) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

const fillModal = (data) => {
    updateProductSelect(data?.productName || "")

    orderId = data?.orderId || 0;
    document.getElementById('order_client_name').value = data?.requestBy || '';
    document.getElementById('order_qty').value = data?.qty || '';
    document.getElementById('order_unit').value = data?.unit || '';
    document.getElementById('order_due_date').value = data?.deadline || '';
}

function fillEditModal(prId) {
    fetch(`/pro/${prId}`)  //  수정
        .then(response => response.json())
        .then(data => {
            fillModal(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('주문 정보를 불러오는데 실패했습니다.');
        });
}

const checkOrderValidate = () => {
    const productId = document.getElementById('order_product_select').value;
    const clientName = document.getElementById('order_client_name').value;
    const quantity = document.getElementById('order_qty').value;
    const unit = document.getElementById('order_unit').value;
    const dueDate = document.getElementById('order_due_date').value;

    if (!productId || !clientName || !quantity || !unit || !dueDate) {
        alert('필수 항목을 모두 입력해주세요.');
        return false;
    }

    if (isNaN(quantity) || parseInt(quantity) <= 0) {
        alert('수량은 1 이상의 숫자여야 합니다.');
        return false;
    }

    const dueDateObj = new Date(dueDate);
    if (isNaN(dueDateObj.getTime())) {
        alert('올바른 날짜 형식을 입력해주세요.');
        return false;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (dueDateObj < today) {
        alert('납기일은 오늘 이후여야 합니다.');
        return false;
    }
}

const insertData = () => {
    const serialCode = document.getElementById('order_product_select').value;
    const clientName = document.getElementById('order_client_name').value;
    const quantity = document.getElementById('order_qty').value;
    const unit = document.getElementById('order_unit').value;
    const dueDate = document.getElementById('order_due_date').value;

    return {
        serialCode: serialCode,
        requestBy: clientName.trim(),
        qty: parseInt(quantity),
        unit: unit.trim(),
        deadline: dueDate
    };
}

const onSubmit = () => {
    const isAvail = checkOrderValidate();
    if (isAvail === false) return;

    const orderData = insertData();
    const postAPI = orderId > 0 ? `/pro/${orderId}` : "/pro";
    fetch(postAPI, {  //  수정
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    console.error('서버 응답:', text);
                    throw new Error(text || '수정 실패');
                });
            }
            return response.json();
        })
        .then(result => {
            closeOrderModal();
            if (orderId > 0) {
                alert('발주 요청이 수정되었습니다.');
                const tabs = document.querySelectorAll("button[class='tab']")
                let currentTap = "pending";
                tabs.forEach(tab => {
                    if (tab.className.includes("active")) {
                        currentTap = tab.getAttribute("name");
                    }
                });
                loadData(currentTap);
            } else {
                alert('생산 요청이 성공적으로 접수되었습니다.');
                loadData('pending');
            }
        })
        .catch(error => {
            if (orderId > 0) printEditError(error);
            else printInsertError(error);
        });
}

const printInsertError = (error) => {
    console.error('Error:', error);
    alert('생산 요청 중 오류가 발생했습니다: ' + (error.message || '알 수 없는 오류'));
}

const printEditError = (error) => {
    console.error('Error:', error);
    if (error.message && error.message.includes('이미 생산이 시작')) {
        alert('이미 생산이 시작된 주문은 수정할 수 없습니다.');
    } else if (error.message && error.message.includes('ORA-01861')) {
        alert('날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식이어야 합니다)');
    } else {
        alert('발주 요청 수정에 실패했습니다.\n' + error.message);
    }
}
