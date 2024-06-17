// productFormImg.js
function handleFiles(files) {
    const allowedFileTypes = ['image/jpeg', 'image/jpg', 'image/bmp', 'image/png', 'image/gif'];
    const preview = document.getElementById('preview-images');
    preview.innerHTML = ''; // 기존의 미리보기 내용 제거
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        if (!allowedFileTypes.includes(file.type)) {
            alert('이미지 파일 형식은 jpg, jpeg, bmp, png, gif 만 가능합니다.');
            continue;
        }

        reader.onload = (function(file) {
            return function(e) {
                const imgContainer = document.createElement('div');
                imgContainer.className = 'img-container';

                const img = document.createElement('img');
                img.src = e.target.result;
                img.className = 'preview-img';

                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'delete-btn';
                deleteBtn.innerHTML = 'X';
                deleteBtn.onclick = function() {
                    imgContainer.remove();
                };

                imgContainer.appendChild(img);
                imgContainer.appendChild(deleteBtn);
                preview.appendChild(imgContainer);
            };
        })(file);

        reader.readAsDataURL(file);
    }
}

function deleteImage(imageId) {
    if(confirm('이미지를 삭제하시겠습니까?')) {
        fetch('/admin/product/image/' + imageId + '/delete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('이미지 삭제에 실패했습니다.');
            }
        });
    }
}
