// 네비게이션 메뉴 클릭 이벤트 처리
document.addEventListener('DOMContentLoaded', function() {
    // 각 네비게이션 항목에 대한 클릭 이벤트 리스너 추가
    document.getElementById('navHard').addEventListener('click', function() {
        showProducts('하드');
        updateTopLeft('폰 케이스', '하드');
    });
    document.getElementById('navJelly').addEventListener('click', function() {
        showProducts('젤리');
        updateTopLeft('폰 케이스', '젤리');
    });
    document.getElementById('navCard').addEventListener('click', function() {
        showProducts('카드수납');
        updateTopLeft('폰 케이스', '카드 수납');
    });
    document.getElementById('navRound').addEventListener('click', function() {
        showProducts('원형톡');
        updateTopLeft('톡', '원형톡');
    });
    document.getElementById('navHeart').addEventListener('click', function() {
        showProducts('하트톡');
        updateTopLeft('톡', '하트톡');
    });
    document.getElementById('navAcrylic').addEventListener('click', function() {
        showProducts('아크릴톡');
        updateTopLeft('톡', '아크릴톡');
    });
    document.getElementById('navAirPods1').addEventListener('click', function() {
        showProducts('에어팟1/2세대');
        updateTopLeft('에어팟/버즈', '에어팟1/2세대');
    });
    document.getElementById('navAirPodsPro').addEventListener('click', function() {
        showProducts('에어팟PRO');
        updateTopLeft('에어팟/버즈', '에어팟PRO');
    });
    document.getElementById('navAirPods3').addEventListener('click', function() {
        showProducts('에어팟3세대');
        updateTopLeft('에어팟/버즈', '에어팟3세대');
    });
    document.getElementById('navBuds').addEventListener('click', function() {
        showProducts('버즈');
        updateTopLeft('에어팟/버즈', '버즈');
    });
    document.getElementById('navAppleWatch').addEventListener('click', function() {
        showProducts('애플워치');
        updateTopLeft('디지털', '애플워치');
    });
    document.getElementById('navPhoneCase').addEventListener('click', function() {
        showProducts('폰케이스');
        updateTopLeft('폰 케이스', '전체');
    });
    document.getElementById('navTok').addEventListener('click', function() {
        showProducts('톡');
        updateTopLeft('톡', '전체');
    });
    document.getElementById('navAirPodsBuds').addEventListener('click', function() {
        showProducts('에어팟/버즈');
        updateTopLeft('에어팟/버즈', '전체');
    });
    document.getElementById('navDigital').addEventListener('click', function() {
        showProducts('디지털');
        updateTopLeft('디지털', '전체');
    });
});

// 제품 목록을 필터링하여 표시하는 함수
function showProducts(category) {
    const allProducts = document.querySelectorAll('.product');
    allProducts.forEach(product => {
        const productName = product.querySelector('.product-name a').innerText;
        if (productName.includes(category)) {
            product.style.display = 'block';
        } else {
            product.style.display = 'none';
        }
    });
}

// top_left 섹션의 이름을 변경하는 함수
function updateTopLeft(mainCategory, subCategory) {
    document.getElementById(mainCategory.toLowerCase()).innerText = mainCategory;
    document.querySelectorAll('.' + mainCategory.toLowerCase() + '_drop1')[0].innerText = subCategory;
}
