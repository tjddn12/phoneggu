$(document).ready(function() {
    // 별 색상 변경
    $('.stars input').change(function(){
        var $this = $(this);
        var $rating = $this.val();
        var $labels = $this.closest('.stars').find('label');
        // 모든 레이블 색상 초기화
        $labels.css('color', '#E5E5E5');
        // 선택된 라디오 버튼과 이전 형제 레이블을 색상 변경
        var index = $labels.index($this.next('label'));
        $labels.slice(0, index + 1).css('color', '#fc0');
    });
    window.handleFiles = function(files) {
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
});
