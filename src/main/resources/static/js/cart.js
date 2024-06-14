$(document).ready(function () {
    // 상품 수량 변동에 따른 처리
    $('.item-count').on('change', function () {
        let $input = $(this);
        let newCount = $input.val();
        let cartId = $input.data('cart-id');

        // console.log("보내는 데이터: ", { cartItemId: cartId, count: newCount }); // 확인을 위한 로그

        $.ajax({
            url: '/cart/update',
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

    // 전체주문 클릭시
    $(".allOrder").on("click", function (event) {
        event.preventDefault(); // 기본 폼 전송 동작 막기
        // 전체 체크박스를 선택한 후 전송
        $(".cart-item-checkbox").prop("checked", true);
        sendOrderRequest();
    });

    // 선택주문 클릭시
    $(".selectOrder").on("click", function (event) {
        event.preventDefault(); // 기본 폼 전송 동작 막기
        sendOrderRequest();
    });

    // 전체 선택/해제
    $("#selectAll").on("change", function () {
        $(".cart-item-checkbox").prop("checked", $(this).prop("checked"));
    });

    function sendOrderRequest() {
        // 체크 박스에 체크된 아이템들을 순회하면서 id값 넣어주기
        let selectedItems = $(".cart-item-checkbox:checked").map(function () {
            return $(this).val();
        }).get();
        console.log("값 : ", selectedItems)

        if (selectedItems.length === 0) {
            alert("주문할 상품을 선택하세요.");
            return;
        }

        let form = $('<form>', {
            'method': 'POST',
            'action': '/order'
        });

        selectedItems.forEach(function (item) {
            form.append($('<input>', {
                'type': 'hidden',
                'name': 'cartItemIds',
                'value': item
            }));
        });

        form.append($('<input>', {
            'type': 'hidden',
            'name': 'type',
            'value': 'cart'
        }));

        $('body').append(form);
        form.submit();
    }
});
