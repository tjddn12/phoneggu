$(document).ready(function () {
    // 상품 수량 변동에 따른 처리
    $('.item-count').on('change', function () {
        let $input = $(this);
        let newCount = $input.val();
        let orderDetailId = $input.data('order-detail-id');


        $.ajax({
            url: '/order/updateQuantity',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({orderDetailId: orderDetailId, count: newCount}),
            success: function (response) {
                if (response.success) {
                    console.log('수량 업데이트 성공');
                    location.reload(); // 성공 시 페이지 새로고침
                } else {
                    console.error('수량 업데이트 실패');
                    alert('수량 업데이트 실패');
                }
            },
            error: function (xhr, status, error) {
                console.error('에러 발생:', error);
                alert('에러 발생');
                location.reload();
            }
        });
    });


    // 삭제 버튼 클릭 시
    $('.remove-item').on('click', function (e) {
        e.preventDefault();
        let $button = $(this);
        let orderId = $button.data('order-id');
        let orderDetailId = $button.data('order-detail-id');

        $.ajax({
            url: '/order/remove',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({orderId: orderId, orderDetailId: orderDetailId}),
            success: function (response) {
                if (response.success) {
                    window.location.href = '/order';
                } else {
                    alert('오류: ' + response.message);
                }
            },
            error: function (xhr) {
                let response = JSON.parse(xhr.responseText);
                alert('오류: ' + response.message);
            }
        });
    });

    // 주문 요청
    // 결제하기 버튼 클릭 시
    $('#submit-order').on('click', function (e) {
        e.preventDefault();

        $.ajax({
            url: '/pay/checkout',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({orderId: $('input[name="orderId"]').val()}),
            success: function (response) {
                if (response.success) {
                    window.location.href = response.redirectUrl; // 토스 결제 페이지로 리다이렉트
                } else {
                    alert('오류: ' + response.message);
                }
            },
            error: function (xhr) {
                let response = JSON.parse(xhr.responseText);
                alert('오류: ' + response.message);
            }
        });
    });





});