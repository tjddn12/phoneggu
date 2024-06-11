document.addEventListener("DOMContentLoaded", function() {
    const categorySelect = document.querySelector(".select_category");
    const typeSelect = document.querySelector(".select_type");
    const modelCheckboxes = document.querySelectorAll(".model-checkbox");
    const stockInputsContainer = document.getElementById("stockInputsContainer").querySelector(".col-sm-10");

    const productTypes = {
        "PHONE_CASE": ["HARD", "JELLY", "CARD"],
        "TOK": ["ROUND", "HEART", "ACRYLIC"],
        "AIRPODS": ["AIRPODS_1_2", "AIRPODS_PRO", "AIRPODS_3", "BUDS"],
        "DIGITAL": ["APPLE_WATCH"]
    };

    function updateTypeOptions() {
        const selectedCategory = categorySelect.value;
        const types = productTypes[selectedCategory] || [];

        typeSelect.innerHTML = types.map(type => `<option value="${type}">${type}</option>`).join("");
    }

    function toggleStockInput(checkbox, modelName, index) {
        const inputId = `stockInput_${index}`;
        let stockInputContainer = document.getElementById(`container_${inputId}`);

        if (checkbox.checked) {
            if (!stockInputContainer) {
                stockInputContainer = document.createElement("div");
                stockInputContainer.classList.add("stock-input-container", "d-flex", "align-items-center", "mb-2");
                stockInputContainer.id = `container_${inputId}`;

                const label = document.createElement("label");
                label.classList.add("mr-2");
                label.innerText = `${modelName} :`;

                const hiddenInput = document.createElement("input");
                hiddenInput.type = "hidden";
                hiddenInput.name = `productModelDtoList[${index}].productModelSelect`;
                hiddenInput.value = modelName;

                const stockInput = document.createElement("input");
                stockInput.type = "number";
                stockInput.id = inputId;
                stockInput.name = `productModelDtoList[${index}].prStock`;
                stockInput.classList.add("form-control", "w-auto", "ml-2");
                stockInput.placeholder = "재고 수량";
                stockInput.required = true; // 수량 입력 필수

                stockInputContainer.appendChild(label);
                stockInputContainer.appendChild(stockInput);
                stockInputContainer.appendChild(hiddenInput);
                stockInputsContainer.appendChild(stockInputContainer);
            }
            stockInputContainer.querySelector('input[type="number"]').disabled = false;
        } else {
            if (stockInputContainer) {
                stockInputsContainer.removeChild(stockInputContainer);
            }
        }
    }

    modelCheckboxes.forEach((checkbox, index) => {
        const modelName = checkbox.nextElementSibling.innerText; // 기종 이름 가져오기
        checkbox.addEventListener("change", () => toggleStockInput(checkbox, modelName, index));
    });

    categorySelect.addEventListener("change", updateTypeOptions);

    updateTypeOptions(); // 페이지 로드 시 초기화
});

function handleFiles(files) {
    const allowedFileTypes = ['image/jpeg', 'image/jpg', 'image/bmp', 'image/png', 'image/gif'];
    const previewContainer = document.getElementById('preview-container');
    previewContainer.innerHTML = "";

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
    const previewContainer = document.getElementById('preview-container');
    const filePreview = previewContainer.querySelector(`.file-preview input[data-id="${fileId}"]`).closest('.file-preview');
    previewContainer.removeChild(filePreview);
}

function removeSelectedFiles() {
    const previewContainer = document.getElementById('preview-container');
    const checkboxes = previewContainer.querySelectorAll('.img-checkbox:checked');
    checkboxes.forEach((checkbox) => {
        removeFile(parseInt(checkbox.getAttribute('data-id')));
    });
}

// 폼 제출 이벤트 리스너 추가
const form = document.querySelector("form");
form.addEventListener("submit", function(event) {
    const uncheckedModels = document.querySelectorAll(".model-checkbox:not(:checked)");
    uncheckedModels.forEach((input) => {
        const index = Array.from(modelCheckboxes).indexOf(input);
        const stockInputContainer = document.getElementById(`container_stockInput_${index}`);
        if (stockInputContainer) {
            stockInputContainer.remove();
        }
    });
});
