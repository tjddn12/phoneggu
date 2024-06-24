document.addEventListener("DOMContentLoaded", () => {
    //답변 작성 버튼 변수화(id가 comment-create-btn인 대상)
    const commentCreateBtn = document.querySelector("#comment-create-btn");
    commentCreateBtn.addEventListener("click",function(){
        // console.log("버튼이 클릭되었습니다.");
//새 답변 객체 생성
        const comment = {
            nickname: document.querySelector("#new-comment-nickname").value,
            body: document.querySelector("#new-comment-body").value,
            article_id: document.querySelector("#new-comment-article-id").value
        }
// 답변 객체 출력
//         console.log(comment);

// fetch(url, 옵션) http요청을 보내기 위한 api입니다. - 서버로 부터 데이터를 가져올수 잇다.
        const url = "/api/qnas/"+comment.article_id+"/comments";
        fetch(url,{
            method : "post",
            body:JSON.stringify(comment), // comment JS 객체를 JSON으로 변경
            headers:{
                "Content-Type": "application/json"
            }
        }).then(response =>{
            //http 응답코드에 따른 메세지출력
            const msg = (response.ok) ? "답변이 등록되었습니다.": "답변 등록 실패!!";
            alert(msg);

            //현재 페이지 를 새로고침
            window.location.reload();

        });
    });

    // 모달 요소 선택
    const commentEditModal = document.querySelector("#comment-edit-modal");
    //모달 이벤트 감지
    commentEditModal.addEventListener("show.bs.modal",function(e){
        // 트리거 버튼 선택
        const triggerBtn = e.relatedTarget;
        // 데이터 가져오기
        const id = triggerBtn.getAttribute("data-bs-id");
        const nickname = triggerBtn.getAttribute("data-bs-nickname");
        const body = triggerBtn.getAttribute("data-bs-body");
        const articleId = triggerBtn.getAttribute("data-bs-article-id");

        // 데이터를 반영
        document.querySelector("#edit-comment-nickname").value = nickname;
        document.querySelector("#edit-comment-body").value = body;
        document.querySelector("#edit-comment-id").value = id;
        document.querySelector("#edit-comment-article-id").value = articleId;

        console.log(articleId);
    });

    //수정완료버튼 가져오기
    const commentUpdateBtn = document.querySelector("#comment-update-btn");
    //클릭이벤트 감지 및 처리
    commentUpdateBtn.addEventListener("click",function(){
        // 수정 답변 객체 생성
        const comment = {
            id: document.querySelector("#edit-comment-id").value,
            nickname: document.querySelector("#edit-comment-nickname").value,
            body: document.querySelector("#edit-comment-body").value,
            article_id: document.querySelector("#edit-comment-article-id").value
        };

        // console.log(comment);

        const url = "/api/comments/"+comment.id;
        fetch(url,{
            method : "PATCH",
            body:JSON.stringify(comment), // comment JS 객체를 JSON으로 변경
            headers:{
                "Content-Type": "application/json"
            }
        }).then(response =>{
            //http 응답코드에 따른 메세지출력
            const msg = (response.ok) ? "답변이 수정되었습니다.": "답변 수정 실패!!";
            alert(msg);

            //현재 페이지 를 새로고침
            window.location.reload();

        });

    });

    //삭제 버튼 선택
    const commentDeleteBtns = document.querySelectorAll(".comment-delete-btn");

//삭제 버튼 이벤트 처리
    commentDeleteBtns.forEach(btn => {
        //선택한 삭제 버튼 요소들이 하나의 btn 으로 들어오고 각 버튼의 이벤트 처리를 등록
        btn.addEventListener("click", (event)=>{
            const commentDeleteBtn = event.target;
            const commentId =  commentDeleteBtn.getAttribute("data-comment-id");
            // console.log(`삭제버튼클릭 : ${commentId}번 답변`);

            const url = `/api/comments/${commentId}`;
            fetch(url, {
                method : "DELETE"
            }).then(response=>{
                //답변 삭제 실패 처리
                if(!response.ok){
                    alert("답변 삭제 실패..!!");
                    return;
                }
                //삭제 성공시
                const msg = `${commentId}번 답변을 삭제했습니다.`;
                alert(msg);
                //현재 페이지 새로고침
                window.location.reload();
            });

        });


    });

});
