document.addEventListener("DOMContentLoaded", function() {
    // 카테고리 및 타입 선택 관련 로직
    const categorySelect = document.querySelector(".select_category");
    const typeSelect = document.querySelector(".select_type");
    const modelCheckboxes = document.querySelectorAll(".model-checkbox");
    const stockItems = document.querySelectorAll(".stock-item");

    const productTypes = {
        "PHONE_CASE": ["HARD", "JELLY", "CARD", "ZFLIP"],
        "TOK": ["ROUND", "HEART", "ACRYLIC"],
        "AIRPODS": ["AIRPODS_1_2", "AIRPODS_PRO", "AIRPODS_3", "BUDS"],
        "DIGITAL": ["APPLE_WATCH"]
    };

    // 선택된 카테고리에 따라 타입 옵션을 업데이트하는 함수
    function updateTypeOptions() {
        const selectedCategory = categorySelect.value;
        const types = productTypes[selectedCategory] || [];

        typeSelect.innerHTML = types.map(type => `<option value="${type}">${type}</option>`).join("");
    }

    if (categorySelect && typeSelect) {
        // 초기 호출을 통해 기본 카테고리에 따른 옵션 설정
        updateTypeOptions();

        // 카테고리 선택 변경 시 이벤트 리스너 설정
        categorySelect.addEventListener("change", updateTypeOptions);
    } else {
        console.error("카테고리 선택 요소 또는 타입 선택 요소를 찾을 수 없습니다");
    }


    // 체크박스 상태에 따라 재고 입력 필드를 토글하는 함수
    function toggleStockInput(checkbox, stockItem) {
        if (checkbox.checked) {
            stockItem.style.display = 'flex';
        } else {
            stockItem.style.display = 'none';
            const input = stockItem.querySelector("input[type='number']");
        }
    }

    // 각 체크박스에 대해 초기 상태 설정 및 이벤트 리스너 추가
    modelCheckboxes.forEach((checkbox, index) => {
        const stockItem = stockItems[index];
        const removeButton = stockItem.querySelector(".remove-stock");

        // 초기 상태 설정
        toggleStockInput(checkbox, stockItem);

        checkbox.addEventListener("change", function() {
            toggleStockInput(checkbox, stockItem);
        });
    });
});