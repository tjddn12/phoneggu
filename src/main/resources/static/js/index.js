$(document).ready(function() {
    let currentIndex = 0;
    const initialDisplayCount = 10; // 초기 표시할 제품 수
    const increment = 5; // "more" 버튼 클릭 시 추가로 표시할 제품 수
    const totalProducts = $('.product-item').length; // 전체 제품 수

    function showNextProducts() {
        const endIndex = currentIndex + increment;
        for (let i = currentIndex; i < endIndex && i < totalProducts; i++) {
            $('.product-item').eq(i).show();
        }
        currentIndex = endIndex;
        if (currentIndex >= totalProducts) {
            $('#load-more').hide();
        }
    }

    // 처음 10개의 제품 표시
    for (let i = 0; i < initialDisplayCount && i < totalProducts; i++) {
        $('.product-item').eq(i).show();
    }
    currentIndex = initialDisplayCount;

    $('#load-more').on('click', function() {
        showNextProducts();
    });
});
