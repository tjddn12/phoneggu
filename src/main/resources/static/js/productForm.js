document.addEventListener("DOMContentLoaded", function() {
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

    // 체크박스 상태에 따라 재고 입력 필드를 토글하는 함수
    function toggleStockInput(checkbox, stockItem) {
        if (checkbox.checked) {
            stockItem.style.display = 'flex';
        } else {
            stockItem.style.display = 'none';
            const input = stockItem.querySelector("input[type='number']");
        }
    }

    if (categorySelect && typeSelect) {
        // 초기 호출을 통해 기본 카테고리에 따른 옵션 설정
        updateTypeOptions();

        // 카테고리 선택 변경 시 이벤트 리스너 설정
        categorySelect.addEventListener("change", updateTypeOptions);
    } else {
        console.error("카테고리 선택 요소 또는 타입 선택 요소를 찾을 수 없습니다");
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

function handleFiles(files) {
    const allowedFileTypes = ['image/jpeg', 'image/jpg', 'image/bmp', 'image/png', 'image/gif'];
    const previewContainer = document.getElementById('preview-images');

    for (let i = 0; i < files.length; i++) {
        const file = files[i];

        if (!allowedFileTypes.includes(file.type)) {
            alert('이미지 파일 형식은 jpg, jpeg, bmp, png, gif 만 가능합니다.');
            continue;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            const filePreview = document.createElement('div');
            filePreview.className = 'file-preview d-flex align-items-center mb-2';
            filePreview.innerHTML = `
                    <input type="checkbox" class="form-check-input img-checkbox me-2" data-id="${i}">
                    <img src="${e.target.result}" alt="이미지 미리보기" class="preview-image me-2" style="width: 100px; height: auto;">
                    <p class="file-name mb-0 me-2">${file.name}</p>
                    <button type="button" class="btn btn-danger btn-sm remove-file" data-id="${i}">x</button>
                `;
            previewContainer.appendChild(filePreview);

            const imgCheckbox = filePreview.querySelector('.img-checkbox');
            const previewImage = filePreview.querySelector('.preview-image');
            const removeButton = filePreview.querySelector('.remove-file');

            imgCheckbox.addEventListener('change', function () {
                if (imgCheckbox.checked) {
                    previewImage.style.filter = 'grayscale(100%)';
                } else {
                    previewImage.style.filter = 'none';
                }
            });

            previewImage.addEventListener('click', function () {
                imgCheckbox.checked = !imgCheckbox.checked;
                imgCheckbox.dispatchEvent(new Event('change'));
            });

            removeButton.addEventListener('click', function () {
                removeFile(i);
            });
        };
        reader.readAsDataURL(file);
    }
}

function removeFile(fileId) {
    const previewContainer = document.getElementById('preview-images');
    const filePreview = previewContainer.querySelector(`.file-preview input[data-id="${fileId}"]`).closest('.file-preview');
    previewContainer.removeChild(filePreview);
}

function removeSelectedFiles() {
    const previewContainer = document.getElementById('preview-images');
    const checkboxes = previewContainer.querySelectorAll('.img-checkbox:checked');
    checkboxes.forEach((checkbox) => {
        removeFile(parseInt(checkbox.getAttribute('data-id')));
    });
}