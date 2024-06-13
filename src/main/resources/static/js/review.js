$(document).ready(function(){
    $('.stars input').change(function(){
        var $this = $(this);
        // 모든 레이블 색상 초기화
        $this.siblings('label').css('color', '#E5E5E5');
        // 선택된 라디오 버튼과 이전 형제 레이블을 색상 변경
        $this.next('label').css('color', '#fc0');
        $this.next('label').prevAll('label').css('color', '#fc0');
    });
    // //썸네일 미리보기 및 클릭시 재선택
    // let fileItems = document.querySelectorAll('[type=file]');
    // let imageItem = document.querySelectorAll('.imageItem');
    // let imageBox = document.querySelectorAll('.imageBox');
    //
    // fileItems.forEach(item => item.addEventListener('change', previewImage));
    //
    // function previewImage(){
    //     let index = Array.from(fileItems).indexOf(this); //: fileItems 기준으로 index 생성
    //
    //     //console.log(index);
    //     if(this.files && this.files[0]){
    //         let reader = new FileReader();
    //
    //         reader.readAsDataURL(this.files[0]);
    //         reader.onload = function(){
    //             //: 사진 첨부 코드 추후 수정.
    //         }
    //     }else{
    //
    //     }
    // };
    // //리뷰 등록 폼 제출
    // const urlParams = new URL(location.href).searchParams;
    // const orderNo = urlParams.get('order');
    //
    // console.log(orderNo);
    //
    // function submitWriteForm(form, event){
    //     event.preventDefault();
    //     //양식 제출전 필수 입력값 확인
    //     let text = '';
    //
    //     if(!document.querySelector('input[name=revwTitle]')).value.trim().length > 0){
    //         text = "제목을 입력하세요";
    //         //JSON 데이터 전송 코드 구현 예정.
    //     }
    // }
});