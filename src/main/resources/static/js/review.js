$(document).ready(function(){
    $('.stars input').change(function(){
        var $this = $(this);
        var $rating = $this.val();
        var $labels = $this.closest('.stars').find('label');
        // 모든 레이블 색상 초기화
        $labels.css('color', '#E5E5E5');
        // 선택된 라디오 버튼과 이전 형제 레이블을 색상 변경
        var index = $labels.index($this.next('label'));

        $labels.slice(0, index + 1).css('color', '#fc0');
        //AJAX 요청 보내기
        $.ajax({
            type: 'POST',
            url: '/submitRating', //: 서버의 엔드포인트 URL
            data: JSON.stringify({revwRatings: rating}),
            contentType: 'application/json',
            success: function(response){
                console.log("평점이 성공적으로 전송되었습니다.");
            },
            error: function(xhr, status, error){
                console.error("평점 전송에 실패했습니다.:", error);
            }
        });
    });
    $('.writeBtn').click(function(event){
        event.preventDefault(); //: 폼의 기본 제출 동작 막음.
        //폼 데이터 수집
        var reviewData = {
            userId: $('#userID').val(), //: 사용자 ID
            revwTitle: $('#revwTitle').val(), //: 리뷰 제목
            revwContent: $('#revwContent').val(), //: 리뷰 내용
            revwRegDate: new Date().toISOString() //: 현재 시각을 ISO 형식으로 변환
        };
        //Ajax 요청
        $.ajax({
            type: 'POST',
            url: '/reviews',
            contentType: 'application/json',
            data: JSON.stringify(reviewData),
            success: function(response){
                alert('리뷰가 성공적으로 등록되었습니다.');

                window.location.href = "/reviews"; //: 리뷰 목록 페이지로 리다이렉트
            },
            error: function(error){
                alert("리뷰 등록에 실패했습니다.");

                console.log(error);
            }
        });
    });
    //파일 업로드 함수
    function uploadFiles(files){
        const formData = new FormData();

        for(let i = 0; i < files.length; i++){
            formData.append(`image${i + 1}`, files[i]);
        }
        //서버로 파일 전송(AJAX 요청 등)
        fetch('/reviews/upload', {
            method: 'POST',
            body: formData
        }).then(response => {
                //업로드 성공시 처리 로직
                console.log('파일 업로드 성공');
        }).catch(error => {
            //업로드 실패시 처리 로직
            console.error('파일 업로드 실패: ', error);
        });
    }
    //파일 선택 이벤트 핸들러
    const fileInputs = document.querySelectorAll('.files');

    fileInputs.forEach((input, index) => {
        input.addEventListener('change', (e) => {
            const file = e.target.files[0];

            if(file){
                previewImage(index + 1, file);
            }
        });
    });
    //이미지 제출 폼
    function submitForm(){
        document.getElementById('reviewForm').submit();
    }
});