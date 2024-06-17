// productFormImg.js
function handleFiles(files) {
    const preview = document.getElementById('preview-images');
    preview.innerHTML = ''; // 기존의 미리보기 내용 제거
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

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
