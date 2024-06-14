// 페이지 떠날 때 이벤트 처리
// window.addEventListener('beforeunload', function (e) {
//     if (!isPaymentSuccess) { // 결제 한것이 아니면 cancel 로 이동
//         fetch('/cancel', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             // body: JSON.stringify({orderId: order.orderID})
//         }).then(response => {
//             if (!response.ok) {
//                 console.error('Failed to cancel order');
//             }
//         }).catch(error => {
//             console.error('Error canceling order:', error);
//         });
//
//         // 사용자에게 경고 메시지 표시
//         e.preventDefault();
//         e.returnValue = '주문을 취소합니다.';
//     }
// });

$(document).ready(function () {
    // 상품 수량 변동에 따른 처리
    $('.item-count').on('change', function () {
        let $input = $(this);
        let newCount = $input.val();
        let cartId = $input.data('cart-id');

        // console.log("보내는 데이터: ", { cartItemId: cartId, count: newCount }); // 확인을 위한 로그

        $.ajax({
            url: 'cart/update',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({cartItemId: cartId, count: newCount}),
            success: function (response) {
                console.log('서버 응답: ', response); // 확인을 위한 로그
                if (response.success) {
                    console.log('수량 업데이트 성공');
                    location.reload(); // 성공 시 페이지 새로고침
                } else {
                    console.error('수량 업데이트 실패: ', response.message);
                    alert('오류: ' + response.message);
                }
            },
            error: function (xhr, status, error) {
                let response = JSON.parse(xhr.responseText);
                console.error('에러 발생:', response.message);
                alert('오류: ' + response.message);
                location.reload();
            }
        });
    }); // 수량의 끝




    // 삭제 버튼 클릭 시
    $('.btn-danger').on('click', function (e) {
        e.preventDefault();
        let $form = $(this).closest('form');
        $.ajax({
            url: $form.attr('action'),
            type: $form.attr('method'),
            data: $form.serialize(),
            success: function (response) {
                if (response.success) {
                    location.reload(); // 성공 시 페이지 새로고침
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



