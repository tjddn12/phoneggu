function setDateRange(range) {
    let startDate = document.getElementById('startDate');
    let endDate = document.getElementById('endDate');
    let today = new Date();
    endDate.value = today.toISOString().split('T')[0];

    if (range === 'today') {
        startDate.value = endDate.value;
    } else if (range === 'week') {
        today.setDate(today.getDate() - 7);
        startDate.value = today.toISOString().split('T')[0];
    } else if (range === 'month') {
        today.setMonth(today.getMonth() - 1);
        startDate.value = today.toISOString().split('T')[0];
    } else if (range === 'three_months') {
        today.setMonth(today.getMonth() - 3);
        startDate.value = today.toISOString().split('T')[0];
    } else if (range === 'six_months') {
        today.setMonth(today.getMonth() - 6);
        startDate.value = today.toISOString().split('T')[0];
    }
}

function search() {
    let startDate = document.getElementById('startDate').value;
    let endDate = document.getElementById('endDate').value;
    window.location.href = `/order/search?startDate=${startDate}&endDate=${endDate}`;
}



