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

    // URL에서 category와 type을 가져오는 함수
    function getParameterByName(name) {
        const url = new URL(window.location.href);
        return url.searchParams.get(name);
    }

    const category = getParameterByName('category');
    const type = getParameterByName('type');

    // 모든 이미지를 숨기고 해당하는 이미지를 표시
    $('.top_img img').each(function() {
        const imgCategory = $(this).data('category');
        const imgType = $(this).data('type');
        if (imgCategory === category && imgType === type) {
            $(this).addClass('action');
        } else {
            $(this).removeClass('action');
        }
    });

    $('.sort-link').click(function(e) {
        e.preventDefault();
        let sortBy = $(this).data('sort-by');
        let direction = $(this).data('direction');
        let category = $('#main-category').text();
        let type = $('#sub-category').text().replace(/[()]/g, '');

        window.location.href = `/listProducts?sortBy=${sortBy}&direction=${direction}&category=${category}&type=${type}#anchor`;


    });

});
