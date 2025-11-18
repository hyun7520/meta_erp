const calcDrawDate = (inputDate) => {
    const year = inputDate.getFullYear();
    const month = `${inputDate.getMonth() + 1}`.padStart(2, "0");
    const date = `${inputDate.getDate()}`.padStart(2, "0");

    const hour = inputDate.getHours();
    const minute = inputDate.getMinutes();
    const seconds = inputDate.getSeconds();
    return `${year}-${month}-${date} ${hour}:${minute}:${seconds}`
}
