let fileList = [];

function handleFiles(files) {
    const allowedFileTypes = ['image/jpeg', 'image/jpg', 'image/bmp', 'image/png', 'image/gif'];
    const previewContainer = document.getElementById('preview-container');
    
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        
        // 파일 형식 확인
        if (!allowedFileTypes.includes(file.type)) {
            alert('이미지 파일 형식은 jpg, jpeg, bmp, png, gif 만 가능합니다.');
            continue;
        }

        if (fileList.length < 5) {
            const fileId = fileList.length;
            fileList.push(file);

            const reader = new FileReader();
            reader.onload = function (e) {
                const filePreview = document.createElement('div');
                filePreview.className = 'file-preview d-flex align-items-center mb-2';
                filePreview.innerHTML = `
                    <input type="checkbox" class="form-check-input img-checkbox me-2" data-id="${fileId}">
                    <img src="${e.target.result}" alt="이미지 미리보기" class="preview-image me-2" style="width: 100px; height: auto;">
                    <p class="file-name mb-0 me-2">${file.name}</p>
                    <button type="button" class="btn btn-danger btn-sm remove-file" data-id="${fileId}">x</button>
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
                    removeFile(fileId);
                });
            };
            reader.readAsDataURL(file);
        }
    }
}

function removeFile(fileId) {
    fileList = fileList.filter((file, index) => index !== fileId);
    const previewContainer = document.getElementById('preview-container');
    const filePreview = previewContainer.querySelector(`.file-preview input[data-id="${fileId}"]`).closest('.file-preview');
    previewContainer.removeChild(filePreview);

    // Update IDs
    const checkboxes = previewContainer.querySelectorAll('.img-checkbox');
    checkboxes.forEach((checkbox, index) => {
        checkbox.setAttribute('data-id', index);
    });
    fileList = Array.from(checkboxes).map((checkbox) => fileList[parseInt(checkbox.getAttribute('data-id'))]);
}

function removeSelectedFiles() {
    const previewContainer = document.getElementById('preview-container');
    const checkboxes = previewContainer.querySelectorAll('.img-checkbox:checked');
    checkboxes.forEach((checkbox) => {
        removeFile(parseInt(checkbox.getAttribute('data-id')));
    });
}
