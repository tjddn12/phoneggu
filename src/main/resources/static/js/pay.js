document.addEventListener("DOMContentLoaded", function () {
    const container = document.querySelector(".container");
    const orderId = container.dataset.orderId;
    const amount = container.dataset.amount;
    const productName = container.dataset.productName;
    const order = JSON.parse(container.dataset.order);
    const generateRandomString = () => window.btoa(Math.random()).slice(0, 20);
    const paymentKey = generateRandomString();
    const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";

    const paymentWidget = PaymentWidget(clientKey, paymentKey);
    console.log("확인: ", orderId, amount, paymentKey);

    // 결제 UI 렌더링
    const paymentMethodWidget = paymentWidget.renderPaymentMethods(
        "#payment-method",
        { value: amount },
        { variantKey: "DEFAULT" }
    );

    // 이용약관 UI 렌더링
    paymentWidget.renderAgreement("#agreement", { variantKey: "AGREEMENT" });

    // 결제하기 버튼 누르면 결제창 띄우기
    document.getElementById("payButton").addEventListener("click", function () {
        paymentWidget.requestPayment({
            orderId: orderId,
            orderName: productName,
            successUrl: window.location.origin + "/success",
            failUrl: window.location.origin + "/fail",
            customerEmail: order.email, // 회원 이메일
            customerName: order.userName, // 회원 이름
            customerMobilePhone: order.phone,
        });
    });

    // 페이지 떠날 때 이벤트 처리
    window.addEventListener("beforeunload", function (e) {
        if (!isPaymentSuccess) {
            // 결제 한 것이 아니면 cancel로 이동
            fetch("/cancel", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ orderId: orderId })
            }).then(response => {
                if (!response.ok) {
                    console.error("Failed to cancel order");
                }
            }).catch(error => {
                console.error("Error canceling order:", error);
            });

            // 사용자에게 경고 메시지 표시
            e.preventDefault();
            e.returnValue = "주문을 취소합니다.";
        }
    });
});
