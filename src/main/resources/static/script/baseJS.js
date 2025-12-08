const calcDrawDate = (inputDate) => {
    const year = inputDate.getFullYear();
    const month = `${inputDate.getMonth() + 1}`.padStart(2, "0");
    const date = `${inputDate.getDate()}`.padStart(2, "0");

    const hour = `${inputDate.getHours()}`.padStart(2, "0");
    const minute = `${inputDate.getMinutes()}`.padStart(2, "0");
    const seconds = `${inputDate.getSeconds()}`.padStart(2, "0");
    return `${year}-${month}-${date} ${hour}:${minute}:${seconds}`
}

const updateTime = () => {
    const now = new Date();
    document.getElementById('currentTime').innerHTML = '🕐 ' + calcDrawDate(now);
}

const logout = () => {
    location.href = "/logout";
}

const renderPagination = (currentPage, totalPage, callback, id = "pagination") => {
    const pagination = document.getElementById(id);
    pagination.innerHTML = '';

    if (totalPage <= 1) return;

    const prevBtn = document.createElement('button');
    prevBtn.className = 'pagination-btn';
    prevBtn.innerHTML = '‹';
    prevBtn.disabled = currentPage === 1;
    prevBtn.onclick = () => callback(currentPage - 1);
    pagination.appendChild(prevBtn);

    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPage, currentPage + 2);

    if (startPage > 1) {
        const firstBtn = document.createElement('button');
        firstBtn.className = 'pagination-btn';
        firstBtn.textContent = '1';
        firstBtn.onclick = () => callback(1);
        pagination.appendChild(firstBtn);

        if (startPage > 2) {
            const dots = document.createElement('span');
            dots.className = 'pagination-info';
            dots.textContent = '...';
            pagination.appendChild(dots);
        }
    }

    for (let num = startPage; num <= endPage; num++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = 'pagination-btn' + (num === currentPage ? ' active' : '');
        pageBtn.textContent = num;
        pageBtn.onclick = () => callback(num);
        pagination.appendChild(pageBtn);
    }

    if (endPage < totalPage) {
        if (endPage < totalPage - 1) {
            const dots = document.createElement('span');
            dots.className = 'pagination-info';
            dots.textContent = '...';
            pagination.appendChild(dots);
        }

        const lastBtn = document.createElement('button');
        lastBtn.className = 'pagination-btn';
        lastBtn.textContent = totalPage;
        lastBtn.onclick = () => callback(totalPage);
        pagination.appendChild(lastBtn);
    }

    const nextBtn = document.createElement('button');
    nextBtn.className = 'pagination-btn';
    nextBtn.innerHTML = '›';
    nextBtn.disabled = currentPage === totalPage;
    nextBtn.onclick = () => callback(currentPage + 1);
    pagination.appendChild(nextBtn);
}
